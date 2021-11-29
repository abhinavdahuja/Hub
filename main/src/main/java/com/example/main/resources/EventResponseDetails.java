package com.example.main.resources;

import java.util.List;

public class EventResponseDetails {

	public List<CountryBasedEventDetails> getCountries() {
		return countries;
	}

	public void setCountries(List<CountryBasedEventDetails> countries) {
		this.countries = countries;
	}

	List<CountryBasedEventDetails> countries;
	
}
