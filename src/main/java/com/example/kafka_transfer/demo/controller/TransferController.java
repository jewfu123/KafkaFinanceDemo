package com.example.kafka_transfer.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.kafka_transfer.demo.dto.TransferRequest;
import com.example.kafka_transfer.demo.service.TransferProducerService;

@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {

  private final TransferProducerService producerService;

  public TransferController(TransferProducerService producerService) {
    this.producerService = producerService;
  }

  @PostMapping("/submit")
  public ResponseEntity<String> submitTransfer(@RequestBody TransferRequest request) {
    // 1. 生成唯一的交易 ID
    String txId = java.util.UUID.randomUUID().toString();
    request = new TransferRequest(txId, request.sourceAccount(), request.targetAccount(),
        request.amount());

    // 2. 立即将请求推送到 Kafka
    producerService.sendTransferRequest(request);

    // 3. 立即返回 HTTP 202 响应，表示请求已接受，后台异步处理中
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body("Transaction request accepted. ID: " + txId + ". Processing asynchronously.");
  }
}
