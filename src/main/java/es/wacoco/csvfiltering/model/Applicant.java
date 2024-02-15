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
    private List<String> linkedinUrl = new ArrayList<>();
    private List<String> websiteUrl = new ArrayList<>();
    public Applicant(String name) {
        this.name = name;
    }

    public void addLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl.add(linkedinUrl);
    }
    @Override
    public String toString() {
        return "Applicant{" +
                "name='" + name + '\'' +
                '}';
    }

    public void addWebsiteUrl(String websiteUrl) {
        this.websiteUrl.add(websiteUrl);
    }
}