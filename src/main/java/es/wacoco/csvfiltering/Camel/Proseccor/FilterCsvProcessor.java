package es.wacoco.csvfiltering.Camel.Proseccor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.*;


@Slf4j
public class FilterCsvProcessor implements Processor {
    @Override
    public void process(Exchange exchange){
        MultipartFile file = exchange.getIn().getBody(MultipartFile.class);
        @SuppressWarnings("unchecked")
        List<String> userFields = exchange.getIn().getHeader("userFields", List.class);

        List<Map<String, String>> filteredResults;
        try {
            filteredResults = filterCsvFields(file, userFields);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        exchange.getIn().setBody(filteredResults);
    }
    private List<Map<String, String>> filterCsvFields(MultipartFile file, List<String> userFields) throws Exception {
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
        }
        return filteredResults;
    }
}