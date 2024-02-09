package es.wacoco.csvfiltering.Controller;

import es.wacoco.csvfiltering.Filter.FilterCsv;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final FilterCsv filterCsv;

    public RestController(FilterCsv filterCsv) {
        this.filterCsv = filterCsv;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<?> uploadCsvAndFilter(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "fields", required = false) String fields) {
        List<String> userFields = (fields == null || fields.isEmpty()) ? List.of() : Arrays.asList(fields.split(","));
        List<Map<String, String>> filteredData = filterCsv.filterCsvFields(file, userFields);
        return ResponseEntity.ok(filteredData);
    }
}
