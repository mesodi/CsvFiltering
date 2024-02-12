package es.wacoco.csvfiltering.service;

import es.wacoco.csvfiltering.model.Job;

import java.util.ArrayList;
import java.util.Random;

public class JobService {
    ArrayList<Job> database = new ArrayList<>();
    public void newJob(Job job) {
        database.add(job);
    }

    public String createJobId(String date) {

        return null;
    }

    public String createJobID(String date) {
        Random random = new Random();

        Boolean idTaken;
        String newID;

        do {
            StringBuilder idBuilder = new StringBuilder("QRY-");
            int uniqueNum = 100000 + random.nextInt(900000);
            idBuilder.append(date.replace("-", "") + "-" + Integer.toString(uniqueNum));
            newID = idBuilder.toString();
            idTaken = false;
            for (Job job : database) {
                if (job.getJobID().equals(idBuilder)) {
                    idTaken = true;
                    break;
                }
            }


        } while (idTaken);
        return newID;
    }
}

