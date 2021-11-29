package com.example.main.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.example.main.resources.CountryBasedEventDetails;
import com.example.main.resources.EventResponseDetails;
import com.example.main.resources.PartnerDetails;
import com.example.main.resources.PartnerRequestDetails;

/**
 * @author Abhinav Dahuja
 *
 */

@Service
public class EventSchedulerService {

/**
 * 
 * Schedules the Event based on common partner availability per country
 * 
 * @param partnerDetails
 * @return EventResponseDetails
 */
public EventResponseDetails scheduleHubSpotEvent(PartnerRequestDetails partnerDetails){
    	
    	Map<String,List<PartnerDetails>> partnersCountryMap = new HashMap<String,List<PartnerDetails>>();
    	List<PartnerDetails> partnerDetailList = partnerDetails.getPartners();
    	
    	for(int partnerCounter=0;partnerCounter<partnerDetailList.size();partnerCounter++){
    		PartnerDetails partner = partnerDetailList.get(partnerCounter);
    		List<PartnerDetails> commonCountryPartners = null;
    		if(partnersCountryMap.containsKey(partner.getCountry())) {
    			commonCountryPartners = partnersCountryMap.get(partner.getCountry());
    		}else {
    			commonCountryPartners = new ArrayList<PartnerDetails>();
    		}
    		commonCountryPartners.add(partner);
    		partnersCountryMap.put(partner.getCountry(),commonCountryPartners);
    	}
    	
    	EventResponseDetails commonAvailabilityDetails = new EventResponseDetails();
    	List<CountryBasedEventDetails> countriesBasedPartnerList = new ArrayList<CountryBasedEventDetails>();

    	for(Map.Entry<String, List<PartnerDetails>> entry: partnersCountryMap.entrySet()) {
    		countriesBasedPartnerList.add(getCommonAvailabilityForCountry(entry.getValue()));
    	}
    	
    	commonAvailabilityDetails.setCountries(countriesBasedPartnerList);
    	return commonAvailabilityDetails;
    }
    

    /**
     * 
     * Creates a common Date specific map with Partner availabilities for each country
     * Traverses it to find the date which is common to most partners and returns the CountryBasedEventDetails
     * 
     * @param countryPartner
     * @return CountryBasedEventDetails
     */
    private CountryBasedEventDetails getCommonAvailabilityForCountry(List<PartnerDetails>  countryPartner){
    	
    	TreeMap<Date,List<PartnerDetails>> dateBasedPartnerMap = new TreeMap<Date,List<PartnerDetails>>();
		CountryBasedEventDetails result = new CountryBasedEventDetails();
		result.setAttendeeCount(0);
		result.setAttendees(new ArrayList<String>());

    	for(int countryPartnerCounter=0;countryPartnerCounter<countryPartner.size();countryPartnerCounter++){
    		PartnerDetails partner = countryPartner.get(countryPartnerCounter);
    		List<Date> dateList = partner.getAvailableDates();
    		Collections.sort(dateList);
    		
    		for(int availabaleDateCounter=0;availabaleDateCounter <dateList.size()-1;availabaleDateCounter ++) {
    			long diffInMillies = Math.abs(dateList.get(availabaleDateCounter+1).getTime() - dateList.get(availabaleDateCounter).getTime());
    		    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    			
    			if(diff==1) {
    				Date currentAvailableDate =dateList.get(availabaleDateCounter);
    	    		List<PartnerDetails> commonDatePartner = null;
    	    		if(dateBasedPartnerMap.containsKey(currentAvailableDate)) {
    	    			commonDatePartner = dateBasedPartnerMap.get(currentAvailableDate);
    	    		}else {
    	    			commonDatePartner = new ArrayList<PartnerDetails>();
    	    		}
    	    		commonDatePartner.add(partner);
    	    		dateBasedPartnerMap.put(currentAvailableDate,commonDatePartner);
    				
    			}
  			 
    		}
    		
    		int maxCommonDateCount=0;
        	
    		for(Map.Entry<Date, List<PartnerDetails>> partnerDetailentry: dateBasedPartnerMap.entrySet()) {
    			if(maxCommonDateCount < partnerDetailentry.getValue().size()) {
    				result  = createCountryDetails(partnerDetailentry);
    				maxCommonDateCount = partnerDetailentry.getValue().size();
    			}
        	}
			 
    	}
    	
    	return result;
    	
    }
  
   

    /**
     * 
     * 
     * @param entry
     * @return CountryBasedEventDetails
     */
    private CountryBasedEventDetails createCountryDetails(Map.Entry<Date, List<PartnerDetails>> entry) {
    	
    	CountryBasedEventDetails countryDetails =new CountryBasedEventDetails();
    	countryDetails.setAttendeeCount(entry.getValue().size());
    	countryDetails.setName(entry.getValue().get(0).getCountry());
    	countryDetails.setStartDate(entry.getKey());
    	
    	List<PartnerDetails> partner = entry.getValue();
    	List<String> attendeelist =new ArrayList<String>();
    	for(int partnerDetailsCounter =0 ;partnerDetailsCounter <partner.size();partnerDetailsCounter++) {
    		attendeelist.add(entry.getValue().get(partnerDetailsCounter).getEmail());
    	}
    	
    	countryDetails.setAttendees(attendeelist);
    	return countryDetails;
	}
}
