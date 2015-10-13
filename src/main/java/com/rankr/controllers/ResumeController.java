package com.rankr.controllers;

import com.rankr.entities.Resume;
import com.rankr.models.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController @RequestMapping("/api/resumes") public class ResumeController {
    @Autowired private ResumeRepository repo;

    @RequestMapping(method = RequestMethod.POST)
    public Resume createResume(@RequestParam(value = "jobID") String jobID,
        @RequestParam(value = "file") MultipartFile resumeFile) {
        if (!resumeFile.isEmpty()) {
            try {
                double score = 50.0;
                String destPath = System.getProperty("user.home") + "/rankr/" + jobID + "/";
                File destFile = new File(destPath + resumeFile.getOriginalFilename());
                destFile.getParentFile().mkdirs();
                resumeFile.transferTo(destFile);
                Resume newResume = new Resume(jobID, destFile.getAbsolutePath(), score);
                repo.save(newResume);
                return newResume;
            } catch (IOException e) {
                System.err.println("Failed to upload to server: " + e.getMessage());
            }
        }

        return null;
    }

    @RequestMapping(value = "/{resumeID}", method = RequestMethod.GET)
    public Resume getResume(@PathVariable(value = "resumeID") String resumeID) {
        return repo.findOne(resumeID);
    }

    /* TODO: Add authentication */
    @RequestMapping(method = RequestMethod.GET) public List<Resume> getResumes(
        @RequestParam(value = "id") String jobID) {
        return repo.findByJobID(jobID, new Sort(Sort.Direction.DESC, "Score"));
    }
}
