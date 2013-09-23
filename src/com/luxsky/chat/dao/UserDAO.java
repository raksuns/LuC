package com.luxsky.chat.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.luxsky.chat.common.SqlSessionMgr;
import com.luxsky.chat.vo.UserVo;

public class UserDAO {
	private final static String MAPPER_PKG_NAME = "com.luxsky.chat.user.Mapper.";
	private SqlSessionFactory sqlSessionFactory = SqlSessionMgr.getSqlSession();
	
	public UserVo selectUser(String nickname) {
		UserVo uvo = null;
		SqlSession session = sqlSessionFactory.openSession();
		try {
			uvo = (UserVo) session.selectOne(MAPPER_PKG_NAME + "selectUser", nickname);
		}
		finally {
			session.close();
		}
		return uvo;
	}
	
	public int memberCheck(UserVo user) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = (Integer) session.selectOne(MAPPER_PKG_NAME + "memberCheck", user);
		}
		finally {
			session.close();
		}
		return result;
	}
	
	public String userStatus(String userEmail) {
		SqlSession session = sqlSessionFactory.openSession();
		String result = "00";
		try {
			result = (String) session.selectOne(MAPPER_PKG_NAME + "userStatus", userEmail);
		}
		finally {
			session.close();
		}
		return result;
	}
}
