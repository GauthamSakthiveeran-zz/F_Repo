package com.ooyala.playback.apps.utils;

import java.io.IOException;

/**
 * Created by Sachin on 1/22/2016.
 */
public class CommandLine{

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static String[] command(String command){
        String[] new_command;
        if(isWindows()) {
            new_command = new String[]{"cmd.exe", "/c", command};
        }
        else {
            new_command = new String[]{"/bin/sh", "-c", command};
        }
            return  new_command;
    }

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

}
