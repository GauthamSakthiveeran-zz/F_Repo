/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ooyala.faclie.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface NoWatchdog.
 *
 * @author pkumar
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface NoWatchdog {

}
