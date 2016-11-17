package com.ooyala.playback.page;

import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 10/27/16.
 */
public class SeekValidator extends BaseValidator {

    public static Logger logger = Logger.getLogger(SeekValidator.class);

	public SeekValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
        addElementToPageElements("play");
	}

	public void validate(String element, int timeout) throws Exception {
        while (true) {
            if (Double.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString()) > 5) {
                seek(7, true);
                //loadingSpinner();
                ((JavascriptExecutor) driver).executeScript("pp.pause();");
                Thread.sleep(2000);
                ((JavascriptExecutor) driver).executeScript("pp.play();");
                break;
            }
        }
        waitOnElement(element, timeout);
        logger.info("Video seeked successfully");
    }

    public void seek(int time,boolean fromLast)throws Exception{
        String seekduration;
        if(fromLast){
            seekduration = "pp.getDuration()";
        }else{
            seekduration ="";
        }
        ((JavascriptExecutor) driver).executeScript("return pp.seek(" + seekduration+"-"+time+")" + "");
    }

    public void seek(String time)throws Exception{
        ((JavascriptExecutor) driver).executeScript("return pp.seek(" +time+")" + "");
    }

}
