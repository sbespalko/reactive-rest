package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TaskService {

  private static final Logger log = LoggerFactory.getLogger(TaskService.class);

  public Mono<Task> save(Mono<Task> task) {
    return task.flatMap(it -> {
      log.info("SAVED: {}", it);
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return Mono.just(it);
    });
  }
}
