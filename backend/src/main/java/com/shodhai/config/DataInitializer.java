package com.shodhai.config;

import com.shodhai.model.*;
import com.shodhai.repository.ContestRepository;
import com.shodhai.repository.ProblemRepository;
import com.shodhai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no contests exist
        if (contestRepository.count() == 0) {
            initializeSampleData();
        }
    }

    private void initializeSampleData() {
        System.out.println("Initializing sample data...");

        // Create sample contest
        Contest contest = new Contest();
        contest.setId("contest1");
        contest.setTitle("Beginner Challenge");
        contest.setDescription("Perfect for beginners");
        contest.setStartTime(LocalDateTime.now());
        contest.setEndTime(LocalDateTime.now().plusDays(1));

        // Create sample problem
        Problem problem = new Problem();
        problem.setTitle("Sum of Two Numbers");
        problem.setDescription("Write a program that takes two integers as input and returns their sum.");
        problem.setStarterCode(
            "import java.util.Scanner;\n\n" +
            "public class Solution {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Your code here\n" +
            "    }\n" +
            "}"
        );

        problem.addTestCase(new TestCase("-5 8", "3", true));
        problem.addTestCase(new TestCase("0 0", "0", false));
        problem.addTestCase(new TestCase("100 250", "350", false));

        Problem problem2 = new Problem();
        problem2.setTitle("Valid Parenthesis");
        problem2.setDescription("Check if the given string of parentheses is valid.");
        problem2.setStarterCode(
            "import java.util.Scanner;\n\n" +
            "public class Solution {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Your code here\n" +
            "    }\n" +
            "}"
        );

        problem2.addTestCase(new TestCase("()", "true", true));
        problem2.addTestCase(new TestCase("((()))", "true", false));
        problem2.addTestCase(new TestCase("(()", "false", false));

        Problem problem3 = new Problem();
        problem3.setTitle("Reverse String");
        problem3.setDescription("Reverse the given string.");
        problem3.setStarterCode(
            "import java.util.Scanner;\n\n" +
            "public class Solution {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Your code here\n" +
            "    }\n" +
            "}"
        );

        problem3.addTestCase(new TestCase("hello", "olleh", true));
        problem3.addTestCase(new TestCase("Shodhai", "iahdohS", false));
        problem3.addTestCase(new TestCase("Java", "avaJ", false));

        contest.getProblems().add(problem);
        contest.getProblems().add(problem2);
        contest.getProblems().add(problem3);

        contestRepository.save(contest);

        Contest contest2 = new Contest();
        contest2.setId("contest2");
        contest2.setTitle("Beginner String Challenge");
        contest2.setDescription("String manipulation problems for beginners");
        contest2.setStartTime(LocalDateTime.now());
        contest2.setEndTime(LocalDateTime.now().plusDays(1));

        Problem p1c2 = new Problem();
        p1c2.setTitle("Say Hello");
        p1c2.setDescription("Write a program that prints 'Hello, <Name>'.");
        p1c2.setStarterCode(
            "import java.util.Scanner;\n\n" +
            "public class Solution {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Your code here\n" +
            "    }\n" +
            "}"
        );

        p1c2.addTestCase(new TestCase("Alice", "Hello, Alice", true));
        p1c2.addTestCase(new TestCase("Bob", "Hello, Bob", false));

        Problem p2c2 = new Problem();
        p2c2.setTitle("Count Vowels");
        p2c2.setDescription("Count the number of vowels in a given string.");
        p2c2.setStarterCode(
            "import java.util.Scanner;\n\n" +
            "public class Solution {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Your code here\n" +
            "    }\n" +
            "}"
        );

        p2c2.addTestCase(new TestCase("hello", "2", true));
        p2c2.addTestCase(new TestCase("Shodhai", "3", false));
        p2c2.addTestCase(new TestCase("Java Programming", "5", false));

        Problem p3c2 = new Problem();
        p3c2.setTitle("Palindrome Check");
        p3c2.setDescription("Check if a given string is a palindrome.");
        p3c2.setStarterCode(
            "import java.util.Scanner;\n\n" +
            "public class Solution {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Your code here\n" +
            "    }\n" +
            "}"
        );

        p3c2.addTestCase(new TestCase("madam", "true", true));
        p3c2.addTestCase(new TestCase("hello", "false", false));
        p3c2.addTestCase(new TestCase("racecar", "true", false));
        
        contest2.getProblems().add(p1c2);
        contest2.getProblems().add(p2c2);
        contest2.getProblems().add(p3c2);

        contestRepository.save(contest2);

        problemRepository.saveAll(Arrays.asList(problem, problem2, problem3, p1c2, p2c2, p3c2));
        
        System.out.println("Sample data initialized successfully!");
    }
}