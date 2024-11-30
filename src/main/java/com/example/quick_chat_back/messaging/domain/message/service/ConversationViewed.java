package com.example.quick_chat_back.messaging.domain.message.service;

import com.example.quick_chat_back.infrastructure.secondary.message.ConversationViewedForNotification;
import com.example.quick_chat_back.messaging.domain.message.aggregate.Message;
import com.example.quick_chat_back.messaging.domain.message.repository.MessageRepository;
import com.example.quick_chat_back.messaging.domain.message.vo.ConversationPublicId;
import com.example.quick_chat_back.messaging.domain.message.vo.MessageSendState;
import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import com.example.quick_chat_back.messaging.domain.user.service.UserReader;
import com.example.quick_chat_back.messaging.domain.user.vo.UserPublicId;
import com.example.quick_chat_back.shared.service.State;

import java.util.List;

public class ConversationViewed {

  private final MessageRepository messageRepository;
  private final MessageChangeNotifier messageChangeNotifier;
  private final UserReader userReader;

  public ConversationViewed(MessageRepository messageRepository,
                            MessageChangeNotifier messageChangeNotifier,
                            UserReader userReader) {
    this.messageRepository = messageRepository;
    this.messageChangeNotifier = messageChangeNotifier;
    this.userReader = userReader;
  }

  public State<Integer, String> markAsRead(
      ConversationPublicId conversationPublicId,
      UserPublicId connectedUserPublicId) {
    List<Message> messagesToUpdateSendState
        = messageRepository.findMessageToUpdateSendState(conversationPublicId, connectedUserPublicId);
    int nbUpdatedMessages = messageRepository.updateMessageSendState(conversationPublicId, connectedUserPublicId,
        MessageSendState.READ);
    List<UserPublicId> usersToNotify = userReader.findUsersToNotify(conversationPublicId, connectedUserPublicId)
        .stream().map(User::getUserPublicId).toList();
    ConversationViewedForNotification conversationViewedForNotification = new ConversationViewedForNotification(
        conversationPublicId.value(),
        messagesToUpdateSendState.stream().map(message -> message.getPublicId().value()).toList());
    messageChangeNotifier.view(conversationViewedForNotification, usersToNotify);

    if (nbUpdatedMessages > 0) {
      return State.<Integer, String>builder().forSuccess(nbUpdatedMessages);
    } else {
      return State.<Integer, String>builder().forSuccess();
    }
  }
}
