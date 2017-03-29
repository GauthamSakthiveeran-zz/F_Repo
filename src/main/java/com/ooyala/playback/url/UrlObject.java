package com.ooyala.playback.url;

import com.ooyala.playback.live.LiveChannel;
import com.ooyala.qe.common.exception.OoyalaException;

public class UrlObject {

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private String adFirstPlay;

	public String getAdFirstPlay() {
		return adFirstPlay;
	}

	public void setAdFirstPlay(String adFirstPlay) {
		this.adFirstPlay = adFirstPlay;
	}

	private String adFrequency;

	public String getAdFrequency() {
		return adFrequency;
	}

	public void setAdFrequency(String adFrequency) {
		this.adFrequency = adFrequency;
	}

	private String streamType;

	public String getStreamType() {
		return streamType;
	}

	public void setStreamType(String streamType) {
		this.streamType = streamType;
	}

    public LiveChannel liveChannel;

    public LiveChannel getLiveChannel() throws OoyalaException {
        if (liveChannel==null){
            liveChannel = new LiveChannel();
        }
        return liveChannel;
    }

    public void setLiveChannel(LiveChannel liveChannel){this.liveChannel = liveChannel;};

}
