package com.luxsky.chat.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.luxsky.chat.dao.ChatRoomDAO;
import com.luxsky.chat.vo.ChatMessageVo;

public class ChatRoom {

	private static ChatRoom INSTANCE = new ChatRoom();

	public static ChatRoom getInstance() {
		return INSTANCE;
	}

	private Logger logger = Logger.getLogger(getClass());
	private Map<String, AsyncContext> clients = new HashMap<String, AsyncContext>();
	private BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();

	private Thread messageHandlerThread;
	private boolean running;
	
	private ChatRoom() {
	}

	public void init() {
		logger.info("Start ChatRoom init....");
		running = true;
		Runnable handler = new Runnable() {
			@Override
			public void run() {
				logger.info("Started Message Handler.");
				while (running) {
					try {
						Message message = messageQueue.take();
						if(message != null) {
							sendMessageToAllInternal(message);
						}
					} catch (InterruptedException ex) {
						ex.printStackTrace();
						break;
					}
				}
			}
		};
		messageHandlerThread = new Thread(handler);
		messageHandlerThread.start();
	}

	public void enter(final String nickname, final AsyncContext asyncCtx) {
		
		logger.info("client puts : " + nickname);
		clients.put(nickname, asyncCtx);
		
		asyncCtx.addListener(new AsyncListener() {
			@Override
			public void onTimeout(AsyncEvent event) throws IOException {
				logger.info("onTimeout");
				HttpServletResponse res = (HttpServletResponse) event.getAsyncContext().getResponse();
				PrintWriter pw = res.getWriter();
				pw.write("{}");
				res.setStatus(HttpServletResponse.SC_OK);
				res.setContentType("application/json");
				clients.remove(nickname);
			}

			@Override
			public void onError(AsyncEvent event) throws IOException {
				logger.info("onError");
				clients.remove(nickname);
			}

			@Override
			public void onStartAsync(AsyncEvent event) throws IOException {
				logger.info("onStartAsync");
			}

			@Override
			public void onComplete(AsyncEvent event) throws IOException {
				logger.info("onComplete");
				clients.remove(nickname);
			}
		});
	}
	
	public void sendMessageToUser(String nickname, ChatMessageVo message) {
		try {
			if(nickname != null && message != null) {
				Message msg = new Message(nickname, message);
				messageQueue.put(msg);
				logger.info("Add message to [" + nickname + "]-[" + message + "] to messageQueue");
			}
			else {
				logger.info("received user is null, and message null.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private void sendMessageToAllInternal(Message message) {
		String receiver = message.getUser();
		logger.info("received user : " + receiver);
		AsyncContext ac = clients.get(receiver);
		try {
			if(ac != null) {
				sendMessageTo(ac, message.getMessage(), receiver);
			}
			else {
				logger.info("AsyncContext is null. Send Push Message....");
				// 채팅 알람 상태도 알아와서... 체크하고..
				
				// device type 알아와서...
				
				// 각 디바이드별..푸시를 보내자..
			}
		} catch (IOException e) {
			clients.remove(receiver);
			e.printStackTrace();
		}
		logger.info("Send message to [" + receiver + "]-[" + message.getMessage() + "]");
	}
	
	private void sendMessageTo(AsyncContext ac, ChatMessageVo message, String receiver) throws IOException {
		logger.info(">>>>>>>>>> sendMessageTo <<<<<<<<<<");
		
		HttpServletResponse res = (HttpServletResponse) ac.getResponse();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chat_message", message);
		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType("application/json;charset=UTF-8");
		res.setCharacterEncoding("UTF-8");
		res.getWriter().write("" + JSONObject.fromObject(map).toString() + "");

		ac.complete();
		
		ChatRoomDAO crdao = new ChatRoomDAO();
		if(receiver.equals(message.getSeller_email())) {
			// seller update latest time..
			crdao.updateLatestToSeller(message.getTalk_room_id());
		}
		else {
			// buyers update latest time..
			crdao.updateLatestToBuyers(message.getTalk_room_id());
		}
	}

	public void close() {
		running = false;
		messageHandlerThread.interrupt();
		logger.info("Stopped Message Handler.");

		List<AsyncContext> list = new ArrayList<AsyncContext>(clients.values());
		for (AsyncContext ac : list) {
			ac.complete();
		}
		logger.info("Complete All Client AsyncContext.");
	}
}
