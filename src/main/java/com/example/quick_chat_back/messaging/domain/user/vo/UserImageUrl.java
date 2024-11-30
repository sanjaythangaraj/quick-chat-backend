package com.example.quick_chat_back.messaging.domain.user.vo;

import com.example.quick_chat_back.shared.error.domain.Assert;

public record UserImageUrl(String value) {

  public UserImageUrl {
    Assert.field("value", value).maxLength(255);
  }
}
