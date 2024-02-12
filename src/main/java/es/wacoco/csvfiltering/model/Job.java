package es.wacoco.csvfiltering.model;

public class Job {

    String jobID;
    String dateCreated;
    String currentStatus;

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }



    public Job(String jobID, String dateCreated, String currentStatus) {
        this.jobID = jobID;
        this.dateCreated = dateCreated;
        this.currentStatus = currentStatus;
    }


}