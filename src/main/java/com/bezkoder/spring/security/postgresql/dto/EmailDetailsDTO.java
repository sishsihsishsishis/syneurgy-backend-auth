package com.bezkoder.spring.security.postgresql.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EmailDetailsDTO {
    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    public EmailDetailsDTO() {}

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getMsgBody() { return msgBody; }
    public void setMsgBody(String msgBody) { this.msgBody = msgBody; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getAttachment() { return attachment; }
    public void setAttachment(String attachment) { this.attachment = attachment; }
}
