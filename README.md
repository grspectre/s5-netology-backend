# s5-netology-backend

__Автор: Шанаурин Антон Александрович__

Предмет: BE:JAVA Бэкенд разработка: разработка серверной части программного приложения на Java

Семестр: 5

email: stud0000298878@study.utmn.ru

https://www.kaggle.com/datasets/ramjasmaurya/top-ranked-supercomputer-2021	Суперкомпьютеры	supecomputer 2021.csv

```bash
# Запустить PostgreSQL
docker-compose up -d

# Собрать и запустить приложение
./mvnw clean package
java -jar target/supercomputers-0.0.1-SNAPSHOT.jar

# Или с переопределением параметров
java -jar target/supercomputers-0.0.1-SNAPSHOT.jar \
  --DB_URL=jdbc:postgresql://localhost:5433/supercomputers \
  --DB_USERNAME=superuser \
  --DB_PASSWORD=superpass

# Запустить тесты (поднимут свой PostgreSQL через Testcontainers)
./mvnw test

# Удалить volume
docker-compose down -v
```