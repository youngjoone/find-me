import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE || 'http://localhost', // Use NEXT_PUBLIC_API_BASE from .env.local
  withCredentials: true, // For HTTP-only cookies
});

export default api;
