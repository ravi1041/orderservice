Order Service Application
	This is a Spring Boot application that manages orders and interacts with a payment service. The application is equipped with a Circuit Breaker pattern using Resilience4j to handle faults in the payment service gracefully.

Features
	Order Management: Create and manage orders.
	Circuit Breaker: Uses Resilience4j to manage failures in the payment service.
	Fallback Mechanism: Ensures graceful degradation by providing a fallback method when the payment service is unavailable.
	
Technologies Used
	Spring Boot: Framework for building Java applications.
	Resilience4j: Library for implementing the Circuit Breaker pattern.
	JUnit 5: Testing framework.
	Mockito: Mocking framework for unit tests.
	WebClient: Non-blocking reactive client for HTTP requests.



Project Structure

	src/
	├── main/
	│   ├── java/
	│   │   ├── com.practice.orderservice/
	│   │   │   ├── controller/   # Contains REST controllers
	│   │   │   ├── dto/          # Data Transfer Objects
	│   │   │   ├── service/      # Service layer
	│   │   │   └── util/         # Utility classes
	│   ├── resources/
	│   │   ├── application.yml   # Configuration files
	├── test/
	│   ├── java/
	│   │   ├── com.practice.orderservice/
	│   │   │   ├── controller/   # Unit tests for controllers


Prerequisites
	Java 17: Ensure that JDK 17 is installed.
	Maven: For building and running the application.
	
Getting Started
1. Clone the Repository
	git clone https://github.com/ravi1041/orderservice.git
	cd orderservice
	
2. Build the Application
	mvn clean install
	
3. Run the Application
	mvn spring-boot:run
	
4. Testing
To run the tests, including Circuit Breaker tests:
	mvn test
	
	
REST API Endpoints
	Create Order
	URL: /api/order
	Method: POST
	Request Body:	
		{
			"amount": 100.0,
			"orderItem": "Sample Item",
			"paymentDetails": {
				"cardNumber": "1234-5678-9876-5432",
				"amount": 100.0
			}
		}
	Responses:
		200 OK: Order placed successfully.
		500 Internal Server Error: Oops! Something went wrong, please order after some time! (If the payment service fails and Circuit Breaker opens).
		
		
Circuit Breaker and Fallback
	The application uses the Resilience4j Circuit Breaker to handle failures when communicating with the payment service. If the payment service fails or is unavailable, the Circuit Breaker will trigger the fallbackMethod to return a user-friendly error message.

Example Fallback Method

	public ResponseEntity fallbackMethod(IllegalArgumentException throwable) {
		log.error("Cannot Place Order Executing Fallback logic");
		return new ResponseEntity("Oops! Something went wrong, please order after some time!",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}


Customization & Configuration
	The application is configured using application.yml. You can customize the Circuit Breaker settings, such as the failure rate threshold, wait duration, etc.

Example configuration:  application.yml file

	resilience4j.circuitbreaker:
	  instances:
		payment:
		  registerHealthIndicator: true
		  slidingWindowSize: 5
		  failureRateThreshold: 50
		  waitDurationInOpenState: 5s
		  permittedNumberOfCallsInHalfOpenState: 3