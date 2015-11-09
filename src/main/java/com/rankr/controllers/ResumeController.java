package com.rankr.controllers;

import com.rankr.entities.Job;
import com.rankr.entities.Resume;
import com.rankr.models.JobRepository;
import com.rankr.models.ResumeRepository;
import cvia.job.JobDesc;
import cvia.main.CVIA;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
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
import java.util.ArrayList;
import java.util.List;

@RestController @RequestMapping("/api/resumes") public class ResumeController {
    @Autowired private ResumeRepository resumeRepo;
    @Autowired private JobRepository jobRepo;

    @RequestMapping(method = RequestMethod.POST)
    public void createResume(@RequestParam(value = "jobID") String jobID,
        @RequestParam(value = "file") MultipartFile resumeFile) {
        if (!resumeFile.isEmpty()) {
            try {
                String destPath = System.getProperty("user.home") + "/rankr/" + jobID + "/";
                File destFile = new File(destPath + resumeFile.getOriginalFilename());
                destFile.getParentFile().mkdirs();
                resumeFile.transferTo(destFile);
                String absolutePath = destFile.getAbsolutePath();

                ZipFile possibleZip = new ZipFile(destFile);
                if (possibleZip.isValidZipFile()) {
                    List<FileHeader> fileHeaders = possibleZip.getFileHeaders();
                    for (FileHeader header : fileHeaders) {
                        String fileName = header.getFileName();
                        System.out.println(fileName);
                        if (fileName.contains(".pdf") && !fileName.contains("MACOSX")) {
                            possibleZip.extractFile(header, destPath);
                            File resume = new File(destPath + fileName);
                            parseResume(jobID, resume.getAbsolutePath());
                        }
                    }
                } else {
                    parseResume(jobID, absolutePath);
                }

            } catch (IOException | ZipException e) {
                System.err.println("Failed to upload to server: " + e.getMessage());
            }
        }
    }

    private void parseResume(String jobID, String absolutePath) {
        /* Interface with CViA to get the resume score */
        CVIA analyzer = CVIA.getInstance();
        Job linkedJob = jobRepo.findOne(jobID);
        JobDesc jobDescription = analyzer
            .initJobDesc(linkedJob.getJobTitle(), linkedJob.getJobIndustry(),
                linkedJob.getJobExperience(), linkedJob.getEducationLevel(),
                linkedJob.getEducationField(), linkedJob.getLanguageSkills(),
                linkedJob.getWorkSkills(), linkedJob.getDescription(), linkedJob.getMultiplierJob(),
                linkedJob.getMultiplierEdu(), linkedJob.getMultiplierLang(),
                linkedJob.getMultiplierSkill());
        cvia.resume.Resume resumeParserObj = analyzer.initResume(absolutePath);
        resumeParserObj = analyzer.parseResume(jobDescription, resumeParserObj);
        String parsedString = resumeParserObj.getParsedContents();
        double score = resumeParserObj.getScore();
        Resume newResume = new Resume(jobID, absolutePath, parsedString, score);
        resumeRepo.save(newResume);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Resume> getResumes(@RequestParam(value = "id") String jobID) {
        return resumeRepo.findByJobID(jobID, new Sort(Sort.Direction.DESC, "Score"));
    }

    @RequestMapping(value = "/{resumeID}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<InputStreamResource> getResume(
        @PathVariable(value = "resumeID") String resumeID) {
        Resume retrievedResume = resumeRepo.findOne(resumeID);
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

    @RequestMapping(value = "/batch", method = RequestMethod.GET, produces = "application/octet-stream")
    public ResponseEntity<InputStreamResource> getResumeZIP(
        @RequestParam(value = "resumes") List<String> resumes,
        @RequestParam(value = "zipUUID") String zipUUID) {
        System.out.println("Hi");
        String destPath = System.getProperty("user.home") + "/rankr/zip/";
        try {
            File destFile = new File(destPath + zipUUID + ".zip");
            destFile.getParentFile().mkdirs();
            ZipFile newZip = new ZipFile(destPath + zipUUID + ".zip");
            ArrayList<File> resumesToAdd = new ArrayList<>();
            for (String resumeID : resumes) {
                Resume retrievedResume = resumeRepo.findOne(resumeID);
                resumesToAdd.add(new File(retrievedResume.getResumePath()));
            }
            ZipParameters param = new ZipParameters();
            param.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            param.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            newZip.addFiles(resumesToAdd, param);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers
                .add("Content-Disposition", "attachment; filename=\"" + destFile.getName() + "\"");
            return ResponseEntity.ok().headers(headers).contentLength(destFile.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new FileInputStream(destFile)));
        } catch (ZipException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
