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

import com.luxsky.chat.dao.ChatRoomDAO;
import com.luxsky.chat.dao.UserDAO;
import com.luxsky.chat.vo.ChatMessageVo;
import com.luxsky.chat.vo.ChatRoomVo;

@SuppressWarnings("serial")
@WebServlet(urlPatterns="/sendchat", asyncSupported = true)
public class SendChatServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		logger.info("Received SEND request");
		
		String sessionEmail = (String)req.getSession().getAttribute("email");
		
		String receiver = req.getParameter("receiver");
		String talk_room_id = req.getParameter("talk_room_id");
		String message = req.getParameter("message");
		
		int reqState = 0;
		
		if(sessionEmail == null || "".equals(sessionEmail)) {
			logger.info("User Session is not found.");
			reqState = 1;
		}
		else if(talk_room_id == null || "".equals(talk_room_id)) {
			logger.info("talk_room_id is not found.");
			reqState = 2;
		}
		else if(receiver == null || "".equals(receiver)) {
			logger.info("receiver is not found.");
			reqState = 3;
		}
		else if(message == null || "".equals(message)) {
			logger.info("message is not found.");
			reqState = 4;
		}
		
		if(reqState == 0) {
			// 대화 전송시 구매자 , 판매자 가입 상태 체크한다.
			UserDAO udao = new UserDAO();
			String userStatus = udao.userStatus(receiver);
			if("02".equals(userStatus) || "03".equals(userStatus)) {
				// status = 501;
			}
			
			ChatRoomDAO crdao = new ChatRoomDAO();
			ChatRoomVo crvo = crdao.getChatRoom(Integer.parseInt(talk_room_id));
			
			if(crvo != null) {
				ChatMessageVo cmvo = new ChatMessageVo();
				cmvo.setProduct_seq(crvo.getProduct_seq());
				cmvo.setSeller_email(crvo.getSeller_email());
				cmvo.setBuyers_email(crvo.getBuyers_email());
				cmvo.setContent(message);
				cmvo.setMsg_type("C");
				cmvo.setTalk_room_id(crvo.getTalk_room_id());
				
				if(sessionEmail.equals(crvo.getSeller_email())) {
					cmvo.setWriter("S");
				}
				else {
					cmvo.setWriter("B");
				}
				
				crdao.insertChatMessage(cmvo);
				
				logger.info("User Session Name : " + sessionEmail);
				ChatRoom.getInstance().sendMessageToUser(receiver, cmvo);
				res.getWriter().print("OK");
			}
			else {
				// 없는 대화 방임..
			}
		}
		else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", (910 + reqState));
			res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			res.setContentType("application/json");
			res.setHeader("Cache-Control", "private");
			res.setHeader("Pragma", "no-cache");
			req.setCharacterEncoding("UTF-8");
		}
	}
}
