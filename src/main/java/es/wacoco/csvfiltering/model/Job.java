package es.wacoco.csvfiltering.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Setter
@Getter
public class Job {

    private String jobID;
    private String dateCreated;
    private Status currentStatus;
    private List<Map<String, String>> filteredData;


    public Job() {
    }

    public Job(String jobID, LocalDateTime dateTime, Status currentStatus, List<Map<String, String>> filteredData) {
        this.jobID = jobID;
        this.dateCreated = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm"));
        this.currentStatus = currentStatus;
        this.filteredData = filteredData;
    }
    public enum Status {
        PROCESSING,
        MANUAL,
        DONE
    }
}
