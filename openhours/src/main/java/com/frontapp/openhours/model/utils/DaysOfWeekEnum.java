/**
 * 
 */
package com.frontapp.openhours.model.utils;

/**
 * @author Alexandre LACROUX
 *
 */
public enum DaysOfWeekEnum {

	sunday(0),
	monday(1), 
	tuesday(2), 
	wednesday(3),
    thursday(4), 
    friday(5), 
    saturday(6);
	
    private int value;
    
    DaysOfWeekEnum(int value) {
    	this.value = value;
    }

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
    
}
