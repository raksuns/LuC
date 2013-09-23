
package com.luxsky.chat.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * A utility class for loading a property file and then
 * retrieving the values for a given key.
 *
 * @author <a href="mailto:ynkrish@india.hp.com">Krishnan Y N</a>
 * @version    1.1
 */
public class Property
{
	private static final String ETALK_PROPERTIES_FILE = "global.properties";
	/** The java.util.Properties object */
	private Properties				m_pProperties;

	/** Line separator */
	private static String			LS				= System.getProperty("line.separator");

	private static Property	instance;

	public Property() throws Exception {
		this(ETALK_PROPERTIES_FILE);
	}

	/**
	 * Loads the property file that is passed as a parameter
	 *
	 * @param sFileName  The Property file that needs to be loaded
	 * @throws Exception in case an error happens while loading
	 * the specified property file
	 */
	public Property(String sFileName) throws Exception {
		InputStream fileinputStream = getClass().getResourceAsStream("/" + sFileName);
		//InputStream fileinputStream = getClass().getResourceAsStream(sFileName);
		//File file = new File(sFileName);
		//FileInputStream fileinputStream = new FileInputStream(file);
		m_pProperties = new Properties();
		try {
			m_pProperties.load(fileinputStream);
		}
		catch(IOException ioe) {
			StringBuffer sbErrMsg = new StringBuffer(50);
			sbErrMsg.append("[Cannot find properties file :");
			sbErrMsg.append(sFileName).append("  ");
			sbErrMsg.append(LS);
			sbErrMsg.append(ioe.getMessage());
			sbErrMsg.append(LS).append("]");
			throw new Exception(sbErrMsg.toString());
		}
		finally {
			if(fileinputStream != null) {
				try {
					fileinputStream.close();
				}
				catch(IOException ioex) {
					throw new Exception("Unable to close inputstream", ioex);
				}
			}
		}
	}

	public static Property getInstance() {
		return getInstance(ETALK_PROPERTIES_FILE);
	}

	public static Property getInstance(String sFileName) {
		if(instance == null) {
			try {
				instance = new Property(sFileName);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	/**
	 * Returns the Value for the requested key by querying the properties file
	 *
	 * @param sKey  The key for which the value needs to be retrieved
	 * @return String the value in this property list with the
	 * specified key value.
	 */
	public String getPropertyValue(String sKey) {
		return m_pProperties.getProperty(sKey);
	}

	public int getPropertyIntValue(String sKey) {
		String value = m_pProperties.getProperty(sKey);
		return Integer.parseInt(value);
	}

	public boolean getPropertyBooleanValue(String sKey) {
		try {
			String value = m_pProperties.getProperty(sKey);
			return Boolean.valueOf(value).booleanValue();
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean getPropertyBooleanValue(String sKey, boolean defaultValue) {
		try {
			String value = m_pProperties.getProperty(sKey);
			if(value == null)
				return defaultValue;

			return Boolean.valueOf(value).booleanValue();
		}
		catch(Exception e) {
			return defaultValue;
		}
	}

	public float getPropertyFloatValue(String sKey) {
		String value = m_pProperties.getProperty(sKey);
		if(null != value) {
			return Float.parseFloat(value);
		}

		return 0.0f;
	}

	public double getPropertyDoubleValue(String sKey) {
		String value = m_pProperties.getProperty(sKey);
		if(null != value) {
			return Double.parseDouble(value);
		}

		return 0.0;
	}

	/**
	 * Returns the Value for the requested key by querying the properties file, if
	 * no value is present, then the default value will be returned
	 *
	 * @param sKey  The key for which the value needs to be retrieved
	 * @param sDefaultValue  The default value
	 * @return String the value in this property list with the
	 * specified key value.
	 */
	public String getPropertyValue(String sKey, String sDefaultValue) {
		return m_pProperties.getProperty(sKey, sDefaultValue);
	}

	public int getPropertyIntValue(String sKey, int sDefaultValue) {
		String value = m_pProperties.getProperty(sKey);
		try {
			return Integer.parseInt(value);
		}
		catch(Exception e) {
			return sDefaultValue;
		}
	}

	public long getPropertyLongValue(String sKey, long sDefaultValue) {
		String value = m_pProperties.getProperty(sKey);
		try {
			return Long.parseLong(value);
		}
		catch(Exception e) {
			return sDefaultValue;
		}
	}

	/**
	 * Returns the Enumeration containing the list of all property names
	 *
	 * @return Enumeration enumeration of all property names
	 */

	public Enumeration getPropertyNames() {
		return m_pProperties.propertyNames();
	}
} //end of class
