package com.practice.orderservice.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.orderservice.dto.OrderRequest;
import com.practice.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

/**
 * This is a RestControler class for Orderservice
 * 
 * @author Ravi Avala
 * 
 *
 */
@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderServiceControler {

	private OrderService orderService;

	@Autowired
	public OrderServiceControler(OrderService orderService) {

		this.orderService = orderService;
	}

	@PostMapping
	@CircuitBreaker(name = "payment", fallbackMethod = "fallbackMethod")
	public ResponseEntity createOrder(@RequestBody OrderRequest orderRequest) {
		String res = orderService.createOrder(orderRequest);
		return ResponseEntity.ok("order placed successfully");
	}

	/*
	 * This is the fallback method for Paymentsevice application is not avilable 
	 */
	public ResponseEntity fallbackMethod(Throwable throwable) {
		log.error("Cannot Place Order Executing Fallback logic");
		return new ResponseEntity("Oops! Something went wrong, please order after some time!",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/*
	 * This is the fallback method for Paymentsevice fails 
	 */
	public ResponseEntity fallbackMethod(OrderRequest orderRequest, IllegalArgumentException throwable) {
		log.error("Cannot Place Order Executing Fallback logic");
		System.out.println("hai.222...........................");
		return new ResponseEntity("Oops! Something went wrong, please order after some time!",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
