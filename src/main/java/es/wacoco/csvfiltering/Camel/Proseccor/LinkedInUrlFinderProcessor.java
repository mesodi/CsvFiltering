package es.wacoco.csvfiltering.Camel.Proseccor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import es.wacoco.csvfiltering.model.Applicant;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LinkedInUrlFinderProcessor implements Processor {

    private static final String API_KEY = "GOOGLE CUSTOM API SEARCH SECRET KEY";
    private static final String CX = "Search Engine ID";

    @Override
    public void process(Exchange exchange){
        @SuppressWarnings("unchecked")
        List<Applicant> applicants = exchange.getProperty("applicants", List.class);

        log.info("Starting LinkedIn URL fetching for {} applicants.", applicants.size());

        for (Applicant applicant : applicants) {
            try {
                List<String> linkedinUrls = fetchSearchResultUrls("site:linkedin.com " + applicant.getName());

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
                    log.info("Non-Company LinkedIn URL for {}: {}", applicant.getName(), nonCompanyUrl);
                }

                if (companyUrl != null) {
                    applicant.addLinkedinUrl(companyUrl);
                    log.info("Company LinkedIn URL for {}: {}", applicant.getName(), companyUrl);
                }

            } catch (Exception e) {
                log.error("Error fetching LinkedIn URLs for applicant {}: {}", applicant.getName(), e.getMessage(), e);
            }
        }

        log.info("Completed LinkedIn URL fetching.");
    }

    private List<String> fetchSearchResultUrls(String query) {
        List<String> urls = new ArrayList<>();
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String urlString = "https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&cx=" + CX + "&q=" + encodedQuery;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
            JsonArray items = jsonObject.getAsJsonArray("items");

            if (items != null) {
                for (JsonElement itemElement : items) {
                    JsonObject itemObject = itemElement.getAsJsonObject();
                    String link = itemObject.get("link").getAsString();
                    if (link.contains("linkedin.com")) {
                        urls.add(link);
                    }
                }
            } else {
                log.warn("No LinkedIn URLs found for the query: {}", query);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL: " + urlString, e);
        } catch (IOException e) {
            throw new RuntimeException("IO Exception during HTTP request to: " + urlString, e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }
        return urls;
    }
}