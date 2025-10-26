import { useState, useEffect } from "react";
import { useRouter } from "next/router";
import CodeEditor from "../../components/CodeEditor";
import Leaderboard from "../../components/Leaderboard";
import SubmissionStatus from "../../components/SubmissionStatus";

export default function ContestPage() {
  const router = useRouter();
  const { contestId, username } = router.query;
  const [contest, setContest] = useState(null);
  const [currentProblem, setCurrentProblem] = useState(null);
  const [code, setCode] = useState("");
  const [submission, setSubmission] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (contestId) {
      fetchContest();
    }
  }, [contestId]);

  const fetchContest = async () => {
    setLoading(true);
    try {
      const res = await fetch(
        `http://localhost:8080/api/contests/${contestId}`
      );
      if (!res.ok) {
        throw new Error(`Failed to fetch contest: ${res.status}`);
      }
      const data = await res.json();
      setContest(data);

      if (data.problems && data.problems.length > 0) {
        handleProblemSelect(data.problems[0]);
      }
    } catch (error) {
      console.error("Error fetching contest:", error);
      alert("Failed to load contest: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleProblemSelect = (problem) => {
    setCurrentProblem(problem);
    setCode(
      problem.starterCode || "class Solution {\n    // Your code here\n}"
    );
    setSubmission(null);
  };

  const handleSubmit = async () => {
    if (!currentProblem) {
      alert("No problem selected");
      return;
    }
    setLoading(true);
    try {
      const submissionData = {
        contestId,
        problemId: currentProblem.id.toString(),
        username,
        code,
        language: "java",
      };

      const res = await fetch("http://localhost:8080/api/submissions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(submissionData),
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(`Submission failed: ${res.status} - ${errorText}`);
      }
      const data = await res.json();
      setSubmission({ id: data.submissionId, status: "PENDING" });
    } catch (error) {
      console.error("Submission error:", error);
      alert("Submission failed: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  if (loading && !contest) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        Loading...
      </div>
    );
  }
  if (!contest) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        Failed to load contest
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-xl font-bold">{contest.title}</h1>
          <div className="text-gray-600">
            Welcome, <span className="font-semibold">{username}</span>
          </div>
        </div>
      </header>

      
      <div className="max-w-7xl mx-auto px-4 py-6 grid grid-cols-4 gap-6">
        <div className="col-span-1 bg-white rounded-lg shadow p-4 self-start">
          <h3 className="text-lg font-medium mb-4">Problems</h3>
          <ul className="space-y-2">
            {contest.problems.map((prob) => (
              <li key={prob.id}>
                <button
                  onClick={() => handleProblemSelect(prob)}
                  className={`w-full text-left px-3 py-2 rounded-md transition-colors ${
                    currentProblem?.id === prob.id
                      ? "bg-blue-600 text-white"
                      : "hover:bg-gray-100"
                  }`}
                >
                  {prob.title}
                </button>
              </li>
            ))}
          </ul>
        </div>

        <div className="col-span-2 space-y-6">
          {currentProblem && (
            <div className="bg-white rounded-lg shadow p-6">
              <h2 className="text-xl font-bold mb-4">{currentProblem.title}</h2>
              <div className="text-gray-700 whitespace-pre-line">
                {currentProblem.description}
              </div>
            </div>
          )}

          <CodeEditor value={code} onChange={setCode} />

          <button
            onClick={handleSubmit}
            disabled={loading}
            className={`px-6 py-2 rounded-md text-white transition-colors ${
              loading
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-blue-600 hover:bg-blue-700"
            }`}
          >
            {loading ? "Submitting..." : "Submit Solution"}
          </button>

          {submission && <SubmissionStatus submissionId={submission.id} />}
        </div>

        <div className="col-span-1 self-start">
          <Leaderboard contestId={contestId} />
        </div>
      </div>
    </div>
  );
}
