package es.wacoco.csvfiltering.Camel.Routes;


import es.wacoco.csvfiltering.Camel.Proceccor.ApplicantExtractorProcessor;
import es.wacoco.csvfiltering.Camel.Proceccor.FilterCsvProcessor;
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

                .process(exchange -> {
                    LocalDateTime now = LocalDateTime.now();
                    String jobId = jobService.createJobID(now);
                    Job job = new Job(jobId, now, Job.Status.PROCESSING, new ArrayList<>());
                    jobService.newJob(job);
                    //save id for later
                    exchange.getIn().setHeader("jobId", jobId);
                })
                .process(new FilterCsvProcessor())
                .process(exchange -> {
                    //get the id
                    String jobId = exchange.getIn().getHeader("jobId", String.class);
                    Job job = jobService.getJob(jobId);
                    @SuppressWarnings("unchecked")
                    List<Map<String, String>> filteredResults = exchange.getIn().getBody(List.class);
                    job.setFilteredData(filteredResults);
                    job.setCurrentStatus(Job.Status.DONE);

                    jobService.updateJob(job);
                    exchange.getIn().setBody(job);
                })
                .to("log:filteredResults?showBody=true&showHeaders=true");
    }
}