package com.luxsky.chat.push;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletConfig;

import org.apache.log4j.Logger;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class AndroidPush {

	protected static Logger accessLogger = Logger.getLogger("ACCESS");
	protected static Logger errorLogger = Logger.getLogger("ERROR");
	
	private static final String PATH = "/android.key";

	private static final int MULTICAST_SIZE = 1000;
	
	private static AndroidPush instance;
	
	private AndroidPush() {
	}
	
	public synchronized static AndroidPush getInstance() {
		if(instance == null) {
			instance = new AndroidPush();
		}
		return instance;
	}

	private Sender sender;

	private static final Executor threadPool = Executors.newFixedThreadPool(5);
	
	protected Sender newSender(ServletConfig config) {
		String key = (String) config.getServletContext().getAttribute(ApiKeyInitializer.ATTRIBUTE_ACCESS_KEY);
		return new Sender(key);
	}

	private void asyncSend(List<String> devicesMacAddress, List<String> partialDevices) {
		// make a copy
		final List<String> devices = new ArrayList<String>(partialDevices);
		final List<String> devicesMac = new ArrayList<String>(devicesMacAddress);
		
		threadPool.execute(new Runnable() {

			public void run() {
				Message message = new Message.Builder().build();
				MulticastResult multicastResult;
				
				try {
					multicastResult = sender.send(message, devices, 5);
				}
				catch (IOException e) {
					errorLogger.error("Error posting messages", e);
					return;
				}
				
				List<Result> results = multicastResult.getResults();
				// analyze the results
				for (int i = 0; i < devices.size(); i++) {
					String macAddress = devicesMac.get(i);
					String regId = devices.get(i);
					Result result = results.get(i);
					String messageId = result.getMessageId();
					if (messageId != null) {
						accessLogger.debug("Succesfully sent message to device: " + regId + "; messageId = " + messageId);
						String canonicalRegId = result.getCanonicalRegistrationId();
						if (canonicalRegId != null) {
							// same device has more than on registration id: update it
							accessLogger.info("canonicalRegId " + canonicalRegId);
							//DataStorage.updateRegistration(macAddress, regId, canonicalRegId);
							// Datastore.updateRegistration(regId, canonicalRegId);
						}
					}
					else {
						String error = result.getErrorCodeName();
						if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
							// application has been removed from device - unregister it
							errorLogger.error("Unregistered device: " + regId);
							// Datastore.unregister(regId);
							//DataStorage.unregister(macAddress, regId);
						}
						else {
							errorLogger.error("Error sending message to " + regId + ": " + error);
						}
					}
				}
			}
		});
	}
	
	public static void main(String[] args) {
		//AndroidPush.getInstance().push("");
	}
	
	//  regId에 입력 할 등록ID라는건 스마트 폰에서 클라이언트 앱을 실행하면 GCM 서비스에 폰을 등록하면서 발급받은 ID
	// 폰 마다 유일한 값이기때문에 테스트 할 폰의 값 - 163 글자 ?
	public boolean push(String regId, String msg, int badgeCount) {
		String key = getKey();
		Sender sender = new Sender(key);
		Message message = new Message.Builder().collapseKey("collapseKey"+System.currentTimeMillis())
		        .timeToLive(3)
		        .delayWhileIdle(true)
		        .addData("message", msg)
		        .build();
		
		Result result;
		try {
			result = sender.send(message, regId, 5);
			accessLogger.debug("======= Send ======");
			
			if (result.getMessageId() != null) {
				String canonicalRegId = result.getCanonicalRegistrationId();
				accessLogger.debug("canonicalRegId : " + canonicalRegId);
				
				if (canonicalRegId != null) {
					accessLogger.debug("same device has more than on registration ID: update database");
				}
				return true;
			}
			else {
				String error = result.getErrorCodeName();
				errorLogger.error("[ERROR]" + error);
				
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					// application has been removed from device - unregister database
				}
				return false;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			errorLogger.error("", e);
			return false;
		}
	}
	
	protected String getKey() {
		InputStream stream = AndroidPush.class.getResourceAsStream(PATH);
		if (stream == null) {
			throw new IllegalStateException("Could not find file " + PATH + " on web resources)");
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		try {
			String key = reader.readLine();
			return key;
		}
		catch (IOException e) {
			throw new RuntimeException("Could not read file " + PATH, e);
		}
		finally {
			try {
				reader.close();
			}
			catch (IOException e) {
				errorLogger.debug("Exception closing " + PATH, e);
			}
		}
	}
}
