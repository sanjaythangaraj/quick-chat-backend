package com.example.quick_chat_back.infrastructure.primary.conversation;

import com.example.quick_chat_back.messaging.domain.message.aggregate.ConversationToCreate;
import com.example.quick_chat_back.messaging.domain.message.aggregate.ConversationToCreateBuilder;
import com.example.quick_chat_back.messaging.domain.message.vo.ConversationName;
import com.example.quick_chat_back.messaging.domain.user.vo.UserPublicId;
import org.jilt.Builder;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record RestConversationToCreate(Set<UUID> members, String name) {

  public static ConversationToCreate toDomain(RestConversationToCreate restConversationToCreate) {

    Set<UserPublicId> userPublicIds = restConversationToCreate.members
        .stream()
        .map(UserPublicId::new)
        .collect(Collectors.toSet());

    return ConversationToCreateBuilder.conversationToCreate()
        .name(new ConversationName(restConversationToCreate.name()))
        .members(userPublicIds)
        .build();
  }
}
