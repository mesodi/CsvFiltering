package es.wacoco.csvfiltering.Camel.Routes;


import es.wacoco.csvfiltering.Camel.Proceccor.FilterCsvProcessor;
import es.wacoco.csvfiltering.model.Job;
import es.wacoco.csvfiltering.service.JobService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
                .process(exchange -> {
                    LocalDateTime now = LocalDateTime.now();
                    String jobId = jobService.createJobID(now);
                    Job job = new Job(jobId, now, Job.Status.PROCESSING, null);
                    exchange.getIn().setHeader("job", job);
                })
                .process(new FilterCsvProcessor())
                .process(exchange -> {
                    Job job = exchange.getIn().getHeader("job", Job.class);
                    List<Map<String, String>> filteredResults = exchange.getIn().getBody(List.class);
                    job.setFilteredData(filteredResults);
                    job.setCurrentStatus(Job.Status.DONE);
                    exchange.getIn().setBody(job); // Set job as the body if you want to use it downstream
                })
                .to("log:filteredResults?showBody=true&showHeaders=true");
    }
}