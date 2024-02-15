package es.wacoco.csvfiltering.Camel.Proseccor;

import es.wacoco.csvfiltering.model.Applicant;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import java.util.Arrays;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
@Slf4j
public class WebsiteUrlFinderProcessor implements Processor {

    private static final String API_KEY = "GOOGLE CUSTOM API SEARCH SECRET KEY";
    private static final String CX = "Search Engine ID";


    @Override
    public void process(Exchange exchange) {
        @SuppressWarnings("unchecked")
        List<Applicant> applicants = exchange.getProperty("applicants", List.class);

        log.info("Starting Website URL fetching for {} applicants.", applicants.size());

        for (Applicant applicant : applicants) {
            try {
                List<String> websiteUrls = fetchSearchResultUrls(applicant.getName() + " company website");
                String websiteUrl = filterWebsiteUrls(websiteUrls);
                if (websiteUrl != null) {
                    applicant.addWebsiteUrl(websiteUrl);
                    log.info("Website URL for {}: {}", applicant.getName(), websiteUrl);
                }
            } catch (Exception e) {
                log.error("Error fetching Website URL for applicant {}: {}", applicant.getName(), e.getMessage(), e);
            }
        }

        log.info("Completed Website URL fetching.");
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
                    urls.add(link);
                }
            }
        } catch (Exception e) {
            log.error("Error fetching search result URLs: {}", e.getMessage(), e);
        }
        return urls;
    }

    private String filterWebsiteUrls(List<String> urls) {
        List<String> excludedDomains = Arrays.asList("linkedin.com", "tiktok.com", "wikipedia.org", "amazon.com", "ads");
        for (String url : urls) {
            boolean excluded = excludedDomains.stream().anyMatch(url::contains);
            if (!excluded) {
                return url;
            }
        }
        return null;
    }
}