package com.luxsky.chat.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.luxsky.chat.common.ConstField;
import com.luxsky.chat.dao.ChatRoomDAO;
import com.luxsky.chat.dao.UserDAO;
import com.luxsky.chat.vo.ChatMessageVo;
import com.luxsky.chat.vo.ChatRoomVo;

@SuppressWarnings("serial")
@WebServlet(urlPatterns="/sendphoto", asyncSupported = true)
public class SendPhotoServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String sessionEmail = (String)req.getSession().getAttribute("email");
		
		String receiver = req.getParameter("receiver");
		String talk_room_id = req.getParameter("talk_room_id");
		String origUrl = req.getParameter("origUrl");
		String thumbUrl = req.getParameter("thumbUrl");
		String thumbSizeWidth = req.getParameter("thumbSizeWidth");
		String thumbSizeHeight = req.getParameter("thumbSizeHeight");
		
		int reqState = 0;
		
		if(sessionEmail == null || "".equals(sessionEmail)) {
			logger.info("User Session is not found.");
			reqState = ConstField.ERROR_SESSION_NOT_FOUND;
		}
		else if(talk_room_id == null || "".equals(talk_room_id)) {
			logger.info("talk_room_id is not found.");
			reqState = ConstField.ERROR_REQ_PARAM;
		}
		else if(receiver == null || "".equals(receiver)) {
			logger.info("receiver is not found.");
			reqState = ConstField.ERROR_REQ_PARAM;
		}
		else if(origUrl == null || "".equals(origUrl)) {
			logger.info("origUrl is not found.");
			reqState = ConstField.ERROR_REQ_PARAM;
		}
		else if(thumbUrl == null || "".equals(thumbUrl)) {
			logger.info("thumbUrl is not found.");
			reqState = ConstField.ERROR_REQ_PARAM;
		}
		else if(thumbSizeWidth == null || "".equals(thumbSizeWidth)) {
			logger.info("thumbSizeWidth is not found.");
			reqState = ConstField.ERROR_REQ_PARAM;
		}
		else if(thumbSizeHeight == null || "".equals(thumbSizeHeight)) {
			logger.info("thumbSizeHeight is not found.");
			reqState = ConstField.ERROR_REQ_PARAM;
		}
		
		if(reqState == 0) {
			// 대화 전송시 구매자 , 판매자 가입 상태 체크한다.
			UserDAO udao = new UserDAO();
			String userStatus = udao.userStatus(receiver);
			if("02".equals(userStatus) || "03".equals(userStatus)) {
				reqState = ConstField.ERROR_USER_NOT_FOUND;
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", reqState);
				res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
				res.setStatus(HttpServletResponse.SC_OK);
				res.setContentType("application/json");
				res.setHeader("Cache-Control", "private");
				res.setHeader("Pragma", "no-cache");
				req.setCharacterEncoding("UTF-8");
			}
			else {
				ChatRoomDAO crdao = new ChatRoomDAO();
				ChatRoomVo crvo = crdao.getChatRoom(Integer.parseInt(talk_room_id));
				
				if(crvo != null) {
					HashMap<String, Object> photo = new HashMap<String, Object>();
					photo.put("origUrl", origUrl);
					photo.put("thumbUrl", thumbUrl);
					photo.put("thumbSizeWidth", thumbSizeWidth);
					photo.put("thumbSizeHeight", thumbSizeHeight);
					
					ChatMessageVo cmvo = new ChatMessageVo();
					cmvo.setProduct_seq(crvo.getProduct_seq());
					cmvo.setSeller_email(crvo.getSeller_email());
					cmvo.setBuyers_email(crvo.getBuyers_email());
					cmvo.setContent(JSONObject.fromObject(photo).toString());
					cmvo.setMsg_type("P");
					cmvo.setTalk_room_id(crvo.getTalk_room_id());
					
					if(sessionEmail.equals(crvo.getSeller_email())) {
						cmvo.setWriter("S");
					}
					else {
						cmvo.setWriter("B");
					}
					
					crdao.insertChatMessage(cmvo);
					
					logger.info("select talk seq : " + cmvo.getTalk_seq());
					cmvo = crdao.selectChatMessage(cmvo.getTalk_seq());
					
					logger.info("User Session Name : " + sessionEmail);
					ChatRoom.getInstance().sendMessageToUser(receiver, cmvo);
					res.getWriter().print("OK");
				}
				else {
					// 없는 대화 방임..
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("status", ConstField.ERROR_NOT_FOUND_CHAT_ROOM);
					res.setStatus(HttpServletResponse.SC_OK);
					res.setContentType("application/json");
					res.setHeader("Cache-Control", "private");
					res.setHeader("Pragma", "no-cache");
					res.setCharacterEncoding("UTF-8");
					res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
				}
			}
		}
		else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", reqState);
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			res.setContentType("application/json");
			res.setHeader("Cache-Control", "private");
			res.setHeader("Pragma", "no-cache");
			res.setCharacterEncoding("UTF-8");
			res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
		}
	}
}
