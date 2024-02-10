package es.wacoco.csvfiltering.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Job {
    String jobID;
    String dateCreated;
    String status;

    public Job(String jobID, String dateCreated, String status) {
        this.jobID = jobID;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public String getJobID() {
        return jobID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    //enum for jobs status
    public enum Status{

        PROCESSING,DONE,MANUAL
    }


}
