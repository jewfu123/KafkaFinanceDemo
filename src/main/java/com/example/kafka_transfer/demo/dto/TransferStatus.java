package com.example.kafka_transfer.demo.dto;

public record TransferStatus(String transactionId, String status, // SUCCESS or FAILED
    String message) {
}
