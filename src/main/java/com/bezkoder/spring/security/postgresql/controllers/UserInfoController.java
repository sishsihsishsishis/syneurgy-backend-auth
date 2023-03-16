package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.ConfirmInviteRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UserInfoRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserInfoResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/userinfo")
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
    public ResponseEntity<?> fillBasicInfo(@Valid @RequestBody UserInfoRequest userInfoRequest, @RequestHeader (name="Authorization") String token) {

        String username = jwtUtils.getExistingUsername(token);

        Optional <User> existingUser = userRepository.findByUsername(username);
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
            if (user.getStep() < 1) {
                user.setStep(1);
            }

            userRepository.save(user);

            return ResponseEntity.ok(new UserInfoResponse(user.getFirstName(), user.getLastName(), user.getCountry(), user.getCountryCode(), user.getCompany(), user.getPosition(), user.getStep(), user.getPhoto()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }

    }

    @GetMapping("/basic")
    public ResponseEntity<?> getUserInfo(@RequestHeader (name="Authorization") String token) {

        String username = jwtUtils.getExistingUsername(token);

        Optional <User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return ResponseEntity.ok(new UserInfoResponse(user.getFirstName(), user.getLastName(), user.getCountry(), user.getCountryCode(), user.getCompany(), user.getPosition(), user.getStep(), user.getPhoto()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }

    }

    @PutMapping("/setup-password")
    public ResponseEntity<?> confirmInvitation(@Valid @RequestBody UserInfoRequest userInfoRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();

        if (userInfoRequest.getPassword() != null) {
            currentUser.setPassword(encoder.encode(userInfoRequest.getPassword()));
        }
        userRepository.save(currentUser);

        return new ResponseEntity<>("Password successfully updated", HttpStatus.OK);
    }

    @PutMapping("/skip-step")
    public ResponseEntity<?> skipStep(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();
        currentUser.setStep(currentUser.getStep() + 1);
        User savedUser = userRepository.save(currentUser);
        return ResponseEntity.ok(new UserInfoResponse(savedUser.getFirstName(), savedUser.getLastName(), savedUser.getCountry(), savedUser.getCountryCode(), savedUser.getCompany(), savedUser.getPosition(), savedUser.getStep(), savedUser.getPhoto()));
    }

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/uploads";
//    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    @PostMapping("/photo")
    public ResponseEntity<?> uploadProfiePhoto(@RequestParam("file") MultipartFile multipartFile, @RequestHeader(name = "Authorization") String token) throws IOException {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
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

        return ResponseEntity.ok(new UserInfoResponse(savedUser.getFirstName(), savedUser.getLastName(), savedUser.getCountry(), savedUser.getCountryCode(), savedUser.getCompany(), savedUser.getPosition(), savedUser.getStep(), savedUser.getPhoto()));
    }

    @GetMapping("/img/{fileName}")
    public ResponseEntity<?> getProfilePhoto(@PathVariable String fileName, @RequestHeader(name = "Authorization") String token) throws IOException {
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
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();
        String FILE_DIRECTORY = System.getProperty("user.dir") + "/src/main/";
        Path fileNameAndPath = Paths.get(FILE_DIRECTORY, currentUser.getPhoto());
//        String FILE_DIRECTORY = System.getProperty("user.dir") + "/";
//        Path fileNameAndPath = Paths.get(FILE_DIRECTORY, currentUser.getPhoto());
        boolean isDeleted = Files.deleteIfExists(fileNameAndPath);
        if (isDeleted) {
            currentUser.setPhoto("");
        }
        User savedUser = userRepository.save(currentUser);
        return ResponseEntity.ok(new UserInfoResponse(savedUser.getFirstName(), savedUser.getLastName(), savedUser.getCountry(), savedUser.getCountryCode(), savedUser.getCompany(), savedUser.getPosition(), savedUser.getStep(), savedUser.getPhoto()));
    }
}
