package hello;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GreetingRouter {

  @Bean
  public RouterFunction<ServerResponse> greetingRouterFunction(GreetingHandler greetingHandler) {
    return RouterFunctions.route(GET("/hello").and(accept(TEXT_PLAIN)), greetingHandler::hello)
        .andRoute(POST("/task/{id}").and(accept(APPLICATION_JSON)), greetingHandler::save);
  }
}
