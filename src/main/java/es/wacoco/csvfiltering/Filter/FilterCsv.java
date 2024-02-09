package es.wacoco.csvfiltering.Filter;

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

    public List<Map<String, String>> filterCsvFields(MultipartFile file, List<String> userFields) {
        List<Map<String, String>> filteredResults = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new InputStreamReader(file.getInputStream()),
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

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
        } catch (Exception e) {
            log.error("Error processing CSV file", e);
        }
        return filteredResults;
    }

}
