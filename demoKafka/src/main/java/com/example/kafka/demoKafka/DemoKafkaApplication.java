package com.example.kafka.demoKafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DemoKafkaApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoKafkaApplication.class, args);
  }

}
