package com.ooyala.playback.util;

import java.util.ArrayList;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class StringArrayAppender extends AppenderSkeleton {

	private static ArrayList<String> log;

	public StringArrayAppender() {
		log = new ArrayList<String>();
	}

	public static ArrayList<String> getLog() {
		return log;
	}

	@Override
	public void close() {
		log = null;

	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
		// Generate message

		if (event.getLevel().equals(Level.INFO)) {
			StringBuilder sb = new StringBuilder();
			sb.append(event.getRenderedMessage().toString());
			log.add(sb.toString());
		}

	}

}
