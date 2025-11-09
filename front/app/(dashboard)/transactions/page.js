'use client';
import { useEffect, useState } from 'react';
import Link from 'next/link';
import { transactionService } from '@/lib/transactions';
import { Plus, Pencil, Trash2, AlertTriangle, Calendar } from 'lucide-react';
import Modal from '@/components/ui/modal';

const monthNames = [
  'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
  'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
];

export default function TransactionsPage() {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deleteModal, setDeleteModal] = useState({ open: false, transaction: null });
  const [dateModal, setDateModal] = useState(false);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1);

  const fetchTransactions = async () => {
    setLoading(true);
    try {
      const data = await transactionService.getAll();
      setTransactions(data);
    } catch (e) {
      alert('Erro ao carregar transações');
    }
    setLoading(false);
  };

  const handleDeleteClick = (transaction) => {
    setDeleteModal({ open: true, transaction });
  };

  const handleDeleteConfirm = async () => {
    try {
      await transactionService.delete(deleteModal.transaction.id);
      setTransactions(transactions.filter(t => t.id !== deleteModal.transaction.id));
      setDeleteModal({ open: false, transaction: null });
    } catch (e) {
      alert('Erro ao excluir');
    }
  };

  const handleApplyDate = () => {
    setDateModal(false);
    // Os dados já são filtrados pelos estados selectedMonth e selectedYear
  };

  useEffect(() => {
    fetchTransactions();
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  const filteredTransactions = transactions.filter(t => {
    const date = new Date(t.date);
    return date.getMonth() + 1 === Number(selectedMonth) && date.getFullYear() === Number(selectedYear);
  });

  const totalIncome = filteredTransactions
    .filter(t => t.type === 'INCOME')
    .reduce((sum, t) => sum + t.amount, 0);

  const totalExpense = filteredTransactions
    .filter(t => t.type === 'EXPENSE')
    .reduce((sum, t) => sum + t.amount, 0);

  const balance = totalIncome - totalExpense;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-title">Transações</h1>
          <p className="text-muted">
            {monthNames[selectedMonth - 1]} / {selectedYear}
          </p>
        </div>
        <div className="flex gap-3">
          <button 
            onClick={() => setDateModal(true)} 
            className="btn-ghost flex items-center gap-2"
          >
            <Calendar size={18} />
            Selecionar Mês
          </button>
          <Link href="/transactions/create" className="btn">
            <Plus size={18} className="mr-2" />
            Nova Transação
          </Link>
        </div>
      </div>

      {/* Cards resumo */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="card p-6 border-l-4 border-green-500 transition-transform hover:scale-[1.02]">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-subtitle muted">Entradas</h3>
              <p className="text-2xl font-bold mt-2 text-green-500">
                {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(totalIncome)}
              </p>
            </div>
            <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
              <Plus className="text-green-500" size={20} />
            </div>
          </div>
        </div>

        <div className="card p-6 border-l-4 border-red-500 transition-transform hover:scale-[1.02]">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-subtitle muted">Saídas</h3>
              <p className="text-2xl font-bold mt-2 text-red-500">
                {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(totalExpense)}
              </p>
            </div>
            <div className="w-8 h-8 bg-red-100 rounded-full flex items-center justify-center">
              <Trash2 className="text-red-500" size={20} />
            </div>
          </div>
        </div>

        <div className="card p-6 border-l-4 border-blue-500 transition-transform hover:scale-[1.02]">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-subtitle muted">Saldo</h3>
              <p className={`text-2xl font-bold mt-2 ${balance >= 0 ? 'text-blue-500' : 'text-red-500'}`}>
                {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(balance)}
              </p>
            </div>
            <div className={`w-8 h-8 rounded-full flex items-center justify-center ${
              balance >= 0 ? 'bg-blue-100' : 'bg-red-100'
            }`}>
              {balance >= 0 ? (
                <Plus className="text-blue-500" size={20} />
              ) : (
                <Trash2 className="text-red-500" size={20} />
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Tabela */}
      <div className="card overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="bg-card border-b border-border">
                <th className="text-left p-4 text-aux muted">Descrição</th>
                <th className="text-left p-4 text-aux muted">Valor</th>
                <th className="text-left p-4 text-aux muted">Tipo</th>
                <th className="text-left p-4 text-aux muted">Data</th>
                <th className="text-right p-4 text-aux muted">Ações</th>
              </tr>
            </thead>
            <tbody>
              {filteredTransactions.length === 0 ? (
                <tr>
                  <td colSpan={5} className="text-center py-8 text-muted">
                    Nenhuma transação encontrada para {monthNames[selectedMonth - 1]} de {selectedYear}
                  </td>
                </tr>
              ) : (
                filteredTransactions.map(transaction => (
                  <tr key={transaction.id} className="border-b border-border hover:bg-card/50 transition-colors">
                    <td className="p-4">{transaction.description}</td>
                    <td className={`p-4 font-medium ${
                      transaction.type === 'INCOME' ? 'text-green-500' : 'text-red-500'
                    }`}>
                      {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(transaction.amount)}
                    </td>
                    <td className="p-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        transaction.type === 'INCOME' 
                          ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200'
                          : 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
                      }`}>
                        {transaction.type === 'INCOME' ? 'Entrada' : 'Saída'}
                      </span>
                    </td>
                    <td className="p-4">
                      {new Date(transaction.date).toLocaleDateString('pt-BR', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric'
                      })}
                    </td>
                    <td className="p-4 text-right">
                      <div className="flex justify-end gap-3">
                        <Link
                          href={`/transactions/${transaction.id}/edit`}
                          className="inline-flex items-center text-primary hover:text-primary-dark transition-colors"
                        >
                          <Pencil size={16} className="mr-1" />
                          Editar
                        </Link>
                        <button
                          onClick={() => handleDeleteClick(transaction)}
                          className="inline-flex items-center text-red-500 hover:text-red-700 transition-colors"
                        >
                          <Trash2 size={16} className="mr-1" />
                          Excluir
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal de seleção de mês */}
      {dateModal && (
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
                onClick={() => setDateModal(false)}
              >
                Cancelar
              </button>
              <button className="btn" onClick={handleApplyDate}>Aplicar</button>
            </div>
          </div>
        </div>
      )}

      {/* Modal exclusão */}
      <Modal
        open={deleteModal.open}
        onClose={() => setDeleteModal({ open: false, transaction: null })}
        title="Confirmar Exclusão"
        footer={
          <>
            <button
              onClick={() => setDeleteModal({ open: false, transaction: null })}
              className="btn-ghost"
            >
              Cancelar
            </button>
            <button
              onClick={handleDeleteConfirm}
              className="btn bg-red-600 hover:bg-red-700"
            >
              Excluir
            </button>
          </>
        }
      >
        <div className="flex items-center gap-4">
          <div className="flex-shrink-0 w-12 h-12 rounded-full bg-red-100 flex items-center justify-center">
            <AlertTriangle className="w-6 h-6 text-red-600" />
          </div>
          <div>
            <h4 className="text-base-sm font-medium mb-1">
              Tem certeza que deseja excluir esta transação?
            </h4>
            {deleteModal.transaction && (
              <p className="text-aux muted">
                {deleteModal.transaction.description} - {' '}
                {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
                  .format(deleteModal.transaction.amount)}
              </p>
            )}
          </div>
        </div>
      </Modal>
    </div>
  );
}