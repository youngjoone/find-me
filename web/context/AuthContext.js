import { createContext, useContext, useState, useEffect } from 'react';
import api from '../lib/api';
import { useRouter } from 'next/router';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    // In a real app, you'd verify the token on the server side
    // or check for a valid session cookie.
    // For this task, we'll assume if a user object exists, they are logged in.
    setLoading(false); // For now, just set loading to false
  }, []);

  const login = async (email, password) => {
    try {
      const response = await api.post('/auth/login', { email, password });
      // Assuming the backend sets an HTTP-only cookie
      setUser({ email: email }); // Set a dummy user for now
      router.push('/summary'); // Redirect to summary page after login
      return response.data;
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  };

  const register = async (email, password, nickname) => {
    try {
      const response = await api.post('/auth/register', { email, password, nickname });
      // Assuming successful registration means user can now login
      router.push('/auth/login'); // Redirect to login page after registration
      return response.data;
    } catch (error) {
      console.error('Registration failed:', error);
      throw error;
    }
  };

  const logout = () => {
    // In a real app, you'd invalidate the session on the server side
    setUser(null);
    router.push('/auth/login');
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
