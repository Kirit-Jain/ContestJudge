package com.shodhai.controller;

import com.shodhai.model.Contest;
import com.shodhai.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contests")
@CrossOrigin(origins = "http://localhost:3000")
public class ContestController {
    
    @Autowired
    private ContestService contestService;
    
    @GetMapping("/{contestId}")
    public ResponseEntity<Contest> getContest(@PathVariable String contestId) {
        return contestService.getContest(contestId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{contestId}/leaderboard")
    public List<Object> getLeaderboard(@PathVariable String contestId) {
        return List.copyOf(contestService.getLeaderboard(contestId));
    }
}