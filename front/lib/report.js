import { api } from './api';

export const reportService = {
  fetchReport: async (startDate, endDate, includeAll = false) => {
    const response = await api.get(`/reports`, {
      params: { startDate, endDate, includeAll }
    });
    return response.data;
  },

  fetchPdf: async (startDate, endDate, includeAll = false) => {
    const response = await api.get(`/reports/pdf`, {
      params: { startDate, endDate, includeAll },
      responseType: 'blob', 
    });
    return response.data;
  },

  downloadPdf: async ( startDate, endDate, includeAll = false) => {
    const blob = await reportService.fetchPdf(startDate, endDate, includeAll);
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `relatorio-${startDate}_to_${endDate}.pdf`;
    link.click();
    URL.revokeObjectURL(url);
  }
};
