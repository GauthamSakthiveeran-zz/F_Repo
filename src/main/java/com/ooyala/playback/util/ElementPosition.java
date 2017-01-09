package com.ooyala.playback.util;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class ElementPosition {

	Point point;
	PlayBackAsserts asserts;
	WebElement element;
	
	
	public ElementPosition(PlayBackAsserts asserts){
		this.asserts = asserts;
	}
	
	public ElementPosition thisIs(WebElement element){
		if(element!=null){
			this.element = element;
			return this;
		}
		return null;
	}
	
	public ElementPosition leftOf(WebElement element){
		Point point = element.getLocation();
		this.point = this.element.getLocation();
		asserts.assertTrue(point.x > this.point.x, this.element.getAttribute("class") + " should be to the left of " + element.getAttribute("class"));
		asserts.assertTrue(point.y == this.point.y, "The elements are not along the same line.");
		this.element = element;
		return this;
	}
	
	public ElementPosition rightOf(WebElement element){
		Point point = element.getLocation();
		this.point = this.element.getLocation();
		asserts.assertTrue(point.x < this.point.x, this.element + " should be to the right of " + element);
		asserts.assertTrue(point.y == this.point.y, "The elements are not along the same line.");
		this.element = element;
		return this;
	}
	
	public ElementPosition above(WebElement element){
		Point point = element.getLocation();
		this.point = this.element.getLocation();
		asserts.assertTrue(point.y < this.point.y, this.element + " should be above " + element);
		asserts.assertTrue(point.x == this.point.x, "The elements are not along the same line.");
		this.element = element;
		return this;
	}
	
	public ElementPosition below(WebElement element){
		Point point = element.getLocation();
		this.point = this.element.getLocation();
		asserts.assertTrue(point.y > this.point.y, this.element + " should be below " + element);
		asserts.assertTrue(point.x == this.point.x, "The elements are not along the same line.");
		this.element = element;
		return this;
	}
	
}
