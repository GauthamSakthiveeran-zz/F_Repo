package com.ooyala.playback.apps.validators;

public enum Events {


    PLAYBACK_STARTED("Notification Received: playStarted"),
    PLAYBACK_PAUSED("Notification Received: stateChanged. state: paused"),
    PLAYBACK_RESUMED("Notification Received: stateChanged. state: playing"),
    PLAYBACK_COMPLETED("Notification Received: playCompleted"),

    AD_STARTED("Notification Received: adStarted"),
    AD_COMPLETED("Notification Received: adCompleted"),
    AD_SKIPPPED("Notification Received: adSkipped"),

    SEEK_STARTED("Notification Received: seekStarted"),
    SEEK_COMPLETED("Notification Received: seekCompleted"),

    CC_ENABLED("Notification Received: languageChanged");


    //TODO All other events

    String event;

    Events(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}

