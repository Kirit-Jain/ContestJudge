package com.shodhai.service;

import com.shodhai.judge.DockerJudgeService;
import com.shodhai.model.*;
import com.shodhai.repository.ContestRepository;
import com.shodhai.repository.SubmissionRepository;
import com.shodhai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class SubmissionService {
    
    public static record SubmissionRequest(String contestId, String problemId, String username, 
                                           String code, String language) {}

    public static record SubmissionResponse(String submissionId) {}
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Autowired
    private ContestRepository contestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DockerJudgeService judgeService;
    
    public SubmissionResponse submitCode(SubmissionRequest request) {
        User user = userRepository.findById(request.username())
                .orElse(new User(request.username(), request.username()));
        userRepository.save(user);
        
        Contest contest = contestRepository.findById(request.contestId())
                .orElseThrow(() -> new RuntimeException("Contest not found"));
        
        Problem problem = contest.getProblems().stream()
                .filter(p -> p.getId().equals(Long.parseLong(request.problemId())))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        
        String submissionId = UUID.randomUUID().toString();
        Submission submission = new Submission(submissionId, contest, problem, user, request.code());
        submissionRepository.save(submission);
        
        judgeService.judgeSubmission(submission);
        
        return new SubmissionResponse(submissionId);
    }
    
    public Submission getSubmission(String submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }
}