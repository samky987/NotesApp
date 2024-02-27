# Simple Note Manager App


## Features

**User Authentication:** Users can authenticate using JWT. Invalid credentials are handled gracefully.

**User Registration:** New users can register with the system. Duplicate usernames are not allowed.

**Note Management:**

* Create Note: Users can create new notes to their own accounts.
* Retrieve Note: Users can view their own notes. Access is restricted to the owner of the notes.
* Delete Note: Users can delete their own notes. Access is restricted to the owner of the notes.

## Design Decisions

**DTOs (Data Transfer Objects):** DTOs are used for data exchange between the client and the server, to decouple the internal persistence models from presentation. ModelMapper library is used to map the DTOs to Model objects and vice versa.

**Standard Response Objects:** Standard response objects are used across the application to provide consistent API responses. ResponseWithData is used to encapsulate DTOs, and ResponseWithError is used to encapsulate failure in field validation or errors.

**Virtual Threads:** Virtual threads are used as the default threads for tomcat server, virtual threads allow developers to write code that is similar to asynchronous code(non-blocking) without the overhead of promises and callback. Virtual threads provide better scalability than platform threads since they are managed by the JVM and are cheap to create.

**Security:** The application handles password hashing, and JWT token authentication.

**Global Error Handling:** RestControllerAdvice and AuthenticationEntryPoint are used with custom exceptions to centralize exception handling, and hide internal implementation from the user.

**Caching:** Caching is utilized to improve performance using Spring's default caching with ConcurrentHashMap. Cache keys are based on the JWT token and Note ID to make sure of data privacy. For production grade caching another caching provider such as Caffeine or Redis can be used.


## Postman Collection

Postman collection with sample data attached in the projects file

## Application Flow:

1. New user registration using the endpoint POST /public/users
 
2. New session using the endpoint POST /public/users/session. The result will be a JWT token that should be copied and pasted into the Authorization header for subsequent requests.

3. (Post, Get, Delete) Note: /notes


## Requirements to run:

* Lombok plugin for the IDE (Lombok is a Java library that allows developers to focus on the business logic of their applications without worrying about boilerplate, eclipse and intellij provide a plugin to use the library)
* Maven
* Docker
