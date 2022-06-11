package com.superv.TimeScheduler;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Person {
	@Getter private String name;
	@Getter private List<TimePeriod> availability;
	
	public Person(String name) {
		this.name = name;
		this.availability = new ArrayList<TimePeriod>();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass().equals(this.getClass())) {
			Person other = (Person)o;
			if (other.name.equals(this.name)) {
				for(int i = 0; i < this.availability.size(); i++) {
					if (!other.availability.get(i).equals(this.availability.get(i))) {
						return false;
					}
				}
				return true;
			} else
				return false;
		} else
			return false;
	}
	
	/**
	 * Read a list of strings and converts each of them to TimePeriods and adds them to availability.
	 * TODO: error checking
	 * 
	 * @param raw_periods
	 * @return True if successful.
	 */
	public boolean add_availability(List<String> raw_periods) {
		for (int i = 0; i < 7; i++) {
			if (!"".equals(raw_periods.get(i))){
				String[] raw_times = raw_periods.get(i).replaceAll(" ",  "").split("-");
				int dayOfWeek = i;
				
				TimePeriod timePeriod = new TimePeriod(dayOfWeek, raw_times[0], raw_times[1]);
				
				availability.add(timePeriod);
			}
		}
		
		return true;
	}
}
