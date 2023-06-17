# Rest API for Online Hotel Booking Application - (StayWell)

A Spring Boot REST API web service for an online hotel booking application, developed using Core Java and the Spring Framework. The application utilizes a MySQL database for data storage and CRUD operations. The team consists of five members. This service enables customers to browse and book hotels, and provides management functionalities for inventory and customer orders. The API includes endpoints for hotel and reservation management, user authentication, and more. The project is open-source and hosted on GitHub.

    
## Tech Stack and Tools
- Core Java
- Spring Framework
- Spring Boot
- Spring Data JPA
- Spring Validation
- Hibernate
- MySQL
- Lombok
- JUnit
- Swagger-UI


## ER-Diagram
<p>
        <img
        align="center"
        src=""
        alt="ER-Diagram"
        width="700"
        style="display: block"/>
    </p>
    <br>

## Modules
- Login & Logout Module
- Admin Module
- Customer Module
- Hotel Module
- Room Module
- Reservation Module

## Features:
### Admin Features
 - Admin can do all the operations like:
 - Add hotel/room
 - Delete hotel/room
 - View all hotels/rooms
 
### Customer Features
 - Customer can do all the operations like:
 - Register himself
 - Login himself
 - Delete himself
 - Search for hotels/rooms
 - View hotel/room details
 - Make a reservation for a hotel/room

### Hotel Features
 - Hotel can do all the operations like:
 - Register himself
 - Login himself
 - Delete himself
 - Display hotel amenities
 - Showcase room types and descriptions
 - Provide hotel location information
 - Display hotel ratings and reviews
 - Showcase hotel photos and virtual tours
 - Offer special promotions and discounts
  
## Installation & Run
- To run this API server, you should update the database configuration inside the application.properties file which is present in the src/main/resources folder.
- Update the port number, username and password as per your local database configuration.
server.port=8808
spring.datasource.url=jdbc:mysql://localhost:3306/StayWell
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=your_username_here
spring.datasource.password=your_password_here

## API Root Endpoint

https://localhost:8808/
```
```
https://localhost:8808/swagger-ui/index.html
```
