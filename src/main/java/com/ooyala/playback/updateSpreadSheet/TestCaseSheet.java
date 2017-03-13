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
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.CutPasteRequest;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.TextFormat;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ooyala.qe.common.exception.OoyalaException;

public class TestCaseSheet {
	
	private static Logger logger = Logger.getLogger(TestCaseSheet.class);

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
		
		String user = "user";
		
		if(TestCaseSheetProperties.email_address!=null){
			user = TestCaseSheetProperties.email_address;
		}
		
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(user);
		
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
		} else if(TestCaseSheetProperties.spreadSheetId!=null){
			return TestCaseSheetProperties.spreadSheetId;
		}else{
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
	
	
	private static TestResult getTestResult(ITestResult result, String testCaseName) {

		String[] failures = result.getThrowable().getMessage().split(":")[1].trim().split(",");
		if (failures == null || failures.length <= 0) {
			failures[0] = result.getThrowable().getMessage().split(":")[1].trim();
		}

		if (result.getStatus() == ITestResult.SUCCESS) {
			return TestResult.PASSED;
		}
		if (result.getStatus() == ITestResult.FAILURE) {

			for (String failure : failures) {
				if (testCaseName.toLowerCase().contains(failure.toLowerCase())) {
					return TestResult.FAILED;
				}
			}
			return TestResult.PASSED;
		}
		if (result.getStatus() == ITestResult.SKIP) {
			return TestResult.SKIPPED;
		}
		return TestResult.SKIPPED;
	}

	public static void update(Map<String, ITestResult> testDetails, String platform, String browser,
			String browserVersion, String v4Version) throws Exception {

		if (testDetails == null || testDetails.isEmpty())
			return;

		Sheets service = getSheetsService();
		String spreadsheetId = getSpreadSheetId();
		HashMap<String, TestCaseData> sheetNameList = new HashMap<>();

		String pattern = TestCaseSheetProperties.dateFormat;

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String date = format.format(new Date());

		List<Sheet> sheets = service.spreadsheets().get(spreadsheetId).execute().getSheets();
		

		for (Map.Entry<String, ITestResult> entry : testDetails.entrySet()) {
			
			List<Request> requests = new ArrayList<>();

			String[] singleTests = entry.getKey().split(TestCaseSheetProperties.delimiterForDifferentTests);

			if (singleTests == null || singleTests.length <= 0) {
				singleTests = new String[1];
				singleTests[0] = entry.getKey();
			}

			for (String singleTest : singleTests) {

				String sheetName = "";
				String testCaseName = "";
				TestResult testResult = TestResult.SKIPPED;

				if (singleTest.contains(TestCaseSheetProperties.delimiterForTabAndTest)) {
					sheetName = singleTest.split(TestCaseSheetProperties.delimiterForTabAndTest)[0];
					testCaseName = singleTest.split(TestCaseSheetProperties.delimiterForTabAndTest)[1];
					ITestResult result = entry.getValue();
					testResult = getTestResult(result, testCaseName);

				} else {
					testCaseName = entry.getKey();
					logger.error("No matching data found in excel for " + testCaseName);
					sheetName = null;
					testResult = TestResult.UNKOWN;
					continue;
				}

				TestCaseData testCaseData = null;

				if (!sheetNameList.containsKey(sheetName)) {
					String range = sheetName + TestCaseSheetProperties.sheetRangeForInitialReading;
					ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
					int sheetId = getSheetId(sheets, spreadsheetId, sheetName);

					List<List<Object>> values = response.getValues();

					String resultColumnTitle = platform + "\n" + browser + " " + browserVersion + "\n"
							+ getV4Version(v4Version) + "\n" + date;

					testCaseData = parseData(values, resultColumnTitle, requests, sheetId);

					if (testCaseData.getHeaderRowNumber() == -1 || testCaseData.getHeaderColumnNumber() == -1
							|| testCaseData.getTestCaseColumnNumber() == -1) {
						throw new OoyalaException("Error while formatting the excel sheet");
					}

					sheetNameList.put(sheetName, testCaseData);

					List<CellData> cellData = new ArrayList<>();
					cellData.add(
							new CellData().setUserEnteredValue(new ExtendedValue().setStringValue(resultColumnTitle))
									.setUserEnteredFormat(new CellFormat().setTextFormat(new TextFormat().setBold(true))
											.setWrapStrategy("WRAP")));

					requests.add(
							new Request()
									.setUpdateCells(
											new UpdateCellsRequest()
													.setStart(new GridCoordinate().setSheetId(sheetId)
															.setRowIndex(testCaseData.getHeaderRowNumber())
															.setColumnIndex(testCaseData.getHeaderColumnNumber()))
													.setRows(Arrays.asList(new RowData().setValues(cellData)))
													.setFields("userEnteredValue,userEnteredFormat.textFormat")));
				}

				if (testCaseData == null) {
					testCaseData = new TestCaseData();
					testCaseData = sheetNameList.get(sheetName);
				}

				if (testCaseData != null && testCaseData.getTestCaseMap().get(testCaseName)!=null) {
					int rowNumber = testCaseData.getTestCaseMap().get(testCaseName);
					
					List<CellData> cellData = new ArrayList<>();
					cellData.add(new CellData()
							.setUserEnteredValue(new ExtendedValue().setStringValue(testResult.getValue()))
							.setUserEnteredFormat(new CellFormat().setBackgroundColor(testResult.getColor())));
					requests.add(new Request().setUpdateCells(new UpdateCellsRequest()
							.setStart(new GridCoordinate().setSheetId(testCaseData.getSheetId()).setRowIndex(rowNumber)
									.setColumnIndex(testCaseData.getHeaderColumnNumber()))
							.setRows(Arrays.asList(new RowData().setValues(cellData)))
							.setFields("userEnteredValue,userEnteredFormat.backgroundColor")));
					logger.info("Row Details " + testCaseName);
				} else {
					logger.error("No Row Details for" + testCaseName);
				}

			}
			
			if (requests == null || requests.isEmpty()) {
				logger.error("No matching data found in excel.");
				return;
			}

			try{
				
				BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);
				service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
				
			}catch(GoogleJsonResponseException ex){
				logger.error(ex.getMessage());
				return;
			}

		}
		
		logger.info("Data written to spreadsheet");
	}

	private static String getV4Version(String branch) {
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
	
	
	private static TestCaseData parseData(List<List<Object>> values, String resultColumnTitle, List<Request> requests,
			int sheetId) {

		TestCaseData testCaseData = new TestCaseData();
		testCaseData.setSheetId(sheetId);
		int lastColumnForTestCase = 0;

		HashMap<String, Integer> map = new HashMap<>();

		int j = 0;
		for (List<Object> row : values) {
			if (row == null || row.size() <= 0) {
				j++;
				continue;
			}
			if (row != null) {
				if (testCaseData.getHeaderColumnNumber() == -1) {
					testCaseData.setHeaderRowNumber(j);
					for (int i = 0; i < row.size(); i++) {
						logger.info(row.get(i).toString().toLowerCase());
						if (row.get(i).toString().toLowerCase()
								.contains(TestCaseSheetProperties.lastColumnForTestCase)) {
							lastColumnForTestCase = i + 2;
						}
						if (row.get(i).toString().toLowerCase().contains(resultColumnTitle.toLowerCase())) {
							testCaseData.setHeaderColumnNumber(i);
							break;
						}
						if (row.get(i).toString().toLowerCase()
								.contains(TestCaseSheetProperties.testCaseDescriptionColumn)) {
							testCaseData.setTestCaseColumnNumber(i);
						}

					}

					if (testCaseData.getHeaderColumnNumber() == -1) {
						testCaseData.setHeaderColumnNumber(lastColumnForTestCase);
						if (lastColumnForTestCase < row.size()) {
							int k = row.size() - 1;
							requests.add(new Request().setCutPaste(new CutPasteRequest()
									.setSource(new GridRange().setSheetId(sheetId)
											.setStartColumnIndex(lastColumnForTestCase).setEndColumnIndex(k))
									.setDestination(new GridCoordinate().setSheetId(sheetId)
											.setColumnIndex(testCaseData.getHeaderColumnNumber() + 1))));
						}
					}

				} else {
					String testCaseName = row.get(testCaseData.getTestCaseColumnNumber()).toString();
					map.put(testCaseName, j);
					testCaseData.setTestCaseMap(map);
				}
				j++;
			}
		}
		return testCaseData;
	}

}
