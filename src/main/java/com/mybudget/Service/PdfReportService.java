package com.mybudget.Service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mybudget.DTOs.FinancialReportDto;
import com.mybudget.DTOs.TransactionReportDto;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfReportService {

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    private static final Font SECTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
    private static final Font TEXT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 12);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

    public ByteArrayInputStream generatePdf(FinancialReportDto report, LocalDate startDate, LocalDate endDate) {
        try {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            addLogo(document);
            addTitle(document);
            addDateRange(document, startDate, endDate);
            addSummary(document, report);
            addMonthlyTable(document, report);
            addTransactionsTable(document, report.getTransactions());

            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private void addLogo(Document document) {
        try {
            Image logo = Image.getInstance(getClass().getResource("/static/images/logo.png")); // ajuste conforme seu projeto
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            // se não conseguir carregar a logo, apenas ignora
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("Relatório Financeiro", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
    }

    private void addDateRange(Document document, LocalDate startDate, LocalDate endDate) throws DocumentException {
        String rangeText = String.format("Período: %s a %s",
                startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Paragraph dateRange = new Paragraph(rangeText, FontFactory.getFont(FontFactory.HELVETICA, 10));
        dateRange.setAlignment(Element.ALIGN_CENTER);
        document.add(dateRange);
        document.add(new Paragraph(" "));
    }

    private void addSummary(Document document, FinancialReportDto report) throws DocumentException {
        document.add(new Paragraph("Resumo Geral", SECTION_FONT));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total de Entradas: R$ " + report.getTotalIncome(), TEXT_FONT));
        document.add(new Paragraph("Total de Saídas: R$ " + report.getTotalExpense(), TEXT_FONT));
        document.add(new Paragraph("Saldo: R$ " + report.getBalance(), TEXT_FONT));
        document.add(new Paragraph("Média de Entradas: R$ " + report.getAverageIncome(), TEXT_FONT));
        document.add(new Paragraph("Média de Saídas: R$ " + report.getAverageExpense(), TEXT_FONT));
        document.add(new Paragraph(" "));
    }

    private void addMonthlyTable(Document document, FinancialReportDto report) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 2, 2});

        addTableHeader(table, new String[]{"Mês", "Entradas (R$)", "Saídas (R$)"});

        report.getSummaryByMonth().forEach(month -> {
            table.addCell(month.getMonth());
            table.addCell("R$ " + month.getIncome());
            table.addCell("R$ " + month.getExpense());
        });

        document.add(table);
        document.add(new Paragraph(" "));
    }

    private void addTransactionsTable(Document document, List<TransactionReportDto> transactions) throws DocumentException {
        if (transactions == null || transactions.isEmpty()) return;

        document.add(new Paragraph("Transações Detalhadas", SECTION_FONT));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 4, 2, 2, 3});

        addTableHeader(table, new String[]{"ID", "Descrição", "Tipo", "Valor (R$)", "Data"});

        transactions.forEach(t -> {
            table.addCell(String.valueOf(t.getId()));
            table.addCell(t.getDescription());
            table.addCell(t.getType().toString());
            table.addCell("R$ " + t.getAmount());
            table.addCell(t.getDate().toString());
        });

        document.add(table);
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(new Color(230, 230, 250));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
}
