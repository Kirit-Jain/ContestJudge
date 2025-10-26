package com.shodhai.controller;

import com.shodhai.model.Submission;
import com.shodhai.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.shodhai.service.SubmissionService.SubmissionRequest;
import com.shodhai.service.SubmissionService.SubmissionResponse;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "http://localhost:3000")
public class SubmissionController {
    
    @Autowired
    private SubmissionService submissionService;
    
    @PostMapping
    public SubmissionResponse submitCode(@RequestBody SubmissionRequest request) {
        return submissionService.submitCode(request);
    }
    
    @GetMapping("/{submissionId}")
    public Submission getSubmission(@PathVariable String submissionId) {
        return submissionService.getSubmission(submissionId);
    }
}