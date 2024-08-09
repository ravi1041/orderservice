Order Service Application
	This is a Spring Boot application that manages orders and interacts with a payment service. The application is equipped with a Circuit Breaker pattern using Resilience4j to handle faults in the payment service gracefully.

Features
	Order Management: Create and manage orders.
	Circuit Breaker: Uses Resilience4j to manage failures in the payment service.
	Fallback Mechanism: Ensures graceful degradation by providing a fallback method when the payment service is unavailable.
	Health Monitoring: Provides a health check endpoint to monitor the application's status.
	
Technologies Used
	Spring Boot: Framework for building Java applications.
	Resilience4j: Library for implementing the Circuit Breaker pattern.
	Spring Boot Actuator: Provides production-ready features like monitoring and management.
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
	

Here's the updated README.md file with the inclusion of health check monitoring using the Spring Boot Actuator's /actuator/health endpoint:

Order Service Application
This is a Spring Boot application that manages orders and interacts with a payment service. The application is equipped with a Circuit Breaker pattern using Resilience4j to handle faults in the payment service gracefully.

Features
Order Management: Create and manage orders.
Circuit Breaker: Uses Resilience4j to manage failures in the payment service.
Fallback Mechanism: Ensures graceful degradation by providing a fallback method when the payment service is unavailable.
Health Monitoring: Provides a health check endpoint to monitor the application's status.
Technologies Used
Spring Boot: Framework for building Java applications.
Resilience4j: Library for implementing the Circuit Breaker pattern.
Spring Boot Actuator: Provides production-ready features like monitoring and management.
JUnit 5: Testing framework.
Mockito: Mocking framework for unit tests.
WebClient: Non-blocking reactive client for HTTP requests.
Project Structure
bash
Copy code
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
│   │   │   ├── service/      # Unit tests for services
Prerequisites
Java 17: Ensure that JDK 17 is installed.
Maven: For building and running the application.
Getting Started
1. Clone the Repository
bash
Copy code
git clone https://github.com/your-username/orderservice.git
cd orderservice
2. Build the Application
bash
Copy code
mvn clean install
3. Run the Application
bash
Copy code
mvn spring-boot:run
4. Testing
To run the tests, including Circuit Breaker tests:

bash
Copy code
mvn test
REST API Endpoints
Create Order
URL: /api/order
Method: POST
Request Body:
json
Copy code
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
java
Copy code
public ResponseEntity fallbackMethod(IllegalArgumentException throwable) {
    log.error("Cannot Place Order Executing Fallback logic");
    return new ResponseEntity("Oops! Something went wrong, please order after some time!",
            HttpStatus.INTERNAL_SERVER_ERROR);
}
Health Monitoring
The application uses Spring Boot Actuator to expose a health check endpoint at /actuator/health. This endpoint provides information about the application's health and can be used for monitoring purposes.

Checking Application Health
	URL: http://localhost:8081/actuator/health
	
	You should see a response like this when the application is healthy:
	
	{
	  "status": "UP",
	  "components": {
	    "circuitBreakers": {
	      "status": "UNKNOWN",
	      "details": {
	        "payment": {
	          "status": "CIRCUIT_OPEN",
	          "details": {
	            "failureRate": "100.0%",
	            "failureRateThreshold": "50.0%",
	            "slowCallRate": "0.0%",
	            "slowCallRateThreshold": "100.0%",
	            "bufferedCalls": 3,
	            "slowCalls": 0,
	            "slowFailedCalls": 0,
	            "failedCalls": 3,
	            "notPermittedCalls": 4,
	            "state": "OPEN" -------------------------------------------------> Describe the circuit breaker status
	          }
	        }
	      }
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