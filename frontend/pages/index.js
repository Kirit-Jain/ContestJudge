import { useState } from 'react';
import { useRouter } from 'next/router';

export default function JoinContest() {
  const [contestId, setContestId] = useState('');
  const [username, setUsername] = useState('');
  const router = useRouter();

  const handleJoin = (e) => {
    e.preventDefault();
    router.push(`/contest/${contestId}?username=${username}`);
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="max-w-md w-full space-y-8 p-8">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            Join Coding Contest
          </h2>
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleJoin}>
          <div>
            <label htmlFor="contestId" className="sr-only">Contest ID</label>
            <input
              id="contestId"
              name="contestId"
              type="text"
              required
              className="relative block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              placeholder="Contest ID"
              value={contestId}
              onChange={(e) => setContestId(e.target.value)}
            />
          </div>
          <div>
            <label htmlFor="username" className="sr-only">Username</label>
            <input
              id="username"
              name="username"
              type="text"
              required
              className="relative block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <button
            type="submit"
            className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Join Contest
          </button>
        </form>
      </div>
    </div>
  );
}