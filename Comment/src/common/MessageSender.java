package common;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletResponse;

/**
 * 消息发送者
 * @author lsf53_000
 *
 */
public class MessageSender implements Runnable{
	//标示发送者是否在工作,默认工作
	protected boolean runing=true;
	//要发送的消息集合
	public List<String> messages=new ArrayList<String>();
	
	//消息通过该通道发送给客户端
	private ServletResponse response;
	//线程启动，加锁设置通道
	public synchronized void setConnect(ServletResponse response){
		this.response=response;
		//通道设置成功，唤醒线程
		notify();
	}
	public void add(String message){
		//防止多线程对消息集合同时加入
		synchronized (messages) {
			messages.add(message);
			System.out.println("当前等待被发送消息条数:"+messages.size());
			messages.notify();
		}
	}
	
	public void run() {
		while(runing){
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			//没有要发送的消息，等待。。。。。
			if(messages.size()==0){
				try {
					synchronized (messages) {
						messages.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				try {
					if(response==null){
						synchronized (this) {
							wait();
						}
					}
					synchronized (messages) {
						response.setCharacterEncoding("utf-8");
						PrintWriter writer = response.getWriter();
						StringBuilder sb=new StringBuilder("当前时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date())+"<br/>");
						for (int j = 0; j <messages.size(); j++) {
		                	sb.append(j+":"+messages.get(j) + "<br>");
		                }
		                writer.println(sb.toString());
		                System.out.println(111);
		                writer.flush();
		                writer.close();
		                messages.clear();
		                response= null;
					}
				} catch (Exception e) {
				}
			}
		}
	}

}
