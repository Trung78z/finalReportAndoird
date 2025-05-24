package com.hcmus.management.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
	private String totalPrice;
	List<TransactionItemsDTO> transactionItems;
	
	
}
