package com.mybudget.Controller;

import com.mybudget.DTOs.FinancialReportDto;
import com.mybudget.Service.FinancialReportService;
import com.mybudget.Service.PdfReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FinancialReportController {

    private final FinancialReportService financialReportService;
    private final PdfReportService pdfReportService;

    @GetMapping("/{userId}")
    public ResponseEntity<FinancialReportDto> getReport(
            @PathVariable  Long userId  ,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "false") boolean includeAll )  {

        FinancialReportDto report = financialReportService.generateReportForUser(userId, startDate, endDate, includeAll);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/pdf/{userId}")
    public ResponseEntity<byte[]> exportPdf(
            @PathVariable Long userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "false") boolean includeAll ){

        FinancialReportDto report = financialReportService.generateReportForUser(userId, startDate, endDate, includeAll);
        ByteArrayInputStream pdf = pdfReportService.generatePdf(report, startDate, endDate);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=relatorio-financeiro.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf.readAllBytes());
    }


}
