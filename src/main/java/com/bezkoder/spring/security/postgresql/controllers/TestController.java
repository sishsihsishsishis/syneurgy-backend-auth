package com.bezkoder.spring.security.postgresql.controllers;

import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.MessageResponse;
import com.postmarkapp.postmark.client.data.model.templates.TemplatedMessage;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import io.sentry.Sentry;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.Executors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    private static final String STORAGE_DIR = "D:/video-storage/"; // Change this as needed

    @GetMapping("/all")
    public String allAccess() {

        // ApiClient client =
        // Postmark.getApiClient("2274a4ca-df74-4850-8b4c-06d1da6c14a2");
        // Message message = new Message("notifications@syneurgy.com",
        // "anbraun117@gmail.com", "Join your team!", " invited you to collaborate in "
        // + " \n http://127.0.0.1:5173/confirm-invitation?token=");
        // try {
        // com.postmarkapp.postmark.client.data.model.message.MessageResponse response =
        // client.deliverMessage(message);
        //
        // int sss = 1;
        // } catch (PostmarkException e) {
        // throw new RuntimeException(e);
        // } catch (IOException e) {
        // throw new RuntimeException(e);
        // }

        try {
            throw new Exception("This is a test");
        } catch (Exception e) {
            Sentry.captureException(e);
        }

        return "Public Content111.";
    }

    @GetMapping("/test")
    public String test() {
        ApiClient client = Postmark.getApiClient("2274a4ca-df74-4850-8b4c-06d1da6c14a2");
        TemplatedMessage message = new TemplatedMessage("notifications@syneurgy.com", "anbraun117@gmail.com");
        message.setTemplateId(31950979);

        HashMap model = new HashMap<String, Object>();
        model.put("invite_receiver_email", "anbraun117@gmail.com");
        model.put("action_url", "http://localhost:5173");
        message.setTemplateModel(model);

        try {
            MessageResponse response = client.deliverMessageWithTemplate(message);
            int sss = 1;
            return response.toString();
        } catch (PostmarkException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // return "Private Content";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    private void extractAndPrintProgress(String logLine) {
        // Regex to extract "time=00:00:10.25" (current video processing time)
        Pattern pattern = Pattern.compile("time=(\\d+):(\\d+):(\\d+).(\\d+)");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            int hours = Integer.parseInt(matcher.group(1));
            int minutes = Integer.parseInt(matcher.group(2));
            int seconds = Integer.parseInt(matcher.group(3));
            int milliseconds = Integer.parseInt(matcher.group(4));

            System.out.printf("🔹 Progress: %02d:%02d:%02d.%d\n", hours, minutes, seconds, milliseconds);
        }
    }

    @PostMapping(value = "/mov-to-mp4", consumes = "multipart/form-data")
    public ResponseEntity<String> convertAndSave(@RequestParam("file") MultipartFile file) {
        try {
            // Ensure storage directory exists
            Files.createDirectories(Path.of(STORAGE_DIR));

            // Save the uploaded file first
            String inputFilename = System.currentTimeMillis() + "-uploaded.mov";
            Path inputPath = Path.of(STORAGE_DIR, inputFilename);
            Files.copy(file.getInputStream(), inputPath);

            // Generate output filename
            String outputFilename = System.currentTimeMillis() + "-converted.mp4";
            Path outputPath = Path.of(STORAGE_DIR, outputFilename);

            // FFmpeg command optimized for speed with progress output
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg", "-y", // Overwrite output if exists
                    "-i", inputPath.toString(), // Read from saved file
                    "-preset", "ultrafast", // Faster encoding
                    "-b:v", "5000k", // Video bitrate (reduce for smaller file, increase for better quality)
                    "-b:a", "256k", // Audio bitrate
                    "-vcodec", "libx264",
                    "-acodec", "aac",
                    "-movflags", "+faststart", // Optimize for fast streaming
                    "-progress", "pipe:1", // Enable progress output
                    outputPath.toString());

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Capture FFmpeg progress
            Executors.newSingleThreadExecutor().submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line); // Print all FFmpeg logs (optional)

                        // Extract and print conversion progress
                        extractAndPrintProgress(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Wait for FFmpeg to finish
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return ResponseEntity.status(500).body("FFmpeg failed with exit code: " + exitCode);
            }

            return ResponseEntity.ok("Success! File saved as: " + outputFilename);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
