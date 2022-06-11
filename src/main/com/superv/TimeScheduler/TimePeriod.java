package com.superv.TimeScheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data public class TimePeriod {
	private LocalDateTime start_time;
	private LocalDateTime end_time;
	
	final static List<String> TIME_FORMATS = Arrays.asList("H", "HH", "h:mm", "hh:mm", "ha", "hha", "hh:mma");  // TODO: change so 
		// so that it uses DateTimeForammter instead
	
	public TimePeriod(int day, String raw_start_time, String raw_end_time) {
		this.start_time = TimePeriod.create_datetime(day, raw_start_time);
		this.end_time = TimePeriod.create_datetime(day, raw_end_time);
	}
	
	/**
	 * 
	 * @param day
	 * @param raw_start_time
	 * @param length Length of class in minutes.
	 */
	public TimePeriod(int day, String raw_start_time, int length) {
		this.start_time = TimePeriod.create_datetime(day, raw_start_time);
		this.end_time = this.start_time.plusMinutes(length);
	}
	
	public TimePeriod(LocalDateTime start_time, int length) {
		this.start_time = start_time;
		this.end_time = this.start_time.plusMinutes(length);
	}
	
	public boolean equals(Object o) {
		if (o.getClass().equals(this.getClass()) && ((TimePeriod)o).start_time == this.start_time &&
				((TimePeriod)o).end_time == this.end_time) {
			return true;
		} else
			return false;
	}
	
	/**
	 * TODO: error check
	 * 
	 * @param minutes
	 * @return
	 */
	public boolean shift_period(int minutes) {
		this.start_time = this.start_time.plusMinutes(minutes);
		this.end_time = this.end_time.plusMinutes(minutes);
		
		return true;
	}
	
	/**
	 * Creates a LocalDateTime object using the day and time.
	 * Uses 2022, June, 6th as the Monday. 
	 * Basically, we only care about the day of the week, the hour, the minutes, and the AM/PM period.
	 * Defaults to PM. 
	 * 
	 * @param day The day of the week.
	 * @param raw_date_time The string that represents the time.
	 * @return The LocalDateTime object. Returns null if unable to parse.
	 */
	static public LocalDateTime create_datetime(int day, String raw_date_time) {
		String cleaned_raw_date_time = raw_date_time.replaceAll(" ", "");
		
		for (String format : TIME_FORMATS) {
			try {
				LocalTime localTime = LocalTime.parse(cleaned_raw_date_time, DateTimeFormatter.ofPattern(format));
				
				// if no AM/PM is given, defaults to PM
				if (format.contains("H") && localTime.getHour() != 12) {
					localTime = localTime.plusHours(12);
				}
				
				return LocalDateTime.of(LocalDate.of(2022, 6, 6+day), localTime);
			} catch (DateTimeParseException e) {}
		}
		
		return null;
	}
	
	/**
	 * Check if time_period1 is within time_period2.
	 * 
	 * @param time_period1
	 * @param time_period2
	 * @return True if it is, False if it's not.
	 */
	static public boolean check_period_within(TimePeriod time_period1, TimePeriod time_period2) {
		return time_period1.end_time.compareTo(time_period2.end_time) <= 0 && 
				time_period1.start_time.compareTo(time_period2.start_time) >= 0;
	}
	
	/**
	 * Check if time_period is within time period, given a list of them to compare against
	 * 
	 * @param time_period
	 * @param constraints
	 * @return Returns True if it's within, otherwise False.
	 */
	static public boolean check_period_within_list(TimePeriod time_period, List<TimePeriod> constraints) {
		for (TimePeriod constraint : constraints) {
			if (check_period_within(time_period, constraint))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if there is a collision between two TimePeriods.
	 * 
	 * @param time_period1
	 * @param time_period2
	 * @return True if there is a collision, False is there is not.
	 */
	static public boolean check_period_collision(TimePeriod time_period1, TimePeriod time_period2) {
		return (time_period1.start_time.compareTo(time_period2.end_time) < 0 && 
				time_period1.start_time.compareTo(time_period2.end_time) > 0) ||
					(time_period2.start_time.compareTo(time_period1.end_time) < 0 && 
						time_period2.start_time.compareTo(time_period1.end_time) > 0);
	}
	
	/**
	 * Checks if there is a collision in between two TimePeriods, given a list of them to compare against
	 * 
	 * @param time_period
	 * @param constraints
	 * @return Returns True if collision detect, otherwise False.
	 */
	static public boolean check_period_list_collision(TimePeriod time_period, List<TimePeriod> constraints) {
		for (TimePeriod constraint : constraints) {
			if (check_period_collision(time_period, constraint))
				return true;
		}
		return false;
	}
}
