package com.superv.TimeScheduler;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class TimePeriodTest {

	@Test
	void testTimePeriodIntStringString() {
		TimePeriod test = new TimePeriod(1, "4", "5");
		
		LocalDateTime start_time = TimePeriod.create_datetime(1, "4");
		LocalDateTime end_time = TimePeriod.create_datetime(1, "5");
		
		assertEquals(start_time, test.getStart_time());
		assertEquals(end_time, test.getEnd_time());
		
		TimePeriod test2 = new TimePeriod(1, "4AM", "5AM");
		
		LocalDateTime start_time2 = TimePeriod.create_datetime(1, "4");
		start_time2 = start_time2.withHour(4);
		LocalDateTime end_time2 = TimePeriod.create_datetime(1, "5");
		end_time2 = end_time2.withHour(5);
		
		assertEquals(start_time2, test2.getStart_time());
		assertEquals(end_time2, test2.getEnd_time());
	}

	@Test
	void testTimePeriodIntStringInt() {
		// TODO
	}

	@Test
	void testTimePeriodLocalDateTimeInt() {
		// TODO
	}

	@Test
	void testShift_period() {
		// TODO
	}

	@Test
	void testCreate_datetime() {
		// TODO
	}

	@Test
	void testCheck_period_within() {
		LocalDateTime start12 = TimePeriod.create_datetime(0, "12");
		TimePeriod t12_16 = new TimePeriod(start12, 240);
		TimePeriod t12_18 = new TimePeriod(start12, 360);
		
		assertTrue(TimePeriod.check_period_within(t12_16, t12_18));
	}

	@Test
	void testCheck_period_within_list() {
		// TODO
	}

	@Test
	void testCheck_period_collision() {
		// TODO
	}

	@Test
	void testCheck_period_list_collision() {
		// TODO
	}

}
