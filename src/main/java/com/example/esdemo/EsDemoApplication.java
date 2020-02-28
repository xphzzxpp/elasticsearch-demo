package com.example.esdemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.lucene.util.NamedThreadFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsDemoApplication {

  private ExecutorService executorService = Executors.newFixedThreadPool(10);

  private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,
      5,1000, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1000),
      new NamedThreadFactory("test-"));

  public static void main(String[] args) {
    SpringApplication.run(EsDemoApplication.class, args);
  }

}
