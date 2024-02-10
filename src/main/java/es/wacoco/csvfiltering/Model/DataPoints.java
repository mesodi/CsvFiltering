package es.wacoco.csvfiltering.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DataPoints {
    private String jurisdiction;
    private int publication_Year;
    private int application_Number;
    private String title;
    private String description;
    private String applicants;
    private String inventors;
    private String cpc;
    private String owner;
    public DataPoints() {

    }

}
