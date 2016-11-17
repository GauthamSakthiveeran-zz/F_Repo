package com.ooyala.playback.url;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by jitendra
 */
public class UrlGenerator {

	static TestPage test = null;
	static String url;
	static Map<PlayerPropertyKey, PlayerPropertyValue> playerProperties = new HashMap<PlayerPropertyKey, PlayerPropertyValue>();
	private static Logger logger = Logger.getLogger(TestPageData.class);

	/**
	 * @param embedcode
	 *            ,pcode,videoPlugin,adPlugin,additionalPlugin,
	 *            playerConfigParameter
	 * @return returns dynamically created link from above parameters
	 */
	public static String getURL(String embedcode, String pcode,String pbid,
			String videoPlugin, String adPlugin, String additionalPlugin,
			String playerConfigParameter) {
		if (System.getProperty("environment").equals("PRODUCTION")){
			playerProperties.put(PlayerPropertyKey.ENVIRONMENT,
					PlayerPropertyValue.PRODUCTION);
		}else{
			playerProperties.put(PlayerPropertyKey.ENVIRONMENT,
					PlayerPropertyValue.STAGING);git agit a
		}

		test = new TestPage(playerProperties);
		url = test.getURL(embedcode, pcode, pbid,videoPlugin, adPlugin,
				additionalPlugin, playerConfigParameter);
		logger.info("URL : " + url);
		return url;
	}

	/**
	 *
	 * @param testName
	 * @param nodes
	 *            Passing node list so that we can have access to the nodes data
	 *            from xml file
	 * @return output : output contains two dimentional Object in which test
	 *         name and url is returned
	 */
	public static Object[][] parseXmlDataProvider(String testName,
			NodeList nodes) {
		logger.info("Getting test url and test name from property file");
		Map<String, String> map = UrlGenerator.ReadDataFromXML(testName, nodes);
		Object[][] output = new Object[1][2];
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue().equals(testName)) {
				String embedCode = map.get("embedCode");
				String pCode = map.get("pcode");
				String videoPlugin = map.get("plugins");
				String adPlugin = map.get("adPlugins");
				String additionalPlugin = map.get("additionalPlugins");
				String playerParameter = map.get("playerParameter");
				output[0][0] = entry.getKey();
				String pbid = map.get("pbid");
				output[0][1] = UrlGenerator.getURL(embedCode, pCode,pbid,
						videoPlugin, adPlugin, additionalPlugin,
						playerParameter);
				break;
			}
		}
		return output;
	}

	/**
	 * @param testName
	 *            name of the test which is currently running
	 * @param nList
	 *            nList has all the NodeList which are present in .xml file
	 *            where we put all the required data needs to create a dynamic
	 *            url
	 * @return returns the map which contains all the required parameter for
	 *         creating url
	 */
	public static Map<String, String> ReadDataFromXML(String testName,
													  NodeList nList) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					if (testName.contains(nNode.getAttributes()
							.getNamedItem("name").getNodeValue())) {
						Element eElement = (Element) nNode;

						map.put("testName",
								nNode.getAttributes().getNamedItem("name")
										.getNodeValue());
						map.put("description",
								eElement.getElementsByTagName("description")
										.item(0).getAttributes()
										.getNamedItem("name").getNodeValue());
						map.put("embedCode",
								eElement.getElementsByTagName("embed_code")
										.item(0).getAttributes()
										.getNamedItem("name").getNodeValue());
						map.put("pcode", eElement.getElementsByTagName("pcode")
								.item(0).getAttributes().getNamedItem("name")
								.getNodeValue());
						map.put("plugins",
								eElement.getElementsByTagName("plugins")
										.item(0).getAttributes()
										.getNamedItem("name").getNodeValue());
						map.put("adPlugins",
								eElement.getElementsByTagName("adPlugins")
										.item(0).getAttributes()
										.getNamedItem("name").getNodeValue());
						map.put("additionalPlugins", eElement
								.getElementsByTagName("additionalPlugins")
								.item(0).getAttributes().getNamedItem("name")
								.getNodeValue());
						map.put("playerParameter", eElement
								.getElementsByTagName("playerParameter")
								.item(0).getTextContent());
						map.put("pbid",eElement
								.getElementsByTagName("pbid")
								.item(0).getAttributes()
								.getNamedItem("name").getNodeValue());
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return map;
	}
}


