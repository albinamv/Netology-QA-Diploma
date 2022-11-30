# Дипломный проект по профессии «Тестировщик»

## Документы
- [План автоматизации](https://github.com/albinamv/QA-Diploma/blob/main/docs/Plan.md)

## Запуск авто-тестов

### Предустановка программ
1. Установка Docker Desktop с [официального сайта](https://www.docker.com/) (для Windows 10 (и выше), либо Linux/MacOS)

### Шаги по воспроизведению
1. Открыть Docker Desktop
2. Перейти в терминал
3. Запустить контейнеры в терминале командой
```docker-compose up```
4. Запустить SUT командой
```java -jar artifacts/aqa-shop.jar```
6. Запустить авто-тесты командой
```gradlew clean test```
или
```./gradlew clean test```
7. Сгенерировать отчёты командами
```gradlew allureReport gradlew allureServe```

8. Для завершения работы allureServe выполнить команду:
`Ctrl + С`, далее `Y`

9. Остановить и удалить все контейнеры
```docker-compose down```