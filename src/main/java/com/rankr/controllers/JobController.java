package com.rankr.controllers;

import com.rankr.authentication.PasswordHash;
import com.rankr.authentication.SessionManager;
import com.rankr.entities.Job;
import com.rankr.models.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController public class JobController {
    @Autowired private JobRepository repo;

    @RequestMapping(value = "jobs", method = RequestMethod.POST)
    public Job createJob(@RequestParam(value = "description") String description,
        @RequestParam(value = "path") String descriptionPath,
        @RequestParam(value = "title") String title,
        @RequestParam(value = "password") String password,
        @RequestParam(value = "visibility") int visibility) {
        String salt = new String(PasswordHash.generateSalt(), StandardCharsets.UTF_8);
        byte[] hashArray = PasswordHash.generateHash(password.toCharArray(), salt.getBytes());
        String hash = new String(hashArray, StandardCharsets.UTF_8);
        Job newJob = new Job(description, descriptionPath, title, hash, salt, visibility);
        repo.save(newJob);
        return newJob;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Job requestJob(@PathVariable(value = "id") String id) {
        return repo.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateJob(@PathVariable(value = "id") String id,
        @RequestParam(value = "password") String password,
        @RequestParam(value = "visibility") int visibility) {
        Job foundJob = repo.findOne(id);
        foundJob.setPassword(password);
        foundJob.setVisibility(visibility);
        repo.save(foundJob);
    }

    @RequestMapping(value = "/{id}/auth", method = RequestMethod.GET)
    public String authenticateJob(@PathVariable(value = "id") String id,
        @RequestParam(value = "password") String password) {
        Job foundJob = repo.findOne(id);
        boolean authenticated = PasswordHash
            .matchHash(password.toCharArray(), foundJob.getSalt().getBytes(),
                foundJob.getPassword().getBytes());
        return authenticated ? SessionManager.getManager().createSession(id) : null;
    }
}
