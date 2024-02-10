package es.wacoco.csvfiltering.filter;

import es.wacoco.csvfiltering.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.*;


@Slf4j
@Service

public class FilterCsv {

    public Job filterCsvFields(MultipartFile file, List<String> userFields) {
        Job job = new Job();
        job.setStatus(Job.JobStatus.PROCESS);

        try {

            Thread.sleep(5000);

            try (CSVParser parser = new CSVParser(new InputStreamReader(file.getInputStream()),
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
                List<Map<String, String>> filteredResults = new ArrayList<>();
                for (CSVRecord record : parser) {
                    Map<String, String> dataMap = new HashMap<>();
                    if (userFields.isEmpty()) {
                        dataMap.putAll(record.toMap());
                    } else {
                        for (String field : userFields) {
                            if (record.isMapped(field) && record.get(field) != null) {
                                dataMap.put(field, record.get(field));
                            }
                        }
                    }
                    if (!dataMap.isEmpty()) {
                        filteredResults.add(dataMap);
                    }
                }
                job.setFilteredData(filteredResults);
            }
            job.setStatus(Job.JobStatus.DONE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            job.setStatus(Job.JobStatus.MANUAL);
        } catch (Exception e) {
            log.error("Error processing CSV file", e);
            job.setStatus(Job.JobStatus.MANUAL);
        }
        return job;
    }



}
