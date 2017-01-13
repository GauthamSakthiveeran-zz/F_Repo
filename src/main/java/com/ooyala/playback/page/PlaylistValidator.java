package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by snehal on 13/01/17.
 */
public class PlaylistValidator extends PlayBackPage implements PlaybackValidator{

    public static Logger logger = Logger.getLogger(PlaylistValidator.class);

    public PlaylistValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("playlist");
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {
        return true;
    }

    public boolean playlistValidator(String tcName, String value){

        switch (tcName){
            case "Orientation":
                return getOrientation(value);
            case "Position":
                return getPosition(value);
        }
        return false;
    }

    public boolean getOrientation(String orientationValue){
        String orientation = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-orientation");
        logger.info("Playlist Orientation is - "+orientation);
        boolean flag = orientation.contains(orientationValue);
        return flag;
    }

    public boolean getPosition(String positionValue){
        String position = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-layout");
        logger.info("Playlist layout is - "+position);
        boolean flag =position.contains(positionValue);
        return flag;
    }
}
