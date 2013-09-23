package com.luxsky.chat.vo;

public class ChatMessageListVo {
	private String latest;
	private int product_seq;
	private String seller_email;
	private String buyers_email;
	private int talk_seq;
	private String reg_dt;
	private String content;
	private String writer; // S-판매자, B-구매자
	private String msg_type; // C :채팅 메시지, P : 채팅중 사진 전송
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

	public int getTalk_seq() {
		return talk_seq;
	}

	public void setTalk_seq(int talk_seq) {
		this.talk_seq = talk_seq;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

}
