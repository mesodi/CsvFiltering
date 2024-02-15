package es.wacoco.csvfiltering.service;

import es.wacoco.csvfiltering.model.Job;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class JobService {
    private final Map<String, Job> jobs = new ConcurrentHashMap<>(

    );

    public String createJobID(LocalDateTime dateTime) {
        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String uniqueKey = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "QRY-" + formattedDate + "-" + uniqueKey;
    }

    public void newJob(Job job) {
        jobs.put(job.getJobID(), job);
    }

    public Job getJob(String jobId) {
        return jobs.get(jobId);
    }

    public void updateJob(Job job) {
        jobs.put(job.getJobID(), job);
    }

    public List<Job> getAllJobs() {
        return new ArrayList<>(jobs.values());
    }
}