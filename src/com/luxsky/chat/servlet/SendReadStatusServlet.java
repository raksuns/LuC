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
import com.luxsky.chat.vo.ChatReadStatusVo;

@SuppressWarnings("serial")
@WebServlet(urlPatterns="/readstatus", asyncSupported = true)
public class SendReadStatusServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		logger.info("Received SEND request");
		
		String sessionEmail = (String)req.getSession().getAttribute("email");
		
		String talk_room_id = req.getParameter("talk_room_id");
		
		int reqState = 0;
		
		if(sessionEmail == null || "".equals(sessionEmail)) {
			logger.info("User Session is not found.");
			reqState = 1;
		}
		else if(talk_room_id == null || "".equals(talk_room_id)) {
			logger.info("talk_room is not found.");
			reqState = 2;
		}
		
		// 채팅 메시지 읽은 시간 요청한다. 각 사용자들은 이 시간을 기준으로 대화 상대가 대화를 읽었는지 안읽었는지 판단한다.
		if(reqState == 0) {
			ChatRoomDAO crdao = new ChatRoomDAO();
			ChatReadStatusVo crsvo = crdao.getChatReadStatus(Integer.parseInt(talk_room_id));
			
			if(crsvo != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", 0);
				map.put("read_status", crsvo);
				res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				res.setContentType("application/json");
				res.setHeader("Cache-Control", "private");
				res.setHeader("Pragma", "no-cache");
				req.setCharacterEncoding("UTF-8");
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
