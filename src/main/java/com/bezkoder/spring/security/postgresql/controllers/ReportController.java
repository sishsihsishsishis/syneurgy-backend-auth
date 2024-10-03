package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;


    @GetMapping("/download")
    public ResponseEntity<?> getReportDownloadLink(
            @RequestParam Integer meetingId,
            @RequestParam Integer osVersion,
            @RequestParam Integer userId,
            @RequestParam Long time) {
        String updatedReportPath = reportService.getUpdatedReportPath(meetingId, osVersion, time);

        if (!updatedReportPath.isEmpty()) {
            File file = new File(updatedReportPath);
            if (file.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new FileSystemResource(file));
            }
        }

        return ResponseEntity.ok("");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadReport(@RequestParam Integer meetingId,
                                               @RequestParam Integer osVersion,
                                               @RequestParam("file") MultipartFile file) {
        try {
            String message = reportService.saveReport(meetingId, osVersion, file);
            return ResponseEntity.ok(message);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
        }
    }
}