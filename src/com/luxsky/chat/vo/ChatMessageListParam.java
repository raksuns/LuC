package com.luxsky.chat.vo;

public class ChatMessageListParam {
	private String latest;
	private int talk_room_id;

	public String getLatest() {
		return latest;
	}

	public void setLatest(String latest) {
		this.latest = latest;
	}

	public int getTalk_room_id() {
		return talk_room_id;
	}

	public void setTalk_room_id(int talk_room_id) {
		this.talk_room_id = talk_room_id;
	}
}
