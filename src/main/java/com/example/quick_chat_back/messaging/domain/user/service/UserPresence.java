package com.example.quick_chat_back.messaging.domain.user.service;

import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import com.example.quick_chat_back.messaging.domain.user.repository.UserRepository;
import com.example.quick_chat_back.messaging.domain.user.vo.UserPublicId;

import java.time.Instant;
import java.util.Optional;

public class UserPresence {
  private final UserRepository userRepository;
  private final UserReader userReader;

  public UserPresence(UserRepository userRepository, UserReader userReader) {
    this.userRepository = userRepository;
    this.userReader = userReader;
  }

  public void updatePresence(UserPublicId userPublicId) {
    userRepository.updateLastSeenByPublicId(userPublicId, Instant.now());
  }

  public Optional<Instant> getLastSeenByPublicId(UserPublicId userPublicId) {
    Optional<User> userOptional = userReader.getByPublicId(userPublicId);
    return userOptional.map(User::getLastSeen);
  }
}
