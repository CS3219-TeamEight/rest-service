package com.rankr.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document public class Job {
    @Id private String id;
    private String jobTitle;
    private String jobIndustry;
    private String jobExperience;
    private String educationLevel;
    private String educationField;
    private String languageSkills;
    private String workSkills;
    private String description;

    private double multiplierJob;
    private double multiplierEdu;
    private double multiplierLang;
    private double multiplierSkill;

    @PersistenceConstructor
    public Job(String id, String jobTitle, String jobIndustry, String jobExperience,
        String educationLevel, String educationField, String languageSkills, String workSkills,
        String description, double multiplierJob, double multiplierEdu, double multiplierLang,
        double multiplierSkill) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.jobIndustry = jobIndustry;
        this.jobExperience = jobExperience;
        this.educationLevel = educationLevel;
        this.educationField = educationField;
        this.languageSkills = languageSkills;
        this.workSkills = workSkills;
        this.description = description;
        this.multiplierJob = multiplierJob;
        this.multiplierEdu = multiplierEdu;
        this.multiplierLang = multiplierLang;
        this.multiplierSkill = multiplierSkill;
    }

    public String getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobIndustry() {
        return jobIndustry;
    }

    public void setJobIndustry(String jobIndustry) {
        this.jobIndustry = jobIndustry;
    }

    public String getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(String jobExperience) {
        this.jobExperience = jobExperience;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getEducationField() {
        return educationField;
    }

    public void setEducationField(String educationField) {
        this.educationField = educationField;
    }

    public String getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(String languageSkills) {
        this.languageSkills = languageSkills;
    }

    public String getWorkSkills() {
        return workSkills;
    }

    public void setWorkSkills(String workSkills) {
        this.workSkills = workSkills;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMultiplierJob() {
        return multiplierJob;
    }

    public void setMultiplierJob(double multiplierJob) {
        this.multiplierJob = multiplierJob;
    }

    public double getMultiplierEdu() {
        return multiplierEdu;
    }

    public void setMultiplierEdu(double multiplierEdu) {
        this.multiplierEdu = multiplierEdu;
    }

    public double getMultiplierLang() {
        return multiplierLang;
    }

    public void setMultiplierLang(double multiplierLang) {
        this.multiplierLang = multiplierLang;
    }

    public double getMultiplierSkill() {
        return multiplierSkill;
    }

    public void setMultiplierSkill(double multiplierSkill) {
        this.multiplierSkill = multiplierSkill;
    }
}
