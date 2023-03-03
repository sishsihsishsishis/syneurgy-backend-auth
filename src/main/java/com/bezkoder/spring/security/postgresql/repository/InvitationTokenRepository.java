package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.InvitationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationTokenRepository extends JpaRepository<InvitationToken, String> {
//    InvitationToken findByInvitationToken(String invitationToken);
}
