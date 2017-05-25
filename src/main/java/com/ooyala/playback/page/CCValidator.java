package com.ooyala.playback.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/3/16.
 */
public class CCValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(CCValidator.class);

	List<WebElement> lang, textColor, bgColor, ccWinColor, ccFontSize, ccTextEnhancement;
	String previewTextSelected, textSelected, ccFontSizeBefore, ccFontSizeAfter, ccFontTypeBefore, ccFontTypeAfter,
			ccTextEnhancementSelectedBefore, ccTextEnhancementSelectedAfter;
	HashMap<String, String> ccOpacityMapBefore, ccOpacityMapAfter, ccColorSelectionBefore, ccColorSelectionAfter;

	public CCValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("cc");
		addElementToPageElements("fcc");
		addElementToPageElements("fullscreen");
		addElementToPageElements("pause");
		addElementToPageElements("controlbar");
		addElementToPageElements("discovery");
	}

	public boolean closeCCPanel() {
		try {
			if (!clickOnIndependentElement("CC_PANEL_CLOSE"))
				return false;
			return true;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Error while closing cc panel.", e);
			return false;
		}
	}

	private boolean validateClosedCaptionPanel() throws Exception {
		switchToControlBar();
		if (!isElementPresent("CLOSED_CAPTION_PANEL")) {
			if (!clickOnIndependentElement("CC_BTN")) {
				return false;
			}
		}

		if (isElementPresent("CLOSED_CAPTION_PANEL")) {
			extentTest.log(LogStatus.PASS, "Verified closed caption panel");
			return true;
		}
		extentTest.log(LogStatus.FAIL, "CLOSED_CAPTION_PANEL is not present");
		return false;

	}

	private boolean validateSwitchContainer() {
		try {
			if (getBrowser().equalsIgnoreCase("MicrosoftEdge")) {
				WebElement element = getWebElement("CC_SWITCH");
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			}
		} catch (Exception e) {
			logger.error("Error in focus on element cc switch");
		}

		if (clickOnIndependentElement("CC_SWITCH_CONTAINER")) {
			if (!isElementPresent("CC_OFF")) {
				extentTest.log(LogStatus.FAIL, "CC_OFF is not present.");
				return false;
			}

			if (!waitOnElement(By.id("savePlayerSettings_off_1"), 20000)) {
				extentTest.log(LogStatus.INFO, "savePlayerSettings event after making CC button off is not triggering");
			}

			logger.info("savePlayerSettings event after making CC button off is triggering");

			if (!(clickOnIndependentElement("CC_SWITCH_CONTAINER") && isElementPresent("CC_ON"))) {
				extentTest.log(LogStatus.FAIL, "CC_ON is not present");
				return false;
			}

			if (!waitOnElement(By.id("savePlayerSettings_on_1"), 20000)) {
				extentTest.log(LogStatus.INFO, "savePlayerSettings event after making CC button on is not triggering");
			}

			logger.info("savePlayerSettings event after making CC button on is triggering");

		} else {
			extentTest.log(LogStatus.FAIL, "click on CC_SWITCH_CONTAINER failed");
			return false;
		}
		return true;
	}

	public boolean switchToControlBar() {
		try {
			if (isElementPresent("HIDDEN_CONTROL_BAR")) {
				logger.info("hovering mouse over the player");
				moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
			} else if (isElementPresent("CONTROL_BAR")) {
				moveElement(getWebElement("CONTROL_BAR"));
			}
			return true;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Error while switching to control bar.", e);
			return false;
		}
	}

	public boolean closedCaptionMicroPanel() throws Exception {
		try {
			switchToControlBar();

			if (!clickOnIndependentElement("CC_BTN"))
				return false;

			if (!getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")) {
				if (!waitOnElement("CC_POPHOVER_HORIZONTAL", 6000)) {
					switchToControlBar();
					clickOnIndependentElement("CC_BTN");
				}
				boolean horizontal_CC_Option = isElementPresent("CC_POPHOVER_HORIZONTAL");

				if (horizontal_CC_Option) {
					if (!isElementPresent("CC_POPHOVER_HORIZONTAL")) {
						switchToControlBar();
					}
					if (isElementPresent("CC_SWITCH_CONTAINER_HORIZONTAL") && isElementPresent("CC_MORE_CAPTIONS")
							&& clickOnIndependentElement("CC_MORE_CAPTIONS")) {
						return true;
					} else {
						extentTest.log(LogStatus.FAIL, "Verification of cc pop over horizontal elements failed.");
						return false;
					}

				}
			} else {
				return true;
			}
			extentTest.log(LogStatus.FAIL, "CC_POPHOVER_HORIZONTAL is not present!");
			return false;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Horizontal cc option is not present");
			;
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	private boolean checkClosedCaptionLanguages() throws Exception {
		ArrayList<String> langlist = ((ArrayList<String>) (((JavascriptExecutor) driver).executeScript(
				"var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;" + "{return attrb;}")));
		boolean flag = true;
		if (langlist == null || langlist.size() == 0) {
			extentTest.log(LogStatus.FAIL, "langList is null");
			return false;
		}
		logger.info("Closed Caption Available Languages: " + langlist);
		for (int i = 0; i < langlist.size(); i++) {
			((JavascriptExecutor) driver).executeScript("pp.setClosedCaptionsLanguage(\"" + langlist.get(i) + "\")");

			flag = flag && waitOnElement(By.id("cclanguage_" + langlist.get(i)), 10000);

			if (!flag)
				extentTest.log(LogStatus.FAIL, "Wait on element cclanguage_" + langlist.get(i) + " failed.");

		}
		if (flag)
			extentTest.log(LogStatus.PASS, "Verified closed caption panel languages");
		else
			extentTest.log(LogStatus.FAIL, "Closed caption panel languages Failed.");
		return flag;
	}

	private boolean checkArrows() {
		try {
			if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-responsive oo-xsmall")) {

				if (waitOnElement("CC_LEFT_SCROLL_BTN", 10000)) {
					clickOnIndependentElement("CC_LEFT_SCROLL_BTN");
					logger.info("Left CC Scroll arrow is  present");
				}
				if (waitOnElement("CC_RIGHT_SCROLL_BTN", 10000)) {
					clickOnIndependentElement("CC_RIGHT_SCROLL_BTN");
					logger.info("Right CC Scroll arrow is present");
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("Error in checking arrows from closed caption window." + e);
			extentTest.log(LogStatus.FAIL, "Error in checking arrows from closed caption window", e);
			return false;
		}
	}

	private boolean verifyCCPanelElements() {
		try {
			// verify scroll i.e left or right button for languages option if
			// lang more than 4
			boolean isLeftRightBtn = isElementPresent("CC_RIGHT_BTN");
			if (isLeftRightBtn) {
				logger.info("verifying the scrolling for langauges");
				if (!clickOnIndependentElement("RIGHT_BTN")) {
					extentTest.log(LogStatus.FAIL, "Click on RIGHT_BTN failed");
					return false;
				}
				if (!clickOnIndependentElement("LEFT_BTN")) {
					extentTest.log(LogStatus.FAIL, "Click on LEFT_BTN failed");
					return false;
				}
			}

			if (!isElementPresent("CC_PREVIEW_CAPTION")) {
				extentTest.log(LogStatus.FAIL, "CC_PREVIEW_CAPTION is not present.");
			}

			if (!validateSwitchContainer())
				return true;

			return true;
		} catch (Exception e) {
			logger.error("Error while verifying CC Panel Elements " + e);
			extentTest.log(LogStatus.FAIL, "Error while verifying CC Panel Elements", e);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	protected boolean verifyClosedCaptionLanguages() {
		try {
			boolean flag = true;
			// get available languages for video
			Object ccobj = ((JavascriptExecutor) driver).executeScript(
					"var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;" + "{return attrb;}");

			ArrayList<String> langlist = ((ArrayList<String>) ccobj);
			logger.info("\t Closed Caption Available Languages: " + langlist + "\n \t  languages available count :"
					+ langlist.size());

			String[] langl = new String[langlist.size()];
			langlist.toArray(langl);

			// select language and verify that Preview Text is shown
			lang = getWebElementsList("LANG_LIST");
			logger.info("language Count Value in Languages :" + lang.size());
			String langpreview1[] = { "Sample Text", "Texto de muestra", "サンプル" };

			// issue id
			if (getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-responsive oo-xsmall")) {
				for (int j = 0; j < langlist.size(); j++) {
					for (int i = 0; i < lang.size(); i++) {
						lang.get(i).click();
						if (isElementPresent("RIGHT_ARROW")) {
							flag = flag && clickOnIndependentElement("RIGHT_ARROW");
						} else {
							logger.info("Right Arrow is not present");
						}
					}
				}
				if (isElementPresent("LEFT_ARROW")) {
					flag = flag && clickOnIndependentElement("LEFT_ARROW");
				} else {
					logger.info("Left Arrow is not present");
				}
			} else {
				for (int i = 0; i < lang.size(); i++) {
					lang.get(i).click();

					if (!getWebElement("oo-responsive").getAttribute("className")
							.equalsIgnoreCase("oo-responsive oo-small")) {
						if (isElementPresent(By.id("fullscreenChanged_true"))) {
							if (!waitOnElement("CC_PREVIEW_TEXT", 30000)) {
								return false;
							}
							String engPreviewText = getWebElement("CC_PREVIEW_TEXT").getText();
							flag = flag && langpreview1[i].equalsIgnoreCase(engPreviewText);
						}
					}
				}
			}
			return flag;
		} catch (Exception e) {
			logger.error("Preview text is not visible" + e);
			extentTest.log(LogStatus.FAIL, "Error while checking closed caption languages.", e);
			return false;
		}
	}

	protected boolean verifyCCColorSelectionPanel(String testName) {
		try {
			String colorsName[] = { "Transparent", "White", "Blue", "Magenta", "Green", "Yellow", "Red", "Cyan",
					"Black" };
			String colorsCode[] = { "rgba(255, 255, 255, 1)", "rgba(0, 0, 255, 1)", "rgba(255, 0, 255, 1)",
					"rgba(0, 255, 0, 1)", "rgba(255, 255, 0, 1)", "rgba(255, 0, 0, 1)", "rgba(0, 255, 255, 1)",
					"rgba(0, 0, 0, 1)" };
			String colorsCode1[] = { "rgba(255, 255, 255, 0.8)", "rgba(0, 0, 255, 0.8)", "rgba(255, 0, 255, 0.8)",
					"rgba(0, 255, 0, 0.8)", "rgba(255, 255, 0, 0.8)", "rgba(255, 0, 0, 0.8)", "rgba(0, 255, 255, 0.8)",
					"rgba(0, 0, 0, 0.8)" };
			String bgColorsCode[] = { "rgba(0, 0, 0, 0)", "rgba(255, 255, 255, 0.8)", "rgba(0, 0, 255, 0.8)",
					"rgba(255, 0, 255, 0.8)", "rgba(0, 255, 0, 0.8)", "rgba(255, 255, 0, 0.8)", "rgba(255, 0, 0, 0.8)",
					"rgba(0, 255, 255, 0.8)", "rgba(0, 0, 0, 0.8)" };
			String bgColorsCode1[] = { "Transparent", "rgba(255, 255, 255, 0.8)", "rgba(0, 0, 255, 0.8)",
					"rgba(255, 0, 255, 0.8)", "rgba(0, 255, 0, 0.8)", "rgba(255, 255, 0, 0.8)", "rgba(255, 0, 0, 0.8)",
					"rgba(0, 255, 255, 0.8)", "rgba(0, 0, 0, 0.8)" };
			boolean flag = true;

			// verify color selection panel
			if (!waitOnElement("COLOR_SELECTION_PANEL", 30000))
				return false;
			if (!clickOnIndependentElement("COLOR_SELECTION_PANEL"))
				return false;
			logger.info("\n*---------Verifying Color Selection Panel---------*\n");

			// select text colors
			textColor = getWebElementsList("CC_TEXT_COLOR_SELECTOR");

			if (textColor == null || textColor.size() == 0) {
				extentTest.log(LogStatus.FAIL, "CC_TEXT_COLOR_SELECTOR not found");
			}

			logger.info("\t \t \t Color Count Value in Text Color:" + textColor.size());
			logger.info("\n*---------Verify Text Color Selection Panel---------*\n");

			for (int i = 0; i < textColor.size(); i++) {
				textColor.get(i).click();
				String ccTextColor = getWebElement("CC_TEXT_COLOR").getText(); // e.g.
																				// Text
																				// color:
																				// White
				logger.info("\t Text Color Selected :" + ccTextColor);

				if (!colorsName[i + 1].equalsIgnoreCase(ccTextColor)) {
					extentTest.log(LogStatus.FAIL,
							"Color mismatch : Expected - " + colorsName[i + 1] + " Actual : " + ccTextColor);
				}

				// verify color selected
				// issue id
				if (getWebElement("oo-responsive").getAttribute("className")
						.equalsIgnoreCase("oo-responsive oo-large")) {
					String ccPreviewTextColor = getWebElement("CC_PREVIEW_TEXT").getCssValue("color");
					logger.info("\t Preview Text Color Selected :" + ccPreviewTextColor);

					if (colorsCode[i].equalsIgnoreCase(ccPreviewTextColor)) {
						extentTest.log(LogStatus.PASS,
								"Color mismatch : Expected - " + colorsCode[i] + " Actual : " + ccPreviewTextColor);
					} else if (!colorsCode1[i].equalsIgnoreCase(ccPreviewTextColor)) {
						extentTest.log(LogStatus.FAIL,
								"Color mismatch : Expected - " + colorsCode1[i] + " Actual : " + ccPreviewTextColor);
					}
				}
			}
			logger.info("verified text color selection is working fine");

			// select Background colors
			bgColor = getWebElementsList("CC_BACKGROUND_COLOR_SELECTOR");
			logger.info("\t Color Count Value in Background Color:" + bgColor.size());
			logger.info("\n*---------Verify Background color Selection Panel---------*\n");

			if (bgColor == null || bgColor.size() == 0) {
				extentTest.log(LogStatus.FAIL, "CC_BACKGROUND_COLOR_SELECTOR not found");
			}

			for (int i = 0; i < bgColor.size(); i++) {
				if (getWebElement("oo-responsive").getAttribute("className")
						.equalsIgnoreCase("oo-responsive oo-xsmall")) {
					WebElement element = bgColor.get(i);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
					bgColor.get(i).click();
				} else {
					bgColor.get(i).click();
				}

				String ccBgColor = getWebElement("CC_BACKGROUND_COLOR").getText(); // e.g
																					// Background
																					// color:
																					// Black
				logger.info("\t Background Color Selected :" + ccBgColor);
				flag = flag && colorsName[i].equalsIgnoreCase(ccBgColor);

				if (!colorsName[i].equalsIgnoreCase(ccBgColor)) {
					extentTest.log(LogStatus.FAIL,
							"Bg Color mismatch : Expected - " + colorsName[i] + " Actual : " + ccBgColor);
				}

				// issue id
				if (!testName.contains("PlaybackFCCDefaultSettingTests")) {
					if (getWebElement("oo-responsive").getAttribute("className")
							.equalsIgnoreCase("oo-responsive oo-large")) {
						String ccPreviewBgColor = getWebElement("CC_PREVIEW_TEXT_BG").getCssValue("background-color");
						logger.info("\t Background color of Preview Text Selected :" + ccPreviewBgColor);
						if (ccPreviewBgColor.contains("transparent")) {
							if (!bgColorsCode1[i].equalsIgnoreCase(ccPreviewBgColor)) {
								extentTest.log(LogStatus.FAIL, "Bg Color mismatch : Expected - " + bgColorsCode1[i]
										+ " Actual : " + ccPreviewBgColor);
							}
						} else {
							if (!bgColorsCode[i].equalsIgnoreCase(ccPreviewBgColor)) {
								extentTest.log(LogStatus.FAIL, "Bg Color mismatch : Expected - " + bgColorsCode[i]
										+ " Actual : " + ccPreviewBgColor);
							}
						}
					}
				}
			}

			logger.info("verified background color selection is working fine");

			// select Windows colors
			ccWinColor = getWebElementsList("CC_WINDOW_COLOR_SELECTOR");
			logger.info("\n Color Count Value in Windows Color:" + ccWinColor.size() + "\n");
			logger.info("\n*---------Verify Window Color Selection Panel---------*\n");

			if (ccWinColor == null || ccWinColor.size() == 0) {
				extentTest.log(LogStatus.FAIL, "CC_WINDOW_COLOR_SELECTOR not found");
			}

			for (int i = 0; i < ccWinColor.size(); i++) {
				if (getWebElement("oo-responsive").getAttribute("className")
						.equalsIgnoreCase("oo-responsive oo-xsmall")) {
					WebElement element = ccWinColor.get(i);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
					ccWinColor.get(i).click();
				} else {
					ccWinColor.get(i).click();
				}
				String ccWindowColor = getWebElement("CC_WINDOW_COLOR").getText();
				logger.info("\t Window Color Selected :" + ccWindowColor);

				if (!colorsName[i].equalsIgnoreCase(ccWindowColor)) {
					extentTest.log(LogStatus.FAIL,
							"Window Color mismatch : Expected - " + colorsName[i] + " Actual : " + ccWindowColor);
				}
				// issue id
				if (!testName.contains("PlaybackFCCDefaultSettingTests")) {
					if (getWebElement("oo-responsive").getAttribute("className")
							.equalsIgnoreCase("oo-responsive oo-large")) {
						String ccPreviewWinColor = getWebElement("CC_PREVIEW_WIN_COLOR")
								.getCssValue("background-color");
						logger.info("\t Window color of Preview Text Selected :" + ccPreviewWinColor);
						if (ccPreviewWinColor.contains("transparent")) {
							if (bgColorsCode1[i].equalsIgnoreCase(ccPreviewWinColor)) {
								extentTest.log(LogStatus.FAIL, "Window Color mismatch : Expected - " + bgColorsCode1[i]
										+ " Actual : " + ccPreviewWinColor);
							}
						} else {
							if (bgColorsCode[i].equalsIgnoreCase(ccPreviewWinColor)) {
								extentTest.log(LogStatus.FAIL, "Window Color mismatch : Expected - " + bgColorsCode[i]
										+ " Actual : " + ccPreviewWinColor);
							}
						}
					}
				}
			}

			return true;
		} catch (Exception e) {
			logger.error("Error while verifying CC Color Selection Panel " + e);
			extentTest.log(LogStatus.FAIL, "Error while verifying CC Color Selection Panel.", e);
			return false;
		}
	}

	private void slideSliderCaptionOpacity(WebElement slider) {
		if (getBrowser().equalsIgnoreCase("safari")) {
			((JavascriptExecutor) driver).executeScript(
					"function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; "
							+ "simulate(arguments[0],\"mousedown\",0,0); simulate(arguments[0],\"mousemove\",arguments[1],arguments[2]); simulate(arguments[0],\"mouseup\",arguments[1],arguments[2]); ",
					slider);
		} else {
			Actions move = new Actions(driver);
			int width = slider.getSize().getWidth();
			move.dragAndDropBy(slider, (width * 25) / 100, 0).build().perform();
		}
	}

	private boolean validateCaptionOpacity(String opacityValue) {
		if (!getBrowser().equalsIgnoreCase("safari")) {
			if (opacityValue.equals("rgba(0, 0, 0, 0.8)")) {
				return true;
			} else {
				logger.error(opacityValue + "is not matching with rgba(0, 0, 0, 0.8)");
				return false;
			}
		} else {
			if (opacityValue.equals("rgba(0, 0, 0, 0)")) {
				return true;
			}
		}
		logger.error(opacityValue + "value is not matching with rgba(0, 0, 0, 0)");
		return false;
	}

	protected boolean verifyCCOpacityPanel(String testName) {
		try {
			// verify CC Opacity Panel
			if (!clickOnIndependentElement("CAPTION_OPACITY_PANEL"))
				return false;
			logger.info("\n*----------------------Verify Caption Opacity Panel--------------------*\n");

			// select text Opacity
			WebElement slider1 = getWebElement("CC_TEXT_OPACITY_SELECTOR");
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", slider1);

			slideSliderCaptionOpacity(slider1);
			String ccTextOpacity = getWebElement("CC_TEXT_OPACITY").getText();
			logger.info("\t Text Opacity Selected :" + ccTextOpacity);
			String ccPreviewTextOpacity = getWebElement("CC_PREVIEW_TEXT").getCssValue("color");
			logger.info("\t Preview Text Opacity Selected :" + ccPreviewTextOpacity);
			if (!testName.contains("PlaybackFCCDefaultSettingTests")) {
				if (!validateCaptionOpacity(ccPreviewTextOpacity)) {
					return false;
				}
			}
			logger.info("verified text opacity selection is working fine");

			// select Background Opacity
			WebElement slider2 = getWebElement("CC_BACKGROUND_OPACITY_SELECTOR");
			WebElement backgroundOpacity = getWebElement("BACKGROUND_OPACITY");
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", backgroundOpacity);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", slider2);
			slideSliderCaptionOpacity(slider2);
			String ccBgOpacity = getWebElement("CC_BACKGROUND_OPACITY").getText();
			logger.info("\t Background Opacity Selected :" + ccBgOpacity);
			String ccPreviewBgOpacity = getWebElement("CC_PREVIEW_TEXT_BG").getCssValue("background-color");
			logger.info("\t Preview Text Background Opacity Selected :" + ccPreviewBgOpacity);

			if (!testName.contains("PlaybackFCCDefaultSettingTests")) {
				if (!validateCaptionOpacity(ccPreviewBgOpacity)) {
					return false;
				}
			}

			logger.info("verified Background opacity selection is working fine");

			// select Windows Opacity
			WebElement slider3 = getWebElement("CC_WINDOW_OPACITY_SELECTOR");
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", slider3);
			slideSliderCaptionOpacity(slider3);
			String ccWinOpacity = getWebElement("CC_WINDOW_OPACITY").getText();
			logger.info("\t Window Color Selected :" + ccWinOpacity);
			String ccPreviewWinOpacity = getWebElement("CC_PREVIEW_WIN_COLOR").getCssValue("background-color");
			logger.info("\t Window Opacity of Preview Text Selected :" + ccPreviewWinOpacity);

			if (!testName.contains("PlaybackFCCDefaultSettingTests")) {
				if (!validateCaptionOpacity(ccPreviewWinOpacity)) {
					return false;
				}
			}
			logger.info("verified Windows opacity selection is working fine");
			return true;
		} catch (Exception e) {
			logger.error("Errors while verifying CC Opacity Panel " + e);
			extentTest.log(LogStatus.FAIL, "Error while verifying CC Opacity Panel.", e);
			return false;
		}
	}

	protected boolean verifyCCFonttypePanel() {
		try {
			// verify CC Font Type Panel
			if (!clickOnIndependentElement("CC_FONT_TYPE_PANEL"))
				return false;
			logger.info("\n*--------------Verify Font Type Panel-------------------------*\n");

			List<WebElement> ccFontType = getWebElementsList("CC_FONT_TYPE");
			logger.info("\t Font Type Count Value :" + ccFontType.size());

			while (isElementPresent("RIGHT_ARROW")) {
				List<WebElement> ccFontType1 = getWebElementsList("CC_FONT_TYPE");
				for (int i = 0; i < ccFontType1.size(); i++) {
					ccFontType1.get(i).click();
					String ccFontTypeSelected = ccFontType1.get(i).getText();
					logger.info("\n Language Selected - " + ccFontTypeSelected);
					String ccPreviewTextFont = getWebElement("CC_PREVIEW_TEXT").getCssValue("font-family");
					logger.info("\t Font type selected for CC Preview Text :" + ccPreviewTextFont);
				}

				if (isElementPresent("TOGGLING_ARROW")) {
					break;
				}
				if (!clickOnIndependentElement("RIGHT_ARROW"))
					return false;
			}
			logger.info("verified Font Type selection is working fine");

			return true;
		} catch (Exception e) {
			logger.error("Error while verifying CC Font type Panel " + e);
			extentTest.log(LogStatus.FAIL, "Error while verifying CC Font type Panel.", e);
			return false;
		}
	}

	protected boolean verifyCCFontSizePanel() {
		try {
			// Verify Font Size Panel
			String ccFonts[] = { "Small", "Medium", "Large", "Extra Large" };
			// String ccFontSizes[] = {"19.6px","25.2px","30.8px","36.4px"};
			String font_size_xsmall[] = { "0.8em", "1.2em", "1.6em", "2em" };
			String font_size_small[] = { "1em", "1.4em", "1.8em", "2.2em" };
			String font_size_medium[] = { "1.2em", "1.6em", "2em", "2.4em" };
			String font_size_large[] = { "1.4em", "1.8em", "2.2em", "2.6em" };
			boolean flag1 = true;

			if (!clickOnIndependentElement("CC_FONT_SIZE_PANEL"))
				return false;
			logger.info("\n*--------------Verify CC Font Size Panel---------------------*\n");
			ccFontSize = getWebElementsList("CC_FONT_SIZE_SELECTOR");
			logger.info("\t \t \t Font Size Count Value :" + ccFontSize.size());

			for (int i = 0; i < ccFontSize.size(); i++) {
				if (getWebElement("oo-responsive").getAttribute("className")
						.equalsIgnoreCase("oo-responsive oo-xsmall")) {
					WebElement element = ccFontSize.get(i);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
					ccFontSize.get(i).click();
				} else {
					ccFontSize.get(i).click();
				}
				String ccTextFontSize = getWebElement("CC_FONT_SIZE_SELECTED").getText();
				logger.info("\t Text Font Size Selected :" + ccTextFontSize);
				String ccPreviewTextFontSize[] = getWebElement("CC_PREVIEW_TEXT").getAttribute("style").split(";");
				String fontSizeInEm = null;
				int flag = 0;
				for (String cc : ccPreviewTextFontSize) {
					if (!getBrowser().equalsIgnoreCase("safari")) {
						if (cc.contains("font-variant-caps:")) {
							String fontSize[] = ccPreviewTextFontSize[4].split(":");
							fontSizeInEm = fontSize[1].trim();
							flag = 1;
							break;
						}
					}
				}
				if (flag == 0) {
					String fontSize[];
					if (getBrowser().equalsIgnoreCase("internet explorer")
							|| getBrowser().equalsIgnoreCase("MicrosoftEdge")) {
						fontSize = ccPreviewTextFontSize[2].split(":");
						fontSizeInEm = fontSize[1].trim();
					} else {
						fontSize = ccPreviewTextFontSize[3].split(":");
						fontSizeInEm = fontSize[1].trim();
					}

					if (getBrowser().equalsIgnoreCase("safari")) {
						fontSize = ccPreviewTextFontSize[8].split(":");
						fontSizeInEm = fontSize[1].trim();
					}
				}
				logger.info("\t font size in em : " + fontSizeInEm);
				// Assert.assertEquals(ccFonts[i], ccTextFontSize); //verify
				// font size selected
				flag1 = flag1 && ccFonts[i].equalsIgnoreCase(ccTextFontSize);

				if (getWebElement("oo-responsive").getAttribute("className")
						.equalsIgnoreCase("oo-responsive oo-large")) {
					if (!font_size_large[i].equals(fontSizeInEm)) {
						return false;
					}
				}
				if (getWebElement("oo-responsive").getAttribute("className")
						.equalsIgnoreCase("oo-responsive oo-medium")) {
					if (!font_size_medium[i].equals(fontSizeInEm)) {
						return false;
					}
				}
				if (getWebElement("oo-responsive").getAttribute("className")
						.equalsIgnoreCase("oo-responsive oo-small")) {
					if (!font_size_small[i].equalsIgnoreCase(fontSizeInEm)) {
						return false;
					}
				}
				if (getWebElement("oo-responsive").getAttribute("className")
						.equalsIgnoreCase("oo-responsive oo-xsmall")) {
					if (!font_size_xsmall[i].equals(fontSizeInEm)) {
						return false;
					}
				}
			}

			logger.info("verified Font Size selection is working fine");
			return flag1;
		} catch (Exception e) {
			logger.error("Error while verifying font size " + e);
			extentTest.log(LogStatus.FAIL, "Error while verifying CC Font size Panel.", e);
			return false;
		}
	}

	protected boolean verifyCCTextEnhancementPanel() {
		try {
			String textEnName[] = { "Uniform", "Depressed", "Raised", "Shadow" };
			String textEnCode[] = { "none", "rgb(255, 255, 255) 1px 1px 0px",
					"rgb(255, 255, 255) -1px -1px 0px, rgb(0, 0, 0) -3px 0px 5px", "rgb(26, 26, 26) 2px 2px 2px" };
			String textEnCodeForIE[] = { "none", "1px 1px white", "-1px -1px white, -3px 0px 5px black",
					"2px 2px 2px #1a1a1a" };
			if (!clickOnIndependentElement("CC_TEXT_ENHANCEMENT"))
				return false;
			logger.info("\n*---------------Verify CC Text Enhancement Panel--------------*\n");

			ccTextEnhancement = getWebElementsList("CC_TEXT_ENHANCEMENT_SELECTOR");
			logger.info("\t Text Enhancement Type Count Value :" + ccTextEnhancement.size());
			for (int i = 0; i < ccTextEnhancement.size(); i++) {

				if (getWebElement("oo-responsive").getAttribute("className")
						.equalsIgnoreCase("oo-responsive oo-xsmall")) {
					WebElement element = ccTextEnhancement.get(i);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
					ccTextEnhancement.get(i).click();
				} else {
					ccTextEnhancement.get(i).click();
				}

				String ccTextEnh = getWebElement("CC_FONT_SIZE_SELECTED").getText();
				logger.info("\t Text Enhancement Selected :" + ccTextEnh);
				String ccPreviewTextEnh = getWebElement("CC_PREVIEW_TEXT").getCssValue("text-shadow");
				logger.info("\t Text Enhancement for CC Preview Text :" + ccPreviewTextEnh);
				if (!(textEnName[i].equals(ccTextEnh))) {
					logger.error("Preview Text enhancement is not matching");
					return false;
				}
				// verify text enhancement selected
				if (getBrowser().equalsIgnoreCase("internet explorer")
						|| getBrowser().equalsIgnoreCase("MicrosoftEdge")) {
					if (!(textEnCodeForIE[i].equals(ccPreviewTextEnh))) {
						logger.error("Preview Text enhancement is not matching for internet explorer browser");
						return false;
					}
				} else if (!(textEnCode[i].equals(ccPreviewTextEnh))) {
					logger.error("Preview Text enhancement is not matching for " + getBrowser() + "browser");
					return false;
				}
			}
			logger.info("verified CC Text Enhancement selection is working fine");
			return true;
		} catch (Exception e) {
			logger.error("CC Text Enhancement selection is not working" + e);
			extentTest.log(LogStatus.FAIL, "Error while checking CC Text Enhancement panel.", e);
			return false;
		}
	}

	private boolean fcc = false;

	public CCValidator fcc() {
		fcc = true;
		return this;
	}

	public boolean validate(String element, int timeout) throws Exception {

		if (fcc) {
			return switchToControlBar() && closedCaptionMicroPanel() && checkArrows() && verifyCCPanelElements()
					&& verifyClosedCaptionLanguages() && verifyCCColorSelectionPanel("") && verifyCCOpacityPanel("")
					&& verifyCCFonttypePanel() && verifyCCFontSizePanel() && verifyCCTextEnhancementPanel()
					&& closeCCPanel() && clearCache();
		}

		boolean flag = switchToControlBar() && closedCaptionMicroPanel() && validateClosedCaptionPanel()
				&& validateSwitchContainer() && checkClosedCaptionLanguages() && closeCCPanel();

		if (flag) {
			if (clickOnIndependentElement("PAUSE_BUTTON")) {
				flag = flag && waitOnElement(By.id("ccmode_disabled"), 20000);
			} else {
				flag = false;
			}
		}
		if (!flag) {
			extentTest.log(LogStatus.FAIL, "closed caption validation failed.");
		}
		return flag;

	}

	public boolean discoveryCheck() {
		try {
			if (isElementPresent("DISCOVERY_CLOSE")) {
				clickOnIndependentElement("DISCOVERY_CLOSE");
			}
			return true;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Error while closing discovery window.", e);
			return false;
		}
	}

	public boolean beforeRefreshCCSetting() {
		boolean result = true;
		try {
			// CC Languages
			result = result && verifyClosedCaptionLanguages();

			result = result && setClosedCaptionLanguage(2);

			previewTextSelected = getCCLanguagePreviewText();
			logger.info("Preview Text Selected : " + previewTextSelected);
			// CC Color Selection
			result = result && verifyCCColorSelectionPanel("PlaybackFCCDefaultSettingTests");
			result = result && setCCColorSelectionOptions();
			ccColorSelectionBefore = getCCColorSelection();

			// CC Opacity Selection
			result = result && verifyCCOpacityPanel("PlaybackFCCDefaultSettingTests");
			if (!getBrowser().equalsIgnoreCase("safari")) {
				result = result && setCCOpacity();
			}
			ccOpacityMapBefore = getCCOpacityValues();

			// CC Font Type Selection
			result = result && verifyCCFonttypePanel();
			result = result && setFontType();
			ccFontTypeBefore = getFontType();

			// CC Font Size Selection
			result = result && verifyCCFontSizePanel();
			result = result && setFontSize();
			ccFontSizeBefore = getCCFontSizePreviewText();

			// CC Text Enhancement Selection
			result = result && verifyCCTextEnhancementPanel();
			result = result && setTextEnhancement();
			ccTextEnhancementSelectedBefore = getTextEnhancement();

			return result;
		} catch (Exception e) {
			logger.error("Error while checking before Refreshing FCC Setting. " + e);
			extentTest.log(LogStatus.FAIL, "Error while checking before Refreshing FCC Setting.", e);
			return false;
		}
	}

	private boolean setTextEnhancement() {
		try {
			ccTextEnhancement.get(1).click();
			return true;
		} catch (Exception e) {
			logger.error("Error while setting CC Text Enhancement" + e);
			extentTest.log(LogStatus.FAIL, "Error while selecting CC Text Enhancement.", e);
			return false;
		}
	}

	private String getCCFontSizePreviewText() throws Exception {
		clickOnIndependentElement("CC_FONT_SIZE_PANEL");
		String ccTextFontSize = getWebElement("CC_FONT_SIZE_SELECTED").getText();
		logger.info("\t Text Font Size Selected :" + ccTextFontSize);
		return ccTextFontSize;
	}

	private boolean setFontSize() {
		try {
			ccFontSize.get(2).click();
			return true;
		} catch (Exception e) {
			logger.error("Error while setting CC font size" + e);
			extentTest.log(LogStatus.FAIL, "Error while selecting CC Font Size.", e);
			return false;
		}
	}

	private String getTextEnhancement() throws Exception {
		clickOnIndependentElement("CC_TEXT_ENHANCEMENT");
		String ccPreviewTextEnh = getWebElement("CC_PREVIEW_TEXT").getCssValue("text-shadow");
		logger.info("\t Text Enhancement Selected :" + ccPreviewTextEnh);
		return ccPreviewTextEnh;
	}

	private boolean setClosedCaptionLanguage(int index) {
		try {
			index = index - 1;
			if (index < lang.size()) {
				lang.get(index).click();
				return true;
			} else {
				lang = getWebElementsList("LANG_LIST");
				lang.get(0).click();
				return true;
			}
		} catch (Exception e) {
			logger.error("Error in selecting cc language." + e.getMessage());
			extentTest.log(LogStatus.FAIL, "Error while selecting CC langauge.", e);
			return false;

		}
	}

	private boolean setFontType() {
		List<WebElement> ccFontType = getWebElementsList("CC_FONT_TYPE");
		try {
			ccFontType.get(1).click();
			return true;
		} catch (IndexOutOfBoundsException e) {
			ccFontType.get(0).click();
			return true;
		} catch (Exception e) {
			logger.error("Error while setting CC font type" + e);
			extentTest.log(LogStatus.FAIL, "Error while selecting CC Font Type.", e);
			return false;
		}
	}

	private String getFontType() throws Exception {
		clickOnIndependentElement("CC_FONT_TYPE_PANEL");
		String ccPreviewTextFont = getWebElement("CC_PREVIEW_TEXT").getCssValue("font-family");
		return ccPreviewTextFont;
	}

	private String getCCLanguagePreviewText() {
		String previewTextDefault = getWebElement("CC_PREVIEW_TEXT").getText();
		return previewTextDefault;
	}

	private boolean setCCOpacity() {
		try {
			WebElement slider;
			int width;
			Actions move = new Actions(driver);
			slider = getWebElement("CC_TEXT_OPACITY_SELECTOR");
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", slider);
			width = slider.getSize().getWidth();
			move.dragAndDropBy(slider, (width * 20) / 100, 0).build().perform();

			slider = getWebElement("CC_BACKGROUND_OPACITY_SELECTOR");
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", slider);
			width = slider.getSize().getWidth();
			move.dragAndDropBy(slider, (width * 20) / 100, 0).build().perform();

			slider = getWebElement("CC_WINDOW_OPACITY_SELECTOR");
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", slider);
			width = slider.getSize().getWidth();
			move.dragAndDropBy(slider, (width * 20) / 100, 0).build().perform();
			return true;
		} catch (Exception e) {
			logger.error("Error while setting CC opacity" + e);
			extentTest.log(LogStatus.FAIL, "Error while selecting CC opacity.", e);
			return false;
		}
	}

	private boolean setCCColorSelectionOptions() {
		try {
			textColor.get(0).click();
			bgColor.get(6).click();
			ccWinColor.get(2).click();
			return true;
		} catch (Exception e) {
			logger.error("Error while setting cc color selection");
			extentTest.log(LogStatus.FAIL, "Error while selecting CC color.", e);
			return false;
		}
	}

	private HashMap<String, String> getCCColorSelection() {
		// waitOnElement("COLOR_SELECTION_PANEL", 15000);
		clickOnIndependentElement("COLOR_SELECTION_PANEL");

		String ccTextColor, ccBgColor, ccWinColor;
		HashMap<String, String> ccColorMap = new HashMap<String, String>();

		ccTextColor = getWebElement("CC_TEXT_COLOR").getText();
		ccBgColor = getWebElement("CC_BACKGROUND_COLOR").getText();
		ccWinColor = getWebElement("CC_WINDOW_COLOR").getText();

		ccColorMap.put("ccTextColor", ccTextColor);
		ccColorMap.put("ccBgColor", ccBgColor);
		ccColorMap.put("ccWinColor", ccWinColor);

		return ccColorMap;
	}

	private HashMap<String, String> getCCOpacityValues() {
		clickOnIndependentElement("CAPTION_OPACITY_PANEL");

		String ccTextOpacity, ccBgOpacity, ccWinOpacity;
		HashMap<String, String> ccOpacityMap = new HashMap<String, String>();

		clickOnIndependentElement("CAPTION_OPACITY_PANEL");
		ccTextOpacity = getWebElement("CC_PREVIEW_TEXT").getCssValue("color");
		ccBgOpacity = getWebElement("CC_PREVIEW_TEXT_BG").getCssValue("background-color");
		ccWinOpacity = getWebElement("CC_PREVIEW_WIN_COLOR").getCssValue("background-color");

		ccOpacityMap.put("ccTextOpacity", ccTextOpacity);
		ccOpacityMap.put("ccBgOpacity", ccBgOpacity);
		ccOpacityMap.put("ccWinOpacity", ccWinOpacity);

		return ccOpacityMap;
	}

	public boolean afterRefreshCCSettings() {
		boolean result = true;
		try {
			Object ccobj = ((JavascriptExecutor) driver).executeScript(
					"var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;" + "{return attrb;}");

			@SuppressWarnings("unchecked")
			ArrayList<String> langlist = ((ArrayList<String>) ccobj);
			logger.info("\t Closed Caption Available Languages: " + langlist + "\n \t languages available count :"
					+ langlist.size());

			String[] langl = new String[langlist.size()];
			langlist.toArray(langl);

			// Select language and verify that Preview Text is shown
			lang = getWebElementsList("LANG_LIST");
			textSelected = getCCLanguagePreviewText();
			result = result && textSelected.contains(previewTextSelected);

			// Comparing Color selected values before refresh and after refresh
			ccColorSelectionAfter = getCCColorSelection();
			result = result && compareValues(ccColorSelectionBefore, ccColorSelectionAfter);

			// Comparing Opacity values before refresh and after refresh
			ccOpacityMapAfter = getCCOpacityValues();
			result = result && compareValues(ccOpacityMapBefore, ccOpacityMapAfter);

			// Comparing Font Type values before refresh and after refresh
			ccFontTypeAfter = getFontType();
			result = result && ccFontTypeBefore.contains(ccFontTypeAfter);

			// Comparing Font size before refresh and after refresh
			ccFontSizeAfter = getCCFontSizePreviewText();
			result = result && ccFontSizeBefore.contains(ccFontSizeAfter);

			// Comparing Text Enhancement value before refresh and after refresh
			ccTextEnhancementSelectedAfter = getTextEnhancement();
			result = result && ccTextEnhancementSelectedBefore.contains(ccTextEnhancementSelectedAfter);

			return result;
		} catch (Exception e) {
			logger.error("Error while comparing values after refresh " + e);
			return false;
		}
	}

	private boolean compareValues(HashMap<String, String> beforeRefreshPageMap,
			HashMap<String, String> afterRefreshValuesMap) {
		for (final String key : afterRefreshValuesMap.keySet()) {
			if (afterRefreshValuesMap.containsKey(key) && beforeRefreshPageMap.containsKey(key)) {
				if (!afterRefreshValuesMap.get(key).equals(beforeRefreshPageMap.get(key))) {
					extentTest.log(LogStatus.FAIL, "Issue with ccOpacity Values for :" + key + " : Before Refresh"
							+ beforeRefreshPageMap.get(key) + " After Refresh : " + afterRefreshValuesMap.get(key));
					return false;
				}
			}
		}
		return true;
	}

}
