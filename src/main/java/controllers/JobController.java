package controllers;

import entities.Job;
import models.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController public class JobController {
    @Autowired private JobRepository repo;

    @RequestMapping(value = "/job", method = RequestMethod.POST)
    public Job createJob(@RequestParam(value = "description") String description,
        @RequestParam(value = "path") String descriptionPath,
        @RequestParam(value = "title") String title,
        @RequestParam(value = "password") String password,
        @RequestParam(value = "visibility") int visibility) {
        return new Job(description, descriptionPath, title, password, visibility);
    }

    @RequestMapping(value = "/job/{id}", method = RequestMethod.POST)
    public Job requestJob(@PathVariable(value = "id") String id,
        @RequestParam(value = "password") String password) {
        return repo.findOne(id);
    }

    @RequestMapping(value = "/job/{id}", method = RequestMethod.PUT)
    public void updateJob(@PathVariable(value = "id") String id,
        @RequestParam(value = "password") String password,
        @RequestParam(value = "visibility") int visibility) {
        Job foundJob = repo.findOne(id);
        foundJob.setPassword(password);
        foundJob.setVisibility(visibility);
        repo.save(foundJob);
    }
}
