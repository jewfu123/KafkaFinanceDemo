package com.example.kafka.demoKafka;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KafkaDemoService {
  private static final Logger log = LoggerFactory.getLogger(KafkaDemoService.class);
  private static final String TOPIC = "test-topic";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  // --- 模拟发送者 ---
  // 每 5 秒自动发送一条消息到 Kafka
  @Scheduled(fixedRate = 5000)
  public void sendMessage() {
    String message = "Hello Kafka! Time: " + LocalDateTime.now();
    log.info(">>>> [PRODUCER] 发送消息: {}", message);

    // 发送并添加回调，确认是否发送成功
    // Spring Boot 3.x 返回的是 CompletableFuture
    kafkaTemplate.send(TOPIC, message).whenComplete((result, ex) -> {
      if (ex == null) {
        // 情况 A: 发送成功 (异常为空)
        log.info(">>>> [PRODUCER] 发送成功，Offset: {}", result.getRecordMetadata().offset());
      } else {
        // 情况 B: 发送失败 (处理异常)
        log.error(">>>> [PRODUCER] 发送失败: {}", ex.getMessage());
      }
    });
  }

  // --- 模拟接收者 ---
  // 监听指定的 Topic
  @KafkaListener(topics = TOPIC, groupId = "test-group")
  public void listen(String message) {
    log.info("<<<< [CONSUMER] 收到消息并解析: {}", message);

    // 这里模拟业务处理过程
    try {
      processData(message);
    } catch (Exception e) {
      log.error("解析数据出错", e);
    }
  }

  private void processData(String data) {
    log.info("执行业务逻辑：数据 [{}] 已经成功存入 Log 记录", data);
  }

}


