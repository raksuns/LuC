package com.luxsky.chat.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionMgr implements HttpSessionListener {
	private int sessionCount = 0;

	public void sessionCreated(HttpSessionEvent event) {

		HttpSession session = event.getSession();
		session.setMaxInactiveInterval(60 * 30);// 초단위로 세션유지 시간을 설정합니다
		System.out.println(session.getId() + ": 세션이 생성되었습니다.");
		
		synchronized (this) {
			sessionCount++;
		}

		System.out.println("Session Created: " + event.getSession().getId());
		System.out.println("Total Sessions: " + sessionCount);
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		synchronized (this) {
			sessionCount--;
		}
		HttpSession session = event.getSession();
		System.out.println(session.getId() + ": 세션이 소멸되었습니다.");
		System.out.println("Session Destroyed: " + event.getSession().getId());
		System.out.println("Total Sessions: " + sessionCount);
	}
}