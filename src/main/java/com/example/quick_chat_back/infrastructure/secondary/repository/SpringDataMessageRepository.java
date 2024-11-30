package com.example.quick_chat_back.infrastructure.secondary.repository;

import com.example.quick_chat_back.infrastructure.secondary.entity.ConversationEntity;
import com.example.quick_chat_back.infrastructure.secondary.entity.MessageEntity;
import com.example.quick_chat_back.infrastructure.secondary.entity.UserEntity;
import com.example.quick_chat_back.messaging.domain.message.aggregate.Conversation;
import com.example.quick_chat_back.messaging.domain.message.aggregate.Message;
import com.example.quick_chat_back.messaging.domain.message.repository.MessageRepository;
import com.example.quick_chat_back.messaging.domain.message.vo.ConversationPublicId;
import com.example.quick_chat_back.messaging.domain.message.vo.MessageSendState;
import com.example.quick_chat_back.messaging.domain.message.vo.MessageType;
import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import com.example.quick_chat_back.messaging.domain.user.vo.UserPublicId;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpringDataMessageRepository implements MessageRepository {
  private final JpaMessageRepository jpaMessageRepository;
  private final JpaMessageBinaryContentRepository jpaMessageBinaryContentRepository;

  public SpringDataMessageRepository(JpaMessageRepository jpaMessageRepository, JpaMessageBinaryContentRepository jpaMessageBinaryContentRepository) {
    this.jpaMessageRepository = jpaMessageRepository;
    this.jpaMessageBinaryContentRepository = jpaMessageBinaryContentRepository;
  }

  @Override
  public Message save(Message message, User sender, Conversation conversation) {
    MessageEntity messageEntity = MessageEntity.from(message);
    messageEntity.setSender(UserEntity.from(sender));
    messageEntity.setConversation(ConversationEntity.from(conversation));

    if (message.getContent().type() != MessageType.TEXT) {
      jpaMessageBinaryContentRepository.save(messageEntity.getContentBinary());
    }

    MessageEntity messageSaved = jpaMessageRepository.save(messageEntity);
    return MessageEntity.toDomain(messageSaved);
  }

  @Override
  public int updateMessageSendState(ConversationPublicId conversationPublicId, UserPublicId userPublicId, MessageSendState state) {
    return jpaMessageRepository.updateMessageSendState(conversationPublicId.value(), userPublicId.value(), state);
  }

  @Override
  public List<Message> findMessageToUpdateSendState(ConversationPublicId conversationPublicId, UserPublicId userPublicId) {
    return jpaMessageRepository.findMessageToUpdateSendState(conversationPublicId.value(), userPublicId.value())
        .stream().map(MessageEntity::toDomain).toList();
  }
}
