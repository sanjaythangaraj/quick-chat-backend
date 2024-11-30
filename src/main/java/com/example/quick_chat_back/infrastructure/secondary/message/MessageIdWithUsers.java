package com.example.quick_chat_back.infrastructure.secondary.message;

import com.example.quick_chat_back.messaging.domain.user.vo.UserPublicId;

import java.util.List;

public record MessageIdWithUsers(ConversationViewedForNotification conversationViewedForNotification,
                                 List<UserPublicId> usersToNotify) {
}
