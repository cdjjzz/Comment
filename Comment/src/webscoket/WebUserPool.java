package webscoket;

import java.awt.event.FocusAdapter;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 当前连接用户用户池
 * @author lsf53_000
 *
 */
public class WebUserPool {
	
	private static final Map<String, WebMessage> map=new HashMap<String, WebMessage>();
	
	/**
	 * 添加用户
	 * @param user
	 * @param webMessage
	 */
	public static void addUser(String user,WebMessage webMessage){
		synchronized (map) {
			map.put(user,webMessage);
		}
	}
	/**
	 * 获取在线用户
	 * @return
	 */
	public static Set<String> getOnlineUser(){
		return map.keySet();
	}
	/**
	 * 删除用户
	 * @param user
	 */
	public static void removeUser(String user){
		synchronized (map) {
			map.remove(user);
		}
	}
	/**
	 * 全部发送
	 * @param message
	 */
	@SuppressWarnings("deprecation")
	public static void sendMessageToAll(String from,String message){
		try {
			Set<String> onlin=getOnlineUser();
			System.out.println(from+" send message:"+message+" to all");
			for(String user:onlin){
				WebMessage webMessage=map.get(user);
				if(webMessage!=null){
					webMessage.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 私聊
	 * @param user
	 * @param message
	 */
	@SuppressWarnings("deprecation")
	public static void sendMessageToSingel(String to,String from,String message){
		try {
			WebMessage webMessage=map.get(from);
			if(webMessage!=null){
				System.out.println(to + " send message: " + message + " to " +from);  
				webMessage.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
