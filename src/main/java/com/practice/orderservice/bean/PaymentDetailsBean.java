package com.practice.orderservice.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PaymentDetailsBean {
	private String orderId;
	private String cardNumber;
	private double amount;
	public PaymentDetailsBean(String orderId, String cardNumber, double amount) {
		super();
		this.orderId = orderId;
		this.cardNumber = cardNumber;
		this.amount = amount;
	}
}
