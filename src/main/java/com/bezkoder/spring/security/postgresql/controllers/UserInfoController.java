package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.ConfirmInviteRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UserInfoRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UserZoomRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.TokenResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserInfoResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.AuthTokenFilter;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/userinfo")
@Api(tags = "User Information")
public class UserInfoController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder encoder;

    @PutMapping("/basic")
    public ResponseEntity<?> fillBasicInfo(@Valid @RequestBody UserInfoRequest userInfoRequest,
            @RequestHeader(name = "Authorization") String token) {

        String username = jwtUtils.getExistingUsername(token);

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (userInfoRequest.getFirstName() != null) {
                user.setFirstName(userInfoRequest.getFirstName());
            }
            if (userInfoRequest.getLastName() != null) {
                user.setLastName(userInfoRequest.getLastName());
            }
            if (userInfoRequest.getCountry() != null) {
                user.setCountry(userInfoRequest.getCountry());
            }
            if (userInfoRequest.getCompany() != null) {
                user.setCompany(userInfoRequest.getCompany());
            }
            if (userInfoRequest.getPosition() != null) {
                user.setPosition(userInfoRequest.getPosition());
            }

            if (userInfoRequest.getCountryCode() != null) {
                user.setCountryCode(userInfoRequest.getCountryCode());
            }

            userRepository.save(user);

            return ResponseEntity.ok(new UserInfoResponse(user.getFirstName(), user.getLastName(), user.getCountry(),
                    user.getCountryCode(), user.getCompany(), user.getPosition(), user.getStep(), user.getPhoto(),
                    user.getAnswers()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }

    }

    @GetMapping("/basic")
    public ResponseEntity<?> getUserInfo(@RequestHeader(name = "Authorization") String token) {

        String username = jwtUtils.getExistingUsername(token);

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return ResponseEntity.ok(new UserInfoResponse(user.getFirstName(), user.getLastName(), user.getCountry(),
                    user.getCountryCode(), user.getCompany(), user.getPosition(), user.getStep(), user.getPhoto(),
                    user.getAnswers()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }

    }

    @PutMapping("/setup-password")
    public ResponseEntity<?> confirmInvitation(@Valid @RequestBody UserInfoRequest userInfoRequest,
            @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();

        if (userInfoRequest.getPassword() != null) {
            currentUser.setPassword(encoder.encode(userInfoRequest.getPassword()));
        }
        userRepository.save(currentUser);

        return new ResponseEntity<>("Password successfully updated", HttpStatus.OK);
    }

    @PutMapping("/skip-step")
    public ResponseEntity<?> skipStep(@Valid @RequestBody UserInfoRequest userInfoRequest,
            @RequestHeader(name = "Authorization") String token) {

        Integer step = userInfoRequest.getStep();
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();
        if (step == -2) {
            currentUser.setStep(currentUser.getStep() + 1);
        } else {
            currentUser.setStep(step + 1);
        }

        User savedUser = userRepository.save(currentUser);
        return ResponseEntity.ok(new UserInfoResponse(savedUser.getFirstName(), savedUser.getLastName(),
                savedUser.getCountry(), savedUser.getCountryCode(), savedUser.getCompany(), savedUser.getPosition(),
                savedUser.getStep(), savedUser.getPhoto(), savedUser.getAnswers()));
    }

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/uploads";

    // public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") +
    // "/uploads";
    @PostMapping("/photo")
    public ResponseEntity<?> uploadProfiePhoto(@RequestParam("file") MultipartFile multipartFile,
            @RequestHeader(name = "Authorization") String token) throws IOException {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();

        StringBuilder fileNames = new StringBuilder();
        StringBuilder path = new StringBuilder();
        path.append("user");
        path.append("_");
        path.append(currentUser.getId().toString());
        path.append("_");
        path.append(multipartFile.getOriginalFilename());
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, path.toString());
        fileNames.append("uploads/");
        fileNames.append("user");
        fileNames.append("_");
        fileNames.append(currentUser.getId().toString());
        fileNames.append("_");
        fileNames.append(multipartFile.getOriginalFilename());

        Files.write(fileNameAndPath, multipartFile.getBytes());

        String name = fileNames.toString();
        currentUser.setPhoto(name);
        User savedUser = userRepository.save(currentUser);

        return ResponseEntity.ok(new UserInfoResponse(savedUser.getFirstName(), savedUser.getLastName(),
                savedUser.getCountry(), savedUser.getCountryCode(), savedUser.getCompany(), savedUser.getPosition(),
                savedUser.getStep(), savedUser.getPhoto(), savedUser.getAnswers()));
    }

    @GetMapping("/img/{fileName}")
    public ResponseEntity<?> getProfilePhoto(@PathVariable String fileName,
            @RequestHeader(name = "Authorization") String token) throws IOException {
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, fileName);
        byte[] images = Files.readAllBytes(new File(fileNameAndPath.toUri()).toPath());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }

    @DeleteMapping("/photo")
    public ResponseEntity<?> DeleteProfiePhoto(@RequestHeader(name = "Authorization") String token) throws IOException {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();
        String FILE_DIRECTORY = System.getProperty("user.dir") + "/src/main/";
        Path fileNameAndPath = Paths.get(FILE_DIRECTORY, currentUser.getPhoto());
        // String FILE_DIRECTORY = System.getProperty("user.dir") + "/";
        // Path fileNameAndPath = Paths.get(FILE_DIRECTORY, currentUser.getPhoto());
        boolean isDeleted = Files.deleteIfExists(fileNameAndPath);
        if (isDeleted) {
            currentUser.setPhoto("");
        }
        User savedUser = userRepository.save(currentUser);
        return ResponseEntity.ok(new UserInfoResponse(savedUser.getFirstName(), savedUser.getLastName(),
                savedUser.getCountry(), savedUser.getCountryCode(), savedUser.getCompany(), savedUser.getPosition(),
                savedUser.getStep(), savedUser.getPhoto(), savedUser.getAnswers()));
    }

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @PostMapping("/zoom-access")
    public ResponseEntity<?> GetZoomAccess(@Valid @RequestBody UserZoomRequest zoomRequest,
            @RequestHeader(name = "Authorization") String token) throws IOException {

        logger.error("=======> zoom access token <=======");
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }

        User user = existingUser.get();
        String zoomUrl = "https://zoom.us/oauth/token?grant_type=account_credentials&account_id="
                + zoomRequest.getZoomAccountId();

        // Create Authorization Header
        String auth = zoomRequest.getZoomClientId() + ":" + zoomRequest.getZoomClientSecret();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        System.out.println("request url: " + zoomUrl);
        System.out.println("encodedAuth: " + encodedAuth);
        HttpHeaders headers = new HttpHeaders();        
        headers.set("Authorization", "Basic " + encodedAuth);
        // headers.set("Authorization", "Basic aWdWTFdqV2pTQ0tOWDNHQkdMcDJVZzo1bkpKUzYwMmJjOUF0eVhkYzJEa2pUVEpPN2t0cFFPVQ==");
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {            
            // Log request and response details for debugging
            System.out.println("Request: " + request);
            // Use WebClient instead of RestTemplate in newer versions of Spring
            ResponseEntity<String> response = new RestTemplate().postForEntity(zoomUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse response body
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseBody = objectMapper.readTree(response.getBody());
                System.out.println("Token Response: " + response.getBody());

                // Extract fields from response
                String accessToken = responseBody.get("access_token").asText();
                user.setZoomAccountId(zoomRequest.getZoomAccountId());
                user.setZoomClientId(zoomRequest.getZoomClientId());
                user.setZoomClientSecret(zoomRequest.getZoomClientSecret());
                userRepository.save(user);

                // Create custom response object
                // TokenResponse tokenResponse = new TokenResponse(accessToken, expiresIn, refreshToken);

                Date currentDate = new Date();
                // Create a Calendar instance and set it to the current date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                // Subtract one month from the current date
                calendar.add(Calendar.MONTH, -1);
                // Get the updated date (one month ago)
                Date oneMonthAgo = calendar.getTime();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                 // Format the date into a string
                String formattedDate = dateFormat.format(oneMonthAgo);

                zoomUrl = "https://api.zoom.us/v2/users/me/recordings?page_size=100&from="
                + formattedDate;


                headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + accessToken);
                headers.set("Content-Type", "application/json");

                request = new HttpEntity<>(headers);

                ResponseEntity<String> response1 = new RestTemplate().exchange(zoomUrl, HttpMethod.GET, request, String.class);

                if (response1.getStatusCode() == HttpStatus.OK) {
                    // System.out.println("Final Response: " + response1.getBody());
                    return ResponseEntity.ok(response1.getBody());
                }
                // Return response entity
                return ResponseEntity.status(response1.getStatusCode()).body(null);
            } else {
                // Handle error cases (e.g., return an error response)
                return ResponseEntity.status(response.getStatusCode()).body(null);
            }

        } catch (HttpClientErrorException.BadRequest badRequestException) {
            // Log additional details for Bad Request exception
            System.err.println("Bad Request Exception Details: " + badRequestException.getResponseBodyAsString());
            throw badRequestException;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // return ResponseEntity.ok(zoomRequest);
    }
}
