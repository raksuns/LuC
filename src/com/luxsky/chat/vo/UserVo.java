package com.luxsky.chat.vo;

public class UserVo {

	private String email;
	private String passwd;
	private String nickname;
	private String status;
	private String chatAlert;

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChatAlert() {
		return chatAlert;
	}

	public void setChatAlert(String chatAlert) {
		this.chatAlert = chatAlert;
	}

}
