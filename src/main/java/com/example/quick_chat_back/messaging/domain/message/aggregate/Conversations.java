package com.example.quick_chat_back.messaging.domain.message.aggregate;

import com.example.quick_chat_back.shared.error.domain.Assert;

import java.util.List;

public record Conversations(List<Conversation> conversations) {

  public Conversations {
    Assert.field("conversations", conversations).notNull().noNullElement();
  }
}
