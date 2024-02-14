package es.wacoco.csvfiltering.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Applicant {
    private String name;
    private List<String> websiteUrls = new ArrayList<>();
    public Applicant(String name) {
        this.name = name;
    }

    public void addWebsiteUrl(String websiteUrl) {
        this.websiteUrls.add(websiteUrl);
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "name='" + name + '\'' +
                '}';
    }
}