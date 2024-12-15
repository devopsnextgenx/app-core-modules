### Start Database

```bash
docker volume create mysql-data

# Start MySQL container with volume mount
docker run --name mysql-container \
  -v mysql-data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=demo \
  -p 3306:3306 \
  -d mysql:latest

# Wait for MySQL to be ready
sleep 10

# Initialize demo database with sample data
docker exec -i mysql-container mysql -uroot -proot demo << EOF
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);

INSERT INTO users (name, email) VALUES
    ('John Doe', 'john@example.com'),
    ('Jane Smith', 'jane@example.com');
EOF

docker run --name mysql-container -v ./mysql-data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=user -p 3306:3306 -d mysql:latest
```


### Start User Service

- Use Swagger to test the API
  - default inmemory user/password | admin/admin
  - [Swagger API Endpoint](http://localhost:8080/swagger-ui/index.html)