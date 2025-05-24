# Market Price Comparator

This is a Spring Boot application that helps users compare prices and discounts for products across different stores. It also provides tools to view the best and most recent discounts, analyze historical price trends, and optimize shopping lists.

---

## Project Structure Overview

The project follows a layered architecture, with separate packages for controllers, services, models, repositories, and data transfer objects (DTOs). The application uses a SQLite database, initialized with a schema script, and exposes REST endpoints for accessing price history and discount data.

---

## How to Build and Run

### Technologies

- Java
- Maven
- SQLite
- IntelliJ IDEA or any other preferred IDE

### Steps

1. Clone the repository.
2. Ensure that the `market.db` file exists at the path specified in `application.properties`.
3. Place your CSV files into the `resources/data` folder, using the appropriate naming conventions.
4. Run the application using your IDE or via Maven.

---

## Assumptions & Simplifications

- The application assumes that CSV files named using the `{store}_{date}.csv` pattern contain product pricing data and are imported into two tables: `products` (for static product info) and `price_entries` (for time-specific pricing).
- Discount files should follow the `{store}_discounts_{date}.csv` naming convention. They are only processed if the referenced products already exist in the database and have associated price history and are imported into discounts table.

---

## Features & API Endpoints

### 1. Price History

**Endpoint:** `GET /price-history/{productName}`  
**Optional Query Parameters:**

- `productCategory`
- `storeName`
- `brand`

**Example:**
`/price-history/iaurt%20grecesc?productCategory=lactate&brand=Lidl&storeName=lidl`

**Response:**
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
```

### 2. Discounts

**a. Best Discounts**  
**Endpoint:** `GET /discounts/best`  
Returns the top 10 best current discounts (if available).

**b. New Discounts**  
**Endpoint:** `GET /discounts/new`  
Returns discounts added in the last 24 hours.

---

### 3. Unit Price Calculation

Upon application startup, the system calculates the price per unit for products using the `ProductSubstitutesService`.  
This helps in comparing equivalent products with different units (e.g., per kg, per liter). This information will be found in the price_per_unit column in the price entries table.

---

### 4. Basket Optimization

The application includes a basket optimization feature. A predefined shopping list (with quantities and units) is analyzed, and the service suggests optimal purchases by comparing prices and discounts across available stores.

**Example items in the basket:**

- 1L suc de portocale  
- 5kg cașcaval  
- 3L ulei  
- 3kg morcovi 
- 10 bucăți ouă  

The optimizer outputs store-specific suggestions for achieving the best value.

---

### 5. Custom Price Alerts

Users can define custom alerts for when the price of a specific product drops below a certain threshold at a specified store.

**Example:**  
If the price of `"pâine albă"` drops below `2 RON` at **kaufland**, an alert is triggered automatically at startup in .

---
