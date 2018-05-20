package org.tlh.test;

import org.apache.zookeeper.ZooKeeper;

import java.util.List;

public class ZookeeperTest {

    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper=new ZooKeeper("127.0.0.1:2181",60000,null);
            //授权信息
            String authString = "null:null";
            zooKeeper.addAuthInfo("digest", authString.getBytes());
            rmr("/uncode",zooKeeper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rmr(String path,ZooKeeper zk) throws Exception {
        //获取路径下的节点
        List<String> children = zk.getChildren(path, false);
        for (String pathCd : children) {
            //获取父节点下面的子节点路径
            String newPath = "";
            //递归调用,判断是否是根节点
            if (path.equals("/")) {
                newPath = "/" + pathCd;
            } else {
                newPath = path + "/" + pathCd;
            }
            rmr(newPath,zk);
        }
        //删除节点,并过滤zookeeper节点和 /节点
        if (path != null && !path.trim().startsWith("/zookeeper") && !path.trim().equals("/")) {
            zk.delete(path, -1);
            //打印删除的节点路径
            System.out.println("被删除的节点为：" + path);
        }
    }

}
