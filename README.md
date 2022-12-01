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
5. Запустить контейнеры в терминале: `docker-compose up -d`
6. Запустить SUT: `java -jar artifacts/aqa-shop.jar`. В браузере сервис будет доступен по адресу [http://localhost:8080/](http://localhost:8080/) 
7. Запустить авто-тесты командой `gradlew clean test`
8. После прогона авто-тестов сгенерировать отчёт командой `gradlew allureServe`. Отчёт откроется в браузере.
9. Для завершения работы allureServe выполнить команду `Ctrl + С`, далее `Y`
10. Остановить все контейнеры командой `docker-compose down`

### Примечания
В данный момент протестирована только поддержка СУБД MySQL. 
Данные для создания подключения к MySQL (url, логин, пароль) не нужно указывать командами в консоли, сейчас они уже указаны в [SQLHelper.java](https://github.com/albinamv/QA-Diploma/blob/main/src/test/java/ru/netology/helpers/SQLHelper.java)