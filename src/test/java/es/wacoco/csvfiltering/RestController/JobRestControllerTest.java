package es.wacoco.csvfiltering.RestController;

import static org.junit.jupiter.api.Assertions.*;

import es.wacoco.csvfiltering.model.Job;
import es.wacoco.csvfiltering.service.JobService;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(JobRestController.class)
class JobRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @MockBean
    private ProducerTemplate template;

    @Test
    void testUploadCsv() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "text/csv", "content".getBytes());
        List<String> fields = Arrays.asList("field1", "field2");

        // Assuming the process returns a success message or object
        when(template.requestBodyAndHeader(anyString(), any(), anyString(), any())).thenReturn("Success");

        mockMvc.perform(multipart("/upload-csv")
                        .file(file)
                        .param("fields", String.join(",", fields)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Success")));

        verify(template, times(1)).requestBodyAndHeader(eq("direct:processCsv"), any(), eq("userFields"), any());

    }

    @Test
    void testGetAllJobs() throws Exception {
        // Initialize the Job object with required parameters
        LocalDateTime dateTime = LocalDateTime.now(); // Use current time for simplicity
        Job.Status status = Job.Status.DONE; // Example status
        List<Map<String, String>> filteredData = Collections.singletonList(new HashMap<>()); // Simplified empty data
        Job job = new Job("someJobID", dateTime, status, filteredData);

        List<Job> jobs = Collections.singletonList(job); // Add the Job object to a list

        // Configure mock behavior to return the initialized list of Jobs
        when(jobService.getAllJobs()).thenReturn(jobs);

        // Perform the test
        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].jobID", is("someJobID"))); // Check jobID matches expected value

        // Verify jobService.getAllJobs() was called once
        verify(jobService, times(1)).getAllJobs();
    }
}
