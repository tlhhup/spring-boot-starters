# spring-boot-starters
自定义的一些工具starter

- zookeeper 同种类型的认证方式只能存在一处
- uncode业务流程-->核心为心跳检测的线程
  1. ZKScheduleManage的init方法为入口
  
		  	public void init(Properties p) throws Exception {
				    //... 其本身实现了ThreadPoolTaskScheduler接口，为一个调度任务
					initialThread = new InitialThread(this);
					initialThread.setName("ScheduleManager-initialThread");
					initialThread.start();
				} finally {
					this.initLock.unlock();
				}
			}
  
  2. 在看InitialThread的run方法会发现
  			
  			public void run() {
				sm.initLock.lock();
				try {
					int count = 0;
					//检测zookeeper是否可用
					while (!sm.zkManager.checkZookeeperState()) {
						count = count + 1;
						if (count % 50 == 0) {
							sm.errorMessage = "Zookeeper connecting ......"
									+ sm.zkManager.getConnectStr() + " spendTime:"
									+ count * 20 + "(ms)";
							log.error(sm.errorMessage);
						}
						Thread.sleep(20);
						if (this.isStop) {
							return;
						}
					}
					//初始化数据，会启动心跳检测线程
					sm.initialData();
				} catch (Throwable e) {
					log.error(e.getMessage(), e);
				} finally {
					sm.initLock.unlock();
				}
	
			}
  	
  3. 在看ZKScheduleManage的initialData方法
	
			public void initialData() throws Exception {
				this.zkManager.initial();
				this.scheduleDataManager = new ScheduleDataManager4ZK(this.zkManager);
				checkScheduleDataManager();
				if (this.start) {
					// 注册调度管理器
					this.scheduleDataManager.registerScheduleServer(this.currenScheduleServer);
					if (hearBeatTimer == null) {
						hearBeatTimer = new Timer("ScheduleManager-"
								+ this.currenScheduleServer.getUuid() + "-HearBeat");
					}
					// 1.该方法为整个调度的入口，一秒调用一次
					hearBeatTimer.schedule(new HeartBeatTimerTask(this), 1000, this.timerInterval);
					
					//初始化启动数据
					if(initTaskDefines != null && initTaskDefines.size() > 0){
						for(TaskDefine taskDefine : initTaskDefines){
							scheduleDataManager.addTask(taskDefine);
						}
					}
				}
			}
  
  4. 最后看HeartBeatTimerTask的run方法

	  		public void run() {
				try {
					Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
					//该方法会坚持zookeeper中的任务数据，再根据不同的类型进行调度
					manager.refreshScheduleServer();
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
			}
  5. ScheduleDataManager4ZK的checkLocalTask方法
  
  			public boolean checkLocalTask(String currentUuid) throws Exception {
		        if (this.zkManager.checkZookeeperState()) {
		        	 //获取节点中的数据
		            String zkPath = this.pathTask;
		            List<String> children = this.getZooKeeper().getChildren(zkPath, false);
		            List<String> ownerTask = new ArrayList<String>();
		            if (null != children && children.size() > 0) {
		                for (String taskName : children) {
		                    if (isOwner(taskName, currentUuid)) {
		                        String taskPath = zkPath + "/" + taskName;
		                        byte[] data = this.getZooKeeper().getData(taskPath, null, null);
		                        if (null != data) {
		                            //方序列化为TaskDefine对象
		                            String json = new String(data);
		                            TaskDefine td = this.gson.fromJson(json, TaskDefine.class);
		                            TaskDefine taskDefine = new TaskDefine();
		                            taskDefine.valueOf(td);
		                            ownerTask.add(taskName);
		                            TaskDefine runInfo = readRunningInfo(taskName, currentUuid);
		                            taskDefine.valueOf(runInfo);
		                            //根据类型分别交由DynamicTaskManager进行处理
		                            if (TaskDefine.TYPE_UNCODE_SINGLE_TASK.equals(taskDefine.getType())) {
		                                DynamicTaskManager.scheduleSingleTask(taskDefine, new Date(getSystemTime()));
		                            } else if (TaskDefine.TYPE_UNCODE_MULTI_MAIN_TASK.equals(taskDefine.getType())) {
		                                DynamicTaskManager.scheduleMultiMainTask(taskDefine, new Date(getSystemTime()));
		                            } else if (TaskDefine.TYPE_UNCODE_MULTI_SUB_TASK.equals(taskDefine.getType())) {
		                                DynamicTaskManager.scheduleMultiSubTask(taskDefine, new Date(getSystemTime()));
		                            }
		                        }
		                    }
		                }
		            }
		            DynamicTaskManager.clearLocalTask(ownerTask);
		        }
		        return false;
		    }
	6. 前端将所有的任务数据都存储在zookeeper中，由心跳检测线程将数据读取处理在交由DynamicTaskManager进行调度任务的创建和执行
  
