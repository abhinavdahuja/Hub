package com.example.main.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.main.config.AppProperties;
import com.example.main.resources.EventResponseDetails;
import com.example.main.resources.PartnerRequestDetails;

import reactor.core.publisher.Mono;

@Service
public class EventSchedulerClient {

	private static final String API_BASE_URL = "https://candidate.hubteam.com";
	private static final Logger logger = LoggerFactory.getLogger(EventSchedulerClient.class);
	private final WebClient webClient;
	private String key = null;

	@Autowired
	public EventSchedulerClient(AppProperties appProperties) {

		this.key = appProperties.getHub().getApiKey();

		this.webClient = WebClient
				.builder().baseUrl(API_BASE_URL)
				.filter(logRequest()).build();
	}

	
	  public String postCommonAvailabilities(EventResponseDetails commonAvailabilityDetails) { 
		  
		  return webClient.post()
		          .uri("/candidateTest/v3/problem/result" + "?userKey=" + key)
		          .body(Mono.just(commonAvailabilityDetails), EventResponseDetails.class)
		          .retrieve()
		          .bodyToMono(String.class).block();
		  
	  }
	 

	public PartnerRequestDetails getPartnerDetails() {
		return webClient.get().uri("/candidateTest/v3/problem/dataset" + "?userKey=" + key).retrieve()
				.bodyToMono(PartnerRequestDetails.class).block();
	}

	private ExchangeFilterFunction logRequest() {
		return (clientRequest, next) -> {
			logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers()
					.forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
			return next.exchange(clientRequest);
		};
	}

}
