'use client';
import React, { useEffect } from 'react';
import { AuthProvider } from '../context/auth-context';

export default function Providers({ children }) {
  useEffect(() => {
    if (typeof window === 'undefined') return;
    const saved = localStorage.getItem('theme'); // 'dark' | 'light' | null
    const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;

    if (saved === 'dark' || (!saved && prefersDark)) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, []);

  return <AuthProvider>{children}</AuthProvider>;
}