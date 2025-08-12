import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useRouter } from 'next/router';
import api from '../lib/api';

export default function CoachPage() {
  const { user } = useAuth();
  const router = useRouter();
  const [question, setQuestion] = useState('');
  const [coachResponse, setCoachResponse] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  if (!user) {
    router.push('/auth/login');
    return null;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setCoachResponse(null);
    try {
      const response = await api.post('/coach/ask', { question });
      setCoachResponse(response.data);
    } catch (err) {
      setError('Failed to get coaching response: ' + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-3xl mx-auto bg-white p-8 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6 text-center">AI 코치에게 질문하기</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="question" className="block text-sm font-medium text-gray-700">
              질문을 입력하세요:
            </label>
            <textarea
              id="question"
              className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
              rows="4"
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              required
            ></textarea>
          </div>
          <button
            type="submit"
            className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            disabled={loading}
          >
            {loading ? '응답 생성 중...' : '질문하기'}
          </button>
        </form>

        {error && (
          <div className="mt-4 p-3 bg-red-100 text-red-700 rounded-md">
            <p>{error}</p>
          </div>
        )}

        {coachResponse && (
          <div className="mt-6 p-6 border border-gray-200 rounded-lg shadow-sm">
            <h3 className="text-xl font-semibold mb-3">코치 응답:</h3>
            <p><strong>톤:</strong> {coachResponse.tone}</p>
            <p><strong>요약:</strong> {coachResponse.summary}</p>
            <p><strong>이유:</strong> {coachResponse.why}</p>
            {coachResponse.plan && coachResponse.plan.length > 0 && (
              <div>
                <p><strong>계획:</strong></p>
                <ul className="list-disc list-inside ml-4">
                  {coachResponse.plan.map((item, index) => (
                    <li key={index}>{item}</li>
                  ))}
                </ul>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
