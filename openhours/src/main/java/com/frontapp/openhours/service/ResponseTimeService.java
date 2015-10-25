/**
 * 
 */
package com.frontapp.openhours.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.frontapp.openhours.model.OpenHours;
import com.frontapp.openhours.model.OpenHoursDay;
import com.frontapp.openhours.model.utils.Ranges;

/**
 * @author Alexandre LACROUX
 *
 */
@Service
public class ResponseTimeService {
	
	/**
	 * Returns the response time between time1 and time2, reflecting the given openHours.
	 * @param time1 The time when the request is made
	 * @param time2 The time when the teams replies
	 * @param openHours The hours when the team is available
	 * @return The response time
	 */
	public long responseTime(Timestamp time1, Timestamp time2, OpenHours openHours) {
		long responseTime = 0;
		
		// the list of days we have to sum the waiting time
		List<Integer> daysToProcess = daysToProcess(time1, time2);
		
		for(Ranges ranges : getRanges(openHours, daysToProcess, time1, time2)) {
			// for each ranges, we sum the intersection time.
			responseTime += intersection(ranges.getStart1(), ranges.getStart2(), ranges.getEnd1(), ranges.getEnd2());
		}
		
		return responseTime;
	}
	
	/**
	 * Returns the days of week to process to get the response time.
	 * From 0 (sunday) to 6 (saturday)
	 * @param time1
	 * @param time2
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private List<Integer> daysToProcess(Timestamp time1, Timestamp time2) {
		List<Integer> daysToProcess = new ArrayList<Integer>();
		// we add the first day
		daysToProcess.add(time1.getDay());
		
		// if time1 and time2 are not on the same day, we add the rest of the days
		if(! isSameDay(time1, time2)) {
			Timestamp t = time1;
			while(! isSameDay(time2, t)) {
				t.setDate(time1.getDate() + 1);
				daysToProcess.add(t.getDay());
			}
		}
		
		return daysToProcess;
	}
	
	/**
	 * Returns the ranges to calculate the intersection.
	 * @param openHours
	 * @param daysToProcess
	 * @param time1
	 * @param time2
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private List<Ranges> getRanges(OpenHours openHours, List<Integer> daysToProcess, Timestamp time1, Timestamp time2) {
		List<Ranges> ranges = new ArrayList<Ranges>();
		
		// let's add the ranges for the first day
		OpenHoursDay openHoursFirstDay = openHours.getOpenHourDay(daysToProcess.get(0));
		if(openHoursFirstDay.isOpen()) {
			Timestamp openFirstDay = new Timestamp(time1.getYear(), time1.getMonth(), time1.getDate(), openHoursFirstDay.getOpenHours(), openHoursFirstDay.getOpenMinutes(), 0, 0);
			Timestamp closeFirstDay = new Timestamp(time1.getYear(), time1.getMonth(), time1.getDate(), openHoursFirstDay.getCloseHours(), openHoursFirstDay.getCloseMinutes(), 0, 0);
			ranges.add(new Ranges(time1, openFirstDay, daysToProcess.size() == 1 ? time2 : null, closeFirstDay));
		}
		daysToProcess.remove(0);
		
		// let's add the ranges for the rest of the days
		Iterator<Integer> iter = daysToProcess.iterator();
		while(iter.hasNext()) {
			Integer dayToProcess = iter.next();
			OpenHoursDay openHoursDay = openHours.getOpenHourDay(dayToProcess);
			
			// is it the last day ?
			if(! iter.hasNext()) {
				Timestamp openLastDay = new Timestamp(time2.getYear(), time2.getMonth(), time2.getDate(), openHoursDay.getOpenHours(), openHoursDay.getOpenMinutes(), 0, 0);
				Timestamp closeLastDay = new Timestamp(time2.getYear(), time2.getMonth(), time2.getDate(), openHoursDay.getCloseHours(), openHoursDay.getCloseMinutes(), 0, 0);
				ranges.add(new Ranges(null, openLastDay, closeLastDay, time2));
			}
			else {
				if(openHoursDay.isOpen()) {
					Timestamp open = new Timestamp(0, 0, 0, openHoursDay.getOpenHours(), openHoursDay.getOpenMinutes(), 0, 0);
					Timestamp close = new Timestamp(0, 0, 0, openHoursDay.getCloseHours(), openHoursDay.getCloseMinutes(), 0, 0);
					ranges.add(new Ranges(null, open, close, null));
				}
			}
		}
		
		return ranges;
	}
	
	/**
	 * Returns the intersection time in millis between range1 [start1;end1] and range2 [start2;end2]
	 * @param start1
	 * @param start2
	 * @param end1
	 * @param end2
	 * @return The intersection time in millis between range1 [start1;end1] and range2 [start2;end2]
	 */
	private long intersection(Timestamp start1, Timestamp start2, Timestamp end1, Timestamp end2) {
		return min(end1, end2).getTime() - max(start1, start2).getTime();
	}
	
	/**
	 * Returns the min between 2 timestamps.
	 * If one of the timestamps is null, returns the other.
	 * @param time1
	 * @param time2
	 * @return
	 */
	private Timestamp min(Timestamp time1, Timestamp time2) {
		if(time1 == null) {
			return time2;
		}
		else if(time2 == null) {
			return time1;
		}
		else {
			return (time1.getTime() - time2.getTime() < 0) ? time1 : time2;
		}
	}
	
	/**
	 * Returns the max between 2 timestamps.
	 * If one of the timestamps is null, returns the other.
	 * @param time1
	 * @param time2
	 * @return
	 */
	private Timestamp max(Timestamp time1, Timestamp time2) {
		if(time1 == null) {
			return time2;
		}
		else if(time2 == null) {
			return time1;
		}
		else {
			return (time1.getTime() - time2.getTime() < 0) ? time2 : time1;
		}
	}
	
	/**
	 * Returns true if time1 and time2 are on the same day of the year, false otherwise.
	 * @param time1
	 * @param time2
	 * @return true if time1 and time2 are on the same day of the year, false otherwise.
	 */
	@SuppressWarnings("deprecation")
	private boolean isSameDay(Timestamp time1, Timestamp time2) {
		return time1.getYear() == time2.getYear() && time1.getMonth() == time2.getMonth() && time1.getDate() == time2.getDate();
	}
	
}
