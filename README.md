# Дипломный проект по профессии «Тестировщик»
Основная задача: автоматизация тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

[Читать подробнее о задаче и тестируемом приложении](https://github.com/albinamv/Netology-QA-Diploma/blob/main/docs/TaskDiploma.md)


## Что было сделано:
1. Настроено окружение для запуска симулятора и SUT с поддержкой СУБД MySQL и PostgreSQL. 
2. Составлен [план автоматизации тестирования](https://github.com/albinamv/Netology-QA-Diploma/blob/main/docs/Plan.md). Всего было описано по 40 сценариев для каждой из двух форм.
3. Написаны авто-тесты для всех сценариев, прописанных в плане. Работа выполнена согласно шаблону проектирования Page Object, также были написаны отдельные классы-хелперы для генерации тестовых данных и запросов к БД. [Перейти к инструкции по запуску авто-тестов.](https://github.com/albinamv/Netology-QA-Diploma#%D0%B7%D0%B0%D0%BF%D1%83%D1%81%D0%BA-%D0%B0%D0%B2%D1%82%D0%BE-%D1%82%D0%B5%D1%81%D1%82%D0%BE%D0%B2)
4. Составлен [отчёт по итогам тестирования](https://github.com/albinamv/Netology-QA-Diploma/blob/main/docs/Report.md), заведены баг-репорты в разделе [Issues](https://github.com/albinamv/Netology-QA-Diploma/issues). Также даны рекомендации по улучшению страницы проекта.
5. Составлен [отчёт по итогам автоматизации](https://github.com/albinamv/Netology-QA-Diploma/blob/main/docs/Summary.md). На автоматизацию было потрачено меньше часов, чем было запланировано.


## Запуск авто-тестов

### Перед началом работы
1. Установка Docker Desktop с [официального сайта](https://www.docker.com/) (для Windows 10 (и выше), либо Linux/MacOS)

### Шаги для запуска авто-тестов из консоли
1. Открыть Docker Desktop
2. Открыть консоль (для Windows: `Win+R` > Открыть `cmd`)
3. Склонировать репозиторий: `git clone https://github.com/albinamv/Netology-QA-Diploma.git`
4. Сменить папку: `cd Netology-QA-Diploma`
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
