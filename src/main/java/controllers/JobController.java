package controllers;

import entities.Job;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    @RequestMapping("/job")
    public Job job(@RequestParam(value="path", defaultValue="EMPTY") String path) {
        return new Job(path);
    }
}
