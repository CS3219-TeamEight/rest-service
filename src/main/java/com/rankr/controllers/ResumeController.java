package com.rankr.controllers;

import com.rankr.entities.Resume;
import com.rankr.models.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/resume") public class ResumeController {
    @Autowired private ResumeRepository repo;

    @RequestMapping(method = RequestMethod.POST)
    public Resume createResume(@RequestParam(value = "jobID") String jobID,
        @RequestParam(value = "resumePath") String resumePath,
        @RequestParam(value = "score") double score) {
        Resume newResume = new Resume(jobID, resumePath, score);
        repo.save(newResume);
        return newResume;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resume getResume(@PathVariable(value = "id") String id) {
        return repo.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Resume> getJobResumes(@RequestParam(value = "jobID") String jobID) {
        return repo.findByJobID(jobID, new Sort(Sort.Direction.DESC, "Score"));
    }
}
