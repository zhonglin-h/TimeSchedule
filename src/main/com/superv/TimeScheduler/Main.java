package com.superv.TimeScheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data class ScheduleData {
	List<Class> class_list = new ArrayList<Class>();
	Person school = null;
	List<Person> person_list = new ArrayList<Person>();
}

public class Main {
	
	public static ScheduleData get_data_from_file(String file_name) {
		// TODO: error check
		CSVReader reader;
		
		List<Class> class_list = new ArrayList<Class>();
		Person school = null;
		List<Person> person_list = new ArrayList<Person>();
		
		try {
			reader = new CSVReader(new FileReader(new File(file_name)));
			
			String[] line;
			while ((line = reader.readNext()) != null) {
				String line_type = line[0].toLowerCase();

				List<String> data = Arrays.asList(line);
				
				if ("class".equals(line_type)) {
					String class_name = line[1];
					String teacher_name = line[2];
					int length = Integer.parseInt(line[3]);
					
					boolean separate_days = false;
					if (!"".trim().equals(line[5]))
						separate_days = Boolean.parseBoolean(line[5]);
					
					int num_of_students = Integer.parseInt(line[10]);
					List<String> student_list = data.subList(11, 11 + num_of_students);
					
					class_list.add(new Class(class_name, teacher_name, length, separate_days, student_list));
					
					if (!"".trim().equals(line[4])) {
						String[] temp = line[4].split("_");
						int day = Integer.parseInt(temp[0]);
						String raw_time = temp[1];
						class_list.get(class_list.size()-1).setFinal_period(new TimePeriod(day, raw_time, length));
					}
				} else if ("school".equals(line_type) || "person".equals(line_type)) {					
					// TODO: error check!
					if ("person".equals(line_type)) {
						Person new_person = new Person(line[1]);
						new_person.add_availability(data.subList(2, 9));
						person_list.add(new_person);
					}
					else if ("school".equals(line_type)) {
						school = new Person("school");
						school.add_availability(data.subList(1, 8));
					}
				}
			}
			
			return new ScheduleData(class_list, school, person_list);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * 
	 * @param data
	 * @return True if there's a conflict, false if there's not.
	 */
	public static boolean check_conflicts(ScheduleData data) {
		// first, check if time works with everyone's availability
		for (Class a_class : data.class_list) {
			for (Person person : a_class.getMember_list().values()) {
				if (TimePeriod.check_period_within_list(a_class.getTemp_period(), person.getAvailability()))
					continue;
				else
					return true;
			}
		}
		
		// then, check if they have two classes at same time
		int i = 0;
		for (; i < data.class_list.size(); i++) {
			int i2 = 0;
			for (; i < data.class_list.size(); i++) {
				if (i != i2) { // skip if it's the same class
					Class class1 = data.class_list.get(i);
					Class class2 = data.class_list.get(i2);
					// check if there's any conflicts
					for (Person person1 : class1.getMember_list().values()) {
						if (class2.getMember_list().containsKey(person1.getName())) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param data
	 * @param school_avail_i_list
	 * @param class_to_incr
	 * @return 0 is change made, 1 is error, -1 is reached end.
	 */
	public static int change_to_next_avail_time(ScheduleData data, int[] school_avail_i_list, int class_to_incr) {
		Class a_class = data.class_list.get(class_to_incr);
		int avil_i = school_avail_i_list[class_to_incr];
		
		// if it can't be changed
		if (a_class.getFinal_period() != null) {
			if (class_to_incr + 1 < data.class_list.size()) {
				return change_to_next_avail_time(data, school_avail_i_list, class_to_incr + 1);
			} else
				return -1;
		}
		
		a_class.getTemp_period().shift_period(15);
		if (class_to_incr >= 5)
			System.out.println(class_to_incr);
		
		// while out of school availability
		while (!TimePeriod.check_period_within(a_class.getTemp_period(), 
				data.school.getAvailability().get(avil_i))) {
			if (avil_i < data.school.getAvailability().size() - 1) {
				// go to next availability
				school_avail_i_list[class_to_incr] += 1;
				avil_i = school_avail_i_list[class_to_incr];
				
				// change class time to that time too
				LocalDateTime new_start = data.school.getAvailability().get(avil_i).getStart_time();
				a_class.setTemp_period(new TimePeriod(new_start, a_class.getLength()));
			} else { // if reached end of school availability
				// reset everything to the first school availability, then increment the next one
				school_avail_i_list[class_to_incr] = 0;
				avil_i = school_avail_i_list[class_to_incr];
				
				LocalDateTime new_start = data.school.getAvailability().get(avil_i).getStart_time();
				a_class.setTemp_period(new TimePeriod(new_start, a_class.getLength()));
				
				if (class_to_incr + 1 < data.class_list.size()) {
					return change_to_next_avail_time(data, school_avail_i_list, class_to_incr + 1);
				} else
					return -1;
			}
		}
		
		return 0;
	}
	
	public static int[] initialize_school_avail_i_list(ScheduleData data) {
		int[] school_avail_i_list = new int[data.class_list.size()];  // school_availability_i_list
		for (int i = 0; i < school_avail_i_list.length; i++) {
			school_avail_i_list[i] = 0;
		}
		
		return school_avail_i_list;
	}
	
	public static boolean initialize_schedule(ScheduleData data) {
		// TODO: fix issue from class might be longer than first availability of school
		LocalDateTime current = data.school.getAvailability().get(0).getStart_time();
		
		for (Class a_class : data.class_list) {
			if (a_class.getFinal_period() != null) {
				a_class.setTemp_period(a_class.getFinal_period());
			} else {
				a_class.setTemp_period(new TimePeriod(current,a_class.getLength()));
			}
			a_class.set_member_list(data.person_list);
		}
		
		return true;
	}
	
	public static boolean run_algo (ScheduleData data) {
		int[] school_avail_i_list = initialize_school_avail_i_list(data);
		
		while (check_conflicts(data)) {
			int status = change_to_next_avail_time(data, school_avail_i_list, 0);
			
			if (status != 0) {
				return false;
			}
		}
		
		return true;
	}

	public static void main(String[] args) {
		ScheduleData data = get_data_from_file(args[0]);
		
		System.out.println("Loaded file.");
		
		initialize_schedule(data);
		
		System.out.println("Initialized variables.");
		
		if (run_algo(data)) {
			System.out.println("Found something!");
			for(Class a_class : data.class_list) {
				System.out.println(a_class.getName() + ": " + a_class.getTemp_period().getStart_time().toString());
			}
		} else {
			System.out.println("Nope.");
		}
	}

}
