package com.luxsky.chat.vo;

public class UserStatusVo {
	private String status;
	private String chat_alert;
	private String alert_start;
	private String alert_end;
	private String deviceType;
	private String deviceToken;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChat_alert() {
		return chat_alert;
	}

	public void setChat_alert(String chat_alert) {
		this.chat_alert = chat_alert;
	}

	public String getAlert_start() {
		return alert_start;
	}

	public void setAlert_start(String alert_start) {
		this.alert_start = alert_start;
	}

	public String getAlert_end() {
		return alert_end;
	}

	public void setAlert_end(String alert_end) {
		this.alert_end = alert_end;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

}
