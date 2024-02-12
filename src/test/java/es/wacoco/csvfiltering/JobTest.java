package es.wacoco.csvfiltering;

import es.wacoco.csvfiltering.model.Job;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JobTest {
    @Test
    public void testJobConstructor(){
        Job job=new Job("12345","2024-02-12","In progress");
        assertEquals("12345",job.getJobID());
        assertEquals("2024-02-12",job.getDateCreated());
        assertEquals("In progress",job.getCurrentStatus());
    }

}

