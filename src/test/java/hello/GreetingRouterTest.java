package hello;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringJUnitConfig(GreetingRouter.class)
public class GreetingRouterTest {
  private static final Logger log = LoggerFactory.getLogger(GreetingRouterTest.class);

  @Autowired
  private WebTestClient client;

  @Autowired
  RouterFunction<ServerResponse> greetingRouter;

  @Before
  public void setUp() {
    client = client
        .mutate()
        .responseTimeout(Duration.ofMillis(30000))
        .build();
  }

  @Test
  public void testHello() {
    client
        .get()
        .uri("/hello")
        .accept(MediaType.TEXT_PLAIN)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class).isEqualTo("Hello, Spring");
  }

  @Test
  public void testSave() throws InterruptedException {
    Task task = new Task();
    task.id = 1;
    task.name = "testTask";
    int count = 10;
    List<Runnable> requests = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      requests.add(() -> client
          .post()
          .uri("/task/{id}", task.id)
          .contentType(APPLICATION_JSON)
          .accept(APPLICATION_JSON)
          .syncBody(task)
          .exchange()
          .expectStatus().isOk()
          .expectBody(Task.class)
          .consumeWith(result -> {
            assertThat(result.getResponseBody()).isEqualToComparingFieldByField(task);
            log.info(result.getResponseBody().toString());
            Assert.assertNotNull(result.getResponseBody());
          }));
    }
    ExecutorService exec = Executors.newFixedThreadPool(count);
    requests.forEach(exec::execute);
    exec.shutdown();
    exec.awaitTermination(1, TimeUnit.MINUTES);
  }

  @Test
  public void testSave2() {
    Task task = new Task();
    task.id = 1;
    task.name = "testTask";
    WebClient cl = WebClient.create();
    Mono<Task> result = cl.post()
        .uri("/task/1")
        .syncBody(task)
        .retrieve()
        .bodyToMono(Task.class);
    WebTestClient.bindToRouterFunction(greetingRouter).build()
        .post()
        .uri("/task/1")
        .syncBody(task)
        .exchange();
  }

}
