package com.example.kafka_transfer.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.example.kafka_transfer.demo.dto.TransferRequest;
import com.example.kafka_transfer.demo.dto.TransferStatus;

@Service
public class TransferConsumerService {

  private static final Logger log = LoggerFactory.getLogger(TransferConsumerService.class);

  @Value("${app.kafka.topics.status}")
  private String statusTopic;

  private final KafkaTemplate<String, TransferStatus> statusKafkaTemplate;

  // 注入 KafkaTemplate 用于发送状态回执
  public TransferConsumerService(KafkaTemplate<String, TransferStatus> statusKafkaTemplate) {
    this.statusKafkaTemplate = statusKafkaTemplate;
  }

  @KafkaListener(topics = "${app.kafka.topics.request}",
      groupId = "${spring.kafka.consumer.group-id}")
  public void processTransferRequest(TransferRequest request) {
    String txId = request.transactionId();
    log.info("Consumer received TxId: {} for transfer.", txId);

    TransferStatus status;

    try {
      // 1. **幂等性检查**: 检查数据库中 txId 是否已处理
      // if (isProcessed(txId)) return;

      // 2. **核心业务事务** (模拟)：扣减源账户，增加目标账户
      performFinancialTransaction(request);

      // 3. 处理成功，准备状态消息
      status = new TransferStatus(txId, "SUCCESS", "Transfer completed.");

    } catch (Exception e) {
      // 4. 处理失败，准备失败状态消息
      log.error("Transfer failed for TxId: {}", txId, e);
      status = new TransferStatus(txId, "FAILED", "Insufficient funds or system error.");
    }

    // 5. 将处理结果发送到状态 Topic
    statusKafkaTemplate.send(statusTopic, txId, status);
    log.info("Published status for TxId: {} to status topic.", txId);
  }

  private void performFinancialTransaction(TransferRequest request) throws Exception {
    // 模拟数据库事务：扣款/加款
    log.info("Executing database transaction: {} from {} to {}", request.amount(),
        request.sourceAccount(), request.targetAccount());

    // 模拟业务逻辑失败
    if (request.amount().compareTo(new java.math.BigDecimal(10000)) > 0) {
      throw new RuntimeException("Simulated Insufficient Funds");
    }
    // ... (实际代码中包含 @Transactional 数据库操作)
  }
}
