# Order Management Application

The purpose of the application is to manage purchase orders for customers on board an airplane who wish to buy products for breakfast, lunch/dinner, snacks, and beverages. This application is responsible for attaching all the products the customer wants along with their contact information, seat location on the plane, and payment details, and for processing the payment of the order.

## Technologies Used

- Java 17.
- Spring Framework 3.4.5.
- Spring Security.
- H2 Database.
- GitHub Actions.
- .env Variables.
- MapStruct.

## Setup and Execution

Follow the steps below to set up and run the application:

1. Clone the repository from GitHub: `git clone https://github.com/German-Furfori/ms-orders.git`.

2. Open the project in your preferred development environment.

3. Make sure you have a compatible version of Java installed (Java 17 or higher).

4. Build and run the application.

5. The application is now ready to use.

## Authentication

All endpoints require basic authentication.

- **Username:** `admin`
- **Password:** `admin`

Make sure to include these credentials when making requests using Postman or any HTTP client.

## Endpoints

### 📦 Products

| Method | Path                 | Description                         | Query Params     | Response     |
|--------|----------------------|-------------------------------------|------------------|--------------|
| GET    | `/products`          | Get all products (paginated)        | `page`, `size`   | 200 OK       |
| GET    | `/products/{id}`     | Get a product by ID                 | -                | 200 OK / 404 |

---

### 🗂️ Categories

| Method | Path                 | Description                         | Query Params     | Response     |
|--------|----------------------|-------------------------------------|------------------|--------------|
| GET    | `/categories`        | Get all categories (paginated)      | `page`, `size`   | 200 OK       |
| GET    | `/categories/{id}`   | Get a category by ID                | -                | 200 OK / 404 |

---

### 🧾 Orders

| Method | Path                 | Description                        | Request Body                  | Response           |
|--------|----------------------|------------------------------------|-------------------------------|--------------------|
| POST   | `/orders`            | Open a new order                   | `SeatInformationRequestDto`   | 201 CREATED        |
| DELETE | `/orders/{id}`       | Drop an order (mark as DROPPED)    | -                             | 200 OK / 404 / 409 |
| PATCH  | `/orders/{id}`       | Finish an order (mark as FINISHED) | `FinishOrderRequestDto`       | 200 OK / 404 / 409 |

---

### 🔄 Pagination

For endpoints that support pagination (like `GET /products` and `GET /categories`), you can use the following query parameters:

- `page`: Page number (first page: `0`)
- `size`: Number of items per page