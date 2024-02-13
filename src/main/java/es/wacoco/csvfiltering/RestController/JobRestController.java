package es.wacoco.csvfiltering.RestController;

import es.wacoco.csvfiltering.model.Job;
import es.wacoco.csvfiltering.service.JobService;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@org.springframework.web.bind.annotation.RestController
public class JobRestController {
    private final JobService jobService;


    private final ProducerTemplate template;

    public JobRestController(JobService jobService, ProducerTemplate template) {
        this.jobService = jobService;
        this.template = template;
    }


    @PostMapping("/upload-csv")
    public Object uploadCsv(
           @RequestParam("file") MultipartFile file,@RequestParam("fields") List<String> fields) {

        return template.requestBodyAndHeader("direct:processCsv", file, "userFields", fields);
    }

    @GetMapping("/all")
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }
}

