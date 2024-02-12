package es.wacoco.csvfiltering.Camel.Routes;


import es.wacoco.csvfiltering.Camel.Proceccor.FilterCsvProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


 @Component
public class CsvProcessingRoute extends RouteBuilder {

     @Override
     public void configure() throws Exception {
         from("direct:processCsv")
                 .process(new FilterCsvProcessor())
                 .to("log:filteredResults?showBody=true&showHeaders=true");
     }
}