package com.example.main.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Hub hub = new Hub();

    public static class Hub {
        private String apikey;

        public String getApiKey() {
            return apikey;
        }

        public void setApiKey(String apikey) {
            this.apikey = apikey;
        }
    }

    public Hub getHub() {
        return hub;
    }
}
