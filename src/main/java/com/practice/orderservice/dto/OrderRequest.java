package com.practice.orderservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderRequest {
	private double amount;
	private String orderItem;
	private PaymentDetailsDto paymentDetails;
	public OrderRequest(double amount, String orderItem, PaymentDetailsDto paymentDetails) {
		super();
		this.amount = amount;
		this.orderItem = orderItem;
		this.paymentDetails = paymentDetails;
	}	
}
