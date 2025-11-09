import { api } from './api';

export const transactionService = {
  create: async (transactionData) => {
    const response = await api.post('/transactions', transactionData);
    return response.data;
  },

  getAll: async () => {
    const response = await api.get('/transactions');
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/transactions/${id}`);
    return response.data;
  },

  getByType: async (type) => {
    const response = await api.get(`/transactions/type/${type}`);
    return response.data;
  },

  update: async (id, transactionData) => {
    const response = await api.put(`/transactions/${id}`, transactionData);
    return response.data;
  },

  delete: async (id) => {
    await api.delete(`/transactions/${id}`);
  },

  getDashboard: async (year, month) => {
    const response = await api.get('/transactions/dashboard',{
      params: { year, month }
    });
    return response.data;
  }
};