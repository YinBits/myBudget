package com.mybudget.Controller;

import com.mybudget.DTOs.FinancialReportDto;
import com.mybudget.Security.UserDetailsImpl;
import com.mybudget.Service.FinancialReportService;
import com.mybudget.Service.PdfReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping
    public ResponseEntity<FinancialReportDto> getReport(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "false") boolean includeAll )  {

        Long userId = userDetails.getId();

        FinancialReportDto report =
                financialReportService.generateReportForUser(userId, startDate, endDate, includeAll);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "false") boolean includeAll) {

        Long userId = userDetails.getId(); // ✅ pega do token

        FinancialReportDto report =
                financialReportService.generateReportForUser(userId, startDate, endDate, includeAll);

        ByteArrayInputStream pdf = pdfReportService.generatePdf(report, startDate, endDate);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=relatorio-financeiro.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.readAllBytes());
    }

}
