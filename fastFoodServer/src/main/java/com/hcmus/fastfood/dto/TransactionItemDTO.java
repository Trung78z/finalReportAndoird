package com.hcmus.fastfood.dto;

import lombok.Data;

@Data
public class TransactionItemDTO {
    private TransactionDTO transaction;
    private Integer cartId;
}
