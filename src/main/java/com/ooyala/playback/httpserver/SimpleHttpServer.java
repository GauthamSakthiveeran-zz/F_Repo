package com.ooyala.playback.httpserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.ooyala.qe.common.exception.OoyalaException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class SimpleHttpServer {

	public static Logger logger = Logger.getLogger(SimpleHttpServer.class);

	private static HttpServer server;
	public static int portNumber;

	public static void startServer(int portNum) throws OoyalaException {
		try {

			logger.info("Server is getting started on port " + portNum);
			server = HttpServer.create(new InetSocketAddress(portNum), 0);
			server.createContext("/js", new GetHandler());
			server.setExecutor(null); // creates a default executor
			server.start();
			portNumber = portNum;
			logger.info("***** Server Started on port:" + portNumber + "****");
		} catch (Exception ex) {
			logger.info("Not able to start the http server: "
					+ ex.getLocalizedMessage());
			new OoyalaException("Not able to start the httpserver:"
					+ ex.getMessage());
		}
	}

	public static void stopServer() throws OoyalaException {
		try {
			server.stop(2);
			logger.info("Stopped the http server");
		} catch (Exception ex) {
			logger.info("Not able to stop the http server:"
					+ ex.getLocalizedMessage());
			new OoyalaException("Not able to stop the httpserver:"
					+ ex.getMessage());
		}
	}

	static class GetHandler implements HttpHandler {
		public void handle(HttpExchange t) {
			try {

				logger.info("in server");

				Map<String, String> queryParams = queryToMap(t.getRequestURI()
						.getQuery());
				logger.info("File being served by httpserver is "
						+ queryParams.get("fileName"));
				String fileName = queryParams.get("fileName");
				// add the required response header for a js file
				Headers h = t.getResponseHeaders();
				h.add("Content-Type", "text/javascript");

				File file = new File("./src/main/resources/js/" + fileName);
				byte[] bytearray = new byte[(int) file.length()];
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(bytearray, 0, bytearray.length);
				logger.info("written file content as response");

				// ok, we are ready to send the response.
				t.sendResponseHeaders(200, file.length());
				logger.info("Served the javascript file as response");
				OutputStream os = t.getResponseBody();
				os.write(bytearray, 0, bytearray.length);
				os.close();
				fis.close();
				bis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * returns the url parameters in a map
	 * 
	 * @param query
	 * @return map
	 */
	public static Map<String, String> queryToMap(String query) {
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1) {
				result.put(pair[0], pair[1]);
			} else {
				result.put(pair[0], "");
			}
		}
		return result;
	}
	
	public static int getRandomOpenPort() {
        int retry = 4;
        int index = 1;
        while (index < retry) {
            int min = 10000;
            int max = 50000;
            Random rand = new Random();
            int randomPort = min + rand.nextInt((max - min) + 1);
            boolean isPortOpen = checkPort(randomPort);
            if (isPortOpen)
                return randomPort;
            index++;
        }
        return -1;
    }

    private static boolean checkPort(int portNumber) {
        try {
            logger.info("Checking if port open by trying to connect as a client");
            Socket socket = new Socket("localhost", portNumber);
            socket.close();
            logger.info("Port looks like is not open " + portNumber);
        } catch (Exception e) {
            if (e.getMessage().contains("refused")) {
                return true;
            }
        }
        return false;
    }

}
