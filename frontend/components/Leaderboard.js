import { useState, useEffect } from 'react';

export default function Leaderboard({ contestId }) {
  const [leaderboard, setLeaderboard] = useState([]);

  useEffect(() => {
    if (!contestId) return;

    const fetchLeaderboard = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/contests/${contestId}/leaderboard`);
        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        const data = await res.json();
        setLeaderboard(data);
      } catch (error) {
        console.error('Failed to fetch leaderboard:', error);
      }
    };

    fetchLeaderboard();
    const interval = setInterval(fetchLeaderboard, 15000);
    return () => clearInterval(interval);
  }, [contestId]);

  return (
    <div className="bg-white rounded-lg shadow">
      <div className="px-4 py-2 border-b border-gray-200">
        <h3 className="text-lg font-medium">Leaderboard</h3>
      </div>
      <div className="p-4">
        {leaderboard.map((entry, index) => (
          <div key={entry.username} className="flex justify-between py-2 border-b">
            <span className="font-medium">#{index + 1} {entry.username}</span>
            <span className="text-green-600">{entry.score} pts</span>
          </div>
        ))}
        {leaderboard.length === 0 && (
          <div className="text-gray-500 text-center py-4">No submissions yet</div>
        )}
      </div>
    </div>
  );
}