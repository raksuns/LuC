package com.luxsky.chat.push;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;
import javapns.exceptions.DuplicateDeviceException;
import javapns.exceptions.NullIdException;
import javapns.exceptions.UnknownDeviceException;

import org.apache.log4j.Logger;

import com.luxsky.chat.common.Property;

public class IPhonePush {
	
	protected static Logger accessLogger = Logger.getLogger("ACCESS");
	protected static Logger errorLogger = Logger.getLogger("ERROR");
	
    private static final int PORT = 2195;

    private static String passwd = "white4204";

	public static int RUN_MODE_DEVELOPMENT = 1;
	public static int RUN_MODE_PRODUCTION = 2;
	
	private int runMode = RUN_MODE_DEVELOPMENT;
	
	private static IPhonePush instance;
	
	private String pushDevKey = Property.getInstance().getPropertyValue("etalk.iphone.push.key.dev");
	private String pushProductKey = Property.getInstance().getPropertyValue("etalk.iphone.push.key.product");
	
	private IPhonePush() {
	}
	
	public synchronized static IPhonePush getInstance() {
		if(instance == null) {
			instance = new IPhonePush();
		}
		return instance;
	}
	
	public boolean push(String deviceToken, String message, int badgeCount) {
		
		PushNotificationManager pushManager = PushNotificationManager.getInstance();
		
		try {
			PayLoad payLoad = new PayLoad();
			payLoad.addAlert(message);
			payLoad.addBadge(badgeCount);
			payLoad.addSound("default");


			try {
				pushManager.addDevice("iPhone", deviceToken);
			}
			catch(DuplicateDeviceException dde) {
				try {
					pushManager.removeDevice("iPhone");
				} catch (UnknownDeviceException | NullIdException e) {
					e.printStackTrace();
				}
				pushManager.addDevice("iPhone", deviceToken);
			}
			
			String host = null;
			if (runMode == RUN_MODE_DEVELOPMENT) {
				host = "gateway.sandbox.push.apple.com";
				pushManager.initializeConnection(host, PORT, pushDevKey, passwd, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
			}
			else if (runMode == RUN_MODE_PRODUCTION) {
				host = "gateway.push.apple.com";
				pushManager.initializeConnection(host, PORT, pushProductKey, passwd, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
			}
			
			Device client = pushManager.getDevice("iPhone");
			pushManager.sendNotification(client, payLoad);
			pushManager.stopConnection();

			return true;
		}
		catch (Exception ex) {
			try {
				pushManager.removeDevice("iPhone");
			} catch (UnknownDeviceException | NullIdException e) {
				e.printStackTrace();
			}
			ex.printStackTrace();
			return false;
		}
	}
	
	public static void main(String... args) throws Exception{
		IPhonePush apns = new IPhonePush();
		apns.push("d74542a75b946a965b38e74e171191f77d8a04b840bc9f8a950b1bbc183aee10", "etalk push 테스트..", 3);
	}
}
