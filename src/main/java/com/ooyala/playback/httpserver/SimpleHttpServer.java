package com.ooyala.playback.httpserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ooyala.qe.common.exception.OoyalaException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SimpleHttpServer {

	public static Logger logger = Logger.getLogger(SimpleHttpServer.class);

	private static HttpServer server;

	public static void startServer() throws OoyalaException {
		try {
			server = HttpServer.create(new InetSocketAddress(8000), 0);
			server.createContext("/js", new GetHandler());
			server.setExecutor(null); // creates a default executor
			server.start();
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
				// add the required response header for a PDF file
				Headers h = t.getResponseHeaders();
				h.add("Content-Type", "text/javascript");

				// a PDF (you provide your own!)
				File file = new File("./src/test/resources/js/" + fileName);
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

	// public static void main(String[] args) throws IOException {
	// HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
	// server.createContext("/js", new GetHandler());
	// server.setExecutor(null); // creates a default executor
	// server.start();
	// }
}
