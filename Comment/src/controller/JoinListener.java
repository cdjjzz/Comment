package controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.comet4j.core.CometConnection;
import org.comet4j.core.CometContext;
import org.comet4j.core.event.ConnectEvent;
import org.comet4j.core.listener.ConnectListener;

public class JoinListener extends ConnectListener{
	
	@Override
	public boolean handleEvent(ConnectEvent obj) {
		final CometConnection conn = obj.getConn();
		System.out.println(conn);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CometContext.getInstance().getEngine().sendTo("saber", conn,"上线时间:"+dateFormat.format(new Date()));  
		return true;
	}

}
