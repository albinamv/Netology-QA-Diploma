# Дипломный проект по профессии «Тестировщик»

## Документы
- [План автоматизации](https://github.com/albinamv/QA-Diploma/blob/main/docs/Plan.md)

## Запуск авто-тестов

### Перед началом работы
1. Установка Docker Desktop с [официального сайта](https://www.docker.com/) (для Windows 10 (и выше), либо Linux/MacOS)

### Шаги для запуска авто-тестов из консоли
1. Открыть Docker Desktop
2. Открыть консоль (для Windows: `Win+R` > Открыть `cmd`)
3. Склонировать репозиторий: `git clone https://github.com/albinamv/QA-Diploma.git`
4. Сменить папку: `cd QA-diploma`
5. Запустить контейнеры (СУБД MySQL, PostgreSQL, gate-simulator) в консоли: `docker-compose up -d`
6. Запустить SUT
   1. Команда для запуска с поддержкой СУБД MySQL: `java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar artifacts/aqa-shop.jar`
   2. Команда для запуска с поддержкой СУБД PostgreSQL: `java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app -jar artifacts/aqa-shop.jar`
7. В браузере сервис будет доступен по адресу [http://localhost:8080/](http://localhost:8080/) 
8. Запустить авто-тесты
   1. Команда для запуска тестов с MySQL: `gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app`
   2. Команда для запуска тестов с PostgreSQL: `gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/app`
9. После завершения прогона авто-тестов сгенерировать отчёт командой `gradlew allureServe`. Отчёт откроется в браузере.
10. Для завершения работы allureServe выполнить команду `Ctrl + С`, далее `Y`
11. Остановить все контейнеры командой `docker-compose down`
