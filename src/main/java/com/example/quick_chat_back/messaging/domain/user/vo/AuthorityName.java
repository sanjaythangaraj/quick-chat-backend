package com.example.quick_chat_back.messaging.domain.user.vo;

import com.example.quick_chat_back.shared.error.domain.Assert;

public record AuthorityName(String name) {
  public AuthorityName {
    Assert.field("name", name).notNull();
  }
}
