package com.swapll.gradu.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatSummaryDTO {
    private Integer chatId;
    private Integer otherUserId;
    private String otherUsername;
    private String otherPicture;
    private String lastMessage;
    private String lastMessageTime;
    // Getters and Setters
}
