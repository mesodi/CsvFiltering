package es.wacoco.csvfiltering.model;

import java.time.LocalDateTime;

public class Job {
    String jobID;
    LocalDateTime dateCreated;

    Status status;

    public Job(String jobID, LocalDateTime dateCreated, Status status) {
        this.jobID = jobID;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public String getJobID() {
        return jobID;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public Status getStatus() {
        return status;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    //enum for jobs status
    public enum Status{
        PROCESSING,DONE,MANUAL
    }
}
