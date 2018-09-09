package hello;

import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {

  @Autowired
  private TaskService service;

  public Mono<ServerResponse> hello(ServerRequest request) {
    return ok()
        .contentType(TEXT_PLAIN)
        .body(fromObject("Hello, Spring"));
  }

  public Mono<ServerResponse> save(ServerRequest request) {
    Mono<Task> task = request.bodyToMono(Task.class);
    return request.bodyToMono(Task.class)
        .transform(service::save)
        .flatMap(it -> ok().body(fromObject(it)))
        .switchIfEmpty(badRequest().build());
  }
}
