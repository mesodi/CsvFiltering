package es.wacoco.csvfiltering.Camel.Proceccor;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import es.wacoco.csvfiltering.model.Applicant;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.web.multipart.MultipartFile;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStreamReader;

@Slf4j
public class ApplicantExtractorProcessor implements Processor {
    private static final String API_KEY = "GOOGLE CUSTOM API SEARCH SECRET KEY";
    private static final String CX = "Search Engine ID";
    @Override
    public void process(Exchange exchange) throws Exception {
        MultipartFile file = exchange.getIn().getBody(MultipartFile.class);
        List<Applicant> applicants = new ArrayList<>();

        try (CSVParser parser = new CSVParser(new InputStreamReader(file.getInputStream()), CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            for (CSVRecord record : parser) {
                if (record.isMapped("Applicants") && record.get("Applicants") != null) {
                    Applicant applicant = new Applicant(record.get("Applicants"));

                    List<String> linkedinUrls = fetchSearchResultUrls(applicant.getName() + " \"LinkedIn\"");

                    String companyUrl = null;
                    String nonCompanyUrl = null;
                    for (String url : linkedinUrls) {
                        if (url.contains("linkedin.com/company")) {
                            if (companyUrl == null) companyUrl = url;
                        } else if (url.contains("linkedin.com/in") || url.contains("linkedin.com/pub")) {
                            if (nonCompanyUrl == null) nonCompanyUrl = url;
                        }
                    }

                    if (nonCompanyUrl != null) {
                        applicant.addLinkedinUrl(nonCompanyUrl);
                        log.info("{} - Non-Company LinkedIn URL: {}", applicant.getName(), nonCompanyUrl);
                    }

                    if (companyUrl != null) {
                        applicant.addLinkedinUrl(companyUrl);
                        log.info("{} - Company LinkedIn URL: {}", applicant.getName(), companyUrl);
                    }

                    applicants.add(applicant);
                }
            }
        }

        exchange.setProperty("applicants", applicants);

        applicants.forEach(applicant -> log.info("Applicant: {} - LinkedIn URLs: {}", applicant.getName(), applicant.getLinkedinUrl()));
    }

    private List<String> fetchSearchResultUrls(String query) throws Exception {
        List<String> urls = new ArrayList<>();
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String urlString = "https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&cx=" + CX + "&q=" + encodedQuery;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
            JsonArray items = jsonObject.getAsJsonArray("items");

            for (JsonElement itemElement : items) {
                JsonObject itemObject = itemElement.getAsJsonObject();
                String link = itemObject.get("link").getAsString();
                if (link.contains("linkedin.com")) {
                    urls.add(link);
                }
            }
        }
        return urls;
    }
}