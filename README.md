# Spring Boot REST API (Java Microservice)

A lightweight Spring Boot REST API packaged as a Docker-ready service for CI/CD and container orchestration environments.

**Inventory:**

![Inventory](./images/inventory.png)

**Health Check:**

![Health](./images/health.png)

## Project Structure

```plaintext
.
├── Dockerfile
├── README.md
├── docker-compose.yml
├── pom.xml
└── src
    └── main
        ├── java
        │   └── com
        │       └── rsinventory
        │           ├── Application.java
        │           ├── InventoryController.java
        │           └── Product.java
        └── resources
            └── application.properties
```

---

## API Endpoints

- GET /inventory
- GET /actuator/health

## Build and Run (Local)

```bash id="r2"
mvn clean package
java -jar target/springboot-app.jar
```

Service runs on:

```bash
http://<server-ip>:8080/inventory
```

## Docker

### Build image

```bash id="r3"
docker build -t rs-springboot-app:v1.0.0 .
```

### Run container

```bash id="r4"
docker run -d -p 2002:8080 rs-springboot-app:v1.0.0
```

Service available at:

```
http://<server-ip>:2002/inventory
http://<server-ip>:2002/actuator/health
```

## Docker Compose

```bash
docker-compose up -d
```

- Rebuild image if you have altered the Dockerfile
```bash
docker-compose up -d --build
```

## Clean up

```bash
mvn clean
docker compose down
```

## Notes

* Stateless REST microservice
* Designed for CI/CD pipelines (Jenkins)
* Compatible with Docker and Kubernetes (k3s)
* Versioned image tagging recommended (e.g., v1.0.0)