package com.example.main.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.main.resources.EventResponseDetails;
import com.example.main.resources.PartnerRequestDetails;
import com.example.main.service.EventSchedulerService;

/**
 * @author Abhinav Dahuja
 *
 */
@RestController
@RequestMapping("HubSpotEvent")
public class EventSchedulerController {

    @Autowired
    private EventSchedulerClient schedulingClient;
    
    @Autowired
    private EventSchedulerService schedulingService;

    private static final Logger logger = LoggerFactory.getLogger(EventSchedulerController.class);


    @GetMapping("/scheduleEvent")
    public String scheduleHubSpotEvent() {
    	
    	// Get Partner details
    	PartnerRequestDetails partnerDetails=  schedulingClient.getPartnerDetails();
        
        // Find common event dates and schedule Event
    	EventResponseDetails commonAvailabilityDetails = schedulingService.scheduleHubSpotEvent(partnerDetails);
    	
    	// Post the Scheduled event details output
    	return schedulingClient.postCommonAvailabilities(commonAvailabilityDetails);
    	
    }
    

	@ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        logger.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(),
                ex.getResponseBodyAsString(), ex);
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
	
}
