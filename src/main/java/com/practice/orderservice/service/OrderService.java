package com.practice.orderservice.service;

import com.practice.orderservice.dto.OrderRequest;

public interface OrderService {

	String createOrder(OrderRequest order);

}
