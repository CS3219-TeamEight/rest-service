package com.rankr.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document public class Resume {
    @Id private String id;
    private String jobID;
    private String resumePath;
    private double score;

    @PersistenceConstructor public Resume(String jobID, String resumePath, double score) {
        this.jobID = jobID;
        this.resumePath = resumePath;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getJobID() {
        return jobID;
    }

    public String getResumePath() {
        return resumePath;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
