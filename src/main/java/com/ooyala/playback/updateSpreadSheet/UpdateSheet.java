package com.ooyala.playback.updateSpreadSheet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import org.testng.ITestResult;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.ooyala.qe.common.exception.OoyalaException;
import com.sun.jna.platform.win32.OaIdl.DECIMAL;
import com.google.api.services.sheets.v4.Sheets;

/**
 * Created by jitendra on 4/1/17.
 */
public class UpdateSheet {

	/** Create linkedHasMap for storing data */
	public static LinkedHashMap<String, String> testSheetData = new LinkedHashMap<String, String>();

	public static String regressionFileName;

	/** Application name. */
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/sheets.googleapis.com-java-quickstart.json");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/sheets.googleapis.com-java-quickstart.json
	 */
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws Exception
	 */
	public static Credential authorize() throws Exception {
		/*
		 * // Load client secrets. try { InputStream in =
		 * UpdateSheet.class.getResourceAsStream("/client_secret.json");
		 * GoogleClientSecrets clientSecrets =
		 * GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		 * 
		 * // Build flow and trigger user authorization request.
		 * GoogleAuthorizationCodeFlow flow = new
		 * GoogleAuthorizationCodeFlow.Builder( HTTP_TRANSPORT, JSON_FACTORY,
		 * clientSecrets, SCOPES) .setDataStoreFactory(DATA_STORE_FACTORY)
		 * .setAccessType("offline") .build(); Credential credential = new
		 * AuthorizationCodeInstalledApp( flow, new
		 * LocalServerReceiver()).authorize("playbackqa@gmail.com");
		 * System.out.println( "Credentials saved to " +
		 * DATA_STORE_DIR.getAbsolutePath()); return credential; } catch
		 * (Exception e){ e.printStackTrace(); return null; }
		 */

		InputStream in = new FileInputStream(new File("client_secret.json"));

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;

	}

	/**
	 * Build and return an authorized Sheets API client service.
	 * 
	 * @return an authorized Sheets API client service
	 * @throws Exception
	 */
	public static Sheets getSheetsService() throws Exception {
		Credential credential = authorize();
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}

	private static String getSpreadSheetId() {
		String spreadsheetId = System.getProperty("spreadSheetId");
		if (spreadsheetId != null && !spreadsheetId.isEmpty()) {
			return spreadsheetId;
		} else {
			return "1IPaTRGHgO6hPbmTIBUUhTn9jMtAdHNGCoAzU1HffVAY";
		}

	}

	private static int getSheetId(Sheets service, String spreadsheetId, String sheetName) throws IOException {
		int num_of_sheets = service.spreadsheets().get(spreadsheetId).execute().getSheets().size();
		int sheetId = 0;
		for (int i = 0; i < num_of_sheets; i++) {
			String sheetNames = service.spreadsheets().get(spreadsheetId).execute().getSheets().get(i).getProperties()
					.getTitle();
			if (sheetNames.equals(sheetName))
				sheetId = service.spreadsheets().get(spreadsheetId).execute().getSheets().get(i).getProperties()
						.getSheetId();
		}
		return sheetId;
	}
	
	public static void writetosheet(String sheetName, String testCaseName, String platform, String browser,
			String version, String v4, TestResult result) {

		try {
			Sheets service = getSheetsService();
			String spreadsheetId = getSpreadSheetId();

			String range = sheetName + "!A2:G"; // TODO

			int sheetId = getSheetId(service, spreadsheetId, sheetName);

			ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();

			List<List<Object>> values = response.getValues();
			
			
			int i = 1;
			if (values == null || values.size() == 0) {
				throw new OoyalaException("Spread sheet is empty");
			} else {
				for (List row : values) {

					if (testCaseName.equals(row.get(1).toString())) {
						int col = 8; // TODO
						List<CellData> cellData = new ArrayList<>();
						cellData.add(new CellData()
								.setUserEnteredValue(new ExtendedValue().setStringValue(result.getValue()))
								.setUserEnteredFormat(new CellFormat().setBackgroundColor(result.getColor())));
						List<Request> requests = new ArrayList<>();
						requests.add(new Request().setUpdateCells(new UpdateCellsRequest()
								.setStart(new GridCoordinate().setSheetId(sheetId).setRowIndex(i).setColumnIndex(col))
								.setRows(Arrays.asList(new RowData().setValues(cellData)))
								.setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

						BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
								.setRequests(requests);
						service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
						System.out.println("Data written to spreadsheet");
						break;
					}
					i++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getJenkinsJobLink(String browser) {
		String testSuitename;
		testSuitename = System.getProperty("groups");
		if (testSuitename == null) {
			testSuitename = "default";
		}
		regressionFileName = System.getProperty("tests");
		String jenkinsJobName = "";

		if (regressionFileName != null) {
			switch (testSuitename) {
			case "playerFeatures":
				jenkinsJobName = "playbackweb-playerFeature";
				break;
			case "drm":
				jenkinsJobName = "playbackweb-drm";
				break;
			case "streams":
				jenkinsJobName = "playbackweb-streams";
				break;
			case "FCC":
				jenkinsJobName = "playbackweb-FCC";
				break;
			case "playlist":
				jenkinsJobName = "playbackweb-playlist";
				break;
			case "syndicationRules":
				jenkinsJobName = "playbackweb-syndicationRules";
				break;
			case "default":
				if (regressionFileName.contains("VTC_Regression.xml")) {
					jenkinsJobName = "playbackwebvtc";
					break;
				}
				if (regressionFileName.contains("amf_testng.xml")) {
					switch (browser) {
					case "chrome":
						jenkinsJobName = "playbackweb-AMF-Chrome";
						break;
					case "firefox":
						jenkinsJobName = "playbackweb-AMF-FF";
						break;
					case "safari":
						jenkinsJobName = "playbackweb-AMF-Safari";
						break;
					case "internet explorer":
						jenkinsJobName = "playbackweb-AMF-IE";
						break;
					}
				}
			}
		}
		return ParseJenkinsJobLink.getJenkinsBuild(jenkinsJobName);
	}

	protected static String getV4Version(String branch) {
		String v4Version = "";
		String link = "http://player.ooyala.com/static/v4/candidate/latest/version.txt";

		if (branch.equalsIgnoreCase("candidate")) {
			link = "http://player.ooyala.com/static/v4/candidate/latest/version.txt";
		} else if (branch.equalsIgnoreCase("stable")) {
			link = "http://player.ooyala.com/static/v4/stable/latest/version.txt";
		}

		try {
			URL lnk = new URL(link);
			BufferedReader in = new BufferedReader(new InputStreamReader(lnk.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("Version:")) {
					String[] _version = inputLine.split(" ");
					v4Version = _version[1];
					break;
				}
			}
			in.close();
		} catch (Exception ex) {
			System.out.println("Error occured while reading V4 version in Utils.getV4Version()");
		}
		return v4Version;
	}

	public static void main(String[] args) throws IOException {

		String description = "Sheet1:ABC";

		String testCaseName = description.split(":")[1];
		System.out.println(testCaseName);
		String sheetName = description.split(":")[0];

		String platform = "Windows NT";
		String browser = "Chrome";
		String version = "57";

		String v4 = "4.12.1";

		writetosheet(sheetName, testCaseName, platform, browser, version, v4, TestResult.PASSED);
		writetosheet(sheetName, "DEF", platform, browser, version, v4, TestResult.FAILED);
		writetosheet(sheetName, "PQR", platform, browser, version, v4, TestResult.PASSED);
	}
}
