package com.shodhai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Submission {
    @Id
    private String id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Contest contest;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Problem problem;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private User user;
    
    @Column(columnDefinition = "TEXT")
    private String code;
    private String language;
    
    @Enumerated(EnumType.STRING)
    public SubmissionStatus status;
    
    private String result;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    private LocalDateTime submittedAt;
    private Long executionTime;
    
    public Submission() {
        this.submittedAt = LocalDateTime.now();
        this.status = SubmissionStatus.PENDING;
    }
    
    public Submission(String id, Contest contest, Problem problem, User user, String code) {
        this();
        this.id = id;
        this.contest = contest;
        this.problem = problem;
        this.user = user;
        this.code = code;
        this.language = "java";
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Contest getContest() { return contest; }
    public void setContest(Contest contest) { this.contest = contest; }
    
    public Problem getProblem() { return problem; }
    public void setProblem(Problem problem) { this.problem = problem; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }
    
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    
    public Long getExecutionTime() { return executionTime; }
    public void setExecutionTime(Long executionTime) { this.executionTime = executionTime; }
}