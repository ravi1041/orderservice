package com.practice.orderservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PaymentDetailsDto {
	private String cardNumber;
	private double amount;
	public PaymentDetailsDto(String cardNumber, double amount) {
		super();
		this.cardNumber = cardNumber;
		this.amount = amount;
	}
}
