package com.example.quick_chat_back.messaging.domain.message.vo;

import com.example.quick_chat_back.shared.error.domain.Assert;

import java.time.Instant;

public record MessageSentTime(Instant date) {

  public MessageSentTime {
    Assert.field("date", date).notNull();
  }
}