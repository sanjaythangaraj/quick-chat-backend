package com.example.quick_chat_back.messaging.domain.message.service;

import com.example.quick_chat_back.messaging.domain.message.aggregate.Conversation;
import com.example.quick_chat_back.messaging.domain.message.aggregate.Message;
import com.example.quick_chat_back.messaging.domain.message.aggregate.MessageBuilder;
import com.example.quick_chat_back.messaging.domain.message.aggregate.MessageSendNew;
import com.example.quick_chat_back.messaging.domain.message.repository.MessageRepository;
import com.example.quick_chat_back.messaging.domain.message.vo.MessagePublicId;
import com.example.quick_chat_back.messaging.domain.message.vo.MessageSendState;
import com.example.quick_chat_back.messaging.domain.message.vo.MessageSentTime;
import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import com.example.quick_chat_back.shared.service.State;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class MessageCreator {

  private final MessageRepository messageRepository;
  private final MessageChangeNotifier messageChangeNotifier;
  private final ConversationReader conversationReader;

  public MessageCreator(MessageRepository messageRepository, MessageChangeNotifier messageChangeNotifier, ConversationReader conversationReader) {
    this.messageRepository = messageRepository;
    this.messageChangeNotifier = messageChangeNotifier;
    this.conversationReader = conversationReader;
  }

  public State<Message, String> create(MessageSendNew messageSendNew, User sender) {
    Message message = MessageBuilder.message()
        .content(messageSendNew.messageContent())
        .publicId(new MessagePublicId(UUID.randomUUID()))
        .sendState(MessageSendState.RECEIVED)
        .sentTime(new MessageSentTime(Instant.now()))
        .conversationId(messageSendNew.conversationPublicId())
        .sender(sender.getUserPublicId())
        .build();

    State<Message, String> state;
    Optional<Conversation> conversationalOptional = conversationReader
        .getOneByPublicId(messageSendNew.conversationPublicId());

    if (conversationalOptional.isPresent()) {
      Message messageSaved = messageRepository.save(message, sender, conversationalOptional.get());
      messageChangeNotifier
          .send(
              message,
              conversationalOptional.get().getMembers().stream().map(User::getUserPublicId).toList()
          );
      state = State.<Message, String>builder().forSuccess(messageSaved);
    } else {
      state = State.<Message, String>builder().forError(
          String.format("Unable to find the conversation to link the message with the " +
              "following publicId %s", messageSendNew.conversationPublicId().value())
      );
    }

    return state;

  }
}
