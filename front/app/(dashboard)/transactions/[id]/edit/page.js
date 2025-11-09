'use client';
import { useEffect, useState } from 'react';
import { useRouter, useParams } from 'next/navigation';
import { transactionService } from '@/lib/transactions';
import { ArrowLeft } from 'lucide-react';

export default function EditTransactionPage() {
  const router = useRouter();
  const params = useParams();
  const { id } = params;

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({
    description: '',
    amount: '',
    type: 'INCOME',
    date: ''
  });

  useEffect(() => {
    const fetchTransaction = async () => {
      try {
        const data = await transactionService.getById(id);
        setForm({
          description: data.description,
          amount: data.amount,
          type: data.type,
          date: data.date ? data.date.slice(0, 16) : ''
        });
      } catch (error) {
        alert('Erro ao carregar transação');
        router.push('/transactions');
      } finally {
        setLoading(false);
      }
    };

    fetchTransaction();
  }, [id, router]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await transactionService.update(id, {
        ...form,
        amount: parseFloat(form.amount)
      });
      router.push('/transactions');
    } catch (error) {
      alert('Erro ao atualizar transação');
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-4">
        <button onClick={() => router.back()} className="p-2 hover:bg-card rounded-full">
          <ArrowLeft size={20} />
        </button>
        <h1 className="text-title">Editar Transação</h1>
      </div>

      <div className="card p-6 max-w-md">
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-aux muted mb-1">Descrição</label>
            <input
              type="text"
              value={form.description}
              onChange={e => setForm({ ...form, description: e.target.value })}
              className="w-full border border-border rounded px-3 py-2 bg-card text-text"
              required
            />
          </div>

          <div>
            <label className="block text-aux muted mb-1">Valor</label>
            <input
              type="number"
              step="0.01"
              value={form.amount}
              onChange={e => setForm({ ...form, amount: e.target.value })}
              className="w-full border border-border rounded px-3 py-2 bg-card text-text"
              required
            />
          </div>

          <div>
            <label className="block text-aux muted mb-1">Tipo</label>
            <select
              value={form.type}
              onChange={e => setForm({ ...form, type: e.target.value })}
              className="w-full border border-border rounded px-3 py-2 bg-card text-text"
            >
              <option value="INCOME">Entrada</option>
              <option value="EXPENSE">Saída</option>
            </select>
          </div>

          <div>
            <label className="block text-aux muted mb-1">Data</label>
            <input
              type="date"
              value={form.date}
              onChange={e => setForm({ ...form, date: e.target.value })}
              className="w-full border border-border rounded px-3 py-2 bg-card text-text"
              required
            />
          </div>

          <div className="flex items-center gap-4 pt-4">
            <button type="submit" disabled={saving} className="btn flex-1">
              {saving ? 'Salvando...' : 'Salvar'}
            </button>
            <button type="button" onClick={() => router.back()} className="btn-ghost flex-1">
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}