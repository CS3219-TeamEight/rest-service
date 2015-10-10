package com.rankr.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document public class Session {
    @Id private String id;
    private String jobID;
    private String apiKey;

    public Session(String jobID, String apiKey) {
        this.jobID = jobID;
        this.apiKey = apiKey;
    }

    public String getJobID() {
        return jobID;
    }

    public String getApiKey() {
        return apiKey;
    }
}
