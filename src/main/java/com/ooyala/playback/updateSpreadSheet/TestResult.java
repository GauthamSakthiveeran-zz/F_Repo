package com.ooyala.playback.updateSpreadSheet;

import com.google.api.services.sheets.v4.model.Color;

public enum TestResult {

	PASSED("Passed", new Color().setGreen(0.5F)), 
	FAILED("Failed", new Color().setRed(0.5F)), 
	SKIPPED("Skipped", new Color().setBlue(0.5F)), 
	EXISTING_BUG("Existing Bug", new Color().setRed(0.1F)),;

	String value;
	Color color;

	TestResult(String value, Color color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return this.value;
	}

	public Color getColor() {
		return color;
	}
}
