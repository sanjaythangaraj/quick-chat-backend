package com.example.quick_chat_back.messaging.application;

import com.example.quick_chat_back.messaging.domain.message.aggregate.Conversation;
import com.example.quick_chat_back.messaging.domain.message.aggregate.ConversationToCreate;
import com.example.quick_chat_back.messaging.domain.message.repository.ConversationRepository;
import com.example.quick_chat_back.messaging.domain.message.repository.MessageRepository;
import com.example.quick_chat_back.messaging.domain.message.service.*;
import com.example.quick_chat_back.messaging.domain.message.vo.ConversationPublicId;
import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import com.example.quick_chat_back.messaging.domain.user.repository.UserRepository;
import com.example.quick_chat_back.messaging.domain.user.service.UserReader;
import com.example.quick_chat_back.shared.service.State;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationsApplicationService {
  private final ConversationCreator conversationCreator;
  private final ConversationReader conversationReader;
  private final ConversationDeleter conversationDeleter;
  private final UsersApplicationService usersApplicationService;
  private final ConversationViewed conversationViewed;

  public ConversationsApplicationService(
      ConversationRepository conversationRepository,
      UserRepository userRepository,
      MessageChangeNotifier messageChangeNotifier,
      MessageRepository messageRepository,
      UsersApplicationService usersApplicationService) {
    UserReader userReader = new UserReader(userRepository);
    this.conversationCreator = new ConversationCreator(conversationRepository, userReader);
    this.conversationReader = new ConversationReader(conversationRepository);
    this.conversationDeleter = new ConversationDeleter(conversationRepository, messageChangeNotifier);
    this.usersApplicationService = usersApplicationService;
    this.conversationViewed = new ConversationViewed(messageRepository, messageChangeNotifier, userReader);
  }

  @Transactional
  public State<Conversation, String> create(ConversationToCreate conversationToCreate) {
    User authenticatedUser = usersApplicationService.getAuthenticatedUser();
    return conversationCreator.create(conversationToCreate, authenticatedUser);
  }

  @Transactional(readOnly = true)
  public List<Conversation> getAllByConnectedUser(Pageable pageable) {
    User authenticatedUser = usersApplicationService.getAuthenticatedUser();
    return conversationReader.getAllByUserPublicId(authenticatedUser.getUserPublicId(), pageable)
        .stream().toList();
  }

  @Transactional
  public State<ConversationPublicId, String> delete(ConversationPublicId conversationPublicId) {
    User authenticatedUser = usersApplicationService.getAuthenticatedUser();
    return conversationDeleter.deleteById(conversationPublicId, authenticatedUser);
  }

  @Transactional(readOnly = true)
  public Optional<Conversation> getOneByConversationId(ConversationPublicId conversationPublicId) {
    User authenticatedUser = usersApplicationService.getAuthenticatedUser();
    return conversationReader.getOneByPublicIdAndUserId(conversationPublicId, authenticatedUser.getUserPublicId());
  }

  @Transactional
  public State<Integer, String> markConversationAsRead(ConversationPublicId conversationPublicId) {
    User authenticatedUser = usersApplicationService.getAuthenticatedUser();
    return conversationViewed.markAsRead(conversationPublicId, authenticatedUser.getUserPublicId());
  }

}
