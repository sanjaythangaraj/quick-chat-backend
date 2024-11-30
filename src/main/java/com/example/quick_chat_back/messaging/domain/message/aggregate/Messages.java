package com.example.quick_chat_back.messaging.domain.message.aggregate;

import com.example.quick_chat_back.shared.error.domain.Assert;

import java.util.List;

public record Messages(List<Message> messages) {
  public Messages {
    Assert.field("messages", messages).notNull().noNullElement();
  }
}
