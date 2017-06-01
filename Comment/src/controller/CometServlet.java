package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;

import common.MessageSender;
import common.Weatherman;


public class CometServlet extends HttpServlet implements CometProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MessageSender messageSender=null;
	
	//初始化
	@Override
	public void init(ServletConfig config) throws ServletException {
		messageSender=new MessageSender();
		Thread thread=new Thread(messageSender);
		thread.setDaemon(true);
		thread.start();
		 super.init(config);
	}
	
	
	/**
	 * 监听Comet生命周期，push每个地方的天气
	 * @param CometEvent
	 * 用户第一次与Comet交互，产生一个线程并进入begin周期
	 */
	@Override
	public void event(CometEvent obj) throws IOException, ServletException {
		HttpServletRequest request=obj.getHttpServletRequest();
		HttpServletResponse response=obj.getHttpServletResponse();
		if(obj.getEventType()==CometEvent.EventType.BEGIN){
			 try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			request.setAttribute("org.apache.tomcat.comet.timeout",600 * 1000);
			messageSender.setConnect(response);
            Weatherman weatherman = new Weatherman(new String[]{"北京","成都","上海","广州","天津"},messageSender);
            new Thread(weatherman).start();
		}else if (obj.getEventType() == CometEvent.EventType.ERROR) {
            log("Error for session: ");
            obj.close();
        } else if (obj.getEventType() == CometEvent.EventType.END) {
            log("End for session: ");
            obj.close();
        } else if (obj.getEventType() == CometEvent.EventType.READ) {
            throw new UnsupportedOperationException("This servlet does not accept data");
        }
		
	}

}
