package es.wacoco.csvfiltering.Camel.Proseccor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import es.wacoco.csvfiltering.model.Applicant;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LinkedInUrlFinderProcessor implements Processor {

    private static final String API_KEY = "GOOGLE CUSTOM API SEARCH SECRET KEY";
    private static final String CX = "Search Engine ID";

    @Override
    public void process(Exchange exchange) throws Exception {
        @SuppressWarnings("unchecked")
        List<Applicant> applicants = exchange.getProperty("applicants", List.class);

        for (Applicant applicant : applicants) {
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
            }

            if (companyUrl != null) {
                applicant.addLinkedinUrl(companyUrl);
            }
        }

        applicants.forEach(applicant -> log.info("Applicant: " + applicant.getName() + " - LinkedIn URLs: " + applicant.getLinkedinUrl()));
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