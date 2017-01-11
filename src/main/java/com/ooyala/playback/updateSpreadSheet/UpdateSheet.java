package com.ooyala.playback.updateSpreadSheet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

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
import com.google.api.services.sheets.v4.Sheets;

/**
 * Created by jitendra on 4/1/17.
 */
public class UpdateSheet {

    /** Application name. */
    private static final String APPLICATION_NAME =
            "Google Sheets API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart.json");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart.json
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS);

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
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        try {
            InputStream in =
                    UpdateSheet.class.getResourceAsStream("/client_secret.json");
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(
                            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                            .setDataStoreFactory(DATA_STORE_FACTORY)
                            .setAccessType("offline")
                            .build();
            Credential credential = new AuthorizationCodeInstalledApp(
                    flow, new LocalServerReceiver()).authorize("playbackqa@gmail.com");
            System.out.println(
                    "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
            return credential;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void writetosheet(LinkedHashMap<String,String> map) {
        try {
            String groupName="";
            int sheetId=0;
            String range="";
            Sheets service = getSheetsService();
            String spreadsheetId = "1YV2osdUm-hEbAlrt6BcNA-b_js7OQ5xzhNG5xmlFMDk";
            int num_of_sheets = service.spreadsheets().get(spreadsheetId).execute().getSheets().size();
            HashMap<String , Integer> sheetNameAndID = new HashMap<>();
            for (int i = 0; i < num_of_sheets; i++) {
                String sheetNames = service.spreadsheets().get(spreadsheetId).execute().getSheets().get(i).getProperties().getTitle();
                int sheet_Id = service.spreadsheets().get(spreadsheetId).execute().getSheets().get(i).getProperties().getSheetId();
                sheetNameAndID.put(sheetNames,sheet_Id);
            }

            if (map.get("SuiteName").equals("regression.xml")) {
                groupName = map.get("groups");
                sheetId = sheetNameAndID.get(groupName);
                range = groupName+"!A2:E";
            }

            if (map.get("SuiteName").equals("VTC_Regression.xml")){
                sheetId = 1728994705;
                range = "vtc!A2:E";
            }

            if (map.get("SuiteName").equals("amf_testng.xml")){
                sheetId = 264291465;
                range = "amf!A2:E";
            }

                ValueRange response = service.spreadsheets().values()
                        .get(spreadsheetId, range)
                        .execute();

                // Read Data From Spreadsheet
                List<List<Object>> readvalues = response.getValues();

                // write data to spreadsheet
                List<Request> requests = new ArrayList<>();
                List<CellData> values = new ArrayList<>();
                List<String> valuesInMap = new ArrayList<>();
                for (String key : map.keySet()){
                    String value = map.get(key);
                    System.out.println(key + " : " + value);
                    valuesInMap.add(value);
                }

                for (int i=0;i<valuesInMap.size()-2;i++){
                    values.add(new CellData().setUserEnteredValue(new ExtendedValue().setStringValue(valuesInMap.get(i))));
                }

                requests.add(new Request()
                        .setUpdateCells(new UpdateCellsRequest()
                                .setStart(new GridCoordinate()
                                        .setSheetId(sheetId)
                                        .setRowIndex(readvalues.size() + 1)
                                        .setColumnIndex(0))
                                .setRows(Arrays.asList(
                                        new RowData().setValues(values)))
                                .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

                BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                        .setRequests(requests);
                service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest)
                        .execute();
                System.out.println("Data written to spreadsheet");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
