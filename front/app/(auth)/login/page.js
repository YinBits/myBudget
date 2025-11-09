// ...existing code...
'use client';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/context/auth-context';
import Link from 'next/link';

export default function LoginPage() {
  const { user, login } = useAuth();
  const router = useRouter();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    const result = await login(email, password);
    if (result.success) router.push('/dashboard');
    else setError(result.message);
    setLoading(false);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-bg text-text">
      <div className="card p-8 w-full max-w-md">
        <h2 className="text-title text-center mb-6">Entrar na sua conta</h2>

        <form className="space-y-6" onSubmit={handleSubmit}>
          {error && <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded">{error}</div>}
          <div>
            <label htmlFor="email" className="block text-aux muted">Email</label>
            <input id="email" name="email" type="email" required value={email} onChange={(e) => setEmail(e.target.value)} className="mt-1 block w-full border border-border rounded-md px-3 py-2 bg-card text-text" />
          </div>
          <div>
            <label htmlFor="password" className="block text-aux muted">Senha</label>
            <input id="password" name="password" type="password" required value={password} onChange={(e) => setPassword(e.target.value)} className="mt-1 block w-full border border-border rounded-md px-3 py-2 bg-card text-text" />
          </div>

          <button type="submit" disabled={loading} className="w-full btn">
            {loading ? 'Entrando...' : 'Entrar'}
          </button>

          <div className="text-center mt-3">
            <Link href="/register" className="text-primary text-sm">Não tem conta? Registre-se</Link>
          </div>
        </form>
      </div>
    </div>
  );
}
// ...existing code...