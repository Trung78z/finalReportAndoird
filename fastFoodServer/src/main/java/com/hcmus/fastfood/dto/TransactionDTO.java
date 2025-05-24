package com.hcmus.fastfood.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String id;
    private String totalPrice;
    private String userId;
    private List<TransactionItemDTO> transactionItems;

}
