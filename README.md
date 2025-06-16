# Ice Cream API

A REST API for managing ice cream cones and ice creams. Built with Spring Boot, this project allows you to create, list, and retrieve cones and ice creams, with optional filtering by size.

## How to Run

### Running Locally

1. Clone the repository:
   ```sh
   git clone git@github.com:jrpolesi/ice-cream-api.git
   cd ./ice-cream-api
   ```
2. Build the project:
   ```sh
   ./mvnw clean install
   ```
3. Run the application:

   ```sh
   ./mvnw spring-boot:run
   ```

   The API will be available at `http://localhost:8080`.

## API Endpoints & Example CURLs

### Cone Endpoints

- **Create Cone**

  ```sh
  curl -X POST http://localhost:8080/cone \
    -H "Content-Type: application/json" \
    -d '{
      "size": "M",
      "cone_type": "chocolate"
  }'
  ```

- **List All Cones**

  ```sh
  curl http://localhost:8080/cone
  ```

- **List Cones by Size**

  ```sh
  curl http://localhost:8080/cone?size=M
  ```

- **Get Cone by ID**
  ```sh
  curl http://localhost:8080/cone/1
  ```

### Ice Cream Endpoints

- **Create Ice Cream**

  ```sh
  curl -X POST http://localhost:8080/ice-cream \
    -H "Content-Type: application/json" \
    -d '{
      "flavor": "vanilla",
      "size": "M",
      "price": 3.50,
      "cone_id": 1
  }'
  ```

- **List All Ice Creams**

  ```sh
  curl http://localhost:8080/ice-cream
  ```

- **List Ice Creams by Size**

  ```sh
  curl http://localhost:8080/ice-cream?size=M
  ```

- **Get Ice Cream by ID**
  ```sh
  curl http://localhost:8080/ice-cream/1
  ```

## Testing

To run tests:

```sh
./mvnw test
```

## Configuration

Application properties can be set in `src/main/resources/application.yml`.
