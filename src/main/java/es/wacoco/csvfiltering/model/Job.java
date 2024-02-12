package es.wacoco.csvfiltering.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Setter
@Getter
@AllArgsConstructor
public class Job {

    private String jobID;
    private LocalDateTime dateCreated;
    private Status currentStatus;
    private List<Map<String, String>> filteredData;

    public Job() {
    }

    public enum Status {
        PROCESSING,
        MANUAL,
        DONE
    }
}
