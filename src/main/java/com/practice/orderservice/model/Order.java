package com.practice.orderservice.model;

import java.util.Date;

import com.practice.orderservice.bean.OrderStatus;
import com.practice.orderservice.bean.PaymentStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Order {
	private String orderId;
	private double amount;
	private String orderItem;
	private Date orderTime;
	private OrderStatus orderStatus;
	private String paymentId;
	private PaymentStatus paymentStatus;
	public Order(double amount, String orderItem) {
		super();
		this.amount = amount;
		this.orderItem = orderItem;
		this.orderTime = new Date();
		this.orderStatus = OrderStatus.PENDING;
		this.paymentStatus = PaymentStatus.PENDING;
	}

}
