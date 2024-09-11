# Ride Request Service

## Overview

The **Ride Request Service** is a microservice designed to handle ride requests, interact with MongoDB for data storage, and use RabbitMQ as a message broker. This service exposes REST endpoints for managing ride requests and finding the nearest driver.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Running the Application](#running-the-application)
- [Accessing MongoDB Data](#accessing-mongodb-data)
- [Interacting with RabbitMQ](#interacting-with-rabbitmq)
- [Using the REST Endpoint](#using-the-rest-endpoint)
- [Troubleshooting](#troubleshooting)

### Working Branch
- main


## Prerequisites

Before running the application, ensure you have the following installed:

- Docker
- Docker Compose

## Running the Application

To run the Ride Request Service, follow these steps:

1. **Clone the Repository**

   ```bash
   git clone https://github.com/ardeneric/ride-request-service.git
   cd /ride-request-service
   ```

2. **Build and Start the Containers**

   Use Docker Compose to build and start the services:

   ```bash
   docker-compose up --build
   ```

   This command will:
   - Build the application Docker image.
   - Start the MongoDB and RabbitMQ containers.
   - Start the application container, which depends on MongoDB and RabbitMQ.

3. **Access the Application**

  - Application: http://localhost:8080
  - MongoDB: Accessible at mongodb://localhost:27017/ride-request-db 
  - RabbitMQ Management: http://localhost:15672


## Accessing MongoDB Data

MongoDB is exposed on port `27017`. You can use a MongoDB client to connect to it using the following connection string:

```plaintext
mongodb://localhost:27017/ride-request-db
```

You can also use MongoDB Compass or any other MongoDB management tool to view and interact with the data.

<img width="1435" alt="Screenshot 2024-09-11 at 2 07 28 AM" src="https://github.com/user-attachments/assets/7300c888-b48d-4acc-bda3-e00b82c81468">


## Interacting with RabbitMQ

RabbitMQ is exposed on ports `5672` (AMQP) and `15672` (Management UI). 

- **Management UI**: Access the RabbitMQ Management UI at [http://localhost:15672](http://localhost:15672) using the default credentials:
  - Username: `guest`
  - Password: `guest`

- **AMQP**: Connect to RabbitMQ using the AMQP protocol on port `5672`.

<img width="726" alt="Screenshot 2024-09-11 at 2 10 19 AM" src="https://github.com/user-attachments/assets/7cc1f377-1c52-43f5-8b50-0435a8f8ccfe">


## Using the REST Endpoint

### Send a Ride Request

**Endpoint**: `POST /api/rides`

**Request Body**:
```json
{
  "rideId": 3,
  "passengerLocation": {
    "latitude": 32.7777,
    "longitude": -0.2260
  }
}
```

**Response**:
- Status: `202 Accepted`
- Body: `"Searching for driver..."`

### Get Nearest Driver

**Endpoint**: `GET /api/rides/{rideId}`

**Path Parameter**:
- `rideId`: The ID of the ride request.

**Response**:
- **Success**: Status `200 OK`, returns a `Driver` object.
- **Failure**: Status `404 Not Found`, with message `"Driver not found"`.
<img width="1459" alt="Screenshot 2024-09-11 at 2 13 05 AM" src="https://github.com/user-attachments/assets/26da8e51-a819-4e0e-9d3b-b552157f66ee">


<img width="1424" alt="Screenshot 2024-09-11 at 2 14 13 AM" src="https://github.com/user-attachments/assets/e60f3cc2-91d8-4095-870a-e3fbd51a28cd">


<img width="1425" alt="Screenshot 2024-09-11 at 2 13 51 AM" src="https://github.com/user-attachments/assets/be5c87c2-716e-4934-8e4d-55bc11b4b154">



## Troubleshooting

- **Application Not Starting**: Ensure Docker and Docker Compose are installed correctly. Check the logs for detailed error messages.
- **MongoDB Connection Issues**: Verify that MongoDB is running and accessible on port `27017`.
- **RabbitMQ Connection Issues**: Ensure RabbitMQ is running and accessible on ports `5672` and `15672`.
