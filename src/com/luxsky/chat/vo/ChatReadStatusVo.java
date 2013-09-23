package com.luxsky.chat.vo;

public class ChatReadStatusVo {
	private int talk_room_id;
	private int product_seq;
	private String seller_email;
	private String buyers_email;
	private String seller_read_dt;
	private String buyers_read_dt;

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

	public int getTalk_room_id() {
		return talk_room_id;
	}

	public void setTalk_room_id(int talk_room_id) {
		this.talk_room_id = talk_room_id;
	}

	public String getSeller_read_dt() {
		return seller_read_dt;
	}

	public void setSeller_read_dt(String seller_read_dt) {
		this.seller_read_dt = seller_read_dt;
	}

	public String getBuyers_read_dt() {
		return buyers_read_dt;
	}

	public void setBuyers_read_dt(String buyers_read_dt) {
		this.buyers_read_dt = buyers_read_dt;
	}
}
