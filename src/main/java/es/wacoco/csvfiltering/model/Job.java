package es.wacoco.csvfiltering.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
<<<<<<< HEAD
import java.time.format.DateTimeFormatter;
=======
import java.util.List;
import java.util.Map;
import java.util.UUID;
>>>>>>> 4d17fd8d0105eef19798229deb0ec457d632a360

@Getter
@Setter
@AllArgsConstructor
public class Job {
<<<<<<< HEAD
    String jobID;
    String dateCreated;
    String status;

    public Job(String jobID, String dateCreated, String status) {
        this.jobID = jobID;
        this.dateCreated = dateCreated;
        this.status = status;
=======
    private List<Map<String, String>> filteredData;
    private String jobID;
    private LocalDateTime dateCreated;
    private JobStatus status;

    public Job() {
        this.jobID = UUID.randomUUID().toString();
        this.dateCreated = LocalDateTime.now();
        this.status = JobStatus.PROCESS;
>>>>>>> 4d17fd8d0105eef19798229deb0ec457d632a360
    }

    public enum JobStatus {
        PROCESS, DONE, MANUAL,;
    }
<<<<<<< HEAD

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
=======
        @Override
    public String toString() {
        return "Job{" +
                "jobID='" + jobID + '\'' +
                ", dateCreated=" + dateCreated +
                ", status=" + status +
                ", filteredData=" + filteredData +
                '}';
>>>>>>> 4d17fd8d0105eef19798229deb0ec457d632a360
    }


}
