package com.ooyala.playback.url;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.live.LiveChannel;
import com.ooyala.qe.common.exception.OoyalaException;

public class UrlObject {

	private String url;
    private String adFrequency;
    private String adFirstPlay;
    private String streamType;
    private String channelId;
    private String provider;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAdFirstPlay() {
		return adFirstPlay;
	}

	public void setAdFirstPlay(String adFirstPlay) {
		this.adFirstPlay = adFirstPlay;
	}

	public String getAdFrequency() {
		return adFrequency;
	}

	public void setAdFrequency(String adFrequency) {
		this.adFrequency = adFrequency;
	}

	public String getStreamType() {
		return streamType;
	}

	public void setStreamType(String streamType) {
		this.streamType = streamType;
	}

	public LiveChannel liveChannel;

	public LiveChannel getLiveChannel() throws OoyalaException {
		if (liveChannel == null) {
			liveChannel = new LiveChannel();
		}
		return liveChannel;
	}

	public void setLiveChannel(LiveChannel liveChannel) {
		this.liveChannel = liveChannel;
	};

    public String getChannelId() {return channelId;}

    public String getProvider() {return provider;}

    public void setChannelId(String channelId) {this.channelId = channelId;}

    public void setProvider(String provider) {this.provider = provider;}
}
