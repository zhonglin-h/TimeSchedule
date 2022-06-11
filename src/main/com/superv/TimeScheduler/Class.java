package com.superv.TimeScheduler;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Data public class Class {
	private final String name;
	private final String teacher;
	private final int length;  // in minutes
	private final boolean separate_days;
	private final List<String> student_list;
	private Map<String, Person> member_list = null;
	private TimePeriod final_period = null;
	private TimePeriod temp_period = null;
	
	public final String SCHOOL_KW = "school";
	public final String PERSON_KW = "person";
	public final String CLASS_KW = "class";
	
	public boolean set_member_list(List<Person> master_list) {
		member_list = new HashMap<String, Person>();
		
		for (String name : student_list) {
			// search students
			int i = 0;
			for (; i < master_list.size(); i++) {
				if (master_list.get(i).getName().equals(name)) {
					member_list.put(name, master_list.get(i));
					break;
				}
			}
			
			if (i == master_list.size()) {
				System.out.println("Error, student availability of " + name + " not found.");
				return false;
			}
		}
		
		// find teacher
		if (!"-1".trim().equals(teacher)) {  // skip this step if it's -1
			int i = 0;
			for (; i < master_list.size(); i++) {
				if (master_list.get(i).getName().equals(teacher)) {
					member_list.put(teacher, master_list.get(i));
					break;
				}
			}
			
			if (i == master_list.size()) {
				System.out.println("Error, teacher availability not found.");
				return false;
			}
		}
		
		return true;
	}
}
