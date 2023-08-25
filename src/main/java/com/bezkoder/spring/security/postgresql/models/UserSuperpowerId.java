package com.bezkoder.spring.security.postgresql.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserSuperpowerId implements Serializable {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "superpower_id")
    Long superpowerId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSuperpowerId() {
        return superpowerId;
    }

    public void setSuperpowerId(Long superpowerId) {
        this.superpowerId = superpowerId;
    }
}
