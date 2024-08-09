package com.practice.orderservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.orderservice.controler.OrderServiceControler;
import com.practice.orderservice.dto.OrderRequest;
import com.practice.orderservice.dto.PaymentDetailsDto;
import com.practice.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

/**
 * This is test class for OrderServiceController
 * @author Ravi Avala
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private OrderService orderService;

	@InjectMocks
	private OrderServiceControler orderServiceController;

	@Autowired
	private CircuitBreakerRegistry circuitBreakerRegistry;

	private CircuitBreaker circuitBreaker;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(orderServiceController).build();
		circuitBreaker = circuitBreakerRegistry.circuitBreaker("payment");
		circuitBreaker.reset();
	}

	/*
	 * This method used to test the success case
	 */
	@Test
	public void whenCircuitBreakerIsClosed_thenServiceShouldSucceed() throws Exception {
		PaymentDetailsDto paymentDetails = new PaymentDetailsDto("1234", 200);
		OrderRequest orderRequest = new OrderRequest(200, "Item1", paymentDetails);
		String orderRequestJson = objectMapper.writeValueAsString(orderRequest);
		when(orderService.createOrder(any(OrderRequest.class))).thenReturn("SUCCESS");
		// Act & Assert
		mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON).content(orderRequestJson))
				.andExpect(status().isOk()).andExpect(content().string("order placed successfully"));
	}
	
	/*
	 * This method used to test the Failure case case
	 *
	@Test
	public void whenCircuitBreakerOpens_thenFallbackMethodIsCalled() throws Exception {
		PaymentDetailsDto paymentDetails2 = new PaymentDetailsDto("9991234", 500);
		OrderRequest orderRequest1 = new OrderRequest(500, "Item2", paymentDetails2);
		String orderRequestJson = objectMapper.writeValueAsString(orderRequest1);

		when(orderService.createOrder(orderRequest1)).thenThrow(new IllegalArgumentException("Payment Failed"));
		// Act & Assert
		mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON).content(orderRequestJson))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string("Oops! Something went wrong, please order after some time!"));
	}

	@Test
	public void whenCircuitBreakerOpens_thenServiceShouldReturnFallback() throws Exception {
		PaymentDetailsDto paymentDetails3 = new PaymentDetailsDto("9991234", 400);
		OrderRequest orderRequest3 = new OrderRequest(400, "Item3", paymentDetails3);
		String orderRequestJson = objectMapper.writeValueAsString(orderRequest3);

		// Simulate a failure to trip the circuit breaker
		when(orderService.createOrder(orderRequest3)).thenThrow(new IllegalArgumentException("Payment Failed"));

		// Act - Trigger the circuit breaker to open state		
		for (int i = 0; i < 5; i++) { // Assuming 5 failures will trip the breaker
			mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON).content(orderRequestJson))
					.andExpect(status().isInternalServerError())
					.andExpect(content().string("Oops! Something went wrong, please order after some time!"));
		}
		// Assert
		assert circuitBreaker.getState() == CircuitBreaker.State.OPEN;
	}

	@Test
	public void whenCircuitBreakerIsHalfOpen_thenServiceShouldBeRetried() throws Exception {
		PaymentDetailsDto paymentDetails4 = new PaymentDetailsDto("1234", 400);
		OrderRequest orderRequest4 = new OrderRequest(400, "Item4", paymentDetails4);
		String orderRequestJson = objectMapper.writeValueAsString(orderRequest4);

		// Manually transition the circuit breaker to HALF_OPEN state
		circuitBreaker.transitionToHalfOpenState();

		// Simulate a successful call that should close the circuit breaker
		when(orderService.createOrder(orderRequest4)).thenReturn("SUCCESS");

		// Act & Assert
		for (int i = 0; i < 3; i++) { // Assuming 3 success will close the breaker
		mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON).content(orderRequestJson))
				.andExpect(status().isOk()).andExpect(content().string("order placed successfully"));
		}
		assert circuitBreaker.getState() == CircuitBreaker.State.CLOSED;
	}   */
}