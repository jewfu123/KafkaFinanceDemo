package com.example.kafka_transfer.demo.dto;

public record TransferRequest(String transactionId, String sourceAccount, String targetAccount,
    java.math.BigDecimal amount) {
}
