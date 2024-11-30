package com.example.quick_chat_back.messaging.domain.user.service;

import com.example.quick_chat_back.messaging.domain.message.vo.ConversationPublicId;
import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import com.example.quick_chat_back.messaging.domain.user.repository.UserRepository;
import com.example.quick_chat_back.messaging.domain.user.vo.UserEmail;
import com.example.quick_chat_back.messaging.domain.user.vo.UserPublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UserReader {

  private final UserRepository userRepository;

  public UserReader(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> getByEmail(UserEmail email) {
    return userRepository.getOneByEmail(email);
  }

  public List<User> getUsersByPublicIds(Set<UserPublicId> userPublicIds) {
    return userRepository.getByPublicIds(userPublicIds);
  }

  public Page<User> search(Pageable pageable, String query) {
    return userRepository.search(pageable, query);
  }

  public Optional<User> getByPublicId(UserPublicId userPublicId) {
    return userRepository.getOneByPublicId(userPublicId);
  }

  public List<User> findUsersToNotify(ConversationPublicId conversationPublicId, UserPublicId readerPublicId) {
    return userRepository.getRecipientByConversationIdExcludingReader(conversationPublicId, readerPublicId);
  }
}
