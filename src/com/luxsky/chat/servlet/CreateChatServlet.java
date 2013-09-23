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
import com.luxsky.chat.vo.ChatRoomVo;

@SuppressWarnings("serial")
@WebServlet(urlPatterns="/create", asyncSupported = true)
public class CreateChatServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processConnectionRequest(request, response);
	}

	protected void processConnectionRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		logger.info("Received CreateChatServlet");
		
		String email = (String)req.getSession().getAttribute("email");
		logger.info("Connect User Email : " + email);
		
		if(email != null && !"".equals(email)) {
			
			String sellerEmail = req.getParameter("sellerEmail");
			String buyersEmail = req.getParameter("buyersEmail");
			String productSeq = req.getParameter("productSeq");
			
			int status = 0;
			int talk_room_id = 0;
			
			// buyersEmail 상태 체크..대화요청은 상품 판매자에게만 하기때문에 ..판매자 상태만 확인한다.
			UserDAO udao = new UserDAO();
			String buyersStatus = udao.userStatus(buyersEmail);
			if("02".equals(buyersStatus) || "03".equals(buyersStatus)) {
				status = 501;
			}
			else {
			
				ChatRoomDAO croom = new ChatRoomDAO();
				ChatRoomVo crvo = new ChatRoomVo();
				crvo.setProduct_seq(Integer.parseInt(productSeq));
				crvo.setBuyers_email(buyersEmail);
				crvo.setSeller_email(sellerEmail);
				// 해당 방이 있는지 체크
				
				int result = croom.chatRoomCheck(crvo);
				
				if(result < 1) {
					// 없다면 생성하고, 생성된 방과 구매자, 판매자, 판매 상품 정보를 저장
					talk_room_id = croom.createChatRoom(crvo);
					status  = 0;
				}
				else {
					// 판매 상품에 대한 구매자와 판매자 대화방이 있다면 이미 있는 방이라고 리턴..
					status = 502;
				}
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", status);
			
			if(talk_room_id > 0) {
				map.put("talk_room_id", talk_room_id);
			}
			
			res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
			res.setStatus(HttpServletResponse.SC_OK);
			res.setContentType("application/json");
			res.setHeader("Cache-Control", "private");
			res.setHeader("Pragma", "no-cache");
			req.setCharacterEncoding("UTF-8");
		}
		else {
			logger.info("User Session is not found.");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 901);
			res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			res.setContentType("application/json");
			res.setHeader("Cache-Control", "private");
			res.setHeader("Pragma", "no-cache");
			req.setCharacterEncoding("UTF-8");
		}
		
	}
}
