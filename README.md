# java-explore-with-me
*Spring Boot, Docker, JUnit, PostgreSQL, Hibernate, SQL, JPQL, Maven, REST, lombok, h2, Mockito, Criteria*



### Приложение создано для поиска интересных событий и поиска участников на различные мероприятия. Пользователи данного приложения могут создавать события, подавать заявкина на участие в событиях других пользователей. Сервис помагает ориентироваться в базе событий за счет различных фильтров. Реализован сбор статистики. 

### Приложение состоит из двух сервисов:
### 1) Основной сервис с 3 уровнями доступа
- для пользователей без авторизации;
- для авторизованных пользователей;
- для администратора приложения.
ссылка на данные для swagger: https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json

#### - добавление, удаление и получение информации о пользоватиле
#### - добавление, удаление и получение информации о категории
#### - добавление, удаление и получение информации о событиях
#### - добавление, удаление и получение информации о подборках событиях
#### - добавление лайков/дизлайков по событию
#### - подача заявок на участие в собитиях, подтверждение и отклонения заявок.


### 2) Сервис статистики. 
ссылка на данные для swagger: https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json

#### - сохранение статистики по просмотрам событий и подборок событий
#### - получение статистики по просмотрам событий и подборок событий

******************************************************************************************************
******************************************************************************************************

### *Для запуска приложения на вашем устройстве должна быть установлена платформа контейнеризации Docker.*
- *откройте консоль и передите в корневую директорию программы*
- *введите команду: docker-compose build*
- *введите команду: docker-compose up*
