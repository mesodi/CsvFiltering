package es.wacoco.csvfiltering.Camel.Proseccor;

import es.wacoco.csvfiltering.model.Applicant;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class EmailExtractorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        @SuppressWarnings("unchecked")
        List<Applicant> applicantsWithWebsites = exchange.getProperty("applicants", List.class);

        log.info("Starting Email extraction for {} applicants.", applicantsWithWebsites.size());

        for (Applicant applicant : applicantsWithWebsites) {
            for (String websiteUrl : applicant.getWebsiteUrl()) {
                try {
                    // Fetch the HTML content of the website
                    Document doc = Jsoup.connect(websiteUrl).get();
                    // Extract emails from the HTML content
                    List<String> emails = extractEmails(doc.text());
                    // Add extracted emails to the applicant
                    emails.forEach(applicant::addEmailAddress);

                    // Log found emails for the applicant
                    if (!emails.isEmpty()) {
                        log.info("Extracted emails for {}: {}", applicant.getName(), String.join(", ", emails));
                    }else {
                        log.info("Email not Found");
                    }
                } catch (IOException e) {
                    log.error("Error fetching emails from {}: {}", websiteUrl, e.getMessage(), e);
                }
            }
        }

        log.info("Completed Email extraction.");
    }

    private List<String> extractEmails(String text) {
        List<String> emails = new ArrayList<>();
        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            emails.add(matcher.group());
        }
        return emails;
    }
}