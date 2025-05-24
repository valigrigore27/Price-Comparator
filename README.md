# Market Price Comparator

This is a Spring Boot application that helps users compare prices and discounts for products across different stores. It also provides tools for seeing the best and new discounts, analyzing historical price trends and optimizing shopping lists.

---

##  Project Structure Overview

Project uses a layered architecture, with separate packages for controllers, services, models, repositories, and data transfer objects (DTOs). The application relies on a SQLite database, initialized with a schema script, and provides REST endpoints to access price history and discounts.

---

## How to Build and Run

### Technologies

- Java
- Maven
- SQLite
- Intellij or other IDE

### Steps

1. Clone the repository.
2. Make sure 'market.db' exists at the path specified in 'application.properties'.
3. Add your CSV files into the 'resources/data' folder using the appropriate naming convention.
4. Run the application.

---

## Assumptions & Simplifications
- The application assumes that CSV files named in the '{store}_{date}.csv' format represent product pricing data and are imported into two related tables: products (for static product information) and price_entries (for time-specific price records). Additionally, discount files (named with the pattern '{store}_discounts_{date}.csv') are only processed if the referenced products already exist in the database and have a known price history.

---

## API Endpoints

### Price History
1. GET /price-history/{productName}
Optional Query Params Filters:
- product category
- store
- brand

For example, for '/price-history/iaurt%20grecesc?productCategory=lactate&brand=Lidl&storeName=lidl' 

We'll get:
```json
[
  {
    "date": "2025-05-01",
    "price": 11.5,
    "currency": "RON",
    "storeName": "lidl"
  },
  {
    "date": "2025-05-08",
    "price": 11.6,
    "currency": "RON",
    "storeName": "lidl"
  }
]

### Discounts

2. GET /discounts/best
- We'll get the best 10 available discounts if exists.

3. GET /discounts/new
 - We'll get the discounts that appeared in last 24 hours.
