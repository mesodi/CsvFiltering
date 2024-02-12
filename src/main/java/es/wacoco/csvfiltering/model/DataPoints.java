package es.wacoco.csvfiltering.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DataPoints {
    private int application_Number;
    private String title;
    private String Abstract;
    private String jurisdiction;
    private String applicants;
    private String inventors;
    private int publication_Date;
    public DataPoints() {

    }

}
