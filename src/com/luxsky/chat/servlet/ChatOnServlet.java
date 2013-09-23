package com.luxsky.chat.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.luxsky.chat.common.ConstField;

@SuppressWarnings("serial")
@WebServlet(urlPatterns="/chaton", asyncSupported = true)
public class ChatOnServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processConnectionRequest(request, response);
	}

	protected void processConnectionRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		logger.info("Received ChatOn");
		
		String email = (String)req.getSession().getAttribute("email");//(String)req.getSession().getAttribute("email");//req.getParameter("email");
		
		if(email != null && !"".equals(email)) {
			
			AsyncContext asyncContext = req.startAsync();
			asyncContext.setTimeout(40000);
			ChatRoom.getInstance().enter(email, asyncContext);
			logger.info("Connect User : " + email);
		}
		else {
			logger.info("User Session is not found.");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", ConstField.ERROR_SESSION_NOT_FOUND);
			res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			res.setContentType("application/json");
			res.setHeader("Cache-Control", "private");
			res.setHeader("Pragma", "no-cache");
			req.setCharacterEncoding("UTF-8");
		}

//		res.setContentType("text/plain");
//		res.setHeader("Cache-Control", "private");
//		res.setHeader("Pragma", "no-cache");
//		req.setCharacterEncoding("UTF-8");
		
		
		// 사용자 로그인 결과를 여기서 확인 하자..DB조회..
		
//		PrintWriter writer = res.getWriter();
		//writer.println("<!-- for IE..start chatting -->\n");
//		writer.flush();
		
		
	}
}
