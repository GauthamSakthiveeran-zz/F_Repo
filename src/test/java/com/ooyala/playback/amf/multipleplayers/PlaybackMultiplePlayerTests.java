package com.ooyala.playback.amf.multipleplayers;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.MultiplePlayerValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMultiplePlayerTests extends PlaybackWebTest {

	public PlaybackMultiplePlayerTests() throws OoyalaException {
		super();
	}

	private PlayValidator play;
	private MultiplePlayerValidator multiplePlayerValidator;
	private EventValidator eventValidator;

	@Test(groups = { "amf", "multipleplayer" })
	public void verifyMultiplePlayer() throws OoyalaException {

		boolean result = true;

		//TODO need to figure this out
		String url = "https://dl.dropboxusercontent.com/u/344342926/multiple_players_test_page/index.html?ec=RmbHlycTpp3D2ARo5QGzrQa5cMtGSc2m&pbid=7324f0fcd4f45edacc5646d7a6ae394&pcode=pyaDkyOqdnY0iQC2sTO4JeaXggl9&core_player=https%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fcore.min.js&html5_skin=https%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fskin-plugin%2Fhtml5-skin.min.js&skin_asset=https%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fskin-plugin%2Fhtml5-skin.min.css&skin_config=https%3A%2F%2Fdl.dropboxusercontent.com%2Fu%2F344342926%2FQA%2Fskin-default.json&ad_plugin=https%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fad-plugin%2Ffreewheel.js%0A%0Ahttps%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fad-plugin%2Fad_manager_vast.min.js&additional_plugins=https%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fvideo-plugin%2Fmain_html5.min.js%0A%0A&options=%7B%22freewheel-ads-manager%22%3A%7B%22fw_video_asset_id%22%3A%22Q5MXg2bzq0UAXXMjLIFWio_6U0Jcfk6v%22%2C%22html5_ssl_ad_server%22%3A%22https%3A%2F%2Fg1.v.fwmrm.net%22%2C%22html5_player_profile%22%3A%2290750%3Aooyala_html5%22%2C%22fw_mrm_network_id%22%3A%22380912%22%2C%22showInAdControlBar%22%3Atrue%7D%2C%22initialTime%22%3A0%2C%22autoplay%22%3Afalse%7D&ec2=htcmtjczpHnIEJLJUrZ8YUs0CW0pyi2R&pbid2=242f532f58ad4b2e9192f84fd4ff727d&pcode2=pyaDkyOqdnY0iQC2sTO4JeaXggl9&options2=%7B%22showInAdControlBar%22%3Atrue%2C%22vast%22%3A%7B%22tagUrl%22%3A%22https%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2FtestAssets%2Fvast2%2FVastAd_Midroll.xml%22%7D%7D";

		try {

			driver.get(url);

			Thread.sleep(10000);

			injectScript();

			result = result && multiplePlayerValidator.validate("player1_play", 5000);

			result = result && multiplePlayerValidator.validate("player1_pause", 20000);

			result = result && multiplePlayerValidator.validate("player2_play", 20000);

			result = result && multiplePlayerValidator.validate("player2_pause", 20000);

			result = result && multiplePlayerValidator.validateVolume();

			result = result && multiplePlayerValidator.validateFullScreen();

			result = result && multiplePlayerValidator.validate("seek1", 20000);

			result = result && multiplePlayerValidator.validate("seek2", 20000);

			result = result && eventValidator.validate("player1_playing_1", 20000);

			result = result && eventValidator.validate("player1_pause_1", 20000);

			result = result && eventValidator.validate("player2_playing_1", 20000);

			result = result && eventValidator.validate("player2_pause_1", 20000);

			result = result && eventValidator.validate("player1_seeked_1", 20000);

			result = result && eventValidator.validate("player2_seeked_1", 20000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "test failed");
	}

}
