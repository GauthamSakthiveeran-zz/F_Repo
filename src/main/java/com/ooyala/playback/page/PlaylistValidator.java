package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
    public boolean validate(String element, int timeout) {
        try {
            getWebElement("NEXT_ARROW").click();
            Thread.sleep(2000);
            getWebElement("VIDEO").click();
        /*    getWebElement("PREVIOUS_ARROW").click();
            getWebElementsList("VIDEO_LIST").get(11).click();*/
            return true;
        } catch (NoSuchElementException noSuchElement) {
            logger.error("Element is not found" + noSuchElement.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error while validating playlist " + e.getMessage());
            return false;
        }
    }

    public boolean playlistValidator(String tcName, String value) throws InterruptedException {

        switch (tcName){
            case "Orientation":
                return scrollToEitherSide()&&getOrientation(value);
            case "Position":
                return scrollToEitherSide()&&getPosition(value);
            case "Autoplay":
                return isAutoplay(value);
            case "Podtype":
                return getPodType(value);
            case "Thumbnailsize":
                return getThumbnailSize(value);
            case "ThumbnailSpace":
                return getThumbnailSpacing(value);
            case "useFirstVideoFromPlaylist":
                return getFirstVideoFromPlaylist(value);
            case "CaptionPosition":
                return getCaptionPosition(value);
        }
        return false;
    }


     /*
     pass int playNumberOfAsset
     this number of Asset's playback will be checked randomly.
    */

    public boolean selectAssetFromPlaylist(){
        int totalPlaylistVideo = getWebElementsList("PLAYLIST_VIDEOS").size();
        System.out.println("size : "+totalPlaylistVideo);
        boolean result = true;
        for (int i=0;i<=totalPlaylistVideo;i++) {
            try {
                String s = ((JavascriptExecutor) driver).executeScript("return document.getElementsByClassName('oo-thumbnail')["+i+"].getAttribute('id');").toString();
                if(!driver.findElement(By.id(s)).isDisplayed()){
                    getWebElement("SCROLL_DOWN").click();
                }
                if (driver.findElement(By.id(s)).isDisplayed()) {
                    driver.findElement(By.id(s)).click();
                    Thread.sleep(3000);
                    result = result && checkPlayback();
                    i = i + 2;
                }
            } catch (Exception e){
                e.getMessage();
            }

        }
        return result;
    }

    public boolean checkPlayback(){

        try {
            if (!PlayBackFactory.getInstance(driver).getPlayValidator().waitForPage()){return false;}
            loadingSpinner();
            if(!PlayBackFactory.getInstance(driver).getPlayValidator().validate("playing_1",20000)){return false;}
            loadingSpinner();
            if (!PlayBackFactory.getInstance(driver).getPauseValidator().validate("paused_1",20000)){return false;}
            loadingSpinner();
            if (!PlayBackFactory.getInstance(driver).getPlayValidator().validate("playing_2",20000)){return false;}
            loadingSpinner();
            if (!PlayBackFactory.getInstance(driver).getSeekValidator().validate("seeked_1",20000)){return false;}

        } catch (Exception e){
            return false;
        }

        return true;
    }



    public boolean getOrientation(String orientationValue){
        String orientation = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-orientation");
        logger.info("Playlist Orientation is - "+orientation);
        if (!orientation.contains(orientationValue)){return false;}
        return selectAssetFromPlaylist();
    }

    public boolean getPosition(String positionValue){
        String position = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-layout");
        logger.info("Playlist Position is - "+position);
        if (!position.contains(positionValue)){return false;}
        return selectAssetFromPlaylist();
    }

    public boolean getPodType(String podValue){
        String podType = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-pod-type");
        logger.info("Playlist Pod Type is - "+podType);
        if (!podValue.contains(podType)){
            logger.info("pod is not getting");
            return false;
        }
        if (podValue.equalsIgnoreCase("paging")){
            if (!waitOnElement("PAGGING_ELEMENT",20000)){
                logger.error("paging is not set !!!");
                return false;
            }
            int totalPagingElement = getWebElementsList("PAGGING_ELEMENT").size();
            boolean result = true;
            for (int i=0 ; i<totalPagingElement ; i++){
                if ((Boolean) driver.executeScript("return document.getElementsByClassName('oo-thumbnail-paging-ooplayer')["+i+"].isActive()")){
                        int assetsUnderPagingElement =Integer.parseInt(driver.executeScript("return document.getElementsByClassName('oo-thumbnail-paging-ooplayer')["+i+"].getElementsByClassName('oo-thumbnail').length").toString());
                        for (int j=0 ;j<assetsUnderPagingElement ; j++){
                            String assetUnderPagingEmbedCode = driver.executeScript("return document.getElementsByClassName('oo-thumbnail-paging-ooplayer')["+i+"].getElementsByClassName('oo-thumbnail')["+j+"].getAttribute('id')").toString();
                            driver.findElement(By.id(assetUnderPagingEmbedCode)).click();
                            j = j+2;
                            result = result && checkPlayback();
                        }
                    if ((Boolean) driver.executeScript("return $(document.getElementsByClassName('oo-next')).is(\":visible\");")){
                        if (!clickOnIndependentElement("SCROLL_DOWN")){
                            logger.error("Failed while clicking on next button in Playlist");
                            return false;
                        }
                    }
                }
            }
            return result;
        }
        return true;
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
        if (!thumbnailSize.equals(thumbnailSizeValue)){
            return false;
        }
        return true;
    }

    public boolean getThumbnailSpacing(String thumbnailSpaceValue){
        String thumbnailSpace = getWebElement("PLAYLIST_PLAYER").getAttribute(""); /// Searching got thumbnail spacing element
        logger.info("Playlist Caption Position is - "+thumbnailSpace);
        boolean flag = thumbnailSpace.equals(thumbnailSpaceValue);
        return flag;
    }

    public boolean isAutoplay(String isAutoPlay){
        try {
            if (isAutoPlay.equalsIgnoreCase("true")) {
                if (!PlayBackFactory.getInstance(driver).getEventValidator().validate("playing_1", 20000)){
                    logger.error("Auto play is not working");
                    return false;
                }
            }

            if (isAutoPlay.equalsIgnoreCase("false")){
                loadingSpinner();
                boolean isVideoPlaying = (Boolean)((JavascriptExecutor) driver).executeScript("return pp.isPlaying();");
                if (isVideoPlaying){
                    logger.info("Video is playing even if Auto play is set to false..");
                    return false;
                }
            }
        } catch (Exception e){

        }
        return true;
    }

    public boolean scrollToEitherSide(){
        int totalPlaylistVideo = getWebElementsList("PLAYLIST_VIDEOS").size();
        for (int i=0;i<totalPlaylistVideo;i++) {
            try{
                if (isElementPresent("SCROLL_DOWN")){
                    getWebElement("SCROLL_DOWN").click();
                }
            }catch (Exception e){
                logger.info("No more bext scroll button is present.");
            }

        }

        for (int i=0;i<totalPlaylistVideo;i++) {
            try{
                if (isElementPresent("PREVIOUS_ARROW")){
                    getWebElement("PREVIOUS_ARROW").click();
                }
            }catch (Exception e){
                logger.info("NO more previous button present");
            }

        }

        return true;
    }

}
