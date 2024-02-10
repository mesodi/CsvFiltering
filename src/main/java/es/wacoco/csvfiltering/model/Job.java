package es.wacoco.csvfiltering.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Job {
    private List<Map<String, String>> filteredData;
    private String jobID;
    private LocalDateTime dateCreated;
    private JobStatus status;

    public Job() {
        this.jobID = UUID.randomUUID().toString();
        this.dateCreated = LocalDateTime.now();
        this.status = JobStatus.PROCESS;
    }

    public enum JobStatus {
        PROCESS, DONE, MANUAL,;
    }
        @Override
    public String toString() {
        return "Job{" +
                "jobID='" + jobID + '\'' +
                ", dateCreated=" + dateCreated +
                ", status=" + status +
                ", filteredData=" + filteredData +
                '}';
    }
}
