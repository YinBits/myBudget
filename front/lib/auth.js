import { api } from './api';

export const authService = {
  login: async (email, password) => {
    const response = await api.post('/users/login', { email, password });
    return response.data;
  },

  register: async (userData) => {
    const response = await api.post('/users', userData);
    return response.data;
  },

  logout: () => {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token');
    }
  },

  getToken: () => {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('token');
    }
    return null;
  },

  isAuthenticated: () => {
    if (typeof window !== 'undefined') {
      return !!localStorage.getItem('token');
    }
    return false;
  }
};