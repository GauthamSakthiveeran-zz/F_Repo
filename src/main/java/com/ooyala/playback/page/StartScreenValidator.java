package com.ooyala.playback.page;

import com.ooyala.playback.url.Url;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.playback.utils.APIUtils;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/16/16.
 */
public class StartScreenValidator extends PlayBackPage implements
		PlaybackValidator {

	private static Logger logger = Logger.getLogger(StartScreenValidator.class);

    APIUtils api = new APIUtils();

	public StartScreenValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("startscreen");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
        return true;
	}

    public boolean validateMetadata(UrlObject url){

        if (!waitOnElement("STATE_SCREEN_POSTER", 60000)) {
            return false;
        }

        boolean flag = true;

        // get the style attribute of class startScreenPoster which contailns
        // preview image url so that we compare it.
        String promoImageAttribute = getWebElement("STATE_SCREEN_POSTER").getAttribute(
                "style");

        if (promoImageAttribute != null) {
            String promoImage = promoImageAttribute.replaceAll(".*\\(|\\).*", "");
            promoImage = promoImage.replaceAll("^\"|\"$", "");
            if (!promoImage.contains(api.getPromoImageUrl(url.getEmbedCode(),url.getPCode()))) {
                logger.error("Promo Image is not matching");
                extentTest.log(LogStatus.FAIL, "Promo Image is not matching");
                flag = false;
            }
        } else {
            extentTest.log(LogStatus.FAIL,
                    "STATE_SCREEN_POSTER style attribute returns null.");
            flag = false;
        }

        // get title of video
        try {

            String startScreenTitle = getWebElement("STATE_SCREEN_TITLE")
                    .getText();

            // get Discription of video
            String description = getWebElement("STATE_SCREEN_DEC").getText();

            String title = ((JavascriptExecutor) driver).executeScript(
                    "var title=pp.getTitle();" + "{return title;}").toString();

            String desc = ((JavascriptExecutor) driver).executeScript(
                    "var description=pp.getDescription();"
                            + "{return description;}").toString();
            if (!startScreenTitle.equalsIgnoreCase(title)) {
                extentTest.log(LogStatus.FAIL,
                        "Title is not matching on start screen");
                flag = false;
            }

            if (!description.trim().equalsIgnoreCase(desc.trim())) {
                extentTest.log(LogStatus.FAIL,
                        "Description is not matching on Start Screen");
                flag = false;
            }

        } catch (Exception e) {
            logger.error("Title or description is failing");
            flag = false;
        }
        return flag;
    }
}
