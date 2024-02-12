package es.wacoco.csvfiltering.service;

import es.wacoco.csvfiltering.model.Job;

import java.util.ArrayList;

public class JobService {
    ArrayList<Job> database = new ArrayList<>();
    public void newJob(Job job) {
        database.add(job);
    }

    public String createJobId(String date) {

        return null;
    }
}
