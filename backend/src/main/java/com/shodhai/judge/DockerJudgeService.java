package com.shodhai.judge;

import com.shodhai.model.Submission;
import com.shodhai.model.SubmissionStatus;
import com.shodhai.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

@Service
public class DockerJudgeService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Async
    public void judgeSubmission(Submission submission) {
        Path tempDir = null;
        try {
            submission.setStatus(SubmissionStatus.RUNNING);
            submissionRepository.save(submission);

            tempDir = Files.createTempDirectory("judge-no-docker");
            Path codeFile = tempDir.resolve("Solution.java");
            Files.writeString(codeFile, submission.getCode());

            String testInput = submission.getProblem().getTestCases().get(0).getInput();
            String expectedOutput = submission.getProblem().getTestCases().get(0).getExpectedOutput();

            ProcessBuilder compilePb = new ProcessBuilder(
                    "javac", codeFile.toString());

            Process compileProcess = compilePb.start();
            boolean compiled = compileProcess.waitFor(5, TimeUnit.SECONDS);

            if (!compiled || compileProcess.exitValue() != 0) {
                String error = new String(compileProcess.getErrorStream().readAllBytes());
                submission.setStatus(SubmissionStatus.COMPILATION_ERROR);
                submission.setErrorMessage(error);
                submissionRepository.save(submission);
                return; 
            }

            ProcessBuilder runPb = new ProcessBuilder(
                    "java", "-cp", tempDir.toString(), "Solution");

            Process runProcess = runPb.start();

            try (OutputStream stdin = runProcess.getOutputStream()) {
                stdin.write(testInput.getBytes());
                stdin.flush();
            } catch (IOException e) {
                String error = new String(runProcess.getErrorStream().readAllBytes());
                submission.setStatus(SubmissionStatus.RUNTIME_ERROR);
                submission.setErrorMessage("Crash on start: " + e.getMessage() + "\n" + error);
                submissionRepository.save(submission);
                return;
            }

            boolean completed = runProcess.waitFor(10, TimeUnit.SECONDS);

            if (!completed) {
                runProcess.destroy();
                submission.setStatus(SubmissionStatus.TIME_LIMIT_EXCEEDED);
                submission.setResult("Time Limit Exceeded");
            } else {
                String output = new String(runProcess.getInputStream().readAllBytes()).trim();
                String error = new String(runProcess.getErrorStream().readAllBytes());

                if (runProcess.exitValue() != 0) {
                    submission.setStatus(SubmissionStatus.RUNTIME_ERROR);
                    submission.setErrorMessage(error);
                } else {
                    boolean passed = output.equals(expectedOutput.trim());
                    submission.setStatus(passed ? SubmissionStatus.ACCEPTED : SubmissionStatus.WRONG_ANSWER);
                    submission.setResult(
                            passed ? "Accepted" : "Wrong Answer. Expected: " + expectedOutput + ", Got: " + output);
                }
            }

        } catch (Exception e) {
            submission.setStatus(SubmissionStatus.RUNTIME_ERROR);
            submission.setErrorMessage("Judge Service Error: " + e.getMessage());
        } finally {
            submissionRepository.save(submission);

            if (tempDir != null) {
                try {
                    Files.walk(tempDir)
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                } catch (IOException e) {
                    System.err.println("Failed to delete temp directory: " + e.getMessage());
                }
            }
        }
    }
}