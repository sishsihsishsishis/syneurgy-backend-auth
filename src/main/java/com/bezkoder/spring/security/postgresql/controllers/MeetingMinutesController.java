package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.MeetingMinutes;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserMinutes;
import com.bezkoder.spring.security.postgresql.payload.request.InitializeMeetingMinutesRequest;
import com.bezkoder.spring.security.postgresql.payload.request.MeetingMinutesRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.MeetingMinutesRepository;
import com.bezkoder.spring.security.postgresql.repository.UserMinutesRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.*;
import java.nio.file.Files;
import java.io.*;

import java.util.*;

@RestController
@RequestMapping("/api/meetings")
public class MeetingMinutesController {

    @Value("${video.upload.dir}")
    private String videoUploadDir;

    @Autowired
    private MeetingMinutesRepository meetingMinutesRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMinutesRepository userMinutesRepository;

    @PostMapping("/initialize-meeting-minutes")
    public ResponseEntity<?> initializeMeetingMinutes(@RequestBody InitializeMeetingMinutesRequest request) {
        // Check if a record with userId and meetingId already exists
        Optional<MeetingMinutes> existingMeetingMinutes = meetingMinutesRepository
                .findByUserIdAndMeetingId(request.getUserId(), request.getMeetingId());

        if (!existingMeetingMinutes.isPresent()) {
            // If the record doesn't exist, create a new one with meeting_minutes set to 0
            MeetingMinutes meetingMinutes = new MeetingMinutes();
            meetingMinutes.setUserId(request.getUserId());
            meetingMinutes.setMeetingId(request.getMeetingId());
            meetingMinutes.setMeetingMinutes(0);

            // Save the new record in the database
            meetingMinutesRepository.save(meetingMinutes);

            return ResponseEntity.ok("Meeting minutes initialized successfully.");
        } else {
            // If the record exists, do nothing
            return ResponseEntity.ok("Meeting minutes already exist for this user and meeting.");
        }
    }

    @PostMapping("/minutes")
    public ResponseEntity<?> saveMeetingMinutes(@RequestBody MeetingMinutesRequest request,
            @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }

        List<MeetingMinutes> updatedMeetingMinutesList = new ArrayList<>();

        // Iterate over each meeting in the request
        for (MeetingMinutesRequest.Meeting meeting : request.getMeetings()) {
            // Check if a record with userId and meetingId exists
            Optional<MeetingMinutes> existingMeetingMinutes = meetingMinutesRepository
                    .findByUserIdAndMeetingId(request.getUserId(), meeting.getMeetingId());

            if (existingMeetingMinutes.isPresent()) {
                // If the record exists, update the meeting minutes
                MeetingMinutes meetingMinutes = existingMeetingMinutes.get();
                meetingMinutes.setMeetingMinutes(meeting.getMinutes());
                updatedMeetingMinutesList.add(meetingMinutes);
            }
        }

        // Save the updated meeting minutes
        if (!updatedMeetingMinutesList.isEmpty()) {
            meetingMinutesRepository.saveAll(updatedMeetingMinutesList);
        }

