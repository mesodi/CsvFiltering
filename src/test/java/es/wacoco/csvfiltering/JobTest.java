package es.wacoco.csvfiltering;

import es.wacoco.csvfiltering.model.Job;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JobTest {
   @Test
    public void TestJobAttributes(){
       String jobID="123";
       LocalDateTime dateCreate= LocalDateTime.now();
       Job.Status status= Job.Status.PROCESSING;
       Job job = new Job(jobID,dateCreate,status);
       assertEquals(jobID,job.getJobID());
       assertEquals(dateCreate,job.getDateCreated());
       assertEquals(status,job.getStatus());


    }
}

