package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
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

    int eventCount = 0;

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
                return scrollToEitherSide()&&getThumbnailSpacing(value);
            case "useFirstVideoFromPlaylist":
                return getFirstVideoFromPlaylist(value);
            case "CaptionPosition":
                return scrollToEitherSide()&&getCaptionPosition(value);
            case "Menustyle":
                return getMenuSytle(value);
            case "WrapperFontSize":
                return getWrapperFontSize(value);
            case "Caption":
                return getCaption();
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
        int count =1;
        eventCount=0;
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
                    result = result && checkPlayback(count);
                    count++;
                    i = i + 2;
                }
            } catch (Exception e){
                e.getMessage();
            }

        }
        return result;
    }

    public boolean checkPlayback(int count){
        count = count+eventCount;
        try {
            if (!PlayBackFactory.getInstance(driver).getPlayValidator().waitForPage()){return false;}
            loadingSpinner();
            Thread.sleep(5000);
            if(!PlayBackFactory.getInstance(driver).getPlayValidator().validate("playing_"+count+"",20000)){return false;}
            loadingSpinner();
            if (!PlayBackFactory.getInstance(driver).getPauseValidator().validate("paused_"+(count-eventCount)+"",20000)){return false;}
            loadingSpinner();
            if (!PlayBackFactory.getInstance(driver).getPlayValidator().validate("playing_"+(count+1)+"",20000)){return false;}
            loadingSpinner();
            if (!PlayBackFactory.getInstance(driver).getSeekValidator().validate("seeked_"+(count-eventCount)+"",20000)){return false;}
            eventCount++;

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
        eventCount = 0;
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
            boolean isPagingElementActive;
            int count = 1;
            for (int i=0 ; i<totalPagingElement ; i++) {
                try{
                    isPagingElementActive = (Boolean) driver.executeScript("return document.getElementsByClassName('oo-thumbnail-paging-ooplayer')[" + i + "].isActive()");
                }catch (Exception e){
                    logger.info("May be paging element is not active");
                    isPagingElementActive = true;
                }
                try {
                    if (isPagingElementActive) {
                        int assetsUnderPagingElement = Integer.parseInt(driver.executeScript("return document.getElementsByClassName('oo-thumbnail-paging-ooplayer')[" + i + "].getElementsByClassName('oo-thumbnail').length").toString());
                        for (int j = 0; j < assetsUnderPagingElement; j++) {
                            String assetUnderPagingEmbedCode = driver.executeScript("return document.getElementsByClassName('oo-thumbnail-paging-ooplayer')[" + i + "].getElementsByClassName('oo-thumbnail')[" + j + "].getAttribute('id')").toString();
                            Thread.sleep(3000);
                            driver.findElement(By.id(assetUnderPagingEmbedCode)).click();
                            result = result && checkPlayback(count);
                            j = j + 2;
                            count++;
                        }
                        if ((Boolean) driver.executeScript("return $(document.getElementsByClassName('oo-next')).is(\":visible\");")) {
                            if (!clickOnIndependentElement("SCROLL_DOWN")) {
                                logger.error("Failed while clicking on next button in Playlist");
                                return false;
                            }
                        }
                    }
                }catch (Exception e){
                    e.getMessage();
                }
            }
            return result;
        }
        return true;
    }

    public boolean getFirstVideoFromPlaylist(String value){
        List<WebElement> videoList = getWebElementsList("PLAYLIST_VIDEOS");
        String emebedCodeOfFirstAsset = videoList.get(0).getAttribute("id");
        String emebedCodeOfCurrentAsset = getEmbedCode();
        logger.info("\n"+ "First video from playlist is : "+ emebedCodeOfFirstAsset +"\n"+ "Current video playing is : "+emebedCodeOfCurrentAsset);
        if (value.equalsIgnoreCase("true")){
            if (!emebedCodeOfFirstAsset.contains(emebedCodeOfCurrentAsset)){return false;}
        }
        if (value.equalsIgnoreCase("false")){
            if (emebedCodeOfFirstAsset.contains(emebedCodeOfCurrentAsset)){return false;}
        }

        return checkPlayback(1);
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
        int numberOfAsset = getWebElementsList("SPCAING_PLAYLIST_ASSETS").size();
        int givenThumbnailSize = Integer.parseInt(thumbnailSpaceValue);
        for (int i=0;i<numberOfAsset;i++){
            String assetName = driver.executeScript("return document.getElementsByClassName('slide-ooplayer')["+i+"].innerText").toString();
            logger.info("Asset Name is : "+assetName);
            int thumbnailSizeWithAsset = Integer.parseInt(driver.executeScript("return document.getElementsByClassName('slide-ooplayer')["+i+"].offsetHeight").toString());
            int assetSize = Integer.parseInt(driver.executeScript("return document.getElementsByClassName('slide-ooplayer')["+i+"].getElementsByClassName('oo-thumbnail')[0].offsetHeight;").toString());
            int obtainedThumbnailSize = thumbnailSizeWithAsset-assetSize;
            if (!(obtainedThumbnailSize == givenThumbnailSize)){return false;}
        }
        return true;
    }

    public boolean getMenuSytle(String value){
        if (!waitOnElement("PLAYLISTS_PLAYER",10000)){return false;}
        int totalPlaylists = getWebElementsList("PLAYLISTS").size();
        for (int i=0; i<totalPlaylists; i++){
            String playlistId = driver.executeScript("return document.getElementsByClassName('oo-menu-items oo-cf')[0].getElementsByTagName('li')["+i+"].id").toString();
            String plalistName = driver.executeScript("return document.getElementsByClassName('oo-menu-items oo-cf')[0].getElementsByTagName('li')["+i+"].innerText").toString();
            logger.info("playlistId :"+playlistId +"\n"+"playlistName : "+plalistName+ " \n");
            if (!playlistId.contains(plalistName)){return false;}
            // select each playlist
            if (!clickOnIndependentElement(By.id(playlistId))){return false;}
            if (!getPodType(driver.executeScript(
                    "return document.getElementById('PlaylistsPlayerWrapper-ooplayer')." +
                            "getAttribute('data-playlist-pod-type')" +
                            "").toString())){return false;}
        }
        return true;
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

    public boolean getWrapperFontSize(String wrapperSizeValue){
        String fontSize =getWebElement("PLAYLIST_PLAYER").getCssValue("font-size");
        logger.info("Playlist Font Size is - "+fontSize);
        boolean flag = fontSize.contains(wrapperSizeValue);
        return flag;
    }

    public boolean getCaption(){
        boolean flag=true;
        List<WebElement> titleList = getWebElementsList("ASSET_TITLE");
        List<WebElement> desciptionList = getWebElementsList("ASSET_DESCRIPTION");
        List<WebElement> durationList = getWebElementsList("ASSET_DURATION");

        for(int i=0; i<titleList.size(); i++){
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", titleList.get(i));
            onmouseOver(desciptionList.get(i));
            logger.info("Title,Description,Duration of asset "+(i+1)+" is : "+'\n'+titleList.get(i).getText()+'\n'+desciptionList.get(i).getText()+'\n'+durationList.get(i).getText());
            flag = desciptionList.get(i).getText().contains("Test Description");
            flag = flag && durationList.get(i).getText().contains(":");
        /*  Only two values are displaying on asset. Issue id PLAYER-457.
            flag = flag && titleList.get(i).getText().contains("tb_");   */
        }
        return flag;
    }

    public String getEmbedCode(){
        return ((JavascriptExecutor) driver).executeScript("return pp.getEmbedCode()").toString();
    }
}

