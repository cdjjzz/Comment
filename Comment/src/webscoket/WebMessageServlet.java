package webscoket;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
/**
 * 
 * @author lsf53_000
 * WebSocket 监听进程
 *
 */
@SuppressWarnings({ "serial", "deprecation" })
public class WebMessageServlet extends WebSocketServlet{
	
	public static int ONLINE_USER_COUNT;
	
	@Override
	protected StreamInbound createWebSocketInbound(String obj,
			HttpServletRequest request) {
		System.out.println(ONLINE_USER_COUNT);
		String user=(String) request.getSession().getAttribute("user");
		return new WebMessage(user);
	}

}
