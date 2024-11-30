package com.example.quick_chat_back.infrastructure.primary.conversation;

import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import org.jilt.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record RestUserForConversation(
    String lastName,
    String firstName,
    UUID publicId,
    String imageUrl,
    Instant lastSeen) {

  public static RestUserForConversation from(User user) {
    RestUserForConversationBuilder restUserForConversationBuilder = RestUserForConversationBuilder.restUserForConversation();

    if (user.getImageUrl() != null) {
      restUserForConversationBuilder.imageUrl(user.getImageUrl().value());
    }

    return restUserForConversationBuilder
        .lastName(user.getLastName().value())
        .firstName(user.getFirstname().value())
        .publicId(user.getUserPublicId().value())
        .lastSeen(user.getLastSeen())
        .build();
  }

  public static List<RestUserForConversation> from(Set<User> users) {
    return users.stream().map(RestUserForConversation::from).toList();
  }
}
