package com.example.quick_chat_back.messaging.domain.message.vo;

import org.jilt.Builder;

@Builder
public record MessageContent(String text, MessageType type, MessageMediaContent media) {
}
