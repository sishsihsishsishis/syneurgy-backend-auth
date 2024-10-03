package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.repository.ReportRepository;
import com.bezkoder.spring.security.postgresql.models.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.io.File;
import java.io.IOException;

@Service
public class ReportService {

    @Value("${report.upload.dir}")
    private String uploadDir;

    @Autowired
    private ReportRepository reportRepository;

    public String getUpdatedReportPath(Integer meetingId, Integer osVersion, Long time) {
        Optional<Report> reportOpt = reportRepository.findByMeetingIdAndOsVersion(meetingId, osVersion);

        if (reportOpt.isPresent()) {
            Report report = reportOpt.get();
            String reportPath = report.getReportPath();

            // Parse the current file name
            File reportFile = new File(reportPath);
            String originalFileName = reportFile.getName();

            // Convert timestamp to local date
            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
            String formattedDate = date.format(formatter);

            // Rename the file with updated date
            String newFileName = originalFileName.replaceFirst("\\d{2}_\\d{2}_\\d{4}", formattedDate);
            String newFilePath = reportFile.getParent() + File.separator + newFileName;

            // Rename the file on disk
            File renamedFile = new File(newFilePath);
            if (reportFile.renameTo(renamedFile)) {
                // Update the report record with the new file path
                report.setReportPath(newFilePath);
                reportRepository.save(report);
            }

            // Return the updated file path for downloading
            return newFilePath;
        }

        return ""; // No report found
    }


    public String saveReport(Integer meetingId, Integer osVersion, MultipartFile file) throws IOException {
        String REPORT_DIRECTORY = System.getProperty("user.dir") + uploadDir;
        File directory = new File(REPORT_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }


        String fileName = file.getOriginalFilename().replace("/", "_");

//        String formattedDate = new SimpleDateFormat("MM-dd-yyyy").format(new Date());  // Use dashes or another valid separator
//        String fileName1 = "SyneurgyTPR_GitLab_PMM_" + formattedDate + ".pdf";
        String filePath = directory.getAbsolutePath() + File.separator + fileName;

        Optional<Report> existingReport = reportRepository.findByMeetingIdAndOsVersion(meetingId, osVersion);
        if (existingReport.isPresent()) {
            File existingFile = new File(existingReport.get().getReportPath());
            if (existingFile.exists()) {
                existingFile.delete();  // Delete the existing file
            }

            // Update the existing report
            Report reportToUpdate = existingReport.get();
            reportToUpdate.setReportPath(filePath);
            reportToUpdate.setCreatedDate(new Date());
            reportRepository.save(reportToUpdate);  // Update the existing record
        } else {
            Report newReport = new Report();
            newReport.setMeetingId(meetingId);
            newReport.setOsVersion(osVersion);
            newReport.setReportPath(filePath);
            newReport.setCreatedDate(new Date());
            reportRepository.save(newReport);
        }

        try {
            File dest = new File(filePath);
            file.transferTo(dest);
        } catch (IOException e) {
            // Handle and log the IOException
            e.printStackTrace();
        } catch (SecurityException e) {
            // Handle SecurityException (file system security issues)
            e.printStackTrace();
        }

        return "File uploaded successfully!";
    }
}
