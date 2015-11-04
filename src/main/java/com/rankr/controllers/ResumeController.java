package com.rankr.controllers;

import com.rankr.entities.Resume;
import com.rankr.models.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
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

    @RequestMapping(value = "/{resumeID}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<InputStreamResource> getResume(
        @PathVariable(value = "resumeID") String resumeID) {
        Resume retrievedResume = repo.findOne(resumeID);
        if (retrievedResume == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            File resumeFile = new File(retrievedResume.getResumePath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-Disposition",
                "attachment; filename=\"" + resumeFile.getName() + "\"");

            try {
                return ResponseEntity.ok().headers(headers).contentLength(resumeFile.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(new FileInputStream(resumeFile)));
            } catch (IOException e) {
                System.err.println("Failed to retrieve resume: " + e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    /* TODO: Add authentication */
    @RequestMapping(method = RequestMethod.GET) public List<Resume> getResumes(
        @RequestParam(value = "id") String jobID) {
        return repo.findByJobID(jobID, new Sort(Sort.Direction.DESC, "Score"));
    }
}
