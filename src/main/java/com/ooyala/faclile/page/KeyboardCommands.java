/**
 * 
 */
package com.ooyala.faclile.page;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyboardCommands.
 */
public class KeyboardCommands {
	
	/** The robot. */
	private Robot robot = null;
	
	/**
	 * Instantiates a new keyboard commands.
	 */
	public KeyboardCommands(){
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Instantiates a new keyboard commands.
	 *
	 * @param passedRobot the passed robot
	 */
	public KeyboardCommands(Robot passedRobot){
		this.robot = passedRobot;
	}	
	
	/** The Constant keyMapping. */
	private static final HashMap<Character,Integer> keyMapping = new HashMap<Character, Integer>() {
		// TODO where are we using this serialVersionUID.
		private static final long serialVersionUID = -548474825443797830L;
		{
			put('~', 192);
			put('!', KeyEvent.VK_1);
			put('@', KeyEvent.VK_2);
			put('#', KeyEvent.VK_3);
			put('$', KeyEvent.VK_4);
			put('%', KeyEvent.VK_5);
			put('^', KeyEvent.VK_6);
			put('&', KeyEvent.VK_7);
			put('*', KeyEvent.VK_8);
			put('(', KeyEvent.VK_9);
			put(')', KeyEvent.VK_0);
			put('_', 45);
			put('+', KeyEvent.VK_EQUALS);
			put('{', 91);
			put('}', 93);
			put('|', 92);
			put(':', 59);
			put('"', 222);
			put('<', 44);
			put('>', 46);
			put('?', 47);			
		}	
	};

	/**
	 * Key press.
	 *
	 * @param event the event
	 */
	public void keyPress(int event){
		robot.keyPress(event);
	}

	/**
	 * Key release.
	 *
	 * @param event the event
	 */
	public void keyRelease(int event){
		robot.keyRelease(event);
	}
	
	// TODO: actual conversion needed
	/**
	 * Keys.
	 *
	 * @param str the str
	 */
	public void keys(String str) {

		for(int i = 0; i < str.length(); i++) {
			Character ch = str.charAt(i);

			// special shifted keys
			// no ' and other shifted characters
			if (keyMapping.containsKey(ch)) {
				int keyCode = keyMapping.get(ch);
				ch = (char)keyCode;
				robot.keyPress(KeyEvent.VK_SHIFT);	
			}

			if (Character.isLetter(ch) && Character.isUpperCase(ch)) {
				robot.keyPress(KeyEvent.VK_SHIFT);	
			}

			if (ch == '\'') {
				robot.keyRelease(KeyEvent.VK_SHIFT);
				ch = 222;
			}

			if (ch == '`') {
				robot.keyRelease(KeyEvent.VK_SHIFT);
				ch = 192;
			}

			ch = Character.toUpperCase(ch);
			robot.keyPress(ch);
			robot.keyRelease(ch);
			robot.keyRelease(KeyEvent.VK_SHIFT);
		}
		WebPage.wait(500);
	}
	
	/**
	 * Tab.
	 */
	public void tab() {
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
	}

	/**
	 * Tab in reverse.
	 */
	public void tabReverse() {
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_SHIFT);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Enter.
	 */
	public void enter() {
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	/**
	 * Space.
	 */
	public void space() {
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
	}

	/**
	 * Up.
	 */
	public void up() {
		robot.keyPress(KeyEvent.VK_UP);
		robot.keyRelease(KeyEvent.VK_UP);
	}

	/**
	 * Down.
	 */
	public void down() {
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);		
	}

	/**
	 * Alt1.
	 */
	public void alt1() {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_1);
		robot.keyRelease(KeyEvent.VK_1);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	/**
	 * Alt down.
	 */
	public void altDown() {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_DOWN);		
		robot.keyRelease(KeyEvent.VK_DOWN);		
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	/**
	 * Ctrl a.
	 */
	public void ctrlA() {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_A);
	}
	
	/**
	 * Backspace.
	 */
	public void backspace() {
		robot.keyPress(KeyEvent.VK_BACK_SPACE);
		robot.keyRelease(KeyEvent.VK_BACK_SPACE);
	}

	/**
	 * Ctrl c.
	 */
	public void ctrlC() {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_C);
	}

	/**
	 * Delete.
	 */
	public void delete() {
		robot.keyPress(KeyEvent.VK_DELETE);
		robot.keyRelease(KeyEvent.VK_DELETE);
	}

	/**
	 * F5.
	 */
	public void f5() {
		robot.keyPress(KeyEvent.VK_F5);
		robot.keyRelease(KeyEvent.VK_F5);
	}

	/**
	 * Closes a browser window using robot keys.
	 */
	public void altFC() {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_F);
		robot.keyRelease(KeyEvent.VK_F);
		WebPage.wait(1000);	
		robot.keyPress(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_C);
	}

	/**
	 * This keyboard command will save a txn in Firefox.
	 */
	public void altShiftS() {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyPress(KeyEvent.VK_S);
		WebPage.wait(250);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_SHIFT);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	/**
	 * This keyboard command will save a txn in IE.
	 */
	public void altS() {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_S);
		WebPage.wait(250);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	/**
	 * Closes a modal browser window using robot keys.
	 */
	public void altF4() {

		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_F4);
		robot.keyRelease(KeyEvent.VK_F4);
		robot.keyRelease(KeyEvent.VK_ALT);
		WebPage.wait(1000);
	}

	/**
	 * Command v.
	 */
	public void commandV() {
		robot.keyPress(KeyEvent.VK_META);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_META);
	}

	/**
	 * Command a.
	 */
	public void commandA() {
		robot.keyPress(KeyEvent.VK_META);
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_META);
		WebPage.wait(1000);
	}

	/**
	 * Command c.
	 */
	public void commandC() {
		robot.keyPress(KeyEvent.VK_META);
		robot.keyPress(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_META);
		WebPage.wait(1000);
	}
}