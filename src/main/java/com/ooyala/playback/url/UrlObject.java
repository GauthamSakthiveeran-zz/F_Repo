package com.ooyala.playback.url;

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

	private String pcode;

	public String getPCode() {
		return pcode;
	}

	public void setPCode(String pcode) {
		this.pcode = pcode;
	}

	private String embedCode;

	public String getEmbedCode() {
		return embedCode;
	}

	public void setEmbedCode(String embedCode) {
		this.embedCode = embedCode;
	}

	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	private String secret;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String errorCode;

	public String getErrorCode() {return errorCode;}

	public void setErrorCode(String errorCode) {this.errorCode = errorCode;}

	private String errorDescription;

	public String getErrorDescription() {return errorDescription;}

	public void setErrorDescription(String errorDescription) {this.errorDescription = errorDescription;}

    private String channelId;

    public void setChannelId(String channelId) {this.channelId = channelId;}

    public String getChannelId() {return channelId;}

    public String provider;

    public String getProvider() {return provider;}

    public void setProvider(String provider) {this.provider = provider;}
}
