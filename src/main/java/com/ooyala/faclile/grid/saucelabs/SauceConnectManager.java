package com.ooyala.faclile.grid.saucelabs;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;

import com.ooyala.faclie.util.ReadPropertyFile;

// TODO: Auto-generated Javadoc
/**
 * The Class SauceConnectManager.
 */
public class SauceConnectManager {

	/** The logger. */
	public static Logger logger = Logger.getLogger(SauceConnectManager.class);

	/** The p. */
	public static Process p = null;

	/** The Constant SAUCE_CONFIG_PATH. */
	public static final String SAUCE_CONFIG_PATH = "/config/sauce.properties";

	/**
	 * Start sauce connect.
	 */
	public static void startSauceConnect() {

		URL sauceConnect = SauceConnectManager.class.getClass().getResource(
				"/lib/Sauce-Connect.jar");
		String command = "java -jar "
				+ sauceConnect.getPath()
				+ " "
				+ ReadPropertyFile.getConfigurationParameter(SAUCE_CONFIG_PATH,
						"SAUCE_USERNAME")
				+ " "
				+ ReadPropertyFile.checkConfigParameter(SAUCE_CONFIG_PATH,
						"SAUCE_API_KEY")
				+ "  -p localhost:9090 -tunnel-identifier \"browsermobproxy_tunnel\" -readyfile \"/tmp/sauce_readyfe.txt\"";

		try {
			logger.info("starting sauce connect...");
			p = Runtime.getRuntime().exec(command);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String s = null;

			// read the output from the command
			while ((s = stdInput.readLine()) != null) {
				logger.info(s);
				if (s.contains("Connected! You may start your tests.")) {
					stdInput.close();
					break;
				}
			}

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				logger.error(s);
				stdError.close();
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Stop sauce connect.
	 */
	public static void stopSauceConnect() {

		if (p != null) {
			logger.info("stopping sauce connect");
			p.destroy();
		}
	}
}
