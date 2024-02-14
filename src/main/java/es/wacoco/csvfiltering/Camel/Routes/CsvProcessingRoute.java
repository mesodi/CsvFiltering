package es.wacoco.csvfiltering.Camel.Routes;


import es.wacoco.csvfiltering.Camel.Proseccor.ApplicantExtractorProcessor;
import es.wacoco.csvfiltering.Camel.Proseccor.FilterCsvProcessor;
import es.wacoco.csvfiltering.Camel.Proseccor.LinkedInUrlFinderProcessor;
import es.wacoco.csvfiltering.model.Applicant;
import es.wacoco.csvfiltering.model.Job;
import es.wacoco.csvfiltering.service.JobService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class CsvProcessingRoute extends RouteBuilder {
    private final JobService jobService;

    public CsvProcessingRoute(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public void configure() {
        from("direct:processCsv")
                .process(new ApplicantExtractorProcessor())
                .process(new LinkedInUrlFinderProcessor())

                .process(exchange -> {
                    LocalDateTime now = LocalDateTime.now();
                    String jobId = jobService.createJobID(now);
                    Job job = new Job(jobId, now, Job.Status.PROCESSING, new ArrayList<>());
                    jobService.newJob(job);
                    exchange.getIn().setHeader("jobId", jobId);
                })

                .process(new FilterCsvProcessor())

                .process(exchange -> {
                    @SuppressWarnings("unchecked")
                    List<Applicant> applicants = exchange.getProperty("applicants", List.class);
                    @SuppressWarnings("unchecked")
                    List<Map<String, String>> filteredResults = exchange.getIn().getBody(List.class);

                    for (Map<String, String> result : filteredResults) {
                        String applicantName = result.get("Applicants");

                        Applicant matchingApplicant = applicants.stream()
                                .filter(applicant -> applicant.getName().equals(applicantName))
                                .findFirst()
                                .orElse(null);

                        if (matchingApplicant != null && !matchingApplicant.getLinkedinUrl().isEmpty()) {
                            result.put("LinkedIn URL 1", matchingApplicant.getLinkedinUrl().get(0));
                            if (matchingApplicant.getLinkedinUrl().size() > 1) {
                                result.put("LinkedIn URL 2", matchingApplicant.getLinkedinUrl().get(1));
                            }
                        }
                    }
                    exchange.getIn().setBody(filteredResults);
                })

                .process(exchange -> {
                    String jobId = exchange.getIn().getHeader("jobId", String.class);
                    Job job = jobService.getJob(jobId);
                    @SuppressWarnings("unchecked")
                    List<Map<String, String>> filteredResults = exchange.getIn().getBody(List.class);
                    job.setFilteredData(filteredResults);
                    job.setCurrentStatus(Job.Status.DONE);
                    jobService.updateJob(job);
                })
                .to("log:filteredResults?showBody=true&showHeaders=true");
    }

}