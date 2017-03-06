package com.ooyala.playback.updateSpreadSheet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.ITestResult;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.TextFormat;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ooyala.qe.common.exception.OoyalaException;

public class TestCaseSheet {
	
	private static Logger logger = Logger.getLogger(TestCaseSheet.class);

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

		InputStream in = new FileInputStream(new File("client_secret.json"));

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		logger.info("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
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
			return "1q0FLf0Oq5KwMzcHjMUlgLG0Dyw8po8-fBLVreFAemic";
		}

	}

	private static int getSheetId(List<Sheet> sheets, String spreadsheetId, String sheetName) throws IOException {

		int num_of_sheets = sheets.size();
		int sheetId = 0;
		for (int i = 0; i < num_of_sheets; i++) {
			String sheetNames = sheets.get(i).getProperties().getTitle();
			if (sheetNames.equals(sheetName))
				sheetId = sheets.get(i).getProperties().getSheetId();
		}
		return sheetId;
	}

	public static void update(Map<String, ITestResult> testDetails, String platform, String browser, String browserVersion,
			String v4Version) throws Exception {

		Sheets service = getSheetsService();
		String spreadsheetId = getSpreadSheetId();
		HashMap<String, ValueRange> sheetNameList = new HashMap<>();
		List<Request> requests = new ArrayList<>();

		String pattern = "MM/dd/yyyy";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String date = format.format(new Date());
		
		List<Sheet> sheets  = service.spreadsheets().get(spreadsheetId).execute().getSheets();

		for (Map.Entry<String, ITestResult> entry : testDetails.entrySet()) {
			
			String[] singleTests = entry.getKey().split("\\|");

			if (singleTests == null || singleTests.length <= 0) {
				singleTests = new String[1];
				singleTests[0] = entry.getKey();
			}
			
			for (String singleTest : singleTests) {
				
				String sheetName = "";
				String testCaseName = "";
				TestResult testResult = TestResult.SKIPPED;
				
				
				if (singleTest.contains(":")) {
					sheetName = singleTest.split(":")[0];
					testCaseName = singleTest.split(":")[1];
					ITestResult result = entry.getValue();
					
					if (result.getStatus() == ITestResult.SUCCESS) {
						testResult = TestResult.PASSED;
					} else if (result.getStatus() == ITestResult.FAILURE) {
						testResult = TestResult.FAILED;
					} else if (result.getStatus() == ITestResult.SKIP) {
						testResult = TestResult.SKIPPED;
					} else{
						testResult = TestResult.SKIPPED;
					}
				} else {
					testCaseName = entry.getKey();
					sheetName = null;
					testResult = TestResult.UNKOWN;
					continue;
				}
				
				int sheetId = getSheetId(sheets, spreadsheetId, sheetName);

				if (sheetNameList.size() == 0 || !sheetNameList.containsKey(sheetName)) {
					List<CellData> cellData = new ArrayList<>();
					cellData.add(new CellData()
							.setUserEnteredValue(new ExtendedValue().setStringValue(platform + "\n" + browser + " "
									+ browserVersion + "\n" + getV4Version(v4Version) + "\n" + date))
							.setUserEnteredFormat(new CellFormat().setTextFormat(new TextFormat().setBold(true))
									.setWrapStrategy("WRAP")));

					requests.add(new Request().setUpdateCells(new UpdateCellsRequest()
							.setStart(new GridCoordinate().setSheetId(sheetId).setRowIndex(3).setColumnIndex(6)) // TODO
							.setRows(Arrays.asList(new RowData().setValues(cellData)))
							.setFields("userEnteredValue,userEnteredFormat.backgroundColor")));
					
					String range = sheetName + "!A1:Z"; // TODO
					ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
					sheetNameList.put(sheetName, response);
					logger.info("Column header " + sheetName);
				}
				
				List<List<Object>> values = sheetNameList.get(sheetName).getValues();

				int i = 3; // TODO

				if (values == null || values.size() == 0) {
					logger.error("Spread sheet is empty");
					throw new OoyalaException("Spread sheet is empty");
				} else {

					for (List row : values) {
						if(row.size()==0){
							i++;
							continue;
						}
						if (testCaseName.equals(row.get(2).toString())) { // TODO
							int col = 6; // TODO
							List<CellData> cellData = new ArrayList<>();
							cellData.add(new CellData()
									.setUserEnteredValue(
											new ExtendedValue().setStringValue(testResult.getValue()))
									.setUserEnteredFormat(
											new CellFormat().setBackgroundColor(testResult.getColor())));
							requests.add(new Request().setUpdateCells(new UpdateCellsRequest()
									.setStart(new GridCoordinate().setSheetId(sheetId).setRowIndex(i).setColumnIndex(col))
									.setRows(Arrays.asList(new RowData().setValues(cellData)))
									.setFields("userEnteredValue,userEnteredFormat.backgroundColor")));
							logger.info("Row Details " + testCaseName);

							break;
						}
						i++;
					}
				}
			}
			

			


		}

		BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
				.setRequests(requests);
		service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
		logger.info("Data written to spreadsheet");
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
			logger.info("Error occured while reading V4 version in Utils.getV4Version()");
			return branch;
		}
		return v4Version;
	}
	
	
	private static int getColumnNumber(List<List<Object>> values){
		
		int columnNumber  = 0;
		
		for(List<Object> row : values){
			if(row==null){
				continue;
			}
		}
		
		return columnNumber;
	}

	public static void main(String[] args) throws IOException {

		/*String description = "Sheet1:ABC";

		String testCaseName = description.split(":")[1];
		String sheetName = description.split(":")[0];

		String platform = "Windows NT";
		String browser = "Chrome";
		String version = "57";

		String v4 = "4.12.1";

		List<TestDetails> testDetails = Collections.synchronizedList(new ArrayList<TestDetails>());
		TestDetails tests = new TestDetails();
		tests.setTestCaseName("ABC");
		tests.setSheetName("Sheet1");
		tests.setTestResult(TestResult.PASSED);
		testDetails.add(tests);

		tests = new TestDetails();
		tests.setTestCaseName("ABC");
		tests.setSheetName("Sheet2");
		tests.setTestResult(TestResult.PASSED);
		testDetails.add(tests);

		tests = new TestDetails();
		tests.setTestCaseName("XYZ");
		tests.setSheetName("Sheet1");
		tests.setTestResult(TestResult.FAILED);
		testDetails.add(tests);

		try {
			update(testDetails, platform, browser, version, "candidate");
		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}
}
