package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.NoSuchElementException;

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
    public boolean validate(String element, int timeout){
        try {
            getWebElement("NEXT_ARROW").click();
            Thread.sleep(2000);
            getWebElement("VIDEO").click();
        /*    getWebElement("PREVIOUS_ARROW").click();
            getWebElementsList("VIDEO_LIST").get(11).click();*/
            return true;
        }catch (NoSuchElementException noSuchElement){
            logger.error("Element is not found"+noSuchElement.getMessage());
            return false;
        }catch (Exception e){
            logger.error("Error while validating playlist "+e.getMessage());
            return false;
        }
    }

    public boolean playlistValidator(String tcName, String value) throws InterruptedException {

        switch (tcName){
            case "Orientation":
                return getOrientation(value);
            case "Position":
                return getPosition(value);
            case "Podtype":
                return getPodType(value);
            case "useFirstVideoFromPlaylist":
                return getFirstVideoFromPlaylist(value);
            case "CaptionPosition":
                return getCaptionPosition(value);
            case "ThumbnailSize":
                return getThumbnailSize(value);
            case "ThumbnailSpace":
                return getThumbnailSpacing(value);
        }
        return false;
    }

    public boolean getOrientation(String orientationValue) throws InterruptedException {
        String orientation = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-orientation");
        logger.info("Playlist Orientation is - "+orientation);
        boolean flag = orientation.contains(orientationValue);
        return flag;
    }

    public boolean getPosition(String positionValue){
        String position = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-layout");
        logger.info("Playlist Position is - "+position);
        boolean flag = position.contains(positionValue);
        return flag;
    }

    public boolean getPodType(String podValue){
        String podType = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-pod-type");
        logger.info("Playlist Pod Type is - "+podType);
        boolean flag = podType.contains(podValue);
        return flag;
    }

    public boolean getFirstVideoFromPlaylist(String value){

        return true;
    }

    public boolean getCaptionPosition(String captionPositionValue){
        String captionPosition = getWebElement("PLAYLIST_PLAYER").getAttribute("data-caption-position");
        logger.info("Playlist Caption Position is - "+captionPosition);
        boolean flag = captionPosition.contains(captionPositionValue);
        return flag;
    }

    public boolean getThumbnailSize(String thumbnailSizeValue){
        String thumbnailSize = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlists-thumbnails-size");
        logger.info("Playlist Caption Position is - "+thumbnailSize);
        boolean flag = thumbnailSize.equals(thumbnailSizeValue);
        return flag;
    }

    public boolean getThumbnailSpacing(String thumbnailSpaceValue){
        String thumbnailSpace = getWebElement("PLAYLIST_PLAYER").getAttribute(""); /// Searching got thumbnail spacing element
        logger.info("Playlist Caption Position is - "+thumbnailSpace);
        boolean flag = thumbnailSpace.equals(thumbnailSpaceValue);
        return flag;
    }
}
