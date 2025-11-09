'use client';
import { useState } from 'react';
import { Bar, Doughnut } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement
} from 'chart.js';
import { CalendarDays, Download, TrendingUp } from 'lucide-react';
import { reportService } from '@/lib/report';

// Register ChartJS components
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement
);

// Helper function to format currency
const formatCurrency = (value) => {
  return new Intl.NumberFormat('pt-BR', { 
    style: 'currency', 
    currency: 'BRL' 
  }).format(value);
};

export default function ReportPage() {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [report, setReport] = useState(null);
  const [includeAll, setIncludeAll] = useState(true);

  // Helper to convert month abbreviation to number
  const getMonthNumber = (monthAbbr) => {
    const months = {
      'jan': 0, 'fev': 1, 'mar': 2, 'abr': 3, 'mai': 4, 'jun': 5,
      'jul': 6, 'ago': 7, 'set': 8, 'out': 9, 'nov': 10, 'dez': 11
    };
    return months[monthAbbr.toLowerCase()];
  };

  // Prepare data for bar chart
  const prepareBarChartData = (transactions) => {
    if (!transactions?.length) {
      console.log('No transactions found');
      return null;
    }

    console.log('Processing transactions:', transactions);

    // Group transactions by month
    const grouped = transactions.reduce((acc, curr) => {
      try {
        const date = new Date(curr.date);
        if (isNaN(date)) {
          console.error('Invalid date:', curr.date);
          return acc;
        }

        const monthYear = new Intl.DateTimeFormat('pt-BR', { 
          month: 'short', 
          year: 'numeric' 
        }).format(date);

        if (!acc[monthYear]) {
          acc[monthYear] = { income: 0, expense: 0 };
        }

        const amount = Number(curr.amount);
        if (isNaN(amount)) {
          console.error('Invalid amount:', curr.amount);
          return acc;
        }

        if (curr.type === 'INCOME') {
          acc[monthYear].income += amount;
        } else {
          acc[monthYear].expense += amount;
        }

        return acc;
      } catch (error) {
        console.error('Error processing transaction:', error);
        return acc;
      }
    }, {});

    console.log('Grouped data:', grouped);

    // Sort months chronologically
    const months = Object.keys(grouped).sort((a, b) => {
      const [monthA, yearA] = a.split(' ');
      const [monthB, yearB] = b.split(' ');
      
      const dateA = new Date(yearA, getMonthNumber(monthA));
      const dateB = new Date(yearB, getMonthNumber(monthB));
      
      return dateA - dateB;
    });

    console.log('Sorted months:', months);

    return {
      labels: months,
      datasets: [
        {
          label: 'Entradas',
          data: months.map(month => grouped[month].income),
          backgroundColor: 'rgba(34, 197, 94, 0.8)',
          borderColor: 'rgb(34, 197, 94)',
          borderWidth: 1,
          borderRadius: 4,
          categoryPercentage: 0.8,
          barPercentage: 0.9,
        },
        {
          label: 'Saídas',
          data: months.map(month => grouped[month].expense),
          backgroundColor: 'rgba(239, 68, 68, 0.8)',
          borderColor: 'rgb(239, 68, 68)',
          borderWidth: 1,
          borderRadius: 4,
          categoryPercentage: 0.8,
          barPercentage: 0.9,
        }
      ]
    };
  };

  const handleGenerate = async () => {
    if (!startDate || !endDate) {
      setError('Selecione o período inicial e final.');
      return;
    }

    setLoading(true);
    setError('');
    try {
      const data = await reportService.fetchReport(startDate, endDate, includeAll);
      console.log('Report data:', data);
      setReport(data);
    } catch (err) {
      console.error('Error fetching report:', err);
      setError('Erro ao gerar relatório. Verifique os dados e tente novamente.');
    } finally {
      setLoading(false);
    }
  };
  
  const doughnutData = report ? {
    labels: ['Entradas', 'Saídas'],
    datasets: [{
      data: [report.totalIncome || 0, report.totalExpense || 0],
      backgroundColor: ['rgba(34, 197, 94, 0.8)', 'rgba(239, 68, 68, 0.8)'],
      borderColor: ['rgb(34, 197, 94)', 'rgb(239, 68, 68)'],
      borderWidth: 1,
    }]
  } : null;

  const barChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    interaction: {
      mode: 'index',
      intersect: false,
    },
    plugins: {
      legend: {
        position: 'top',
        labels: {
          usePointStyle: true,
          padding: 20,
        }
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            return `${context.dataset.label}: ${formatCurrency(context.raw)}`;
          }
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        grid: {
          drawBorder: false,
          color: 'rgba(0, 0, 0, 0.1)',
        },
        ticks: {
          padding: 10,
          callback: function(value) {
            return formatCurrency(value);
          }
        }
      },
      x: {
        grid: {
          display: false,
        },
        ticks: {
          padding: 10
        }
      }
    },
    animation: {
      duration: 750
    }
  };

  const doughnutOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
        labels: {
          usePointStyle: true,
          padding: 20,
        }
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            return `${context.label}: ${formatCurrency(context.raw)}`;
          }
        }
      }
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-title">Relatório Financeiro</h1>
        {report && (
          <button
            onClick={() => reportService.downloadPdf(startDate, endDate,includeAll)}
            className="btn-ghost"
          >
            <Download size={18} className="mr-2" />
            Baixar PDF
          </button>
        )}
      </div>

      <div className="card p-6">
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 items-end">
          <div>
            <label className="text-aux muted block mb-1">Data Inicial</label>
            <div className="relative">
              <input
                type="date"
                value={startDate}
                onChange={e => setStartDate(e.target.value)}
                className="w-full border border-border rounded px-3 py-2 bg-card text-text pl-10"
              />
              <CalendarDays className="absolute left-3 top-2.5 h-5 w-5 text-muted" />
            </div>
          </div>

          <div>
            <label className="text-aux muted block mb-1">Data Final</label>
            <div className="relative">
              <input
                type="date"
                value={endDate}
                onChange={e => setEndDate(e.target.value)}
                className="w-full border border-border rounded px-3 py-2 bg-card text-text pl-10"
              />
              <CalendarDays className="absolute left-3 top-2.5 h-5 w-5 text-muted" />
            </div>
          </div>

          <div className="space-y-4">
            <div className="flex items-center space-x-2">
              <input
                type="checkbox"
                id="includeAll"
                checked={includeAll}
                onChange={(e) => setIncludeAll(e.target.checked)}
                className="h-4 w-4 rounded border-border text-primary focus:ring-primary"
              />
              <label htmlFor="includeAll" className="text-sm text-muted">
                Incluir transações recorrentes
              </label>
            </div>

            <button
              onClick={handleGenerate}
              disabled={loading}
              className="btn w-full"
            >
              <TrendingUp size={18} className="mr-2" />
              {loading ? 'Gerando...' : 'Gerar Relatório'}
            </button>
          </div>
        </div>

        {error && (
          <div className="mt-4 bg-red-100 text-red-700 p-3 rounded">
            {error}
          </div>
        )}
      </div>

      {report && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
            <div className="card p-6">
              <h3 className="text-aux muted mb-2">Total de Entradas</h3>
              <p className="text-2xl font-bold text-green-500">
                {formatCurrency(report.totalIncome || 0)}
              </p>
            </div>
            
            <div className="card p-6">
              <h3 className="text-aux muted mb-2">Total de Saídas</h3>
              <p className="text-2xl font-bold text-red-500">
                {formatCurrency(report.totalExpense || 0)}
              </p>
            </div>
            
            <div className="card p-6">
              <h3 className="text-aux muted mb-2">Saldo do Período</h3>
              <p className="text-2xl font-bold text-blue-500">
                {formatCurrency((report.totalIncome || 0) - (report.totalExpense || 0))}
              </p>
            </div>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="card p-6">
              <h3 className="text-subtitle mb-4">Receitas e Despesas por Mês</h3>
              {report.transactions?.length > 0 ? (
                <div className="h-[400px]">
                  <Bar
                    data={prepareBarChartData(report.transactions)}
                    options={barChartOptions}
                  />
                </div>
              ) : (
                <p className="text-center text-muted py-8">
                  Nenhuma transação encontrada para o período selecionado
                </p>
              )}
            </div>

            <div className="card p-6">
              <h3 className="text-subtitle mb-4">Distribuição</h3>
              {doughnutData && (
                <Doughnut
                  data={doughnutData}
                  options={doughnutOptions}
                />
              )}
            </div>
          </div>

          {report.transactions && report.transactions.length > 0 && (
            <div className="card p-6">
              <h3 className="text-subtitle mb-4">Transações do Período</h3>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-4 text-aux muted">Data</th>
                      <th className="text-left p-4 text-aux muted">Descrição</th>
                      <th className="text-left p-4 text-aux muted">Tipo</th>
                      <th className="text-right p-4 text-aux muted">Valor</th>
                    </tr>
                  </thead>
                  <tbody>
                    {report.transactions.map((t, i) => (
                      <tr key={i} className="border-b border-border">
                        <td className="p-4">
                          {new Date(t.date).toLocaleDateString('pt-BR')}
                        </td>
                        <td className="p-4">{t.description}</td>
                        <td className="p-4">
                          {t.type === 'INCOME' ? 'Entrada' : 'Saída'}
                        </td>
                        <td className={`p-4 text-right ${
                          t.type === 'INCOME' ? 'text-green-500' : 'text-red-500'
                        }`}>
                          {formatCurrency(t.amount)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}