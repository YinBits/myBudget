'use client';
import React, { createContext, useState, useContext, useEffect } from 'react';
import { authService } from '@/lib/auth';
import { jwtDecode } from 'jwt-decode';


const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within an AuthProvider');
  return context;
};

export const AuthProvider = ({ children }) => {
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState(null)

 useEffect(() => {
    const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;
    if (token) {
      try {
        const decoded = jwtDecode(token);
        const userEmail = decoded.sub;
        setUser({ email: userEmail });
      } catch (e) {
        console.error('Token inválido:', e);
        localStorage.removeItem('token');
      }
    }
    setLoading(false);
  }, []);

const login = async (email, password) => {
  try {
    const data = await authService.login(email, password);
    localStorage.setItem('token', data.token);
    const decoded = jwtDecode(data.token);
    const userEmail = decoded.sub;
    setUser({ email: userEmail });
    return { success: true, message: 'Logado' };
  } catch (error) {
    return { success: false, message: error.response?.data?.message || 'Erro no login' };
  }
};

const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('email');
  setUser(null);
  if (typeof window !== 'undefined') {
    window.location.href = '/login'; 
  }
}


  return (
    <AuthContext.Provider value={{ user, login, logout, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  );
};
