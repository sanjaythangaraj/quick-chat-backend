package com.example.quick_chat_back.infrastructure.secondary.message;

import java.util.List;
import java.util.UUID;

public record ConversationViewedForNotification(
    UUID conversationId,
    List<UUID> messageIdsViewed
) {
}