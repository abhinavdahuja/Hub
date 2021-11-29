package com.example.main.resources;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CountryBasedEventDetails {

	int attendeeCount;
	List<String> attendees;
	String name;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	Date startDate;

	public int getAttendeeCount() {
		return attendeeCount;
	}

	public void setAttendeeCount(int attendeeCount) {
		this.attendeeCount = attendeeCount;
	}

	public List<String> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<String> attendees) {
		this.attendees = attendees;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
