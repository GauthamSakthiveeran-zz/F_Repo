package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.url.UrlObject;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

import java.util.ArrayList;

/**
 * Created by suraj on 3/23/17.
 */
public class StreamTypeValidator extends PlayBackPage implements PlaybackValidator {
	private static Logger logger = Logger.getLogger(StreamTypeValidator.class);

	public StreamTypeValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}
	
	private String streamType;
	
	public StreamTypeValidator setStreamType(String streamType){
		this.streamType = streamType;
		return this;
	}

	public boolean validate(String element, int timeout) throws Exception {

        if(streamType.contains("mp4")){
            logger.info("checking mp4 stream type");
            String mp4Url = driver.findElementById(element).getText();
            logger.info("opening a new tab");
            driver.executeScript("window.open('"+mp4Url+"')");
            ArrayList<String> tabs = new ArrayList<String>(
                    driver.getWindowHandles());
            Thread.sleep(2000);
            driver.switchTo().window(tabs.get(1));
            logger.info("navigated to new tab");
            Thread.sleep(2000);
            waitOnElement(By.xpath(".//*[@type='video/mp4']"),20000);
            String isMp4 = driver.findElement(By.xpath(".//*[@type='video/mp4']")).getAttribute("type");
            driver.close();
            Thread.sleep(2000);
            driver.switchTo().window(tabs.get(0));
            Thread.sleep(2000);
            if (isMp4.contains("mp4")){
                logger.info("Stream is matching as per expected result " + streamType);
                extentTest.log(LogStatus.PASS, "Stream is matching as per expected result " + streamType);
                return true;
            } else {
                logger.info("Stream is not matching as per expected result " + streamType);
                extentTest.log(LogStatus.PASS, "Stream is not matching as per expected result " + streamType);
                return false;
            }
        }
		
		String streamContains = driver.findElement(By.id("videoPlayingurl")).getText();
		
		if (!streamContains.contains(streamType)) {
			logger.error("Stream is not matching as per expected result " + streamContains);
			extentTest.log(LogStatus.FAIL, "Stream is not matching as per expected result " + streamContains);
			return false;
		}
        logger.info("verified Stream type :"+streamType);
        extentTest.log(LogStatus.PASS,"verified Stream type :"+streamType);
		return true;
	}

	public boolean verifyStreamType(String encoding) throws Exception{
		StreamTypeValidator streams = new PlayBackFactory(driver, extentTest).getStreamTypeValidator();
		if (encoding.contains("hls") || encoding.contains("m3u8")) {
			return streams.setStreamType("m3u8").validate("videoPlayingurl", 6000);
		}
		if (encoding.contains("dash") || encoding.contains("mpd")) {
			return streams.setStreamType("mpd").validate("videoPlayingurl", 6000);
		}
		if (encoding.contains("mp4")) {
			return streams.setStreamType("mp4").validate("videoPlayingurl", 6000);
		}
		if (encoding.contains("hds") || encoding.contains("f4m")) {
			return streams.setStreamType("f4m").validate("videoPlayingurl", 6000);
		}

		return false;
	}

    public boolean verifyPackagingType(String testName, UrlObject url) throws Exception{

        String streamContains = driver.findElement(By.id("videoPlayingurl")).getText();

        if (testName.contains("Elemental")){
            if (!verifyStreamType(url.getStreamType())){
                return false;
            }

            if(!streamContains.contains("http://secure-cf-c.ooyala.com")){
                logger.error("Elemental packaging does not served from expected : http://secure-cf-c.ooyala.com cloud server");
                extentTest.log(LogStatus.FAIL,"Elemental packaging does not served from expected : http://secure-cf-c.ooyala.com cloud server");
                return false;
            }else {
                logger.info("Elemental packaging is served from expected : http://secure-cf-c.ooyala.com cloud server");
                extentTest.log(LogStatus.PASS,"Elemental packaging is served from expected : http://secure-cf-c.ooyala.com cloud server");
            }

            if (!streamContains.contains("1."+streamType)){
                logger.error("Elemental packaging manifest is not in proper format i.e 1."+streamType);
                extentTest.log(LogStatus.FAIL,"Elemental packaging manifest is not in proper format i.e 1."+streamType);
                return false;
            }else {
                logger.info("Elemental packaging manifest is in proper format i.e 1."+streamType);
                extentTest.log(LogStatus.PASS,"Elemental packaging manifest is in proper format i.e 1."+streamType);
            }

        }
        if (testName.contains("Azure")){
            if (!verifyStreamType(url.getStreamType())){
                return false;
            }

            if (!streamContains.contains("http://ooyalaeast.streaming.mediaservices.windows.net")){
                logger.error("Azure packaging does not served from expected : http://ooyalaeast.streaming.mediaservices.windows.net cloud server");
                extentTest.log(LogStatus.FAIL,"Elemental packaging does not served from expected : http://ooyalaeast.streaming.mediaservices.windows.net cloud server");
                return false;
            }else {
                logger.info("Azure packaging gets served from expected : http://ooyalaeast.streaming.mediaservices.windows.net cloud server");
                extentTest.log(LogStatus.PASS, "Elemental packaging gets served from expected : http://ooyalaeast.streaming.mediaservices.windows.net cloud server");
            }

            if (!streamContains.contains(url.getEmbedCode()+".ism")){
                logger.error("Azure packaging does not served in expected format as : "+url.getEmbedCode()+".ism");
                extentTest.log(LogStatus.FAIL,"Azure packaging does not served in expcted format as : "+url.getEmbedCode()+".ism");
                return false;
            }else {
                logger.info("Azure packaging does gets served in expected format as : " + url.getEmbedCode() + ".ism");
                extentTest.log(LogStatus.PASS, "Azure packaging does gets served in expcted format as : " + url.getEmbedCode() + ".ism");
            }
        }
        if (testName.contains("FFMPEG")){

            if(!verifyStreamType(url.getStreamType())){
                return false;
            }

            if (!streamContains.contains("http://player.ooyala.com/player")){
                logger.error("FFMPEG packaging does not served from expected : http://player.ooyala.com/player cloud server");
                extentTest.log(LogStatus.FAIL,"FFMPEG packaging does not served from expected : http://player.ooyala.com/player cloud server");
                return false;
            }else {
                logger.info("FFMPEG packaging gets served from expected : http://player.ooyala.com/player cloud server");
                extentTest.log(LogStatus.PASS, "FFMPEG packaging gets served from expected : http://player.ooyala.com/player cloud server");
            }

            if(!streamContains.contains(url.getEmbedCode()+"."+streamType)){
                logger.error("FFMPEG packaging does not served in expected format as : "+url.getEmbedCode()+streamType);
                extentTest.log(LogStatus.FAIL,"Azure packaging does not served in expcted format as : "+url.getEmbedCode()+streamType);
                return false;
            }else {
                logger.info("FFMPEG packaging does gets served in expected format as : " + url.getEmbedCode() +streamType);
                extentTest.log(LogStatus.PASS, "Azure packaging does gets served in expcted format as : " + url.getEmbedCode() + streamType);
            }
        }
        if (testName.contains("Akamai")){

            if (!verifyStreamType(url.getStreamType())){
                return false;
            }

            if (testName.contains("Akamai") && testName.contains("HD2")){
                if(!streamContains.contains("http://100403.hdn.c.ooyala.com")){
                    logger.error("Akamai packaging does not served from expected : http://100403.hdn.c.ooyala.com cloud server");
                    extentTest.log(LogStatus.FAIL,"Akamai packaging does not served from expected : http://100403.hdn.c.ooyala.com cloud server");
                    return false;
                } else {
                    logger.error("Akamai packaging does gets served from expected : http://100403.hdn.c.ooyala.com cloud server");
                    extentTest.log(LogStatus.FAIL,"Akamai packaging does gets served from expected : http://100403.hdn.c.ooyala.com cloud server");
                }
            } else {
                if(!streamContains.contains("https://acaooyalahd2-lh.akamaihd.net")){
                    logger.error("Akamai packaging does not served from expected : https://acaooyalahd2-lh.akamaihd.net cloud server");
                    extentTest.log(LogStatus.FAIL,"Akamai packaging does not served from expected : https://acaooyalahd2-lh.akamaihd.net cloud server");
                    return false;
                } else {
                    logger.error("Akamai packaging does gets served from expected : https://acaooyalahd2-lh.akamaihd.net cloud server");
                    extentTest.log(LogStatus.FAIL,"Akamai packaging does gets served from expected : https://acaooyalahd2-lh.akamaihd.net cloud server");
                }
            }
        }

        return true;
    }

}
