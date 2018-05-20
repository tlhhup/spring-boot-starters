package com.tlh.uncode.schedule.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.atomic.AtomicInteger;

import com.tlh.uncode.schedule.manager.ConsoleManager;
import com.tlh.uncode.schedule.manager.DynamicTaskManager;
import com.tlh.uncode.schedule.manager.ZKScheduleManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

public class ScheduledMethodRunnable implements Runnable {

	private final TaskDefine taskDefine;
	
	private AtomicInteger count = new AtomicInteger();
	
	
	public ScheduledMethodRunnable(TaskDefine taskDefine) {
		this.taskDefine = taskDefine;
	}
	
	
	public int getRunTimes(){
		return count.get();
	}

	public TaskDefine getTaskDefine() {
		return taskDefine;
	}

	@Override
	public void run() {
		Object bean = null;
		Method method = null;
		try {
			bean = ZKScheduleManager.getApplicationcontext().getBean(taskDefine.getTargetBean());
			if(taskDefine.getParams() != null){
				method = DynamicTaskManager.getMethod(bean, taskDefine.getTargetMethod(), String.class);
			}else{
				method = DynamicTaskManager.getMethod(bean, taskDefine.getTargetMethod());
			}
			ReflectionUtils.makeAccessible(method);
			if(taskDefine.getParams() != null){
				method.invoke(bean, taskDefine.getParams());
			}else{
				method.invoke(bean);
			}
			count.incrementAndGet();
			
			if(StringUtils.isBlank(taskDefine.getCronExpression()) 
					&& taskDefine.getDelay() == 0 && taskDefine.getPeriod() == 0){
				try {
					ConsoleManager.getScheduleManager().getScheduleDataManager().delTask(taskDefine);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		catch (InvocationTargetException ex) {
			ReflectionUtils.rethrowRuntimeException(ex.getTargetException());
		}
		catch (IllegalAccessException ex) {
			throw new UndeclaredThrowableException(ex);
		}
	}

}
