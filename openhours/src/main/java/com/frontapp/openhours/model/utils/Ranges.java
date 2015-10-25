/**
 * 
 */
package com.frontapp.openhours.model.utils;

import java.sql.Timestamp;

/**
 * @author Alexandre LACROUX
 *
 */
public class Ranges {

	private Timestamp start1;
	private Timestamp start2;
	private Timestamp end1;
	private Timestamp end2;
	
	/**
	 * @param start1
	 * @param start2
	 * @param end1
	 * @param end2
	 */
	public Ranges(Timestamp start1, Timestamp start2, Timestamp end1,
			Timestamp end2) {
		super();
		this.start1 = start1;
		this.start2 = start2;
		this.end1 = end1;
		this.end2 = end2;
	}
	
	// GETTERS & SETTERS

	/**
	 * @return the start1
	 */
	public Timestamp getStart1() {
		return start1;
	}

	/**
	 * @param start1 the start1 to set
	 */
	public void setStart1(Timestamp start1) {
		this.start1 = start1;
	}

	/**
	 * @return the start2
	 */
	public Timestamp getStart2() {
		return start2;
	}

	/**
	 * @param start2 the start2 to set
	 */
	public void setStart2(Timestamp start2) {
		this.start2 = start2;
	}

	/**
	 * @return the end1
	 */
	public Timestamp getEnd1() {
		return end1;
	}

	/**
	 * @param end1 the end1 to set
	 */
	public void setEnd1(Timestamp end1) {
		this.end1 = end1;
	}

	/**
	 * @return the end2
	 */
	public Timestamp getEnd2() {
		return end2;
	}

	/**
	 * @param end2 the end2 to set
	 */
	public void setEnd2(Timestamp end2) {
		this.end2 = end2;
	}
	
}
