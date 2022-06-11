package com.superv.TimeScheduler;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class MainTest {

	@Test
	void testGet_data_from_file() {
		ScheduleData data = Main.get_data_from_file("data/MainTest1.csv");
		
		// class
		assertEquals(1,data.class_list.size());
		assertEquals("Class1",data.class_list.get(0).getName());
		assertEquals("Mark",data.class_list.get(0).getTeacher());
		assertEquals(90,data.class_list.get(0).getLength());
		assertEquals("Bob",data.class_list.get(0).getStudent_list().get(0));
		assertEquals("Tom",data.class_list.get(0).getStudent_list().get(1));
		
		// school
		assertEquals(7,data.school.getAvailability().size());
		LocalDateTime test10_12 = LocalDateTime.of(2022, 6, 10, 12, 0, 0);
		LocalDateTime test10_23 = LocalDateTime.of(2022, 6, 10, 23, 0, 0);
		assertEquals(test10_12,data.school.getAvailability().get(4).getStart_time());
		assertEquals(test10_23,data.school.getAvailability().get(4).getEnd_time());
		
		// person
		assertEquals(3,data.person_list.size());
		assertEquals("Bob",data.person_list.get(1).getName());
		LocalDateTime test8_16 = LocalDateTime.of(2022, 6, 8, 16, 0, 0);
		LocalDateTime test8_20 = LocalDateTime.of(2022, 6, 8, 20, 0, 0);
		assertEquals(test8_16,data.person_list.get(1).getAvailability().get(2).getStart_time());
		assertEquals(test8_20,data.person_list.get(1).getAvailability().get(2).getEnd_time());
	}
	
	@Test
	void testInitialize_school_avail_i_list() {
		ScheduleData data = Main.get_data_from_file("data/MainTest1.csv");
		
		int[] school_avail_i_list = Main.initialize_school_avail_i_list(data);
		
		assertEquals(1, school_avail_i_list.length);
		for (int a : school_avail_i_list) {
			assertEquals(0, a);
		}
	}
	
	@Test
	void testChange_to_next_avail_time() {
		ScheduleData data = Main.get_data_from_file("data/MainTest1.csv");
		
		int[] school_avail_i_list = Main.initialize_school_avail_i_list(data);
		
		Main.initialize_schedule(data);
		
		int result = Main.change_to_next_avail_time(data, school_avail_i_list, 0);
		
		assertEquals(6, data.class_list.get(0).getTemp_period().getStart_time().getDayOfMonth());
		assertEquals(12, data.class_list.get(0).getTemp_period().getStart_time().getHour());
		assertEquals(15, data.class_list.get(0).getTemp_period().getStart_time().getMinute());
		assertEquals(6, data.class_list.get(0).getTemp_period().getEnd_time().getDayOfMonth());
		assertEquals(13, data.class_list.get(0).getTemp_period().getEnd_time().getHour());
		assertEquals(45, data.class_list.get(0).getTemp_period().getEnd_time().getMinute());
		assertEquals(0, result);
	}
	
	@Test
	void testCheck_conflicts() {
		// TODO
	}

}
