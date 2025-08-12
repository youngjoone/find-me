import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useRouter } from 'next/router';
import api from '../lib/api';

export default function SurveyPage() {
  const { user } = useAuth();
  const router = useRouter();
  const [objectiveAnswers, setObjectiveAnswers] = useState({});
  const [freeTextAnswers, setFreeTextAnswers] = useState({});

  if (!user) {
    router.push('/auth/login');
    return null;
  }

  const handleObjectiveChange = (qid, value) => {
    setObjectiveAnswers(prev => ({ ...prev, [qid]: value }));
  };

  const handleFreeTextChange = (qid, text) => {
    setFreeTextAnswers(prev => ({ ...prev, [qid]: text }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formattedObjectiveAnswers = Object.keys(objectiveAnswers).map(qid => ({
      qid,
      value: parseInt(objectiveAnswers[qid])
    }));
    const formattedFreeTextAnswers = Object.keys(freeTextAnswers).map(qid => ({
      qid,
      text: freeTextAnswers[qid]
    }));

    try {
      await api.post('/profile/survey/submit', {
        objectiveAnswers: formattedObjectiveAnswers,
        freeTextAnswers: formattedFreeTextAnswers
      });
      alert('Survey submitted successfully! Redirecting to summary.');
      router.push('/summary');
    } catch (error) {
      alert('Survey submission failed: ' + (error.response?.data?.message || error.message));
    }
  };

  // Dummy questions for demonstration
  const questions = [
    { qid: 'Q1', type: 'objective', text: '나는 새로운 경험에 개방적이다.', options: [1, 2, 3, 4, 5] },
    { qid: 'Q2', type: 'objective', text: '나는 다른 사람의 감정을 잘 이해한다.', options: [1, 2, 3, 4, 5] },
    { qid: 'F1', type: 'freeText', text: '최근에 가장 즐거웠던 경험은 무엇인가요?' },
    { qid: 'F2', type: 'freeText', text: '스트레스를 해소하는 자신만의 방법이 있다면 알려주세요.' },
  ];

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-3xl mx-auto bg-white p-8 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6 text-center">설문조사</h2>
        <form onSubmit={handleSubmit} className="space-y-6">
          {questions.map((q) => (
            <div key={q.qid} className="border p-4 rounded-md">
              <p className="font-semibold mb-2">{q.text}</p>
              {q.type === 'objective' && (
                <div className="flex justify-around">
                  {q.options.map(option => (
                    <label key={option} className="inline-flex items-center">
                      <input
                        type="radio"
                        name={q.qid}
                        value={option}
                        onChange={() => handleObjectiveChange(q.qid, option)}
                        checked={objectiveAnswers[q.qid] === option}
                        className="form-radio"
                      />
                      <span className="ml-2">{option}</span>
                    </label>
                  ))}
                  }
                </div>
              )}
              {q.type === 'freeText' && (
                <textarea
                  className="mt-2 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                  rows="3"
                  value={freeTextAnswers[q.qid] || ''}
                  onChange={(e) => handleFreeTextChange(q.qid, e.target.value)}
                ></textarea>
              )}
            </div>
          ))}
          <button
            type="submit"
            className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            설문 제출
          </button>
        </form>
      </div>
    </div>
  );
}
