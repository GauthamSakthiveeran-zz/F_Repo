package com.ooyala.playback.performance;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PerformanceValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PerformanceTest extends PlaybackWebTest {

	private PlayValidator play;
	private PlayAction playAction;
	private PerformanceValidator perf;

	public PerformanceTest() throws OoyalaException {
		super();
	}

	@Test
	public void test() throws OoyalaException {
		boolean result = true;
		try {

			driver.get("http://debug.ooyala.com/ea/index.html?ec=A0NW1vYTE6hCIME00w2On3_iHKOCE6Z7&pbid=378512f55dd44fc287d0fa609353c268&pcode=x2aDkyOt2q3WtOCp-krSyDffASzL&core_player=https%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fcore.js&video_plugins=https%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fvideo-plugin%2Fbit_wrapper.min.js&html5_skin=https%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fskin-plugin%2Fhtml5-skin.min.js&skin_asset=https%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fskin-plugin%2Fhtml5-skin.min.css&skin_config=https%3A%2F%2Fraw.githubusercontent.com%2Farchieb87%2Fpbq%2Fmaster%2Fqa%2Fskin-default.json");

			result = result && play.clearCache();

			perf.getConsoleLogs();

			result = result && play.waitForPage();

			result = result && playAction.startAction();

			Thread.sleep(10000);
			perf.validate("", 1);

		} catch (Exception e) {
			logger.error(e);
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Failed");
	}

}
