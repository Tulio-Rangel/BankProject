package com.tulio.banksofka.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TransactionRequestTest {

    @Test
    void testSetAndGetAmount() {
        TransactionRequest transactionRequest = new TransactionRequest();

        transactionRequest.setAmount(150.75);

        assertThat(transactionRequest.getAmount()).isEqualTo(150.75);
    }

    @Test
    void testDefaultAmountIsZero() {
        TransactionRequest transactionRequest = new TransactionRequest();

        assertThat(transactionRequest.getAmount()).isEqualTo(0.0);
    }
}
