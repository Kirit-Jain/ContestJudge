package com.shodhai.service;

import com.shodhai.model.*;
import com.shodhai.repository.ContestRepository;
import com.shodhai.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    public Optional<Contest> getContest(String contestId) {
        return contestRepository.findById(contestId);
    }

    public List<LeaderboardEntry> getLeaderboard(String contestId) {
        List<Submission> submissions = submissionRepository.findByContestIdOrderBySubmittedAtAsc(contestId);

        Map<String, LeaderboardEntry> userScores = new HashMap<>();

        Map<String, Set<Long>> solvedProblemsPerUser = new HashMap<>();

        for (Submission submission : submissions) {
            if (submission.getStatus() != SubmissionStatus.ACCEPTED) {
                continue;
            }

            String username = submission.getUser().getUsername();
            Long problemId = submission.getProblem().getId();

            LeaderboardEntry entry = userScores.getOrDefault(username,
                    new LeaderboardEntry(username, 0, 0));
            Set<Long> solvedProblems = solvedProblemsPerUser.getOrDefault(username, new HashSet<>());

            if (!solvedProblems.contains(problemId)) {
                solvedProblems.add(problemId);
                entry.setScore(entry.getScore() + 100); 
                entry.setProblemsSolved(entry.getProblemsSolved() + 1);

                userScores.put(username, entry);
                solvedProblemsPerUser.put(username, solvedProblems);
            }
        }

        return userScores.values().stream()
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());
    }
}

class LeaderboardEntry {
    private String username;
    private int score;
    private int problemsSolved;

    public LeaderboardEntry(String username, int score, int problemsSolved) {
        this.username = username;
        this.score = score;
        this.problemsSolved = problemsSolved;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getProblemsSolved() {
        return problemsSolved;
    }

    public void setProblemsSolved(int problemsSolved) {
        this.problemsSolved = problemsSolved;
    }
}