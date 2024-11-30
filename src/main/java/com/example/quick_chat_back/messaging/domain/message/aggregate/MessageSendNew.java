package com.example.quick_chat_back.messaging.domain.message.aggregate;

import com.example.quick_chat_back.messaging.domain.message.vo.ConversationPublicId;
import com.example.quick_chat_back.messaging.domain.message.vo.MessageContent;
import org.jilt.Builder;

@Builder
public record MessageSendNew(MessageContent messageContent, ConversationPublicId conversationPublicId) {
}
