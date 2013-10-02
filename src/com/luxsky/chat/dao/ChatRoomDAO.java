package com.luxsky.chat.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.luxsky.chat.common.SqlSessionMgr;
import com.luxsky.chat.vo.ChatMessageListParam;
import com.luxsky.chat.vo.ChatMessageListVo;
import com.luxsky.chat.vo.ChatMessageVo;
import com.luxsky.chat.vo.ChatReadStatusVo;
import com.luxsky.chat.vo.ChatRoomVo;
import com.luxsky.chat.vo.UnreadCountVo;

public class ChatRoomDAO {
	private final static String MAPPER_PKG_NAME = "com.luxsky.chat.chatroom.Mapper.";
	private SqlSessionFactory sqlSessionFactory = SqlSessionMgr.getSqlSession();
	
	public int chatRoomCheck(ChatRoomVo crvo) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = (Integer) session.selectOne(MAPPER_PKG_NAME + "chatRoomCheck", crvo);
		}
		finally {
			session.close();
		}
		return result;
	}
	
	public int selectChatRoom(ChatRoomVo crvo) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = (Integer) session.selectOne(MAPPER_PKG_NAME + "selectChatRoom", crvo);
		}
		finally {
			session.close();
		}
		return result;
	}
	
	public synchronized int createChatRoom(ChatRoomVo crvo) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = session.insert(MAPPER_PKG_NAME + "createChatRoom", crvo);
			session.commit();
		}
		finally {
			session.close();
		}
		return result;
	}
	
	public ChatRoomVo getChatRoom(int talkRoomId) {
		SqlSession session = sqlSessionFactory.openSession();
		ChatRoomVo crvo = null;
		try {
			crvo = (ChatRoomVo) session.selectOne(MAPPER_PKG_NAME + "getChatRoom", talkRoomId);
		}
		finally {
			session.close();
		}
		return crvo;
	}
	
	public ChatReadStatusVo getChatReadStatus(int talkRoomId) {
		SqlSession session = sqlSessionFactory.openSession();
		ChatReadStatusVo crsvo = null;
		try {
			crsvo = (ChatReadStatusVo) session.selectOne(MAPPER_PKG_NAME + "getChatReadStatus", talkRoomId);
		}
		finally {
			session.close();
		}
		return crsvo;
	}
	
	public synchronized int insertChatMessage(ChatMessageVo cmvo) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = session.insert(MAPPER_PKG_NAME + "insertChatMessage", cmvo);
			session.commit();
		}
		finally {
			session.close();
		}
		return result;
	}
	
	public ChatMessageVo selectChatMessage(int talk_seq) {
		SqlSession session = sqlSessionFactory.openSession();
		ChatMessageVo crvo = null;
		try {
			crvo = (ChatMessageVo) session.selectOne(MAPPER_PKG_NAME + "selectChatMessage", talk_seq);
		}
		finally {
			session.close();
		}
		return crvo;
	}
	
	public List<ChatMessageListVo> getChatMessageList(ChatMessageListParam cmlvo) {
		SqlSession session = sqlSessionFactory.openSession();
		List<ChatMessageListVo> list = null;
		try {
			list = session.selectList(MAPPER_PKG_NAME + "getChatMessageList", cmlvo);
		}
		finally {
			session.close();
		}
		return list;
	}
	
	public synchronized int updateLatestToSeller(int talk_room_id) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = session.update(MAPPER_PKG_NAME + "updateLatestToSeller", talk_room_id);
			session.commit();
		}
		finally {
			session.close();
		}
		return result;
	}
	
	public synchronized int updateLatestToBuyers(int talk_room_id) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = 0;
		try {
			result = session.update(MAPPER_PKG_NAME + "updateLatestToBuyers", talk_room_id);
			session.commit();
		}
		finally {
			session.close();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<UnreadCountVo> selectCountSellerUnread(String sellerEmail) {
		SqlSession session = sqlSessionFactory.openSession();
		List<UnreadCountVo> list = null;
		try {
			list = session.selectList(MAPPER_PKG_NAME + "selectCountSellerUnread", sellerEmail);
		}
		finally {
			session.close();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<UnreadCountVo> selectCountBuyersUnread(String buyersEmail) {
		SqlSession session = sqlSessionFactory.openSession();
		List<UnreadCountVo> list = null;
		try {
			list = session.selectList(MAPPER_PKG_NAME + "selectCountBuyersUnread", buyersEmail);
		}
		finally {
			session.close();
		}
		return list;
	}
}
