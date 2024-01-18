package com.bezkoder.spring.security.postgresql.payload.request;

import java.util.List;

public class MatchUserIdRequest {
    private List<MatchUserIdInfo> info;
    private Long meetingId;

    public List<MatchUserIdInfo> getInfo() {
        return info;
    }

    public void setInfo(List<MatchUserIdInfo> info) {
        this.info = info;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }
}
