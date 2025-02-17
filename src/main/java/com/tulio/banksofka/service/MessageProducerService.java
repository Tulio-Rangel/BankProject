package com.tulio.banksofka.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService {
    private final JmsTemplate jmsTemplate;

    public MessageProducerService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessageAccount(String operation, String message, String userId, boolean isSuccess){
        Map<String, Object> messagePayload = new HashMap<>();
        messagePayload.put("message", message);
        messagePayload.put("userId", userId);
        messagePayload.put("timestamp", LocalDateTime.now().toString());
        messagePayload.put("status", isSuccess ? "success" : "failed");
        jmsTemplate.convertAndSend(operation, messagePayload);
    }

    public void sendMessageTransaction(String operation, String message, boolean isSuccess, HashMap<String, Object> transactionDetails){
        Map<String, Object> messagePayload = new HashMap<>();
        messagePayload.put("message", message);
        messagePayload.put("details", transactionDetails);
        messagePayload.put("timestamp", LocalDateTime.now().toString());
        messagePayload.put("status", isSuccess ? "success" : "failed");
        jmsTemplate.convertAndSend(operation, messagePayload);
    }
}