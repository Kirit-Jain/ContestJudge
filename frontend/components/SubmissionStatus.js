import { useState, useEffect } from 'react';

export default function SubmissionStatus({ submissionId }) {
  const [status, setStatus] = useState('PENDING');

  useEffect(() => {
    if (!submissionId) return;

    const pollStatus = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/submissions/${submissionId}`);
        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        const data = await res.json();
        setStatus(data.status);
        
        if (data.status === 'PENDING' || data.status === 'RUNNING') {
          setTimeout(pollStatus, 2000);
        }
      } catch (error) {
        console.error('Failed to poll status:', error);
      }
    };

    pollStatus();
  }, [submissionId]);

  const getStatusColor = () => {
    switch (status) {
      case 'ACCEPTED': return 'text-green-600';
      case 'WRONG_ANSWER': return 'text-red-600';
      case 'RUNNING': return 'text-blue-600';
      default: return 'text-yellow-600';
    }
  };

  return (
    <div className="bg-white rounded-lg shadow p-4">
      <h4 className="font-medium">Submission Status</h4>
      <div className={`mt-2 font-semibold ${getStatusColor()}`}>
        {status.replace('_', ' ')}
      </div>
    </div>
  );
}