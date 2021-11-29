package com.example.main;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.main.controller.EventSchedulerClient;
import com.example.main.resources.PartnerRequestDetails;
import com.example.main.service.EventSchedulerService;


public class HubClientTest {
	
	@Autowired
    private EventSchedulerClient client;
	
	@Autowired
    private EventSchedulerService service;

	
	  @Test void retrieveAllPartnerRequestDetails(){ 
		  PartnerRequestDetails list = client.getPartnerDetails();
		  assertTrue(list.getPartners().size()>0); 
		  
	  }
	  
	  @Test void scheduleEvent(){ 
		  PartnerRequestDetails list = client.getPartnerDetails();
		  assertTrue(service.scheduleHubSpotEvent(list).getCountries().size()>0); 

	  }

  

}
