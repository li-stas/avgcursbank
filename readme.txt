Задача:

Создать API. Оно должно содержать два метода:
1) Запрос на список курсов по всем источникам, с величинами
   средних курсов по рынку - @GetMapping("/tradavgrate")
2) Запрос на выдачу списка средних курсов по всем источникам за период
  (@GetMapping("/tradavgperiod"))

  На регулярной основе веб приложение загружает данные из
стороннего сервиса во внутреннюю базу данных. Запросы
реализованного API должны работать с локальной базой данных (т.е.
забирать данные из базы данных ).
(package com.lista.avgcursbank.model.services ScheduledTasksCron.java
настройка в application.properties переменная job.cron.rate
для ручной загрузки добавлен @GetMapping("/tradeadd")
)

Что использовать:
1) Необходимо использовать публичное API
https://api.monobank.ua/docs/
https://minfin.com.ua/ua/developers/api/
https://api.privatbank.ua/#p24/exchange
Для каждого из провайдеров реализовать общий интерфейси
отдельные реализации

2) Cron job - для реализации синхронизации данных с apiпровайдеров.
3) Java/Kotlin, Tomcat, Spring Boot, Hibernate, PostgresQL; Git;
4) Запросы должны быть задокументированы с использованием Swagger.
(http://localhost:8888/swagger-ui.html#/)

5) в качестве сборщика проектов использовать Gradle (использовалось Maven)