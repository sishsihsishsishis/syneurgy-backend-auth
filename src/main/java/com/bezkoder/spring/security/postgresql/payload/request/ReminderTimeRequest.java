package com.bezkoder.spring.security.postgresql.payload.request;

public class ReminderTimeRequest {
    private Long[] meeting_ids;
    private Long reminderTime;

    public Long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Long reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Long[] getMeeting_ids() {
        return meeting_ids;
    }

    public void setMeeting_ids(Long[] meeting_ids) {
        this.meeting_ids = meeting_ids;
    }
}
