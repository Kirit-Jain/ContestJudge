Live Coding Contest Platform

Shodhai is a full-stack web application that simulates a live coding contest. It's built with a Spring Boot backend, a React/Next.js frontend, and a custom code-judging engine. Users can join a contest, view a list of problems, submit solutions in Java, and see their results and ranking update in real-time on a live leaderboard.

Core Features
1. Multi-Contest Platform: The backend is designed to host multiple independent contests.
2. Multi-Problem Support: Each contest can contain any number of problems.
3. Live Code Judge: A Java-based judging service (using ProcessBuilder) compiles and runs user-submitted code.
4. Asynchronous Submissions: Submissions are processed asynchronously, allowing the frontend to remain responsive.
5. Real-Time Polling: The frontend polls for submission status and leaderboard updates, simulating a live event.
6. Correct Leaderboard: The leaderboard correctly calculates scores based on unique problems solved, not total submissions.

Setup Instructions
1. Clone the Repository
2. Run the Backend (Spring Boot)
        The backend server runs on http://localhost:8080.
        # Navigate to the backend folder
            cd backend
        # Run the Spring Boot application
            mvn spring-boot:run
   The server will start and pre-load the database with 4 contests.
3. Run the Frontend (Next.js)
   In a new terminal, navigate back to the root and start the frontend.
        # Navigate to the frontend folder
            cd ../frontend
        # Install dependencies
            npm install
        # Run the development server
            npm run dev
   The frontend will start on http://localhost:3000.
4. Access and Test the Application
   4.1. Open your browser and go to http://localhost:3000.
   4.2. You will see the "Join Contest" page. You can use any username.
   4.3. Enter one of the pre-loaded Contest IDs to join:
        contest1
        contest2

API Design
The backend provides a simple REST API for the frontend.
    GET /api/contests/{contestId}
        Fetches all details for a specific contest, including its list of problems.

        Response:
        {
        "id": "test-contest-1",
        "title": "My First Contest",
        "problems": [
            { "id": 1, "title": "Two Sum", "description": "...", "starterCode": "..." },
            { "id": 2, "title": "Add Two Numbers", "description": "...", "starterCode": "..." }
        ]
        }

    POST /api/submissions
        Submits code for judging. This is an asynchronous endpoint.

            Request:
            {
            "contestId": "test-contest-1",
            "problemId": "1",
            "username": "testuser",
            "code": "class Solution { ... }",
            "language": "java"
            }

            Response:
            { "submissionId": "uuid-1234-abcd-5678" }

    GET /api/submissions/{submissionId}
        Polls for the status of a specific submission.

        Response:
        {
        "id": "uuid-1234-abcd-5678",
        "status": "ACCEPTED",
        "result": "Accepted",
        "errorMessage": null
        }

    GET /api/contests/{contestId}/leaderboard
        Fetches the current leaderboard for a contest, sorted by score.

        Response:
        [
        { "username": "user1", "score": 200, "problemsSolved": 2 },
        { "username": "user2", "score": 100, "problemsSolved": 1 }
        ]

Design Choices & Justification
Backend Structure
1. Architecture: The backend follows a standard 3-tier architecture: Controllers (handling HTTP requests), Services (handling business logic), and Repositories (handling data access).
2. Asynchronous Judging: The CodeJudgeService is marked with @Async. When a user posts to /api/submissions, the SubmissionService saves the submission as "PENDING" and immediately returns a submissionId. This makes the API non-blocking and highly responsive. The async thread picks up the judging task in the background, which is the correct pattern for a long-running process (like compiling and running code).

Frontend State Management
1. Approach: For this application's scope, a complex global state library like Redux or Zustand was unnecessary. We used React's built-in hooks, which are simpler and more than sufficient.
2. useState: Used for local component state, such as the code in the editor (code), the currently selected problem (currentProblem), and the latest submission ID (submission).
3. useEffect: Used to fetch data on component mount (like fetchContst) and to manage asynchronous polling. The SubmissionStatus and Leaderboard components use a setTimeout/setInterval loop within useEffect to poll their respective endpoints. This creates the "live" feel of the event without the setup complexity of WebSockets.

Code Judge Engine & Docker Challenge
1. Initial Challenge: The original plan was to use a fully isolated Docker container for the judge. This presented significant challenges, including persistent IOException: The pipe is being closed errors. These errors stemmed from a tricky race condition between the Java ProcessBuilder (which was trying to write to stdin), the Docker container starting, the Java compiler (javac) running, and the Java runtime (java) finally attempting to read from stdin.
2. The Trade-off: To prioritize a functional and testable end-to-end solution for this submission, we pivoted to a non-Docker judge. This judge uses ProcessBuilder to call javac and java directly on the host machine. This allowed us to fix the race condition and deliver a working product.
3. Critical Acknowledgement: We fully acknowledge that this non-Dker approach has major security and stability vulnerabilities. A malicious user could submit code (while(true){}) to cause a 100% CPU lock (a Denial of Service attack) or use java.io.File to read/delete files from the server. In a production environment, this is unacceptable. The Docker-based approach, with its resource constraints (--memory, --cpus) and network/filesystem isolation (--network=none), is the correct and only professional path. For this submission, we chose a working, insecure implementation over a non-working, secure one to demonstrate the full end-to-end application flow.
