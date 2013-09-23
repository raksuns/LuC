package com.luxsky.chat.servlet;

import com.luxsky.chat.vo.ChatMessageVo;

public class Message {

	private String user;
	private ChatMessageVo message;
	
	public Message(String u, ChatMessageVo m) {
		this.user = u;
		this.message = m;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ChatMessageVo getMessage() {
		return message;
	}

	public void setMessage(ChatMessageVo message) {
		this.message = message;
	}

}
