package com.tulio.banksofka.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuditTransactionRequest {
    private String userId;
    private Double initialBalance;
    private Double amount;
    private Double finalBalance;

}