package com.luxsky.chat.vo;

public class ChatRoomVo {

	private int talk_room_id;
	private int product_seq;
	private String seller_email;
	private String buyers_email;

	public int getTalk_room_id() {
		return talk_room_id;
	}

	public void setTalk_room_id(int talk_room_id) {
		this.talk_room_id = talk_room_id;
	}

	public int getProduct_seq() {
		return product_seq;
	}

	public void setProduct_seq(int product_seq) {
		this.product_seq = product_seq;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public String getBuyers_email() {
		return buyers_email;
	}

	public void setBuyers_email(String buyers_email) {
		this.buyers_email = buyers_email;
	}

}
