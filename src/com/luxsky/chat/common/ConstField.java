package com.luxsky.chat.common;

public class ConstField {

	
	public final static String ANDROID = "A";
	public final static String IPHONE = "I";
	
	public final static int SUCCESS = 0;
	public final static int ERROR_SESSION_NOT_FOUND = 600; // 서버에 사용자 세션이 없는 상태
	public final static int ERROR_USER_NOT_FOUND = 601;  // 탈퇴, 계정 정지 등 사용자가 없음.
	public final static int ERROR_EXSIT_CHAT_ROOM = 602; // 이미 존재하는 채팅방
	public final static int ERROR_NOT_FOUND_CHAT_ROOM = 603; // 채팅방이 없음 (talk_room_id에 해당하는 채팅방 없음)
	public final static int ERROR_REQ_PARAM = 604; // 요청 파라미터가 잘못됨.
	public final static int ERROR_LOGIN = 605; // 로그인 정보가 잘못되어 로그인 실패함.
	public final static int ERROR_CHAT_ROOM_FAIL = 606; // 채팅방 만들기 실패함.
	public final static int ERROR_FILEUPLOAD = 666; // 파일 업로드 실패.
	
	
}
