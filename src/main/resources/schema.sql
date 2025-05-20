CREATE TABLE IF NOT EXISTS products (
    product_id TEXT PRIMARY KEY,
    product_name TEXT,
    product_category TEXT,
    brand TEXT,
    package_quantity REAL,
    package_unit TEXT
);

CREATE TABLE IF NOT EXISTS stores (
    name TEXT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS discounts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_product_id TEXT,
    store_name TEXT,
    date TEXT,
    from_date TEXT,
    to_date TEXT,
    percentage_of_discount INTEGER,
    UNIQUE (product_product_id, store_name, from_date, to_date),
    FOREIGN KEY (product_product_id) REFERENCES products(product_id),
    FOREIGN KEY (store_name) REFERENCES stores(name)
);

CREATE TABLE IF NOT EXISTS price_entries (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_product_id TEXT,
    store_name TEXT,
    date TEXT,
    price REAL,
    currency TEXT,
    price_per_unit REAL,
    UNIQUE (product_product_id, store_name, date),
    FOREIGN KEY (product_product_id) REFERENCES products(product_id),
    FOREIGN KEY (store_name) REFERENCES stores(name)
);
