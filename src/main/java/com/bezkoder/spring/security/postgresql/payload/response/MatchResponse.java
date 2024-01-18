package com.bezkoder.spring.security.postgresql.payload.response;

import com.bezkoder.spring.security.postgresql.models.MatchEntity;

import java.util.ArrayList;
import java.util.List;

public class MatchResponse {
    private Long id;
    private Long userId;
    private String username;
    private String speaker;
    private Long meetingId;
    private Long createdDate;
    private Long updatedDate;

    public MatchResponse(Long id, Long userId, String username, String speaker, Long meetingId, Long createdDate, Long updatedDate) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.speaker = speaker;
        this.meetingId = meetingId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public static List<MatchResponse> listFromEntities(List<MatchEntity> entities) {
        List<MatchResponse> responses = new ArrayList<>();
        for (MatchEntity entity : entities) {
            responses.add(new MatchResponse(
                    entity.getId(),
                    entity.getUserId(),
                    entity.getUsername(),
                    entity.getSpeaker(),
                    entity.getMeetingId(),
                    entity.getCreatedDate().getTime(),
                    entity.getUpdatedDate().getTime()
            ));
        }
        return responses;
    }

    public static MatchResponse convertMatchEntity(MatchEntity entity) {
        MatchResponse response = new MatchResponse(
                entity.getId(),
                entity.getUserId(),
                entity.getUsername(),
                entity.getSpeaker(),
                entity.getMeetingId(),
                entity.getCreatedDate().getTime(),
                entity.getUpdatedDate().getTime()
        );

        return response;
    }

}
