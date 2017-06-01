package webscoket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.google.gson.Gson;

/**
 * 用户登录建立连接，输入流和输出流都是 WebSocket 协议定制的。
 * WsOutbound 负责输出结果，StreamInbound 和 WsInputStream 负责接收数据
 * @author lsf53_000
 *
 */
@SuppressWarnings("deprecation")
public class WebMessage extends MessageInbound{
	
	private final String user;
	private Gson gson=new Gson();
	public WebMessage(String user) {
		this.user=user;
	}
	
	public String getUser() {
		return user;
	}

	/**
	 * 建立连接,发送消息，通知
	 */
	@Override
	protected void onOpen(WsOutbound outbound) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("type","login");
		map.put("user", this.user);
		
		WebUserPool.sendMessageToAll("系统",gson.toJson(map));
		
		Map<String,Object> onlins=new HashMap<String,Object>();
		onlins.put("type","onlin");
		onlins.put("users",WebUserPool.getOnlineUser());
		String messageText=gson.toJson(onlins);
		//添加
		WebUserPool.addUser(this.user, this);
		System.out.println(messageText);
		WebUserPool.sendMessageToSingel("系统",this.user,messageText);
	}
	
	@Override
	protected void onClose(int status) {
		WebUserPool.removeUser(this.user);
		Map<String,Object> level=new HashMap<String,Object>();
		level.put("type","level");
		level.put("user", this.user);
		WebUserPool.sendMessageToAll("系统",gson.toJson(level));
	}
	@Override
	protected void onBinaryMessage(ByteBuffer obj) throws IOException {
		System.out.println(obj);
		throw new UnsupportedOperationException("Binary message not supported.");  
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onTextMessage(CharBuffer obj) throws IOException {
		if(obj!=null){
			Map<String, String> message=gson.fromJson(obj.toString(), Map.class);
			System.out.println(message);
			if(!message.get("to").equals("all")){
				WebUserPool.sendMessageToSingel(message.get("from"),message.get("to"),obj.toString());
			}else{
				WebUserPool.sendMessageToAll("系统", obj.toString());
			}
		}
	}
}
