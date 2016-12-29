package com.ooyala.playback.util;

import java.util.Map;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Maps;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class PlayBackAsserts extends Assertion {

	ExtentTest extentTest;

	public PlayBackAsserts(ExtentTest extentTest) {
		this.extentTest = extentTest;
	}

	private Map<AssertionError, IAssert> m_errors = Maps.newLinkedHashMap();
	
	@Override
	public void executeAssert(IAssert a) {
		try {
			a.doAssert();
		} catch (AssertionError ex) {
			m_errors.put(ex, a);
		}
	}

	public void assertAll() throws Exception {
		if (hasErrors()) {
			StringBuilder sb = new StringBuilder("The following asserts failed:\n");
			boolean first = true;
			for (Map.Entry<AssertionError, IAssert> ae : m_errors.entrySet()) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(ae.getValue().getMessage());
			}
			extentTest.log(LogStatus.FAIL, sb.toString());
		}
	}
	
	public boolean hasErrors(){
		return !m_errors.isEmpty();
	}
	
	public ElementPosition elementPosition(){
		return new ElementPosition(this);
	}
	

}
