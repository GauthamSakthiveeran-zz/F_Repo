package com.ooyala.playback.apps.validators;

public enum Events {


    PLAYBACK_STARTED("Notification Received: playStarted", "Notification Not Received: playStarted"),
    PLAYBACK_PAUSED("Notification Received: stateChanged. state: paused", "Notification Not Received: stateChanged. state: paused"),
    PLAYBACK_RESUMED("Notification Received: stateChanged. state: playing", "Notification Not Received: stateChanged. state: playing"),
    PLAYBACK_COMPLETED("Notification Received: playCompleted", "Notification Not Received: playCompleted"),

    AD_STARTED("Notification Received: adStarted", "Notification Not Received: adStarted"),
    AD_COMPLETED("Notification Received: adCompleted", "Notification Not Received: adCompleted"),
    AD_SKIPPPED("Notification Received: adSkipped", "Notification Not Received: adSkipped"),

    SEEK_STARTED("Notification Received: seekStarted", "Notification Not Received: seekStarted"),
    SEEK_COMPLETED("Notification Received: seekCompleted", "Notification Not Received: seekCompleted"),

    CC_ENABLED("Notification Received: languageChanged", "Notification Not Received: languageChanged");


    //TODO All other events

    String event;
    String failureMessage;

    Events(String event, String failureMessage) {
        this.event = event;
        this.failureMessage = failureMessage;
    }

    public String getEvent() {
        return event;
    }
    
    public String getFailureMessage() {
        return failureMessage;
    }
}

