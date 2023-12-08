package com.bezkoder.spring.security.postgresql.payload.request;

public class UserComponentConferenceRequest {
    private Long user_component_id;
    private ConferenceRequest[] conferences;

    public void setUser_component_id(Long user_component_id) {
        this.user_component_id = user_component_id;
    }

    public Long getUser_component_id() {
        return user_component_id;
    }

    public ConferenceRequest[] getConferences() {
        return conferences;
    }

    public void setConferences(ConferenceRequest[] conferences) {
        this.conferences = conferences;
    }


}
