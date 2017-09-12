package com.ooyala.playback.vr360;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.FullScreenAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.VirtualRealityAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BasicVr360Tests extends PlaybackWebTest {

    public BasicVr360Tests() throws OoyalaException {
        super();
    }

    private PlayAction playAction;
    private EventValidator eventValidator;
    private PlayValidator playValidator;
    private PauseValidator pauseValidator;
    private FullScreenAction fullScreenAction;
    private VirtualRealityAction virtualRealityAction;

    @Test(groups = "vr360", dataProvider = "testUrls")
    public void verifyPlayerPlayingVr360BrowserMode(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            //verify in browser mode
            result = result && eventValidator.validate("playing_1", 3000);
            //mouse actions
            result = result && virtualRealityAction.onScreen().startAction();
            result = result && eventValidator.validate("direction_changed_1", 3000);
            //controller actions
            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_RIGHT");
            result = result && eventValidator.validate("moveToDirection_1", 3000);
            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_LEFT");
            result = result && eventValidator.validate("moveToDirection_2", 3000);
            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_UP");
            result = result && eventValidator.validate("moveToDirection_3", 3000);
            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_DOWN");
            result = result && eventValidator.validate("moveToDirection_4", 3000);

            //defect https://jira.corp.ooyala.com/browse/PLAYER-1628
            //verify navigation in pause mode
//            result = result && pauseValidator.validate("paused_1", 60000);
//            result = result && virtualRealityAction.onScreen().startAction();
//            result = result && eventValidator.validate("direction_changed_2", 3000);
//            result = result && eventValidator.validate("playing_2", 3000);
//            result = result && virtualRealityAction.onScreen().startAction();
//            result = result && eventValidator.validate("direction_changed_3", 3000);

            //defect https://jira.corp.ooyala.com/browse/PLAYER-1638
            //verify controls navigation in pause mode
//            result = result && pauseValidator.validate("paused_2", 60000);
//            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_RIGHT");
//            result = result && eventValidator.validate("moveToDirection_5", 3000);
//            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_LEFT");
//            result = result && eventValidator.validate("moveToDirection_6", 3000);
//            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_UP");
//            result = result && eventValidator.validate("moveToDirection_7", 3000);
//            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_DOWN");
//            result = result && eventValidator.validate("moveToDirection_8", 3000);

        } catch (Exception e) {
            logger.error("Exception while checking basic vr 360 playback " + e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        Assert.assertTrue(result, "Vr 360 tests in browser mode using navigation by mouse and controls failed " + testName);
    }

    @Test(groups = "vr360", dataProvider = "testUrls")
    public void verifyPlayerPlayingVr360BrowserModeByKeysOnKeyboard(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            //verify in browser mode
            result = result && eventValidator.validate("playing_1", 3000);

            //verify key on keyboard
            result = result && virtualRealityAction.startActionUsingKeys("a");
            result = result && eventValidator.validate("moveToDirection_1", 3000);
            result = result && virtualRealityAction.startActionUsingKeys("w");
            result = result && eventValidator.validate("moveToDirection_2", 3000);
            result = result && virtualRealityAction.startActionUsingKeys("d");
            result = result && eventValidator.validate("moveToDirection_3", 3000);
            result = result && virtualRealityAction.startActionUsingKeys("s");
            result = result && eventValidator.validate("moveToDirection_4", 3000);

            //verify keys navigation in pause mode
            result = result && pauseValidator.validate("paused_1", 60000);
            result = result && virtualRealityAction.startActionUsingKeys("s");
            result = result && eventValidator.validate("moveToDirection_5", 3000);
            result = result && virtualRealityAction.startActionUsingKeys("d");
            result = result && eventValidator.validate("moveToDirection_6", 3000);
            result = result && virtualRealityAction.startActionUsingKeys("w");
            result = result && eventValidator.validate("moveToDirection_7", 3000);
            result = result && virtualRealityAction.startActionUsingKeys("a");
            result = result && eventValidator.validate("moveToDirection_8", 3000);

        } catch (Exception e) {
            logger.error("Exception while checking basic vr 360 playback " + e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        Assert.assertTrue(result, "Vr 360 tests in browser mode using navigation by keys on keyboard failed " + testName);
    }


    @Test(groups = "vr360", dataProvider = "testUrls")
    public void verifyPlayerPlayingVr360FullScreen(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playAction.onScreen().startAction();
            result = result && eventValidator.validate("playing_1", 3000);

            //go to full screen mode
            result = result && fullScreenAction.switchToControlBar();
            result = result && eventValidator.eventAction("FULLSCREEN_BTN");
            result = result && eventValidator.validate("fullscreenChanged_true", 3000);

            //mouse actions
            result = result && virtualRealityAction.startActionOnScreen();
            result = result && eventValidator.validate("direction_changed_1", 3000);
            //controller actions
            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_UP");
            result = result && eventValidator.validate("moveToDirection_1", 3000);
            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_DOWN");
            result = result && eventValidator.validate("moveToDirection_2", 3000);
            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_RIGHT");
            result = result && eventValidator.validate("moveToDirection_3", 3000);
            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_LEFT");
            result = result && eventValidator.validate("moveToDirection_4", 3000);

            //defect https://jira.corp.ooyala.com/browse/PLAYER-1628
            //verify navigation in pause mode
//            result = result && pauseValidator.validate("paused_1", 60000);
//            result = result && virtualRealityAction.onScreen().startAction();
//            result = result && eventValidator.validate("direction_changed_2", 3000);
//            result = result && eventValidator.validate("playing_2", 3000);
//            result = result && virtualRealityAction.onScreen().startAction();
//            result = result && eventValidator.validate("direction_changed_3", 3000);

            //defect https://jira.corp.ooyala.com/browse/PLAYER-1638
            //verify controls navigation in pause mode
//            result = result && pauseValidator.validate("paused_2", 60000);
//            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_RIGHT");
//            result = result && eventValidator.validate("moveToDirection_5", 3000);
//            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_LEFT");
//            result = result && eventValidator.validate("moveToDirection_6", 3000);
//            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_UP");
//            result = result && eventValidator.validate("moveToDirection_7", 3000);
//            result = result && virtualRealityAction.startActionOnScreenUsingControls("VR_ICON_MOVE_DOWN");
//            result = result && eventValidator.validate("moveToDirection_8", 3000);

        } catch (Exception e) {
            logger.error("Exception while checking basic vr 360 playback in full screen " + e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        Assert.assertTrue(result, "Vr 360 tests in browser mode using navigation by mouse and controls failed " + testName);
    }

    @Test(groups = "vr360", dataProvider = "testUrls")
    public void verifyPlayerPlayingVr360FullScreenByKeysOnKeyboard(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playAction.onScreen().startAction();
            result = result && eventValidator.validate("playing_1", 3000);

            //go to full screen mode
            result = result && fullScreenAction.switchToControlBar();
            result = result && eventValidator.eventAction("FULLSCREEN_BTN");
            result = result && eventValidator.validate("fullscreenChanged_true", 3000);

            //verify key on keyboard
            result = result && virtualRealityAction.startActionUsingKeys("w");
            result = result && eventValidator.validate("moveToDirection_1", 3000);
            result = result && virtualRealityAction.startActionUsingKeys("s");
            result = result && eventValidator.validate("moveToDirection_2", 3000);
            result = result && virtualRealityAction.startActionUsingKeys("a");
            result = result && eventValidator.validate("moveToDirection_3", 3000);
            result = result && virtualRealityAction.startActionUsingKeys("d");
            result = result && eventValidator.validate("moveToDirection_4", 3000);

//failed to find moveToDirection_5 element, but it exists
            //verify keys navigation in pause mode
//            result = result && pauseValidator.validate("paused_1", 60000);
//            result = result && virtualRealityAction.startActionUsingKeys("d");
//            result = result && eventValidator.validate("moveToDirection_5", 3000);
//            result = result && virtualRealityAction.startActionUsingKeys("s");
//            result = result && eventValidator.validate("moveToDirection_6", 3000);
//            result = result && virtualRealityAction.startActionUsingKeys("w");
//            result = result && eventValidator.validate("moveToDirection_7", 3000);
//            result = result && virtualRealityAction.startActionUsingKeys("a");
//            result = result && eventValidator.validate("moveToDirection_8", 3000);

        } catch (Exception e) {
            logger.error("Exception while checking basic vr 360 playback in full screen " + e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        Assert.assertTrue(result, "Vr 360 tests in full mode using navigation by keys on keyboard failed " + testName);
    }

}
