package com.shodhai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Contest {
    @Id
    private String id;
    
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Problem> problems = new ArrayList<>();
    
    @OneToMany(mappedBy = "contest")
    @JsonManagedReference
    private List<Submission> submissions = new ArrayList<>();
    
    public Contest() {}
    
    public Contest(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now().plusDays(1);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public List<Problem> getProblems() { return problems; }
    public void setProblems(List<Problem> problems) { this.problems = problems; }
    
    public List<Submission> getSubmissions() { return submissions; }
    public void setSubmissions(List<Submission> submissions) { this.submissions = submissions; }
}