package com.bezkoder.spring.security.postgresql.payload.response;

public class NotificationResponse {
    private Long id;
    private int type;
    private String description;
    private String name;
    private boolean readStatus;
    private Long objId;
    private Long userId;
    private Long createdDate;
    private Long updatedDate;

    public NotificationResponse(Long id, int type, String description, String name, boolean readStatus, Long objId, Long userId, Long createdDate, Long updatedDate) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.name = name;
        this.readStatus = readStatus;
        this.objId = objId;
        this.userId = userId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public Long getObjId() {
        return objId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}


