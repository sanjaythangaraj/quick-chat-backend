package com.example.quick_chat_back.infrastructure.primary.user;

import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import org.jilt.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record RestUser(
    UUID publicId,
    String firstName,
    String lastName,
    String email,
    String imageUrl,
    Set<RestAuthority> authorities
) {

  static RestUser from(User user) {
    RestUserBuilder restUserBuilder = RestUserBuilder.restUser();

    if (user.getImageUrl() != null) {
      restUserBuilder.imageUrl(user.getImageUrl().value());
    }

    return restUserBuilder
        .email(user.getEmail().value())
        .firstName(user.getFirstname().value())
        .lastName(user.getLastName().value())
        .publicId(user.getUserPublicId().value())
        .authorities(RestAuthority.from(user.getAuthorities()))
        .build();
  }
}
