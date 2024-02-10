package es.wacoco.csvfiltering.Controller;

import es.wacoco.csvfiltering.Filter.FilterCsv;
import es.wacoco.csvfiltering.model.Job;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final FilterCsv filterCsvService;

    public RestController(FilterCsv filterCsv) {
        this.filterCsvService = filterCsv;
    }

    @PostMapping("/upload-csv")
    public Job uploadAndFilterCsv(@RequestParam("file") MultipartFile file,
                                  @RequestParam("fields") String[] fields) {
        List<String> userFields = Arrays.asList(fields);
        return filterCsvService.filterCsvFields(file, userFields);
    }
}
