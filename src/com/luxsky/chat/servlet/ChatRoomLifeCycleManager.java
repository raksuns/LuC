package com.luxsky.chat.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ChatRoomLifeCycleManager implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ChatRoom.getInstance().init();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ChatRoom.getInstance().close();
	}
}