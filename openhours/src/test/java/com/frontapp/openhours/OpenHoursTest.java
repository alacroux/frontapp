/**
 * 
 */
package com.frontapp.openhours;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.frontapp.openhours.model.OpenHours;
import com.frontapp.openhours.service.ResponseTimeService;
import com.google.gson.Gson;

/**
 * @author Alexandre LACROUX
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OpenHoursTest {
	
	private OpenHours openHours;
	
	@Autowired
	private ResponseTimeService responseTimeService;
	
	@Before
	public void before() throws FileNotFoundException {
		openHours = getOpenHoursFromJson();
	}
	
	@Test
	public void testOneDay() {
		Timestamp time1 = Timestamp.valueOf("2015-10-19 08:00:00.000");
		Timestamp time2 = Timestamp.valueOf("2015-10-19 16:15:00.000");
		
		System.out.println("Time 1 : " + time1);
		System.out.println("Time 2 : " + time2);
		
		long responseTime = responseTimeService.responseTime(time1, time2, openHours);
		System.out.println("Response time : " + responseTimeToString(responseTime));
		System.out.println("\n");
		
		Assert.assertEquals("7h15", responseTimeToString(responseTime));
	}
	
	@Test
	public void testTwoDays() {
		Timestamp time1 = Timestamp.valueOf("2015-10-19 12:30:00.000");
		Timestamp time2 = Timestamp.valueOf("2015-10-20 09:40:00.000");
		
		System.out.println("Time 1 : " + time1);
		System.out.println("Time 2 : " + time2);
		
		long responseTime = responseTimeService.responseTime(time1, time2, openHours);
		System.out.println("Response time : " + responseTimeToString(responseTime));
		System.out.println("\n");
		
		Assert.assertEquals("5h10", responseTimeToString(responseTime));
	}
	
	@Test
	public void testThreeDays() {
		Timestamp time1 = Timestamp.valueOf("2015-10-19 09:00:00.000");
		Timestamp time2 = Timestamp.valueOf("2015-10-21 17:00:00.000");
		
		System.out.println("Time 1 : " + time1);
		System.out.println("Time 2 : " + time2);
		
		long responseTime = responseTimeService.responseTime(time1, time2, openHours);
		System.out.println("Response time : " + responseTimeToString(responseTime));
		System.out.println("\n");
		
		Assert.assertEquals("24h00", responseTimeToString(responseTime));
	}
	
	@Test
	public void testWithClosedDay() {
		Timestamp time1 = Timestamp.valueOf("2015-10-19 13:00:00.000");
		Timestamp time2 = Timestamp.valueOf("2015-10-27 10:50:00.000");
		
		System.out.println("Time 1 : " + time1);
		System.out.println("Time 2 : " + time2);
		
		long responseTime = responseTimeService.responseTime(time1, time2, openHours);
		System.out.println("Response time : " + responseTimeToString(responseTime));
		System.out.println("\n");
		
		Assert.assertEquals("48h50", responseTimeToString(responseTime));
	}
	
	@Test
	public void testRequestOnClosedDay() {
		Timestamp time1 = Timestamp.valueOf("2015-10-18 12:00:00.000");
		Timestamp time2 = Timestamp.valueOf("2015-10-20 11:05:00.000");
		
		System.out.println("Time 1 : " + time1);
		System.out.println("Time 2 : " + time2);
		
		long responseTime = responseTimeService.responseTime(time1, time2, openHours);
		System.out.println("Response time : " + responseTimeToString(responseTime));
		System.out.println("\n");
		
		Assert.assertEquals("10h05", responseTimeToString(responseTime));
	}
	
	private OpenHours getOpenHoursFromJson() throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("openHours.json")));
		
		Gson gson = new Gson();
		OpenHours openHours = gson.fromJson(br, OpenHours.class);
		return openHours;
	}
	
	private String responseTimeToString(long responseTime) {
		long hours = TimeUnit.MILLISECONDS.toHours(responseTime);
		responseTime -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(responseTime);
		return String.format("%dh%02d", hours, minutes);
	}
	
}
