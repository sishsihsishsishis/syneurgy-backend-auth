package com.bezkoder.spring.security.postgresql.payload.request;

public class UserZoomRequest {
    private String zoomAccountId;

    private String zoomClientId;

    private String zoomClientSecret;

    public UserZoomRequest(String zoomAccountId, String zoomClientId, String zoomClientSecret) {
        //TODO Auto-generated constructor stub
        this.zoomAccountId = zoomAccountId;
        this.zoomClientId = zoomClientId;
        this.zoomClientSecret = zoomClientSecret;
    }

    public String getZoomAccountId() {
        return zoomAccountId;
    }

    public void setZoomAccountId(String zoomAccountId) {
        this.zoomAccountId = zoomAccountId;
    }

    public String getZoomClientId() {
        return zoomClientId;
    }

    public void setZoomClientId(String zoomClientId) {
        this.zoomClientId = zoomClientId;
    }

    public String getZoomClientSecret() {
        return zoomClientSecret;
    }

    public void setZoomClientSecret(String zoomClientSecret) {
        this.zoomClientSecret = zoomClientSecret;
    }

}
