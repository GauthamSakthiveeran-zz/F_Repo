package com.ooyala.playback.page;

import static java.lang.Double.parseDouble;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 28/11/16.
 */
public class MultiplePlayerValidator extends PlayBackPage implements PlaybackValidator {

	private static Logger logger = Logger.getLogger(MultiplePlayerValidator.class);

	public MultiplePlayerValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("multipleplayer");
		addElementToPageElements("replay");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		if (element.equalsIgnoreCase("player1_play")) {
			waitOnElement("PLAYER1_START_SCREEN", timeout);
			if (!clickOnHiddenElement("PLAYER1_START_SCREEN")) {
				extentTest.log(LogStatus.FAIL,"PLAYER1_START_SCREEN locator is not present");
				logger.error("PLAYER1_START_SCREEN locator is not present");
				return false;
			}
			extentTest.log(LogStatus.PASS,"Player 1 playing");
			logger.info("Player 1 playing");
		}

		if (element.contains("player1_pause")) {
			waitOnElement("PLAYER1_PAUSE", timeout);
			if (!clickOnHiddenElement("PLAYER1_PAUSE")) {
				extentTest.log(LogStatus.FAIL,"PLAYER1_PAUSE locator is not present");
				logger.error("PLAYER1_PAUSE locator is not present");
				return false;
			}
			extentTest.log(LogStatus.PASS,"Player 1 paused");
			logger.info("Player 1 paused");
		}

		if (element.equalsIgnoreCase("player2_play")) {
			waitOnElement("PLAYER2_START_SCREEN", timeout);
			if (!clickOnHiddenElement("PLAYER2_START_SCREEN")) {
				extentTest.log(LogStatus.FAIL,"PLAYER2_START_SCREEN locator is not present");
				logger.error("PLAYER2_START_SCREEN locator is not present");
				return false;
			}
			extentTest.log(LogStatus.PASS,"Player 2 playing");
			logger.info("Player 2 playing");
		}

		if (element.contains("player2_pause")) {
			waitOnElement("PLAYER2_PAUSE", timeout);
			if (!clickOnHiddenElement("PLAYER2_PAUSE")) {
				extentTest.log(LogStatus.FAIL,"PLAYER2_PAUSE locator is not present");
				logger.error("PLAYER2_PAUSE locator is not present");
				return false;
			}
			extentTest.log(LogStatus.PASS,"Player 2 paused");
			logger.info("Player 2 paused");
		}

		if (element.contentEquals("seek1")) {
			if ((Boolean) ((JavascriptExecutor) driver).executeScript("return pp1.isPlaying();")) {
				((JavascriptExecutor) driver).executeScript("return pp1.seek(pp1.seek(pp1.getDuration()-1));");
				logger.info("Video 1 is Seeked");

			} else {
				clickOnHiddenElement("PLAYER1_START_SCREEN");
				((JavascriptExecutor) driver).executeScript("return pp1.seek(pp1.seek(pp1.getDuration()-1));");
				logger.info("Video 1 is Seeked");
			}
		}

		if (element.contentEquals("seek2")) {
			if ((Boolean) ((JavascriptExecutor) driver).executeScript("return pp2.isPlaying();")) {
				((JavascriptExecutor) driver).executeScript("return pp1.seek(pp2.seek(pp2.getDuration()-1));");
				logger.info("Video 2 is Seeked");
			} else {
				clickOnHiddenElement("PLAYER2_START_SCREEN");
				((JavascriptExecutor) driver).executeScript("return pp1.seek(pp2.seek(pp2.getDuration()-1));");
				logger.info("Video 2 is Seeked");
			}
		}
		return true;
	}

	public boolean validateVolume() {

		boolean flag = true;

		double alreadysetvol = parseDouble(
				((JavascriptExecutor) driver).executeScript("return pp[0].getVolume()").toString());
		double expectedmutevol = 0.0;

		if (!clickOnIndependentElement("PLAYER1_ONVOLUME"))
			return false;

		double getmutevol = parseDouble(
				((JavascriptExecutor) driver).executeScript("return pp[0].getVolume()").toString());

		if (getmutevol != expectedmutevol) {
			extentTest.log(LogStatus.FAIL, "Player1, Mute Volume isnt working.");
			flag = false;
		}

		if (!clickOnIndependentElement("PLAYER1_MUTEVOLUME"))
			return false;

		double getmaxvol = parseDouble(
				((JavascriptExecutor) driver).executeScript("return pp[0].getVolume()").toString());

		if (getmaxvol != alreadysetvol) {
			extentTest.log(LogStatus.FAIL, "Player1, Max Volume isnt working.");
			flag = false;
		}

		alreadysetvol = parseDouble(((JavascriptExecutor) driver).executeScript("return pp[1].getVolume()").toString());
		expectedmutevol = 0.0;

		if (!clickOnIndependentElement("PLAYER2_ONVOLUME"))
			return false;

		getmutevol = parseDouble(((JavascriptExecutor) driver).executeScript("return pp[1].getVolume()").toString());

		if (getmutevol != expectedmutevol) {
			extentTest.log(LogStatus.FAIL, "Player2, Mute Volume isnt working.");
			flag = false;
		}

		if (!clickOnIndependentElement("PLAYER2_MUTEVOLUME"))
			return false;

		getmaxvol = parseDouble(((JavascriptExecutor) driver).executeScript("return pp[1].getVolume()").toString());

		if (getmaxvol != alreadysetvol) {
			extentTest.log(LogStatus.FAIL, "Player1, Max Volume isnt working.");
			flag = false;
		}

		return flag;
	}

	public boolean validateFullScreen() throws Exception {

		if (!clickOnIndependentElement("PLAYER1_FULLSCREEN"))
			return false;

		if (!waitOnElement("PLAYER1_NORMALSCREEN", 10000))
			return false;

		if (!clickOnIndependentElement("PLAYER1_NORMALSCREEN"))
			return false;

		if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("firefox"))) {
			if (!waitOnElement(By.id("player1_fullscreenChanged_true"), 20000))
				return false;
			if (!waitOnElement(By.id("player1_fullscreenChanged_false"), 20000))
				return false;
		}

		if (!clickOnIndependentElement("PLAYER2_FULLSCREEN"))
			return false;

		if (!clickOnIndependentElement("PLAYER2_NORMALSCREEN"))
			return false;

		if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("firefox"))) {
			if (!waitOnElement(By.id("player2_fullscreenChanged_true"), 20000))
				return false;
			if (!waitOnElement(By.id("player2_fullscreenChanged_false"), 20000))
				return false;
		}

		return true;
	}
}
