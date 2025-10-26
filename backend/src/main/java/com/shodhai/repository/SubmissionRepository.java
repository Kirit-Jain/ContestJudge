package com.shodhai.repository;

import com.shodhai.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
    List<Submission> findByContestIdOrderBySubmittedAtDesc(String contestId);
    List<Submission> findByContestIdAndUserUsername(String contestId, String username);
    List<Submission> findByContestIdOrderBySubmittedAtAsc(String contestId);
}