package com.example.quick_chat_back.infrastructure.secondary.repository;

import com.example.quick_chat_back.infrastructure.secondary.entity.MessageContentBinaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMessageBinaryContentRepository extends JpaRepository<MessageContentBinaryEntity, Long> {
}
