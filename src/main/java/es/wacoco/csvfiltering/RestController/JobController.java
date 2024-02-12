package es.wacoco.csvfiltering.RestController;

import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@org.springframework.web.bind.annotation.RestController
public class JobController {

    private final ProducerTemplate template;

    public JobController(ProducerTemplate template) {
        this.template = template;
    }


    @PostMapping("/upload-csv")
    public Object uploadCsv(
           @RequestParam("file") MultipartFile file,@RequestParam("fields") List<String> fields) {

        return template.requestBodyAndHeader("direct:processCsv", file, "userFields", fields);
    }
}

