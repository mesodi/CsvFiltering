package es.wacoco.csvfiltering.controller;

import es.wacoco.csvfiltering.model.Job;
import es.wacoco.csvfiltering.service.JobService;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    private ProducerTemplate producerTemplate;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDateTime timeNow = LocalDateTime.now();

    JobService jobService = new JobService();

    @PostMapping("/create")
    public ResponseEntity<?> createJob(@RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            producerTemplate.sendBody("direct:processCsv", inputStream);
            jobService.newJob(new Job(jobService.createJobID(dtf.format(timeNow)), dtf.format(timeNow), "Processing"));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Problems processing the file", HttpStatus.BAD_REQUEST);
        }
    }
}
