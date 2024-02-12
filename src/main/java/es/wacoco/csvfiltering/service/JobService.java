package es.wacoco.csvfiltering.service;

import es.wacoco.csvfiltering.model.Job;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

@Service
public class JobService {
    ArrayList<Job> database = new ArrayList<>();
    public void newJob(Job job) {
        database.add(job);
    }

    public String createJobID(LocalDateTime dateTime) {
        Random random = new Random();
        // Formatting date here is only for ID purpose, not for setting the dateCreated attribute
        String date = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        StringBuilder idBuilder = new StringBuilder("QRY-");
        int uniqueNum = 100000 + random.nextInt(900000);
        idBuilder.append(date).append("-").append(uniqueNum);
        return idBuilder.toString();
    }
}

