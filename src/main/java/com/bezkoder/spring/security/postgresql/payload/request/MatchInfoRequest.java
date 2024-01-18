package com.bezkoder.spring.security.postgresql.payload.request;

import java.util.List;

public class MatchInfoRequest {
    private List<MatchInfo> info;
    private Long meetingId;

    public List<MatchInfo> getInfo() {
        return info;
    }

    public void setInfo(List<MatchInfo> info) {
        this.info = info;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() {
        return meetingId;
    }
}
