package com.example.kafka_transfer.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.example.kafka_transfer.demo.dto.TransferRequest;

@Service
public class TransferProducerService {

  private static final Logger log = LoggerFactory.getLogger(TransferProducerService.class);

  @Value("${app.kafka.topics.request}")
  private String requestTopic;

  private final KafkaTemplate<String, TransferRequest> kafkaTemplate;

  public TransferProducerService(KafkaTemplate<String, TransferRequest> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  // 核心发送方法
  public void sendTransferRequest(TransferRequest request) {
    log.info("Sending request for TxId: {}", request.transactionId());

    // 关键：使用 transactionId 作为 Key，确保相同交易发送到同一分区，便于有序处理
    kafkaTemplate.send(requestTopic, request.transactionId(), request);
  }
}
