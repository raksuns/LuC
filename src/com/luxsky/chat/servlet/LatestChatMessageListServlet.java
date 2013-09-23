package com.luxsky.chat.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.luxsky.chat.vo.ChatMessageListVo;
import com.luxsky.chat.vo.ChatRoomVo;

/**
 * latest 시간 이후의 채팅 메시지를 요청 / 응답.
 *
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns="/latest", asyncSupported = true)
public class LatestChatMessageListServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		logger.info("Received SEND request");
		
		String sessionEmail = (String)req.getSession().getAttribute("email");
		
		String talk_room_id = req.getParameter("talk_room_id");
		String latest = req.getParameter("latest"); // yyyyMMDDhhmmss
		
		int reqState = 0;
		
		if(sessionEmail == null || "".equals(sessionEmail)) {
			logger.info("User Session is not found.");
			reqState = ConstField.ERROR_SESSION_NOT_FOUND;
		}
		else if(talk_room_id == null || "".equals(talk_room_id)) {
			logger.info("talk_room is not found.");
			reqState = ConstField.ERROR_REQ_PARAM;
		}
		else if(latest == null || "".equals(latest)) {
			logger.info("latest is not found.");
			reqState = ConstField.ERROR_REQ_PARAM;
		}
		
		if(reqState == 0) {
			int trid = Integer.parseInt(talk_room_id);
			ChatRoomDAO crdao = new ChatRoomDAO();
			ChatRoomVo crvo = crdao.getChatRoom(trid);
			
			if(crvo != null) {
				ChatMessageListVo cmlvo = new ChatMessageListVo();
				cmlvo.setTalk_room_id(crvo.getTalk_room_id());
				cmlvo.setLatest(latest);

				logger.info("User Session Name : " + sessionEmail);
				
				List<ChatMessageListVo> list = crdao.getChatMessageList(cmlvo);
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", 0);
				if(list != null) {
					map.put("count", list.size());
					map.put("list", list);
				}
				else {
					map.put("count", 0);
				}
				
				if(sessionEmail.equals(crvo.getSeller_email())) {
					// seller update latest time..
					crdao.updateLatestToSeller(trid);
				}
				else {
					// buyers update latest time..
					crdao.updateLatestToBuyers(trid);
				}
				
				res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
				res.setStatus(HttpServletResponse.SC_OK);
				res.setContentType("application/json");
				res.setHeader("Cache-Control", "private");
				res.setHeader("Pragma", "no-cache");
				req.setCharacterEncoding("UTF-8");
			}
			else {
				// 없는 대화 방임..
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", ConstField.ERROR_NOT_FOUND_CHAT_ROOM);
				res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
				res.setStatus(HttpServletResponse.SC_OK);
				res.setContentType("application/json");
				res.setHeader("Cache-Control", "private");
				res.setHeader("Pragma", "no-cache");
				req.setCharacterEncoding("UTF-8");
			}
		}
		else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", reqState);
			res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			res.setContentType("application/json");
			res.setHeader("Cache-Control", "private");
			res.setHeader("Pragma", "no-cache");
			req.setCharacterEncoding("UTF-8");
		}
	}
}
