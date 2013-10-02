package com.luxsky.chat.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.luxsky.chat.dao.ChatRoomDAO;
import com.luxsky.chat.dao.UserDAO;
import com.luxsky.chat.vo.ChatMessageVo;
import com.luxsky.chat.vo.UnreadCountVo;
import com.luxsky.chat.vo.UserStatusVo;


public class PushMgr {
	
	public final static int FriendChatRequest = 0;
	public final static int FriendRelation = 1;
	public final static int AdminSmsResponse = 2;
	public final static int AlbumContentComplain = 3;
	public final static int SmsSend = 4;
	
	private static Logger accessLogger =  Logger.getLogger("ACCESS");
	private static Logger errorLogger =  Logger.getLogger("ERROR");
	
	private static PushMgr instance = null;
	private IPhonePush iphone = null;
	private AndroidPush android = null;

	private UserDAO udao = null;
	
	private PushMgr() {
		iphone = IPhonePush.getInstance();
		android = AndroidPush.getInstance();
	}
	
	public synchronized static PushMgr getInstance() {
		if(instance == null) {
			instance = new PushMgr();
		}
		return instance;
	}
	
	/**
	 * 
	 * @param nickname
	 * @param msg
	 * @param type : 0 - FriendChatRequest, 1 - Friend Relation Response.
	 */
	public boolean sendPushOne(String receiver, ChatMessageVo message) {
		
		accessLogger.info("Send Push to : " + receiver);
		
		try {
			// get user device type
			udao = new UserDAO();
			
			UserStatusVo usvo = udao.selectUserStatus(receiver);
			
			int timeRange = udao.checkAlertTime(receiver);
			
			if("Y".equals(usvo.getChat_alert()) && timeRange > 0) {
				String dvcToken = usvo.getDeviceToken();
				if(dvcToken != null && !"".equals(dvcToken)){
					// badgeCount 는...전체 몇개..로...
					// 메시지는 신규 메시지로 받은.. 채팅방 목록 ?
					ChatRoomDAO crdao = new ChatRoomDAO();
					List<UnreadCountVo> badgeList = null;
					if(receiver.equals(message.getSeller_email())) {
						badgeList = crdao.selectCountSellerUnread(receiver);
					}
					else {
						badgeList = crdao.selectCountBuyersUnread(receiver);
					}
					
					int badgeCount = 0;
					if(badgeList != null) {
						int size = badgeList.size();
						for(int i = 0; i > size; i++) {
							badgeCount += badgeList.get(i).getTalk_count();
						}
					}
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("list", badgeList);
					
					// android => push
					if(usvo != null && "A".equals(usvo.getDeviceType())) {
						accessLogger.info("send android push");
						return android.push(dvcToken, JSONObject.fromObject(map).toString(), badgeCount);
					}
					else if(usvo != null && "I".equals(usvo.getDeviceType())) {
						// iphone => push
						accessLogger.info("send iphone push");
						return iphone.push(dvcToken, JSONObject.fromObject(map).toString(), badgeCount);
					}
					else {
						accessLogger.info("send nothing");
						return false;
					}
				}
			}
		}
		catch(Exception e) {
			errorLogger.error("", e);
		}
		return false;
	}
	
	public void testPush(String receiver, boolean isSeller) {
		ChatRoomDAO crdao = new ChatRoomDAO();
		List<UnreadCountVo> badgeList = null;
		if(isSeller) {
			badgeList = crdao.selectCountSellerUnread(receiver);
		}
		else {
			badgeList = crdao.selectCountBuyersUnread(receiver);
		}
		
		int badgeCount = 0;
		if(badgeList != null) {
			int size = badgeList.size();
			for(int i = 0; i > size; i++) {
				badgeCount += badgeList.get(i).getTalk_count();
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", badgeList);
		
		System.out.println(JSONObject.fromObject(map).toString());
	}
	
	public static void main(String[] args) {
		PushMgr push = new PushMgr();
		push.testPush("test@naver.com", false);
		push.testPush("devstories@daum.net", true);
	}
}
