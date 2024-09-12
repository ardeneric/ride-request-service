# Ride Request Service

## Overview

The **Ride Request Service** is a microservice designed to handle ride requests, interact with MongoDB for data storage, Caffeine for Caching and use RabbitMQ as a message broker. This service exposes REST endpoints for managing ride requests and finding the nearest driver.

## Table of Contents

- [Assumptions](#assumptions)
- [Prerequisites](#prerequisites)
- [Running the Application](#running-the-application)
- [Accessing MongoDB Data](#accessing-mongodb-data)
- [Interacting with RabbitMQ](#interacting-with-rabbitmq)
- [Using the REST Endpoint](#using-the-rest-endpoint)
- [Troubleshooting](#troubleshooting)

### Working Branch
- main

## Assumptions
- riderId is a unique identifier assigned to each rider for making ride requests. This identifier is typically issued during the rider’s registration process.
- To enhance response times, we cache search results for riders for up to 10 minutes, provided they remain in the same location during that period.
- To avoid using outdated data, we do not retrieve rider search results older than 10 minutes, as riders and passengers are likely to be in motion and their locations may change.

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


- POSTMAN COLLECTION
```json
   {
  "info": {
    "name": "Ride Controller API",
    "description": "Postman collection for the Ride Controller API to manage ride requests and find nearest drivers.",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Submit a new ride request",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json",
            "description": ""
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"riderId\":123,\"passengerLocation\":{\"latitude\":37.7749,\"longitude\":-122.4194}}"
        },
        "url": {
          "raw": "http://localhost:8080/api/rides",
          "protocol": "http",
          "host": [
            "localhost"
          ],
          "port": "8080",
          "path": [
            "api",
            "rides"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Get nearest driver",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/rides/123",
          "protocol": "http",
          "host": [
            "localhost"
          ],
          "port": "8080",
          "path": [
            "api",
            "rides",
            "123"
          ]
        }
      },
      "response": []
    }
  ]
}

```


- SUBMIT NEW RIDE REQUEST
<img width="950" alt="Screenshot 2024-09-12 at 3 51 36 AM" src="https://github.com/user-attachments/assets/a0e969e9-afa9-43bd-b10b-c228c148c410">

- GET NEAREST DRIVER
<img width="945" alt="Screenshot 2024-09-12 at 3 53 45 AM" src="https://github.com/user-attachments/assets/7f007594-96e6-4539-b64d-08d0b62fd961">


- RIDE EXPIRED AFTER 10mins
<img width="945" alt="Screenshot 2024-09-12 at 4 05 44 AM" src="https://github.com/user-attachments/assets/82523da0-0612-40c0-9eb8-e4e5930025e7">


- NO DRIVER NEARBY
<img width="948" alt="Screenshot 2024-09-12 at 3 59 45 AM" src="https://github.com/user-attachments/assets/00127d84-9f48-4467-a8f1-58e533ec5e0f">






## Troubleshooting

- **Application Not Starting**: Ensure Docker and Docker Compose are installed correctly. Check the logs for detailed error messages.
- **MongoDB Connection Issues**: Verify that MongoDB is running and accessible on port `27017`.
- **RabbitMQ Connection Issues**: Ensure RabbitMQ is running and accessible on ports `5672` and `15672`.
