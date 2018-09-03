package cn.edu.bupt.rsx.htmlparser.tools;


import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;


public class ConfigTools{
	
	private static final Logger logger = Logger.getLogger(ConfigTools.class);
	
	private static PropertiesConfiguration config = new PropertiesConfiguration();
	
	static {
		try{
			init("config_server.properties","application.properties");	
		}catch(Exception e){
			logger.error("Configure init error:",e);
		}
	}
	
	private static void init(String ... propertiesFilePaths) throws ConfigurationException{
		
		for(String propertiesFilePath : propertiesFilePaths){
			URL url = ConfigTools.class.getClassLoader().getResource(propertiesFilePath);
			if(url!=null){
				config.load(url);
			}
		}
	}
	
	public static String getProperty(String key, String defaultValue) {
		return config.getString(key, defaultValue);
	}

	public static boolean getProperty(String key, boolean defaultValue) {
		return config.getBoolean(key, defaultValue);
	}

	public static int getProperty(String key, int defaultValue) {
		return config.getInt(key, defaultValue);
	}

	public static long getProperty(String key, long defaultValue) {
		return config.getLong(key, defaultValue);
	}
	
}
