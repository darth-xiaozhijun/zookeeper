package com.geekshow.zookeeper.base;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * Zookeeper base学习笔记
 */
public class ZookeeperBase {

	/** zookeeper地址 */
	static final String CONNECT_ADDR = "192.168.154.128:2181,192.168.154.129:2181,192.168.154.130:2181";
	/** session超时时间 */
	static final int SESSION_OUTTIME = 2000;//ms 
	/** 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号 */
	static final CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception{
		
		ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_OUTTIME, new Watcher(){
			@Override
			public void process(WatchedEvent event) {
				//获取事件的状态
				KeeperState keeperState = event.getState();
				EventType eventType = event.getType();
				//如果是建立连接
				if(KeeperState.SyncConnected == keeperState){
					if(EventType.None == eventType){
						//如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
						connectedSemaphore.countDown();
						System.out.println("zk 建立连接");
					}
				}
			}
		});

		//进行阻塞
		connectedSemaphore.await();
		
		System.out.println("..");
		//创建父节点
//		String result = zk.create("/testRoot", "testRoot".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		System.out.println(result);
		//创建子节点
//		String res = zk.create("/testRoot/children", "children data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		System.out.println(res);
		
//		String res = zk.create("/testRoot/children", "children data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//		System.out.println(res);
		
//		Thread.sleep(10000);
		//获取节点洗信息
//		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));
//		System.out.println(zk.getChildren("/testRoot", false));
		List<String> list = zk.getChildren("/testRoot", false);
		for (String path : list) {
			System.out.println(path);
			String realPath = "/testRoot/" + path;
			System.out.println(new String(zk.getData(realPath, false, null)));
		}
		//修改节点的值
//		zk.setData("/testRoot", "modify data root".getBytes(), -1);
//		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));		
		
		//判断节点是否存在
//		System.out.println(zk.exists("/testRoot/a1", false));
		//删除节点
//		zk.delete("/testRoot/children", -1);
//		System.out.println(zk.exists("/testRoot/children", false));
		
		//异步删除节点
//		zk.delete("/testRoot", -1, (int rc,String path,Object ctx)->{
//			System.out.println(rc);
//			System.out.println(path);
//			System.out.println(ctx);
//		}, "a");
//		
//		System.out.println("继续执行。。。。");
//		Thread.sleep(50000);
		
		zk.close();
		
		
		
	}
	
}
