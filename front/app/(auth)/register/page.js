// ...existing code...
'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { authService } from '@/lib/auth';

export default function RegisterPage() {
  const router = useRouter();
  const [form, setForm] = useState({ name: '', email: '', password: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await authService.register({ ...form, role: 'ROLE_CUSTOMER' });
      router.push('/login');
    } catch (err) {
      setError(err.response?.data?.message || 'Erro ao registrar');
    }
    setLoading(false);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-bg text-text">
      <div className="card p-8 w-full max-w-md">
        <h2 className="text-title text-center mb-6">Criar Conta</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          {error && <div className="bg-red-100 text-red-700 p-2 rounded">{error}</div>}
          <div>
            <label className="block mb-1 text-aux muted">Nome</label>
            <input name="name" type="text" value={form.name} onChange={handleChange} required className="w-full border px-3 py-2 rounded bg-card text-text" />
          </div>
          <div>
            <label className="block mb-1 text-aux muted">Email</label>
            <input name="email" type="email" value={form.email} onChange={handleChange} required className="w-full border px-3 py-2 rounded bg-card text-text" />
          </div>
          <div>
            <label className="block mb-1 text-aux muted">Senha</label>
            <input name="password" type="password" value={form.password} onChange={handleChange} required className="w-full border px-3 py-2 rounded bg-card text-text" />
          </div>
          <button type="submit" disabled={loading} className="w-full btn">{loading ? 'Criando...' : 'Criar Conta'}</button>
        </form>
      </div>
    </div>
  );
}
// ...existing code...