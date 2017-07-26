package com.ooyala.playback.url;


import org.apache.log4j.Logger;

public class TestDataValidator {

    private static final Logger logger = Logger.getLogger(TestDataValidator.class);

    public boolean validateVideoPlugin(String videoPlugin) throws Exception{
        boolean result=false;
        switch (videoPlugin){
            case "MAIN" :
                return true;

            case "BITMOVIN" :
                return true;

            case "OSMF" :
                return true;

            case "ADOBETVSDK" :
                return true;

            case "ANALYTICS" :
                return true;

            default :
                logger.error("Invalid Video Plugin : "+videoPlugin +" .It should be MAIN/BITMOVIN/OSMF/ADOBETVSDK/ANALYTICS hence stoping the execution...!!!");
                return result;
        }
    }
    public boolean validateAdPlugin(String adPlugin) throws Exception{
        boolean result=false;
        switch (adPlugin){
            case "IMA" :
                return true;

            case "FREEWHEEL" :
                return true;

            case "VAST" :
                return true;

            case "VPAID" :
                return true;

            case "PULSE" :
                return true;

            default :
                logger.error("Invalid Video Plugin : "+adPlugin +" .It should be MAIN/BITMOVIN/OSMF/ADOBETVSDK/ANALYTICS hence stoping the execution...!!!");
                return result;
        }
    }
}
