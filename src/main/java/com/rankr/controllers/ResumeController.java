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

@RestController @RequestMapping("/{id}/resumes") public class ResumeController {
    @Autowired private ResumeRepository repo;

    @RequestMapping(method = RequestMethod.POST)
    public Resume createResume(@PathVariable(value = "id") String jobID,
        @RequestParam(value = "file") MultipartFile resumeFile,
        @RequestParam(value = "score") double score) {
        if (!resumeFile.isEmpty()) {
            try {
                String destPath = "/rankr/" + jobID + "/" + resumeFile.getOriginalFilename();
                File destFile = new File(destPath);
                destFile.mkdirs();
                resumeFile.transferTo(destFile);
                Resume newResume = new Resume(jobID, destPath, score);
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
        @PathVariable(value = "id") String jobID) {
        return repo.findByJobID(jobID, new Sort(Sort.Direction.DESC, "Score"));
    }
}
