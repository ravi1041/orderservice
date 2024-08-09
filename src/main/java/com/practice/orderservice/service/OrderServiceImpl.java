package com.practice.orderservice.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.practice.orderservice.bean.OrderStatus;
import com.practice.orderservice.bean.PaymentDetailsBean;
import com.practice.orderservice.bean.PaymentStatus;
import com.practice.orderservice.dto.OrderRequest;
import com.practice.orderservice.model.Order;
import com.practice.orderservice.util.OrderServiceUtil;

import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private WebClient.Builder webClientBuilder;

	/**
	 * This service method is used to call the payment service api payment and based
	 * on its response, its decide the failure and succes cases
	 */
	@Override
	public String createOrder(OrderRequest orderRequest) throws IllegalArgumentException {

		Order order = new Order(orderRequest.getAmount(), orderRequest.getOrderItem());
		order.setOrderId(UUID.randomUUID().toString());
		PaymentDetailsBean pDetails = new PaymentDetailsBean(order.getOrderId(),
				orderRequest.getPaymentDetails().getCardNumber(), orderRequest.getPaymentDetails().getAmount());
		String res = webClientBuilder.build().post().uri("http://localhost:8082/api/payment")
				.contentType(MediaType.APPLICATION_JSON).body(Mono.just(pDetails), Object.class).retrieve()
				.bodyToMono(String.class).block();
		if (null == res || res.isBlank() || "FAIL".equalsIgnoreCase(res)) {
			order.setOrderStatus(OrderStatus.FAILED);
			order.setPaymentStatus(PaymentStatus.FAILED);
			OrderServiceUtil.ORDER_LIST.add(order);
			throw new IllegalArgumentException("Payment Failed, please try again later");
		} else {
			order.setPaymentId(res);
			order.setOrderStatus(OrderStatus.SUCCESS);
			order.setPaymentStatus(PaymentStatus.SUCCESS);
			OrderServiceUtil.ORDER_LIST.add(order);
			return "SUCCESS";
		}

	}

}
