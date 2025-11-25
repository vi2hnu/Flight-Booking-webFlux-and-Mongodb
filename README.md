# Flight Booking WebFlux Application

## Overview
A reactive Flight Booking application built using Spring Boot WebFlux. 

## Features
- Search flights by source, destination, and date
- Book tickets for available flights
- View booking history by email ID
- Cancellation of tickets prior to 24 hours of journey
- Get ticket details using PNR
- Flight inventory management with conflict checks
- Reactive, non-blocking API using Spring WebFlux
- Input validation and custom exception handling

## Tech Stack
- **Java 21**
- **Spring Boot WebFlux**
- **Spring Data MongoDB Reactive**
- **Reactive Programming**
- **JUnit, Mockito & WebTestClient for testing**
- **Lombok**
- **JaCoCo**
- **SonarQube**
- **Spring Boot Validation**

## ER Diagram
<img width="1263" height="801" alt="Image" src="https://github.com/user-attachments/assets/bfcc4412-8f30-4d71-838d-278e917b6ee9" />

## API Endpoints

| Method | Endpoint | Description |
|--------|---------|-------------|
| POST   | /api/v1.0/flight/airline/inventory | Add inventory/schedule for an existing airline |
| POST   | /api/v1.0/flight/search | Search for available flights |
| POST   | /api/v1.0/flight/booking/{flightId} | Book a ticket for a flight |
| GET    | /api/v1.0/flight/ticket/{pnr} | Get booked ticket details using PNR |
| GET    | /api/v1.0/flight/booking/history/{emailId} | Get booked ticket history by email ID |
| DELETE | /api/v1.0/flight/booking/cancel/{pnr} | Cancel a booked ticket using PNR |

(note: find example api request and response in report.pdf or report.doc)

## SonarQube Report
<img width="1451" height="274" alt="Image" src="https://github.com/user-attachments/assets/2cb33c7a-0a4c-43ca-b4ea-dc17f4650955" />

## JaCoCO Report
<img width="1562" height="346" alt="Image" src="https://github.com/user-attachments/assets/4c4b3fa1-78e1-4342-b477-c7a5100098d5" />

## Jmeter Report
### 20 request
<img width="1489" height="371" alt="Image" src="https://github.com/user-attachments/assets/4bc3008f-f4ec-4ae9-b8b4-bcdb17026fe2" />

### 50 request
<img width="1486" height="408" alt="Image" src="https://github.com/user-attachments/assets/67002373-1f0a-495d-92a3-f2b82756888f" />

### 100 request
<img width="1489" height="410" alt="Image" src="https://github.com/user-attachments/assets/f415d95c-63e4-4cf8-8a6e-8b37e96e29ba" />

