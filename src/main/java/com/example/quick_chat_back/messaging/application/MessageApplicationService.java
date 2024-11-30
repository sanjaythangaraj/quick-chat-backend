package com.example.quick_chat_back.messaging.application;

import com.example.quick_chat_back.messaging.domain.message.aggregate.Message;
import com.example.quick_chat_back.messaging.domain.message.aggregate.MessageSendNew;
import com.example.quick_chat_back.messaging.domain.message.repository.ConversationRepository;
import com.example.quick_chat_back.messaging.domain.message.repository.MessageRepository;
import com.example.quick_chat_back.messaging.domain.message.service.ConversationReader;
import com.example.quick_chat_back.messaging.domain.message.service.MessageChangeNotifier;
import com.example.quick_chat_back.messaging.domain.message.service.MessageCreator;
import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import com.example.quick_chat_back.messaging.domain.user.repository.UserRepository;
import com.example.quick_chat_back.messaging.domain.user.service.UserReader;
import com.example.quick_chat_back.messaging.domain.user.vo.UserEmail;
import com.example.quick_chat_back.shared.authentication.application.AuthenticatedUser;
import com.example.quick_chat_back.shared.service.State;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MessageApplicationService {
  private final MessageCreator messageCreator;
  private final UserReader userReader;

  public MessageApplicationService(MessageRepository messageRepository, UserRepository userRepository, ConversationRepository conversationRepository, MessageChangeNotifier messageChangeNotifier) {
    ConversationReader conversationReader = new ConversationReader(conversationRepository);
    this.messageCreator = new MessageCreator(messageRepository, messageChangeNotifier, conversationReader);
    this.userReader = new UserReader(userRepository);
  }

  @Transactional
  public State<Message, String> send(MessageSendNew messageSendNew) {
    State<Message, String> state;

    Optional<User> optional = this.userReader.getByEmail(new UserEmail(AuthenticatedUser.username().username()));
    if (optional.isPresent()) {
      state = this.messageCreator.create(messageSendNew, optional.get());
    } else {
      state = State.<Message, String>builder().forError(String.format("Error retrieving user information from the DB: %s", AuthenticatedUser.username().username()));
    }
    return state;
  }
}
