import { useAuth } from '../context/AuthContext';
import { useRouter } from 'next/router';
import { useQuery } from '@tanstack/react-query';
import api from '../lib/api';

export default function SummaryPage() {
  const { user } = useAuth();
  const router = useRouter();

  if (!user) {
    router.push('/auth/login');
    return null;
  }

  const { data: summary, isLoading, isError } = useQuery({
    queryKey: ['summary', user.email],
    queryFn: async () => {
      const response = await api.get('/profile/summary');
      return response.data;
    },
    enabled: !!user, // Only run query if user is logged in
  });

  if (isLoading) return <div className="min-h-screen flex items-center justify-center">Loading summary...</div>;
  if (isError) return <div className="min-h-screen flex items-center justify-center text-red-500">Error loading summary.</div>;

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-3xl mx-auto bg-white p-8 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6 text-center">Your Profile Summary</h2>
        {summary ? (
          <div className="space-y-4">
            <p><strong>Personality:</strong> {summary.personality}</p>
            <p><strong>Strengths:</strong> {summary.strengths?.join(', ')}</p>
            <p><strong>Weaknesses:</strong> {summary.weaknesses?.join(', ')}</p>
            <p><strong>Advice:</strong> {summary.advice}</p>
            {/* Placeholder for Radar Chart */}
            <div className="bg-gray-200 h-64 flex items-center justify-center rounded-md">
              <p className="text-gray-600">Radar Chart Placeholder</p>
            </div>
          </div>
        ) : (
          <p className="text-center">No summary available. Please complete the survey.</p>
        )}
      </div>
    </div>
  );
}
