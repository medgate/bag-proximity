package ch.bag.screening.common.concurrent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

  private static final String THREAD_NAME_PREFIX = "BagThread";
  private static final int EXECUTOR_CORE_POOL_SIZE = 1;
  private static final int EXECUTOR_MAX_POOL_SIZE = 1;
  private static final int EXECUTOR_QUEUE_SIZE = 10;

  /**
   * Default Task executor used by @Async annotated methods. The thread pool is single threaded,
   * therefore tasks will be executed asynchronously but never in parallel.
   *
   * @return ThreadPoolTaskExecutor default task executor for @Async methods
   */
  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {
    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix(THREAD_NAME_PREFIX + "-");
    executor.setCorePoolSize(EXECUTOR_CORE_POOL_SIZE);
    executor.setMaxPoolSize(EXECUTOR_MAX_POOL_SIZE);
    executor.setQueueCapacity(EXECUTOR_QUEUE_SIZE);
    return executor;
  }
}
