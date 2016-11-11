package com.ooyala.playback.url;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jitendra
 */

/**
 * Read the Property file and put all the data into HashMap
 * 
 * @return return data stored into hash map. i.e dataMap
 */
public class PropertyReader {
	public static Map<String, String> getProperty(String fileName) {
		Properties properties;
		Map<String, String> dataMap = new HashMap<String, String>();
		try {
			properties = new Properties();
			properties.load(new FileInputStream(new File(fileName
					+ ".properties")));
			for (int i = 0; i < properties.keySet().size(); i++) {
				dataMap.put(properties.keySet().toArray()[i].toString(),
						properties.values().toArray()[i].toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}
}
