package com.example.quick_chat_back.infrastructure.primary.message;

import com.example.quick_chat_back.messaging.application.MessageApplicationService;
import com.example.quick_chat_back.messaging.domain.message.aggregate.Message;
import com.example.quick_chat_back.messaging.domain.message.aggregate.MessageSendNew;
import com.example.quick_chat_back.shared.service.State;
import com.example.quick_chat_back.shared.service.StatusNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/messages")
public class MessageResource {

  private final MessageApplicationService messageApplicationService;

  private ObjectMapper objectMapper = new ObjectMapper();

  public MessageResource(MessageApplicationService messageApplicationService) {
    this.messageApplicationService = messageApplicationService;
  }

  @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<RestMessage> send(@RequestPart(value = "file", required = false) MultipartFile file,
                                          @RequestPart("dto") String messageRaw) throws IOException {
    RestMessage restMessage = objectMapper.readValue(messageRaw, RestMessage.class);
    if (restMessage.hasMedia()) {
      restMessage.setMediaAttachment(file.getBytes(), file.getContentType());
    }

    MessageSendNew messageSendNew = RestMessage.toDomain(restMessage);

    State<Message, String> state = messageApplicationService.send(messageSendNew);
    if (state.getStatus().equals(StatusNotification.OK)) {
      return ResponseEntity.ok(RestMessage.from(state.getValue()));
    } else {
      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, state.getError());
      return ResponseEntity.of(problemDetail).build();
    }
  }
}
