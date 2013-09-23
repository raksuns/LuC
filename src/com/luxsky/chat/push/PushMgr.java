package com.luxsky.chat.push;

import java.util.List;

import org.apache.log4j.Logger;

import com.etalk.common.ETalkCommon;
import com.etalk.dao.PushDAO;
import com.etalk.dao.UserDAO;
import com.etalk.web.vo.AlarmVo;
import com.etalk.web.vo.DeviceVo;
import com.etalk.web.vo.UserVo;

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
	private DeviceVo dvo = null;
	private PushDAO pdao = null;
	private AlarmVo avo  = null;
	
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
	public boolean sendPushOne(String nickname, String msg, int type) {
		
		accessLogger.info("Send Push to : " + nickname);
		
		try {
			// get user device type
			udao = new UserDAO();
			dvo = new DeviceVo();
			pdao = new PushDAO();
			
			dvo = udao.getDeviceVo(nickname);
			dvo.setNickName(nickname);
			
			avo = udao.selectAlarm(nickname);
			int timeRange = udao.checkAlarmTime(nickname);
			
			if(type == FriendChatRequest && "Y".equals(avo.getAlarmChat()) && timeRange > 0) {
				String dvcToken = dvo.getDeviceToken();
				if(dvcToken != null && !"".equals(dvcToken)){
					int badgeCount = 0;
					badgeCount = pdao.getFriendChatReqPushListCount(nickname);
					
					// android => push
					if(dvo != null && ETalkCommon.TYPE_ANDROID.equals(dvo.getDeviceType())) {
						accessLogger.info("send android push");
						return android.push(dvcToken, msg, badgeCount);
					}
					else if(dvo != null && ETalkCommon.TYPE_IPHONE.equals(dvo.getDeviceType())) {
						// iphone => push
						accessLogger.info("send iphone push");
						return iphone.push(dvcToken, msg, badgeCount);
					}
					else {
						accessLogger.info("send nothing");
					}
					return true;
				}
			}
			else if(type == FriendRelation && "Y".equals(avo.getAlarmFriend()) && timeRange > 0) {
				String dvcToken = dvo.getDeviceToken();
				if(dvcToken != null && !"".equals(dvcToken)){
					int badgeCount = 0;
					badgeCount = pdao.getFriendResPushListCount(nickname);
					
					// android => push
					if(dvo != null && ETalkCommon.TYPE_ANDROID.equals(dvo.getDeviceType())) {
						accessLogger.info("send android push");
						return android.push(dvcToken, msg, badgeCount);
					}
					else if(dvo != null && ETalkCommon.TYPE_IPHONE.equals(dvo.getDeviceType())) {
						// iphone => push
						accessLogger.info("send iphone push");
						return iphone.push(dvcToken, msg, badgeCount);
					}
					else {
						accessLogger.info("send nothing");
					}
					return true;
				}
			}
			else if(type == SmsSend && "Y".equals(avo.getAlarmSms()) && timeRange > 0) {
				String dvcToken = dvo.getDeviceToken();
				if(dvcToken != null && !"".equals(dvcToken)){
					int badgeCount = 0;
					badgeCount = pdao.getFriendChatReqPushListCount(nickname);
					badgeCount += pdao.getFriendResPushListCount(nickname);
					
					// android => push
					if(dvo != null && ETalkCommon.TYPE_ANDROID.equals(dvo.getDeviceType())) {
						accessLogger.info("send android push");
						return android.push(dvcToken, msg, badgeCount);
					}
					else if(dvo != null && ETalkCommon.TYPE_IPHONE.equals(dvo.getDeviceType())) {
						// iphone => push
						accessLogger.info("send iphone push");
						return iphone.push(dvcToken, msg, badgeCount);
					}
					else {
						accessLogger.info("send nothing");
					}
					return true;
				}
			}
		}
		catch(Exception e) {
			errorLogger.error("", e);
		}
		return false;
	}
}
