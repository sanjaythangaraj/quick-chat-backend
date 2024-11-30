package com.example.quick_chat_back.messaging.domain.message.service;

import com.example.quick_chat_back.messaging.domain.message.aggregate.Conversation;
import com.example.quick_chat_back.messaging.domain.message.repository.ConversationRepository;
import com.example.quick_chat_back.messaging.domain.message.vo.ConversationPublicId;
import com.example.quick_chat_back.messaging.domain.user.vo.UserPublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class ConversationReader {

  private final ConversationRepository conversationRepository;

  public ConversationReader(ConversationRepository conversationRepository) {
    this.conversationRepository = conversationRepository;
  }

  public Page<Conversation> getAllByUserPublicId(UserPublicId userPublicId, Pageable pageable) {
    return conversationRepository.getConversationByUserPublicId(userPublicId, pageable);
  }

  public Optional<Conversation> getOneByPublicId(ConversationPublicId conversationPublicId) {
    return conversationRepository.getOneByPublicId(conversationPublicId);
  }

  public Optional<Conversation> getOneByPublicIdAndUserId(ConversationPublicId conversationPublicId, UserPublicId userPublicId) {
    return conversationRepository.getConversationByUserPublicIdAndPublicId(userPublicId, conversationPublicId);
  }
}
