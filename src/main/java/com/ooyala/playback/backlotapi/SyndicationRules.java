package com.ooyala.playback.backlotapi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class SyndicationRules {
	
	private String getToken(String embedCode, String apiKey, String secret)
			throws FileNotFoundException, ScriptException {
		ScriptEngine jruby = new ScriptEngineManager().getEngineByName("jruby");
		jruby.eval(new BufferedReader(new FileReader("TokenGen-OPT.rb")));

		jruby.put("ec", embedCode);
		jruby.put("key", apiKey);
		jruby.put("secret", secret);

		String signature = (String) jruby.eval("generate_signature($ec,$key,$secret)");
		
		jruby.put("sig", signature);

		String token = (String) jruby.eval("image_request($sig)");

		return token;

	}
	
	public void modifyFlightTimeRule(String embedCode, String apiKey, String secret) {
		
	}
	
	public static void main(String...strings) throws FileNotFoundException, ScriptException{
		SyndicationRules synd = new SyndicationRules();
		String token  = synd.getToken("RpeWttMDE6atfCpq0hP5t95qpd9NbxLv", "pyaDkyOqdnY0iQC2sTO4JeaXggl9.Hvg5M", "h_DaSZWs-FQTfcxug3lt7hPGj05FYUwM1HvoDPAF");
		System.out.println(token);
	}

}
