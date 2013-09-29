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
import com.luxsky.chat.dao.UserDAO;
import com.luxsky.chat.vo.UserVo;

@SuppressWarnings("serial")
@WebServlet(urlPatterns="/logon", asyncSupported = true)
public class LogOnServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processConnectionRequest(request, response);
	}

	protected void processConnectionRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		logger.info("Received ChatOn");
		
		req.setCharacterEncoding("UTF-8");
		
		String email = req.getParameter("email");
		String passwd = req.getParameter("passwd");
		logger.info("Connect User Email : " + email);
		
		UserVo user = new UserVo();
		user.setEmail(email);
		user.setPasswd(passwd);
		
		UserDAO udao = new UserDAO();
		int result = udao.memberCheck(user);
		
		if(result > 0) {
			req.getSession().setAttribute("email", email);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", ConstField.SUCCESS);
			res.setStatus(HttpServletResponse.SC_OK);
			res.setContentType("application/json");
			res.setHeader("Cache-Control", "private");
			res.setHeader("Pragma", "no-cache");
			res.setCharacterEncoding("UTF-8");
			res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
			
		}
		else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", ConstField.ERROR_LOGIN);
			res.setStatus(HttpServletResponse.SC_OK);
			res.setContentType("application/json");
			res.setHeader("Cache-Control", "private");
			res.setHeader("Pragma", "no-cache");
			res.setCharacterEncoding("UTF-8");
			res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");
		}
		// 사용자 로그인 결과를 여기서 확인 하자..DB조회..
		
//		PrintWriter writer = res.getWriter();
		//writer.println("<!-- for IE..start chatting -->\n");
//		writer.flush();
		
	}
}
