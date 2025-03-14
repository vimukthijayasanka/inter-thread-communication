# MyCP - Custom JDBC Connection Pool

MyCP is a lightweight, thread-safe JDBC connection pool designed for managing database connections efficiently.

## Features

- Connection pooling for MySQL databases
- Synchronized connection management
- Supports configurable pool size via `application.properties`
- Thread-safe implementation using `wait()` and `notify()`
- Provides HTTP endpoints to obtain and release connections

## Installation & Usage

1. **Add** `application.properties` **with database configurations:**

   ```properties
   app.db.host=localhost
   app.db.port=3306
   app.db.user=root
   app.db.password=yourpassword
   app.db.database-name=mydatabase
   app.pool-size=4
   ```

2. **Initialize the connection pool:**

   ```java
   MyCP connectionPool = new MyCP();
   getServletContext().setAttribute("myCP", connectionPool);
   ```

3. **Obtain a connection via HTTP GET request:**

   ```http
   GET /connections/random
   ```
   Response:
   ```html
   <h1>ID: connection_id</h1>
   <h1>Connection Ref: connection_reference</h1>
   ```

4. **Release a specific connection via HTTP DELETE request:**

   ```http
   DELETE /connections/{id}
   ```
   Response:
   ```html
   <h1>Connection id: {id} released</h1>
   ```

5. **Release all connections via HTTP DELETE request:**

   ```http
   DELETE /connections/
   ```
   Response:
   ```html
   <h1>All connections released</h1>
   ```

## License

This project is licensed under the MIT License. See [LICENSE.txt](license.txt) for details.

