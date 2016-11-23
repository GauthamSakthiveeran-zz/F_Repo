package com.ooyala.playback.url;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
	private static Logger logger = Logger.getLogger(UrlGenerator.class);

	/**
	 * @param embedcode
	 *            ,pcode,videoPlugin,adPlugin,additionalPlugin,
	 *            playerConfigParameter
	 * @return returns dynamically created link from above parameters
	 */
	public static String getURL(String embedcode, String pcode, String pbid,
			String videoPlugin, String adPlugin, String additionalPlugin,
			String playerConfigParameter) {

		String environment = System.getProperty("environment");
		if ((environment == null || environment.equals(""))) {
			playerProperties.put(PlayerPropertyKey.ENVIRONMENT,
					PlayerPropertyValue.STAGING);
		} else if (environment.equalsIgnoreCase("PRODUCTION")) {
			String v4Version = System.getProperty("v4Version");
			if (v4Version == null || v4Version.equals("")
					|| v4Version.equals("candidate/latest")) {
				logger.error("Please Provide V4 Version of Production Instance");
				logger.info("Running test on STAGING Environment as v4Version pointing to staging");
				playerProperties.put(PlayerPropertyKey.ENVIRONMENT,
						PlayerPropertyValue.STAGING);
			} else {
				logger.info("V4 Version is :: " + v4Version);
				playerProperties.put(PlayerPropertyKey.ENVIRONMENT,
						PlayerPropertyValue.PRODUCTION);
			}
		}

		test = new TestPage(playerProperties);
		url = test.getURL(embedcode, pcode, pbid, videoPlugin, adPlugin,
				additionalPlugin, playerConfigParameter);
		return url;
	}

	// TODO - need to figure out where to place this appro

	public static List<Url> filterTestDataBasedOnTestName(String testName,
			Testdata testData) {
		for (Test data : testData.getTest()) {
			if (data.getName().equals(testName)) {
				return data.getUrl();
			}
		}
		return null;
	}

	/**
	 *
	 * @param testName
	 * @param testData
	 *            Passing node list so that we can have access to the nodes data
	 *            from xml file
	 * @return output : output contains two dimentional Object in which test
	 *         name and url is returned
	 */
	public static List<String> parseXmlDataProvider(String testName,
			Testdata testData) {
		logger.info("Getting test url and test name from property file");

		List<String> urlsGenerated = new LinkedList<String>();
		for (Test data : testData.getTest()) {
			if (data.getName().equals(testName)) {
				List<Url> urls = data.getUrl();
				for (Url url : urls) {
					String embedCode = url.getEmbedCode().getName();
					// String embedCode = test.;
					String pCode = url.getPcode().getName();
					String videoPlugin = url.getPlugins().getName();
					String adPlugin = url.getAdPlugins().getName();
					String additionalPlugin = url.getAdditionalPlugins()
							.getName();
					String playerParameter = new String(url
							.getPlayerParameter().getBytes());
					String pbid = url.getPbid().getName();
					String urlGenerated = UrlGenerator.getURL(embedCode, pCode,
							pbid, videoPlugin, adPlugin, additionalPlugin,
							playerParameter);
					urlsGenerated.add(urlGenerated);

				}
			}
		}
		return urlsGenerated;
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
						map.put("pbid", eElement.getElementsByTagName("pbid")
								.item(0).getAttributes().getNamedItem("name")
								.getNodeValue());

					}
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return map;
	}
}