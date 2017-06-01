package controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;

//启动初始化comet组件
public class CometListener implements ServletContextListener{

	private static final String CHANNEL_ONE="saber";
	@Override
	public void contextDestroyed(ServletContextEvent obj) {
	}

	@Override
	public void contextInitialized(ServletContextEvent obj) {
		//初始化Comet上下文
		CometContext cometContext=CometContext.getInstance();
		//注册两条通道
		cometContext.registChannel(CHANNEL_ONE);
		CometEngine engine =cometContext.getEngine();  
		engine.addConnectListener(new JoinListener());
	}

}