        // Step 2: Calculate total minutes consumed by the user
        Integer totalConsumedMinutes = meetingMinutesRepository.sumMeetingMinutesByUserId(request.getUserId());
        Integer totalConsumedMinutesSafe = totalConsumedMinutes != null ? totalConsumedMinutes : 0;
        // Step 3: Update the consumed_minutes in user_minutes table
        Optional<UserMinutes> userMinutes1 = userMinutesRepository.findByUserId(request.getUserId());
        if (!userMinutes1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("There is no user minutes."));
        }
        UserMinutes userMinutes = userMinutes1.get();
        userMinutes.setConsumedMinutes(userMinutes.getConsumedMinutes() + totalConsumedMinutesSafe);
        userMinutesRepository.save(userMinutes);
        // Step 4: Return the updated user_minutes details
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", userMinutes.getUserId());
        response.put("all_minutes", userMinutes.getAllMinutes());
        response.put("added_minutes", userMinutes.getAddedMinutes());
        response.put("consumed_minutes", userMinutes.getConsumedMinutes());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/compress-video")
    public ResponseEntity<?> compressVideoandUpload(
            @RequestParam("data") MultipartFile chunk,
            @RequestParam("uploadId") String uploadId,
            @RequestParam("upload-path") String uploadPath,
            @RequestParam("partNumber") String partNumber,
            @RequestParam("chunkCount") Integer chunkCount,
            @RequestHeader(name = "Authorization") String token) {

        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);
        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }

        String STORAGE_DIR = videoUploadDir + "/";

        try {
            // Step 1: Save incoming chunk
            File uploadDir = new File(STORAGE_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            File chunkFile = new File(uploadDir, "chunk-" + partNumber);
            chunk.transferTo(chunkFile);

            // Check if all chunks have been uploaded (this is an assumption; you may need a
            // better check)
            File[] chunkFiles = uploadDir.listFiles();
            if (chunkFiles == null || chunkFiles.length < chunkCount) { // Replace '5' with the expected number of
                                                                        // chunks
                return ResponseEntity.ok("Chunk " + partNumber + " uploaded successfully.");
            }

            // Step 2: Merge chunks into one .mov file
            File mergedFile = new File(STORAGE_DIR + uploadId + "/merged.mov");
            try (FileOutputStream fos = new FileOutputStream(mergedFile)) {
                Arrays.sort(chunkFiles, Comparator.comparingInt(f -> Integer.parseInt(f.getName().split("-")[1])));
                for (File chunkPart : chunkFiles) {
                    Files.copy(chunkPart.toPath(), fos);
                    chunkPart.delete();
                }
            }

            // Step 3: Convert the merged .mov to .mp4 using FFmpeg
            File outputMp4 = new File(STORAGE_DIR + uploadId + "/converted.mp4");

            // ProcessBuilder processBuilder = new ProcessBuilder(
            // "ffmpeg", "-i", mergedFile.getAbsolutePath(), "-c:v", "libx264", "-preset",
            // "ultrafast",
            // "-movflags", "faststart", outputMp4.getAbsolutePath());

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg", "-y", // Overwrite output if exists
                    "-i", mergedFile.toString(), // Read from saved file
                    "-preset", "ultrafast", // Faster encoding
                    "-b:v", "2048k", // Video bitrate (reduce for smaller file, increase for better quality)
                    "-b:a", "256k", // Audio bitrate
                    "-vcodec", "libx264",
                    "-acodec", "aac",
                    "-movflags", "+faststart", // Optimize for fast streaming
                    "-progress", "pipe:1", // Enable progress output
                    outputMp4.toString());

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Capture FFmpeg output for debugging
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return ResponseEntity.status(500).body("FFmpeg conversion failed.");
            }
            mergedFile.delete();

            // Step 4: Split the .mp4 file and upload chunks to another server
            long chunkSize = 10 * 1024 * 1024; // 10 MB

            // String[] egZs = [];
            List<String> tag_list = new ArrayList<String>();

            try (RandomAccessFile raf = new RandomAccessFile(outputMp4, "r")) {
                long totalSize = outputMp4.length();
                int partNumberForUpload = 0;

                while (raf.getFilePointer() < totalSize) {
                    long remaining = totalSize - raf.getFilePointer();
                    long size = Math.min(chunkSize, remaining);

                    byte[] buffer = new byte[(int) size];
                    raf.readFully(buffer);
                    final int currentPartNumber = partNumberForUpload + 1;

                    // Prepare the chunk as ByteArrayResource
                    ByteArrayResource resource = new ByteArrayResource(buffer) {
                        @Override
                        public String getFilename() {
                            return "part-" + currentPartNumber;
                        }
                    };

                    // Send the chunk to the target server
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                    body.add("data", resource);
                    body.add("uploadId", uploadId);
                    body.add("partNumber", partNumberForUpload + 1);

                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.postForEntity(uploadPath, requestEntity,
                            String.class);

                    if (!response.getStatusCode().is2xxSuccessful()) {
                        return ResponseEntity.status(500).body("Failed to upload part " + partNumberForUpload);
                    }

                    System.out.println(response.getBody());
                    tag_list.add(response.getBody());

                    partNumberForUpload++;
                }
            }
            outputMp4.delete();
            return ResponseEntity.ok(tag_list);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred during video processing.");
        }

    }

}
