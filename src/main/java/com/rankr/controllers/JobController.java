package com.rankr.controllers;

import com.rankr.authentication.PasswordHash;
import com.rankr.authentication.SessionManager;
import com.rankr.entities.Job;
import com.rankr.models.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController @RequestMapping("/api/jobs") public class JobController {
    @Autowired private JobRepository repo;

    @RequestMapping(method = RequestMethod.POST)
    public Job createJob(@RequestBody Map<String, Object> jobJSON) {
        String description = jobJSON.get("description").toString();
        String descriptionPath = jobJSON.get("path").toString();
        String title = jobJSON.get("title").toString();
        String password = jobJSON.get("password").toString();
        int visibility = (Integer) jobJSON.get("visibility");

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
        byte[] hashArray =
            PasswordHash.generateHash(password.toCharArray(), foundJob.getSalt().getBytes());
        String hash = new String(hashArray, StandardCharsets.UTF_8);
        foundJob.setPassword(hash);
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
