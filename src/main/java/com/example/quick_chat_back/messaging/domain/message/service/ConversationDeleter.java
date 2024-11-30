package com.example.quick_chat_back.messaging.domain.message.service;

import com.example.quick_chat_back.messaging.domain.message.aggregate.Conversation;
import com.example.quick_chat_back.messaging.domain.message.repository.ConversationRepository;
import com.example.quick_chat_back.messaging.domain.message.vo.ConversationPublicId;
import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import com.example.quick_chat_back.shared.service.State;

import java.util.Optional;

public class ConversationDeleter {

  public final ConversationRepository conversationRepository;
  private final MessageChangeNotifier messageChangeNotifier;

  public ConversationDeleter(ConversationRepository conversationRepository, MessageChangeNotifier messageChangeNotifier) {
    this.conversationRepository = conversationRepository;
    this.messageChangeNotifier = messageChangeNotifier;
  }

  public State<ConversationPublicId, String> deleteById(ConversationPublicId conversationPublicId, User connectedUser) {
    State<ConversationPublicId, String> stateResult;

    Optional<Conversation> conversationOptional = this.conversationRepository
        .getConversationByUserPublicIdAndPublicId(connectedUser.getUserPublicId(), conversationPublicId);
    if (conversationOptional.isPresent()) {
      this.conversationRepository.deleteByPublicId(connectedUser.getUserPublicId(), conversationPublicId);
      messageChangeNotifier.delete(conversationPublicId,
          conversationOptional.get().getMembers().stream().map(User::getUserPublicId).toList());
      stateResult = State.<ConversationPublicId, String>builder().forSuccess(conversationPublicId);
    } else {
      stateResult = State.<ConversationPublicId, String>builder().forError("This conversation doesn't belong to this user or doesn't exist");
    }
    return stateResult;
  }
}
