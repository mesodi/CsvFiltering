package es.wacoco.csvfiltering.Camel.Proseccor;

import es.wacoco.csvfiltering.model.Applicant;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStreamReader;

public class ApplicantExtractorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        MultipartFile file = exchange.getIn().getBody(MultipartFile.class);
        List<Applicant> applicants = new ArrayList<>();

        try (CSVParser parser = new CSVParser(new InputStreamReader(file.getInputStream()), CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            for (CSVRecord record : parser) {
                if (record.isMapped("Applicants") && record.get("Applicants") != null) {
                    applicants.add(new Applicant(record.get("Applicants")));
                }
            }
        }

        exchange.setProperty("applicants", applicants);
    }
}