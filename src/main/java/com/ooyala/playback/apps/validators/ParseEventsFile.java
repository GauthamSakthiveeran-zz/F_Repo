package com.ooyala.playback.apps.validators;

import org.apache.log4j.Logger;

import com.ooyala.playback.apps.utils.CommandLine;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ParseEventsFile {
    final static Logger logger = Logger.getLogger(ParseEventsFile.class);

    public static int latestCount(String line){
        int count1;
        String[] tokens = line.split(":");
        String trimToken = tokens[3].trim();
        count1=Integer.parseInt(trimToken);
        return count1;
    }

    public static int parseeventfile(Events event, int count ){

        try{
            String[] final_command = CommandLine.command("adb shell cat /sdcard/log.file");
            ProcessBuilder processBuilder = new ProcessBuilder(final_command);
            processBuilder.redirectErrorStream(true);
            Process p = processBuilder.start();
            p.waitFor();
            String line = "";
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            line = buf.readLine();
            while(line != null){
                if(line.contains("state: ERROR"))
                {
                    logger.fatal("App crashed");
                    org.testng.Assert.fail("App is crashed during playback");
                    //System.exit(0);
                }
                if(line.contains(event.getEvent()))
                {
                  if (latestCount(line)>count) {

                        logger.debug("Event Recieved From SDK AND Sample App :- " + line);
                        count=latestCount(line);
                        return count;
                    }
                    
                }
                line = buf.readLine();
            }
        }
        catch (Exception e)
        {
            logger.error("Exception " + e);
            e.printStackTrace();
        }
        return -1;
    }
}
