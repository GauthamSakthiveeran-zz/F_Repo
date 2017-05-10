package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

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
        addElementToPageElements("play");
    }

    int eventCount = 0;

    @Override
    public boolean validate(String element, int timeout) {
        try {
            getWebElement("NEXT_ARROW").click();
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

    public boolean playlistValidator(String tcName, String value, String videoPlugin) throws InterruptedException {

		switch (tcName) {
		case "Default themebuilder parameters" :
            if (videoPlugin.equalsIgnoreCase("MAIN"))
                return scrollToEitherSide() && getOrientation("vertical") && getThumbnailSize("130") && getPosition("right");
            if (videoPlugin.equalsIgnoreCase("BITMOVIN"))
                return scrollToEitherSide() && getOrientation("horizontal") && getThumbnailSize("130") && getPosition("bottom");
            if (videoPlugin.equalsIgnoreCase("OSMF"))
                return scrollToEitherSide() && getOrientation("horizontal") && getThumbnailSize("150") && getPosition("top");
		case "Orientation":
			return scrollToEitherSide() && getOrientation(value);
		case "Position":
			return scrollToEitherSide() && getPosition(value);
		case "Autoplay":
			return isAutoplay(value);
		case "Podtype":
			return getPodType(value);
		case "Thumbnailsize":
			return getThumbnailSize(value);
		case "ThumbnailSpace":
			return scrollToEitherSide() && getThumbnailSpacing(value);
		case "useFirstVideoFromPlaylist":
			return getFirstVideoFromPlaylist(value);
		case "CaptionPosition":
			return scrollToEitherSide() && getCaptionPosition(value);
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

	public boolean selectAssetFromPlaylist() {
		int totalPlaylistVideo = getWebElementsList("PLAYLIST_VIDEOS").size();
		logger.info("size : " + totalPlaylistVideo);
		int count = 1;
		eventCount = 0;
		boolean result = true;
		for (int i = 0; i < 3; i++) {
			try {
				String asset = ((JavascriptExecutor) driver)
						.executeScript(
								"return document.getElementsByClassName('oo-thumbnail')[" + i + "].getAttribute('id');")
						.toString().trim();
				/*driver.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id(asset)));
				if (i != 0) {
					// scrolling to left as asset may get hide in right arrow
					// button
					if (isElementVisible("SCROLL_DOWN")) {
						getWebElement("SCROLL_DOWN").click();
					}
				}*/
				if (driver.findElement(By.id(asset)).isDisplayed()) {
					result = result && clickOnIndependentElement(By.id(asset));
					result = result && checkPlayback(count);
					if (!result) {
						extentTest.log(LogStatus.FAIL, "Failed to play video : " + count);
					}
					count++;
				}
			} catch (Exception e) {
				e.getMessage();
			}

		}
		return result;
	}

	public boolean checkPlayback(int count) {
		logger.info("Playing video no : " + count);
		count = count + eventCount;
		try {
			if (getBrowser().equalsIgnoreCase("MicrosoftEdge")) {
				try {
					WebElement element = getWebElement("PLAY_BUTTON");
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
				} catch (Exception e) {
					logger.error("Error while focus on element play button.");
				}
			}
			PlayBackFactory factory = new PlayBackFactory(driver, extentTest);
			
			loadingSpinner();
			
			if (!waitOnElement(By.id("playing_" + count), 5000)) {
				if (!factory.getPlayValidator().validate("playing_" + count + "", 20000)) {
					return false;
				}
			} else {
				if (!factory.getEventValidator().validate("playing_" + count + "", 20000)) {
					return false;
				}
			}
			loadingSpinner();
			
			count = count - eventCount;

			if (getBrowser().equalsIgnoreCase("internet explorer")) {
				factory.getSeekAction().fromLast().setTime(12).startAction();
				if (!factory.getEventValidator().validate("seeked_" + count, 20000))
					return false;
			} else {
				if (!factory.getSeekValidator().validate("seeked_" + count, 20000)) {
					return false;
				}
			}
			
			eventCount++;

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(LogStatus.FAIL, e.getMessage());
			return false;
		}
		return true;
	}


	public boolean getOrientation(String orientationValue) {
		String orientation = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-orientation");
		logger.info("Playlist Orientation is - " + orientation);
		if (!orientation.contains(orientationValue)) {
			extentTest.log(LogStatus.FAIL, "Orientation is incorrect");
			return false;
		}
		return selectAssetFromPlaylist();
	}

	public boolean getPosition(String positionValue) {
		String position = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-layout");
		logger.info("Playlist Position is - " + position);
		if (!position.contains(positionValue)) {
			extentTest.log(LogStatus.FAIL, "Playlist Position is incorrect");
			return false;
		}
		return selectAssetFromPlaylist();
	}

	public boolean getPodType(String podValue) {
		String podType = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlist-pod-type");
		eventCount = 0;
		logger.info("Playlist Pod Type is - " + podType);
		if (!podValue.contains(podType)) {
			logger.info("pod is not getting");
			extentTest.log(LogStatus.FAIL, "data-playlist-pod-type is incorrect");
			return false;
		}
		if (podValue.equalsIgnoreCase("paging")) {
			if (!waitOnElement("PAGGING_ELEMENT", 20000)) {
				extentTest.log(LogStatus.FAIL, "paging is not set !!!");
				return false;
			}
			int totalPagingElement = getWebElementsList("PAGGING_ELEMENT").size();
			boolean result = true;
			boolean isPagingElementActive;
			int count = 1;
			for (int i = 0; i < totalPagingElement; i++) {
				try {
					isPagingElementActive = (Boolean) driver
							.executeScript("return document.getElementsByClassName('oo-thumbnail-paging-ooplayer')[" + i
									+ "].isActive()");
				} catch (Exception e) {
					logger.info("May be paging element is not active");
					isPagingElementActive = true;
				}
				try {
					if (isPagingElementActive) {
						int assetsUnderPagingElement = Integer
								.parseInt(driver
										.executeScript(
												"return document.getElementsByClassName('oo-thumbnail-paging-ooplayer')["
														+ i + "].getElementsByClassName('oo-thumbnail').length")
										.toString());
						for (int j = 0; j < assetsUnderPagingElement; j++) {
							String assetUnderPagingEmbedCode = driver.executeScript(
									"return document.getElementsByClassName('oo-thumbnail-paging-ooplayer')[" + i
											+ "].getElementsByClassName('oo-thumbnail')[" + j + "].getAttribute('id')")
									.toString();
							driver.findElement(By.id(assetUnderPagingEmbedCode)).click();
							result = result && checkPlayback(count);
							if (!result) {
								extentTest.log(LogStatus.FAIL, "Issue with playback of video no " + count);
							}
							j = j + 2;
							count++;
						}
						if ((Boolean) driver.executeScript(
								"return $(document.getElementsByClassName('oo-next')).is(\":visible\");")) {
							if (!clickOnIndependentElement("SCROLL_DOWN")) {
								extentTest.log(LogStatus.FAIL, "Failed while clicking on next button in Playlist");
								return false;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					extentTest.log(LogStatus.FAIL, e.getMessage());
					return false;
				}
			}
			return result;
		} else {
			return checkPlayback(1);
		}
	}

	public boolean getFirstVideoFromPlaylist(String value) {
		eventCount = 0;
		List<WebElement> videoList = getWebElementsList("PLAYLIST_VIDEOS");
		String emebedCodeOfFirstAsset = videoList.get(0).getAttribute("id");
		String emebedCodeOfCurrentAsset = getEmbedCode();
		logger.info("\n" + "First video from playlist is : " + emebedCodeOfFirstAsset + "\n"
				+ "Current video playing is : " + emebedCodeOfCurrentAsset);
		if (value.equalsIgnoreCase("true")) {
			if (!emebedCodeOfFirstAsset.contains(emebedCodeOfCurrentAsset)) {
				extentTest.log(LogStatus.FAIL, "emebedCodeOfFirstAsset should be the same as emebedCodeOfCurrentAsset");
				return false;
			}
		}
		if (value.equalsIgnoreCase("false")) {
			if (emebedCodeOfFirstAsset.contains(emebedCodeOfCurrentAsset)) {
				extentTest.log(LogStatus.FAIL, "emebedCodeOfFirstAsset should not be the same as emebedCodeOfCurrentAsset");
				return false;
			}
		}

		return checkPlayback(1);
	}

    public boolean getCaptionPosition(String captionPositionValue){
        String captionPosition = getWebElement("PLAYLIST_PLAYER").getAttribute("data-caption-position");
        logger.info("Playlist Caption Position is - "+captionPosition);
        if(!captionPosition.contains(captionPositionValue)){return false;}
        eventCount = 0;
        return checkPlayback(1);
    }

	public boolean getThumbnailSize(String thumbnailSizeValue) {
		eventCount = 0;
		String thumbnailSize = getWebElement("PLAYLIST_PLAYER").getAttribute("data-playlists-thumbnails-size");
		logger.info("Playlist Caption Position is - " + thumbnailSize);
		if (!thumbnailSize.equals(thumbnailSizeValue)) {
			extentTest.log(LogStatus.FAIL, " Playlist Caption Position is incorrect. Expected : " + thumbnailSize);
			return false;
		}
		return checkPlayback(1);
	}

	public boolean getThumbnailSpacing(String thumbnailSpaceValue) {
		eventCount = 0;
		int numberOfAsset = getWebElementsList("SPCAING_PLAYLIST_ASSETS").size();
		int givenThumbnailSize = Integer.parseInt(thumbnailSpaceValue);
		for (int i = 0; i < numberOfAsset; i++) {
			String assetName = driver
					.executeScript("return document.getElementsByClassName('slide-ooplayer')[" + i + "].innerText")
					.toString();
			logger.info("Asset Name is : " + assetName);
			int thumbnailSizeWithAsset = Integer.parseInt(driver
					.executeScript("return document.getElementsByClassName('slide-ooplayer')[" + i + "].offsetHeight")
					.toString());
			int assetSize = Integer
					.parseInt(driver.executeScript("return document.getElementsByClassName('slide-ooplayer')[" + i
							+ "].getElementsByClassName('oo-thumbnail')[0].offsetHeight;").toString());
			int obtainedThumbnailSize = thumbnailSizeWithAsset - assetSize;
			if (obtainedThumbnailSize != givenThumbnailSize) {
				extentTest.log(LogStatus.FAIL, " Issue with Thumbnail spacing.");
				return false;
			}
		}
		return checkPlayback(1);
	}

	public boolean getMenuSytle(String value) {
		if (!waitOnElement("PLAYLISTS_PLAYER", 10000)) {
			return false;
		}
		int totalPlaylists = getWebElementsList("PLAYLISTS").size();
		
		if(totalPlaylists==1){
			extentTest.log(LogStatus.FAIL, "There is only one playlist");
			return false;
		}
		
		extentTest.log(LogStatus.INFO, "Total no of playlists : " + totalPlaylists);
		
		List<WebElement> playlists = getWebElementsList("PLAYLISTS");
		
		for (int i = 0; i < totalPlaylists; i++) {
			
			String playlistId = playlists.get(i).getAttribute("id");
			
			String plalistName = playlists.get(i).getText();

			logger.info("playlistId :" + playlistId + "\n" + "playlistName : " + plalistName + " \n");
			
			if (!playlistId.contains(plalistName)) {
				return false;
			}
			// select each playlist
			WebElement playlist = driver.findElement(By.xpath(".//*[@id='"+playlistId+"']"));
			
			playlist.click();
			
			if(!playlist.getAttribute("class").contains("oo-menu-active")){
				extentTest.log(LogStatus.FAIL, "Correct tab not selected.");
			}
		}
		return true;
	}

    public boolean isAutoplay(String isAutoPlay){
        try {
            if (isAutoPlay.equalsIgnoreCase("true")) {
                if (!new PlayBackFactory(driver,extentTest).getEventValidator().validate("playing_1", 20000)){
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
        	extentTest.log(LogStatus.FAIL, e.getMessage());
        	return false;
        }
        return true;
    }

	public boolean scrollToEitherSide() {
		
		boolean flag = true;

		if (getWebElement("PREVIOUS_ARROW").isDisplayed()) {
			extentTest.log(LogStatus.FAIL, "PREVIOUS_ARROW should not be present.");
			flag = false;
		}

		int totalPlaylistVideo = getWebElementsList("PLAYLIST_VIDEOS").size();
		for (int i = 0; i < totalPlaylistVideo; i++) {
			try {
				if (isElementPresent("SCROLL_DOWN")) {
					getWebElement("SCROLL_DOWN").click();
					logger.info("Scrolled down");
				} else {
					extentTest.log(LogStatus.FAIL, "SCROLL_DOWN should be present.");
					flag = false;
				}
			} catch (Exception e) {
				logger.info("No more next scroll button is present.");
				if (getWebElement("SCROLL_DOWN").isDisplayed()) {
					extentTest.log(LogStatus.FAIL, "SCROLL_DOWN should not be present.");
					flag = false;
				}
				break;
			}

		}

		for (int i = 0; i < totalPlaylistVideo; i++) {
			try {
				if (isElementPresent("PREVIOUS_ARROW")) {
					getWebElement("PREVIOUS_ARROW").click();
					logger.info("scrolled up");
				}
			} catch (Exception e) {
				logger.info("No more previous button present");
				if (getWebElement("PREVIOUS_ARROW").isDisplayed()) {
					extentTest.log(LogStatus.FAIL, "PREVIOUS_ARROW should not be present.");
					flag = false;
				}
				break;
			}

		}

		return flag;
	}

    public boolean getWrapperFontSize(String wrapperSizeValue){
        eventCount = 0;
        String fontSize =getWebElement("PLAYLIST_PLAYER").getCssValue("font-size");
        logger.info("Playlist Font Size is - "+fontSize);
        if(!fontSize.contains(wrapperSizeValue)){
            return false;
        }
        return checkPlayback(1);
    }

	public boolean getCaption() {
		eventCount = 0;
		boolean flag = true;
		List<WebElement> titleList = getWebElementsList("ASSET_TITLE");
		List<WebElement> desciptionList = getWebElementsList("ASSET_DESCRIPTION");
		List<WebElement> durationList = getWebElementsList("ASSET_DURATION");

		for (int i = 0; i < titleList.size(); i++) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", titleList.get(i));
			onmouseOver(desciptionList.get(i));
			logger.info("Title,Description,Duration of asset " + (i + 1) + " is : " + '\n' + titleList.get(i).getText()
					+ '\n' + desciptionList.get(i).getText() + '\n' + durationList.get(i).getText());
			flag = desciptionList.get(i).getText().contains("Test Description");
			if (!(flag && durationList.get(i).getText().contains(":"))) {
				return false;
			}
			/*
			 * Only two values are displaying on asset. Issue id PLAYER-457.
			 * flag = flag && titleList.get(i).getText().contains("tb_");
			 */
		}
		return checkPlayback(1);
	}

    public String getEmbedCode(){
        return ((JavascriptExecutor) driver).executeScript("return pp.getEmbedCode()").toString();
    }
}

