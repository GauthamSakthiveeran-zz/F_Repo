package com.ooyala.playback.updateSpreadSheet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by jitendra on 6/1/17.
 */
public class ParseJenkinsJobLink {

    public static String buildNumber;
    public static String jenkinsJobLink;
    public static int count = 1;


    public static String getJenkinsBuild(String jobName){
        String link = "http://jenkins-master1.services.ooyala.net:8080/job/"+jobName+"/lastBuild/api/json";

        try {
            URL url = new URL(link);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                if (inputLine.contains("\"id\"")){
                    String [] _buildNumber = inputLine.split(":");
                    for (int i = 0 ; i < _buildNumber.length ; i++) {
                        if (_buildNumber[i].contains("\"id\"") && count <= 1){
                            String [] buildNos = _buildNumber[i + 1].split(",");
                            buildNumber = buildNos [0].replace("\"", "");
                            count ++ ;
                        }
                    }
                    break;
                }
            }
            in.close();
        }catch (Exception e){
            System.out.println("Error occured while reading jenkins job build number");
        }
        jenkinsJobLink= "http://jenkins-master1.services.ooyala.net:8080/job/"+jobName+"/"+buildNumber+"/console";
        return jenkinsJobLink;
    }
}
