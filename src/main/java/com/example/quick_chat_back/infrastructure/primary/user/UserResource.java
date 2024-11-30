package com.example.quick_chat_back.infrastructure.primary.user;

import com.example.quick_chat_back.messaging.application.UsersApplicationService;
import com.example.quick_chat_back.messaging.domain.user.aggregate.User;
import com.example.quick_chat_back.messaging.domain.user.vo.UserPublicId;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserResource {

  private final UsersApplicationService usersApplicationService;

  public UserResource(UsersApplicationService usersApplicationService) {
    this.usersApplicationService = usersApplicationService;
  }

  @GetMapping("/get-authenticated-user")
  public ResponseEntity<RestUser> getAuthenticatedUser(
      @AuthenticationPrincipal Jwt jwt,
      @RequestParam boolean forceResync) {
    User user = usersApplicationService.getAuthenticatedUserWithSync(jwt, forceResync);
    RestUser restUser = RestUser.from(user);
    return ResponseEntity.ok(restUser);
  }

  @GetMapping("/search")
  public ResponseEntity<List<RestSearchUser>> search(Pageable pageable, @RequestParam String query) {
    List<RestSearchUser> searchResults = usersApplicationService.search(pageable, query)
        .stream().map(RestSearchUser::from)
        .toList();
    return ResponseEntity.ok(searchResults);
  }

  @GetMapping("/get-last-seen")
  ResponseEntity<Instant> getLastSeen(@RequestParam UUID publicId) {
    Optional<Instant> lastSeen = usersApplicationService.getLastSeen(new UserPublicId(publicId));
    if (lastSeen.isPresent()) {
      return ResponseEntity.ok(lastSeen.get());
    } else {
      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Unable to fetch the presence of the user " + publicId);
      return ResponseEntity.of(problemDetail).build();
    }
  }

}
