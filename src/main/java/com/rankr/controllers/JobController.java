package com.rankr.controllers;

import com.rankr.entities.Job;
import com.rankr.entities.Resume;
import com.rankr.models.JobRepository;
import com.rankr.models.ResumeRepository;
import cvia.job.JobDesc;
import cvia.main.CVIA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/api/jobs") public class JobController {
    @Autowired private ResumeRepository resumeRepo;
    @Autowired private JobRepository jobRepo;

    @RequestMapping(method = RequestMethod.POST)
    public Job createJob(@RequestBody Map<String, Object> jobJSON) {
        String id = jobJSON.get("desiredID").toString();
        String jobTitle = jobJSON.get("jobTitle").toString();
        String jobIndustry = jobJSON.get("jobIndustry").toString();
        String jobExperience = jobJSON.get("jobExperience").toString();
        String educationLevel = jobJSON.get("educationLevel").toString();
        String educationField = jobJSON.get("educationField").toString();
        String languageSkills = jobJSON.get("languageSkills").toString();
        String workSkills = jobJSON.get("workSkills").toString();
        String description = jobJSON.get("description").toString();

        double multiplierJob = Double.parseDouble(jobJSON.get("multiplierJob").toString());
        double multiplierEdu = Double.parseDouble(jobJSON.get("multiplierEdu").toString());
        double multiplierLang = Double.parseDouble(jobJSON.get("multiplierLang").toString());
        double multiplierSkill = Double.parseDouble(jobJSON.get("multiplierSkill").toString());

        Job newJob =
            new Job(id, jobTitle, jobIndustry, jobExperience, educationLevel, educationField,
                languageSkills, workSkills, description, multiplierJob, multiplierEdu,
                multiplierLang, multiplierSkill);

        jobRepo.save(newJob);
        return newJob;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Job requestJob(@PathVariable(value = "id") String id) {
        return jobRepo.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Job updateJobMetric(@PathVariable(value = "id") String id,
        @RequestBody Map<String, Object> jobJSON) {
        double multiplierJob = Double.parseDouble(jobJSON.get("multiplierJob").toString());
        double multiplierEdu = Double.parseDouble(jobJSON.get("multiplierEdu").toString());
        double multiplierLang = Double.parseDouble(jobJSON.get("multiplierLang").toString());
        double multiplierSkill = Double.parseDouble(jobJSON.get("multiplierSkill").toString());
        Job job = jobRepo.findOne(id);
        job.setMultiplierJob(multiplierJob);
        job.setMultiplierEdu(multiplierEdu);
        job.setMultiplierLang(multiplierLang);
        job.setMultiplierSkill(multiplierSkill);
        jobRepo.save(job);

        CVIA analyzer = CVIA.getInstance();
        JobDesc jobDescription = analyzer
            .initJobDesc(job.getJobTitle(), job.getJobIndustry(), job.getJobExperience(),
                job.getEducationLevel(), job.getEducationField(), job.getLanguageSkills(),
                job.getWorkSkills(), job.getDescription(), job.getMultiplierJob(),
                job.getMultiplierEdu(), job.getMultiplierLang(), job.getMultiplierSkill());

        List<Resume> linkedResumes =
            resumeRepo.findByJobID(id, new Sort(Sort.Direction.DESC, "Score"));
        for (Resume resume : linkedResumes) {
            cvia.resume.Resume resumeParserObj = analyzer.initParsedResume(resume.getParsedInfo());
            resumeParserObj = analyzer.scoreParsed(jobDescription, resumeParserObj);
            String parsedString = resumeParserObj.getParsedContents();
            double score = resumeParserObj.getScore();
            resume.setScore(score);
            resume.setParsedInfo(parsedString);
            resumeRepo.save(resume);
        }

        return job;
    }
}
