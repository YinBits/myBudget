'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ArrowUp, ArrowDown, Plus, List, Calendar } from 'lucide-react';
import { transactionService } from '@/lib/transactions';

const monthNames = [
  'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
  'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
];

export default function DashboardPage() {
  const [dashboardData, setDashboardData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1); // 1-12
  const router = useRouter();

  const fetchDashboard = async (year, month) => {
    setLoading(true);
    try {
      const data = await transactionService.getDashboard(year, month);
      setDashboardData({
        currentBalance: data.balance,
        monthlyIncome: data.totalIncome,
        monthlyExpenses: data.totalExpenses,
      });
    } catch (err) {
      console.error('Erro ao buscar dashboard:', err);
      setError('Não foi possível carregar o dashboard.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDashboard(selectedYear, selectedMonth);
  }, []);

  const handleApplyDate = () => {
    fetchDashboard(selectedYear, selectedMonth);
    setShowModal(false);
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center text-red-500 p-4">{error}</div>
    );
  }

  const cards = [
    {
      title: 'Saldo Atual',
      value: dashboardData.currentBalance,
      border: 'border-green-500',
      icon: <ArrowUp className="text-green-500" size={24} />,
    },
    {
      title: 'Receitas',
      value: dashboardData.monthlyIncome,
      border: 'border-blue-500',
      icon: <ArrowUp className="text-blue-500" size={24} />,
    },
    {
      title: 'Despesas',
      value: dashboardData.monthlyExpenses,
      border: 'border-red-500',
      icon: <ArrowDown className="text-red-500" size={24} />,
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-title">Dashboard</h1>
          <p className="text-muted">
            {monthNames[selectedMonth - 1]} / {selectedYear}
          </p>
        </div>
        <div className="flex gap-3">
          <button onClick={() => setShowModal(true)} className="btn-ghost flex items-center gap-2">
            <Calendar size={18} />
            Selecionar Mês
          </button>
          <button onClick={() => router.push('/transactions/create')} className="btn">
            <Plus size={18} className="mr-2" />
            Nova Transação
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {cards.map((card, index) => (
          <div key={index} className={`card p-6 border-l-4 ${card.border} transition-transform hover:scale-[1.02]`}>
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-subtitle muted">{card.title}</h3>
                <p className="text-2xl font-bold mt-2">
                  {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(card.value)}
                </p>
              </div>
              {card.icon}
            </div>
          </div>
        ))}
      </div>

      {/* Modal de seleção de mês */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white dark:bg-gray-800 p-6 rounded-md w-80 space-y-4">
            <h2 className="text-title dark:text-white mb-2">Selecionar Mês</h2>
            <div className="flex gap-2">
              <select
                value={selectedYear}
                onChange={(e) => setSelectedYear(Number(e.target.value))}
                className="border border-gray-300 dark:border-gray-600 px-2 py-1 rounded w-1/2 bg-white dark:bg-gray-700 text-black dark:text-white"
              >
                {Array.from({ length: 5 }).map((_, i) => {
                  const year = new Date().getFullYear() - 2 + i;
                  return <option key={year} value={year}>{year}</option>;
                })}
              </select>

              <select
                value={selectedMonth}
                onChange={(e) => setSelectedMonth(Number(e.target.value))}
                className="border border-gray-300 dark:border-gray-600 px-2 py-1 rounded w-1/2 bg-white dark:bg-gray-700 text-black dark:text-white"
              >
                {Array.from({ length: 12 }).map((_, i) => {
                  const month = i + 1;
                  return <option key={month} value={month}>{month.toString().padStart(2, '0')}</option>;
                })}
              </select>
            </div>
            <div className="flex justify-end gap-2 mt-4">
              <button
                className="btn-ghost dark:text-gray-300"
                onClick={() => setShowModal(false)}
              >
                Cancelar
              </button>
              <button className="btn" onClick={handleApplyDate}>Aplicar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
