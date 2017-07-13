import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.ooyala.qe.common.exception.OoyalaException;

import net.minidev.json.JSONArray;

public class AdFillRate {

	public static void main(String... strings) throws OoyalaException, UnirestException {

		JSONObject json = new JSONObject();
		json.put("email", "dmanohar@ooyala.com");

		JSONArray jsonArray = new JSONArray();
		JSONObject json2 = new JSONObject();
		json2.put("embed_code", "9lNDNhMzE65U7FW_KR1bQm7m41SeT997");
		json2.put("browser_name", "FIREFOX");
		json2.put("platform", "MAC");
		json2.put("environment", "PRODUCTION");
		json2.put("muxingformat", "DASH");
		json2.put("adplugins", "PULSE");
//		json2.put("player_configuration_parameters", "{\"freewheel-ads-manager\":{\"fw_video_asset_id\":\"Q5MXg2bzq0UAXXMjLIFWio_6U0Jcfk6v\",\"html5_ad_server\":\"http://g1.v.fwmrm.net\",\"html5_player_profile\":\"90750:ooyala_html5\",\"fw_mrm_network_id\":\"380912\",\"showInAdControlBar\":true},\"initialTime\":0,\"autoplay\":false}");

		JSONArray jsonArray1 = new JSONArray();
		jsonArray1.add("PREROLL");

		json2.put("validations", jsonArray1);

		jsonArray.add(json2);
		json.put("video_assets", jsonArray);

		System.out.println(json.toString());

		for(int i=0; i<200; i++) {
			HttpResponse<String> res = Unirest.post("http://localhost:8080/playback")
	                .header("accept","application/json")
	                .header("Content-Type","application/json")
	                .body(json)
	                .asString();
			System.out.println(res.getBody());
		}
	}

}
