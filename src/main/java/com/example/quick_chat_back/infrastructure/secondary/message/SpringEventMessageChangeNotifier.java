package com.example.quick_chat_back.infrastructure.secondary.message;

import com.example.quick_chat_back.infrastructure.primary.message.RestMessage;
import com.example.quick_chat_back.messaging.domain.message.aggregate.Message;
import com.example.quick_chat_back.messaging.domain.message.service.MessageChangeNotifier;
import com.example.quick_chat_back.messaging.domain.message.vo.ConversationPublicId;
import com.example.quick_chat_back.messaging.domain.user.vo.UserPublicId;
import com.example.quick_chat_back.shared.service.State;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringEventMessageChangeNotifier implements MessageChangeNotifier {

  private final NotificationService notificationService;
  private ApplicationEventPublisher applicationEventPublisher;

  public SpringEventMessageChangeNotifier(ApplicationEventPublisher applicationEventPublisher, NotificationService notificationService) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.notificationService = notificationService;
  }

  @Override
  public State<Void, String> send(Message message, List<UserPublicId> userToNotify) {
    MessageWithUsers messageWithUsers = new MessageWithUsers(message, userToNotify);
    applicationEventPublisher.publishEvent(messageWithUsers);
    return State.<Void, String>builder().forSuccess();
  }

  @Override
  public State<Void, String> delete(ConversationPublicId conversationPublicId, List<UserPublicId> userToNotify) {
    ConversationIdWithUsers conversationIdWithUsers = new ConversationIdWithUsers(conversationPublicId, userToNotify);
    applicationEventPublisher.publishEvent(conversationIdWithUsers);
    return State.<Void, String>builder().forSuccess();
  }

  @Override
  public State<Void, String> view(ConversationViewedForNotification conversationViewedForNotification, List<UserPublicId> usersToNotify) {
    MessageIdWithUsers messageIdWithUsers = new MessageIdWithUsers(conversationViewedForNotification, usersToNotify);
    applicationEventPublisher.publishEvent(messageIdWithUsers);
    return State.<Void, String>builder().forSuccess();
  }

  @EventListener
  public void handleDeleteConversation(ConversationIdWithUsers conversationIdWithUsers) {
    notificationService.sendMessage(conversationIdWithUsers.conversationPublicId().value(),
        conversationIdWithUsers.users(), NotificationEventName.DELETE_CONVERSATION);
  }

  @EventListener
  public void handleNewMessage(MessageWithUsers messageWithUsers) {
    notificationService.sendMessage(RestMessage.from(messageWithUsers.message()),
        messageWithUsers.userPublicIds(), NotificationEventName.NEW_MESSAGE);
  }

  @EventListener
  public void handleView(MessageIdWithUsers messageIdWithUsers) {
    notificationService.sendMessage(messageIdWithUsers.conversationViewedForNotification(),
        messageIdWithUsers.usersToNotify(), NotificationEventName.VIEWS_MESSAGES);
  }
}
