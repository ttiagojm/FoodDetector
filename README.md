# FoodDetector
Groceries Detector with CodeProjectAI, Docker and Spring Framework

<hr>

# Technologies
Several technologies were used:
* Java is the programming language used
* HTML/CSS to develop the application's frontend
* Spring Framework to create the web application
* Eureka Server to find the various microservices
* API Gateway to create load balancers between microservices
* Thymeleaf to connect the backend and frontend
* Data JPA to connect to the database
* SpringSecurity to manage permissions and authenticate users
* MySQL as the database
* CodeProjectAI server with the YOLOv5 model and previously trained weights (the training is [in this](https://github.com/ttiagojm/GrocerEyeYOLOv5) repository)

# Architecture
![architecture](imgs\arch.png)

# Screens
|                       |                       |
|-----------------------|-----------------------|
| ![detect](imgs\detecao.png) | ![detect](imgs\detecao.png) |
| ![management](imgs\management.png) | ![result](imgs\result.png)  |
| ![tasks](imgs\tasks.png)|                       |

# Credits
This project was developed by [Gonçalo Rosa](https://github.com/GoncalojmRosa) and [Tiago Martins](https://github.com/ttiagojm) for a Distributed applications course in our Computer Engineering Degree