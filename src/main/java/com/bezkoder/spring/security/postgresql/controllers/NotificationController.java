package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Notification;
import com.bezkoder.spring.security.postgresql.models.UserTeam;
import com.bezkoder.spring.security.postgresql.payload.request.NotificationRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UpdateReadStatusRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.NotificationCountResponse;
import com.bezkoder.spring.security.postgresql.payload.response.NotificationResponse;
import com.bezkoder.spring.security.postgresql.repository.UserTeamRepository;
import com.bezkoder.spring.security.postgresql.service.EmailService;
import com.bezkoder.spring.security.postgresql.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notifications")
@Api(tags = "Notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @Autowired
    UserTeamRepository userTeamRepository;

    @Autowired
    private EmailService emailService;

    @Value("${report_server_url}")
    private String reportServerUrl;

    @Value("${data_server_url}")
    private String dataServerUrl;

    @Value("${frontend_base_url}")
    private String frontendBaseUrl;

    @PostMapping
    public ResponseEntity<?> createNotification(@Valid @RequestBody NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        int type = notificationRequest.getType();
        Long objId = notificationRequest.getObjId();
        notification.setReadStatus(false);
        notification.setType(type);
        notification.setObjId(objId);
        Date currentDate = new Date();
        notification.setCreatedDate(currentDate);
        notification.setUpdatedDate(currentDate);
        if (type == 0) { // Processing the meeting is finished
            String meetingInfoUrl = dataServerUrl + "/meeting/info/" + objId;
            String jsonResponse = new RestTemplate().getForObject(meetingInfoUrl, String.class);
            String meetingName = "";
            String userEmail = "";
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseNode = objectMapper.readTree(jsonResponse);

                // Extract the team ID
                long teamId = responseNode.get("data").get("team_id").asLong();
                meetingName = responseNode.get("data").get("meeting_name").asText();
                List<UserTeam> userTeams = userTeamRepository.findByTeamId(teamId);
                if (!userTeams.isEmpty()) {
                    UserTeam userTeam = userTeams.get(0);
                    Long userId = userTeam.getUser().getId();
                    notification.setUserId(userId);
                    userEmail = userTeam.getUser().getEmail();
                }
                // Now you can use the 'teamId' in your code
                System.out.println("Team ID: " + teamId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            notification.setName("Syneurgy");
            notification.setDescription("Processing a meeting for '" + meetingName + "' is finished.");
            notification.setSenderId(0L);
            notification.setSenderImg("");
//            emailService.sendSimpleEmail(userEmail, "Success", "Processing your meeting is finished successfully. Please look into it and match users." + "\n" + frontendBaseUrl + "/meeting-details/" + objId );
            String syneurgyEmailUrl = reportServerUrl + "/chart?meetingid=" + objId;
            String jsonResponse1 = new RestTemplate().getForObject(syneurgyEmailUrl, String.class);

        }

        Notification newNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(new MessageResponse("Notification is created successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(
                new NotificationResponse(
                        notification.getId(),
                        notification.getType(),
                        notification.getDescription(),
                        notification.getName(),
                        notification.isReadStatus(),
                        notification.getObjId(),
                        notification.getUserId(),
                        notification.getCreatedDate().getTime(),
                        notification.getUpdatedDate().getTime(),
                        notification.getSenderId(),
                        notification.getSenderImg()
                )
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> updateReadStatus(@PathVariable Long id, @RequestBody UpdateReadStatusRequest request) throws ChangeSetPersister.NotFoundException {
        Notification notification = notificationService.updateReadStatus(id, request.isReadStatus());
        return ResponseEntity.ok(
                new NotificationResponse(
                        notification.getId(),
                        notification.getType(),
                        notification.getDescription(),
                        notification.getName(),
                        notification.isReadStatus(),
                        notification.getObjId(),
                        notification.getUserId(),
                        notification.getCreatedDate().getTime(),
                        notification.getUpdatedDate().getTime(),
                        notification.getSenderId(),
                        notification.getSenderImg()
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotificationsByUserId(@PathVariable Long userId, Pageable pageable) {
        Page<Notification> notifications = notificationService.getAllNotificationsByUserId(userId, pageable);
        List<NotificationResponse> notificationResponses = notificationService.getNotificationResponses(notifications);
        Page<NotificationResponse> notificationResponsePage = new PageImpl<>(notificationResponses, pageable, notifications.getTotalElements());
        return new ResponseEntity<>(notificationResponsePage, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<?> getUnreadNotificationCount(@PathVariable Long userId) {
        return new ResponseEntity<>(new NotificationCountResponse(notificationService.getUnreadNotificationCount(userId)), HttpStatus.OK);
    }

    @PutMapping("/mark-all-as-read")
    public ResponseEntity<String> markAllNotificationsAsRead(@RequestParam Long userId) {
        notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.ok("All notifications for user " + userId + " marked as read.");
    }
    @PutMapping("/read")
    public String updateReadStatusForMultipleNotifications(@RequestBody UpdateReadStatusRequest request) {
        List<Long> notificationIds = request.getNotificationIds();
        boolean newReadStatus = request.isReadStatus();

        notificationService.updateReadStatusForMultipleNotifications(notificationIds, newReadStatus);

        return "Read status updated for notifications with IDs: " + notificationIds;
    }

    @DeleteMapping("/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "Notification with ID " + id + " deleted successfully.";
    }
}
