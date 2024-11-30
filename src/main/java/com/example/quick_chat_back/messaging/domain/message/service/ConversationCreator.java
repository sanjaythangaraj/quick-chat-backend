package com.example.quick_chat_back.messaging.domain.message.service;

import com.example.quick_chat_back.messaging.domain.message.aggregate.Conversation;
import com.example.quick_chat_back.messaging.domain.message.aggregate.ConversationToCreate;
import com.example.quick_chat_back.messaging.domain.message.repository.ConversationRepository;
import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import com.example.quick_chat_back.messaging.domain.user.service.UserReader;
import com.example.quick_chat_back.messaging.domain.user.vo.UserPublicId;
import com.example.quick_chat_back.shared.service.State;

import java.util.List;
import java.util.Optional;

public class ConversationCreator {
  private final ConversationRepository conversationRepository;
  private final UserReader userReader;

  public ConversationCreator(ConversationRepository conversationRepository, UserReader userReader) {
    this.conversationRepository = conversationRepository;
    this.userReader = userReader;
  }

  public State<Conversation, String> create(ConversationToCreate newConversation, User authenticatedUser) {
    newConversation.getMembers().add(authenticatedUser.getUserPublicId());
    List<User> members = userReader.getUsersByPublicIds(newConversation.getMembers());
    List<UserPublicId> membersPublicIds = members.stream().map(User::getUserPublicId).toList();
    Optional<Conversation> conversationOptional = conversationRepository.getConversationByUserPublicIds(membersPublicIds);
    State<Conversation, String> stateResult;
    if (conversationOptional.isEmpty()) {
      Conversation conversation = conversationRepository.save(newConversation, members);
      stateResult = State.<Conversation, String>builder().forSuccess(conversation);
    } else {
      stateResult = State.<Conversation, String>builder().forError("This conversation already exists");
    }

    return stateResult;
  }
}
