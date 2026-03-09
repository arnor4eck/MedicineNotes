[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-%2320232a.svg?logo=react&logoColor=%2361DAFB)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?logo=typescript&logoColor=fff)](https://www.typescriptlang.org/)
[![Postgres](https://img.shields.io/badge/Postgres-%23316192.svg?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://mit-license.org/)

# MedicineNotes

Система для отслеживания приема лекарств с возможностью создания шаблонов, управления фактами приема и просмотра статистики.

## 📋 Описание

**MedicineNotes** — это full-stack веб-приложение для управления курсом приема лекарственных препаратов.

### Основные возможности

- **Создание шаблонов лекарств** — определение названия препарата, описания, длительности курса и количества приемов в день
- **Автоматическая генерация фактов приема** — система создает плановые записи на основе шаблона
- **Отслеживание статуса** — отметка приемов как выполненные (`DONE`), пропущенные (`SKIPPED`) или ожидаемые (`PENDING`)
- **Персональная статистика** — просмотр статистики по шаблонам и фактам приема
- **JWT-аутентификация** — безопасная система входа с ролями USER и ADMIN

### Архитектура

Приложение построено по архитектуре **REST API** с раздельными frontend и backend модулями:
- **Backend** — Spring Boot приложение предоставляет REST эндпоинты
- **Frontend** — React SPA взаимодействует с backend через Axios
- **База данных** — PostgreSQL хранит данные пользователей, шаблонов и фактов приема
- **Docker** — все сервисы контейнеризированы и запускаются через Docker Compose

## 🛠 Технологический стек

| Категория | Технология | Версия |
|-----------|------------|--------|
| **Backend** | |        |
| Язык программирования | Java | 21     |
| Фреймворк | Spring Boot | 4.0.1  |
| ORM | Spring Data JPA | —      |
| Безопасность | Spring Security + JWT (jjwt) | 0.13.0 |
| Кэширование | Spring Cache + Caffeine | —      |
| Rate Limiting | Resilience4j | 2.3.0  |
| Утилиты | Lombok | —      |
| **Frontend** | |        |
| Язык | TypeScript | 5.9    |
| Фреймворк | React | 19.2.0 |
| Роутинг | React Router DOM | 7.12.0 |
| HTTP-клиент | Axios | 1.13.2 |
| Сборка | Vite | 7.2.4  |
| Линтинг | ESLint | 9.39.1 |
| **База данных** | |        |
| Основная БД | PostgreSQL | 18     |
| **Инфраструктура** | |        |
| Контейнеризация | Docker + Docker Compose | —      |
| Веб-сервер | Nginx | —      |
| **Сборка** | |        |
| Backend | Gradle | —      |
| Frontend | npm | —      |

## 📦 Зависимости

### Backend (Spring Boot)

#### 🔐 Безопасность и аутентификация

| Зависимость | Версия | Назначение |
|-------------|--------|------------|
| `spring-boot-starter-security` | — | Spring Security |
| `jjwt-api` | 0.13.0 | JWT токены (API) |
| `jjwt-impl` | 0.13.0 | JWT токены (Implementation) |
| `jjwt-jackson` | 0.13.0 | JWT токены (JSON serialization) |

#### 🗄️ Работа с данными

| Зависимость | Версия | Назначение |
|-------------|--------|------------|
| `spring-boot-starter-data-jpa` | — | ORM (Hibernate) |
| `spring-boot-starter-validation` | — | Валидация данных |
| `postgresql` | latest | PostgreSQL драйвер |

#### ⚡ Производительность

| Зависимость | Версия | Назначение |
|-------------|--------|------------|
| `spring-boot-starter-cache` | — | Кэширование |
| `caffeine` | — | Caffeine cache provider |
| `resilience4j-spring-boot3` | 2.3.0 | Rate Limiting |
| `spring-boot-starter-actuator` | — | Мониторинг и метрики |

#### 🛠️ Утилиты

| Зависимость | Версия | Назначение |
|-------------|--------|------------|
| `lombok` | — | Автоматическая генерация кода |
| `spring-boot-devtools` | — | Hot reload для разработки |
| `spring-boot-docker-compose` | — | Интеграция с Docker Compose |

#### 🧪 Тестирование

| Зависимость | Версия | Назначение |
|-------------|--------|------------|
| `spring-boot-starter-test` | — | Spring Boot тесты |
| `spring-security-test` | — | Тесты безопасности |
| `junit-platform-launcher` | — | JUnit 5 launcher |

---

### Frontend (React)

#### 📦 Основные зависимости

```json
{
  "dependencies": {
    "react": "^19.2.0",           // UI библиотека
    "react-dom": "^19.2.0",       // DOM рендеринг
    "react-router-dom": "^7.12.0", // Роутинг
    "axios": "^1.13.2"            // HTTP клиент
  }
}
```

#### 🔧 Зависимости для разработки

```json
{
  "devDependencies": {
    "typescript": "~5.9.3",        // Типизация
    "typescript-eslint": "^8.46.4", // ESLint для TS
    "vite": "^7.2.4",              // Сборщик
    "@vitejs/plugin-react": "^5.1.1", // Vite плагин для React
    "eslint": "^9.39.1",           // Линтер
    "eslint-plugin-react-hooks": "^7.0.1", // Правила для хуков
    "eslint-plugin-react-refresh": "^0.4.24", // Правила для Fast Refresh
    "@types/react": "^19.2.5",     // Типы для React
    "@types/react-dom": "^19.2.3", // Типы для ReactDOM
    "@types/node": "^24.10.1"      // Типы для Node.js
  }
}
```

## 📦 Функциональные возможности

- **Управление пользователями**
  - Регистрация и аутентификация
  - JWT-токены
  - Роли: USER, ADMIN

- **Шаблоны лекарств**
  - Создание шаблонов с параметрами (название, описание, длительность курса, количество приемов в день)
  - Просмотр и управление шаблонами

- **Факты приема**
  - Автоматическая генерация фактов приема на основе шаблонов
  - Статусы: `PENDING`, `DONE`, `SKIPPED`
  - Фильтрация по дате

- **Статистика**
  - Статистика по шаблонам
  - Статистика по фактам приема

## 🚀 Быстрый старт

### Требования

- **Java 21+**
- **Node.js 18+**
- **Docker** + **Docker Compose** (рекомендуется)

### Запуск через Docker Compose

```bash
docker-compose up --build
```

После запуска сервисы будут доступны:
- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080
- **PostgreSQL:** localhost:5433

### Локальный запуск

#### Backend

```bash
cd backend
./gradlew bootRun
```

#### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend будет доступен по адресу: http://localhost:5173

## 📁 Структура проекта

```
MedicineNotes/
├── backend/                    # Spring Boot приложение
│   ├── src/main/java/
│   │   └── com/arnor4eck/medicinenotes/
│   │       ├── config/         # Конфигурация (Security, Cache, Limits)
│   │       ├── controller/     # REST контроллеры
│   │       ├── entity/         # JPA сущности
│   │       ├── repository/     # Репозитории
│   │       ├── service/        # Бизнес-логика
│   │       └── util/           # DTO, JWT, валидация
│   ├── src/main/resources/
│   │   └── application*.yaml   # Конфигурация
│   ├── src/test/java/          # Тесты
│   ├── build.gradle
│   └── Dockerfile
│
├── frontend/                   # React приложение
│   ├── src/
│   │   ├── components/         # UI компоненты
│   │   ├── pages/              # Страницы
│   │   ├── service/            # API сервисы
│   │   └── types/              # TypeScript типы
│   ├── package.json
│   ├── vite.config.ts
│   └── Dockerfile
│
└── compose.yaml                # Docker Compose конфигурация
```

### Лимиты (настраиваемые)

```yaml
limits:
  max-templates: 15        # Макс. шаблонов на пользователя
  max-duration: 90         # Макс. длительность курса (дней)
  max-times-a-day: 18      # Макс. приемов в день
```

## 🔐 Безопасность

- **JWT-аутентификация** со временем жизни токена 1 час
- **BCrypt** для хэширования паролей
- **Spring Security** с STATELESS сессиями
- Публичные эндпоинты: `/api/user/registration`, `/api/user/authentication`

## 🧪 Тестирование

### Запуск тестов backend

```bash
cd backend
./gradlew test
```

В проекте используются:
- Unit-тесты сервисов
- MVC-тесты контроллеров

## 📊 Модель данных

### Основные сущности

- **User** — пользователь (id, username, email, password, role)
- **MedicineTemplate** — шаблон лекарства (id, name, description, until, quantityPerDay, creator)
- **Intake** — факт приема (id, template, adoptedIn, shouldAdoptedIn, status)

## ⚙️ Конфигурация

### Кэширование

Включено кэширование для сущностей:
- `intake` — факты приема
- `template` — шаблоны лекарств

Provider: **Caffeine**

## 📝 Скрипты

### Backend (Gradle)

```bash
./gradlew bootRun          # Запуск приложения
./gradlew build            # Сборка
./gradlew test             # Тесты
./gradlew clean            # Очистка
```

### Frontend (npm)

```bash
npm run dev      # Режим разработки
npm run build    # Сборка для production
npm run lint     # Линтинг
npm run preview  # Preview production сборки
```

---

## 📋 Планы на развитие проекта

### 🔜 Ближайшие задачи

- [ ] **Уведомления** — напоминания о предстоящем приеме лекарств
- [ ] **OpenAPI документация** — Swagger UI для REST API
- [ ] **CI/CD** - Настроить CI/CD пайплайн
- [ ] **Мониторинг** — дашборды для Actuator метрик
- [ ] **Scheduler** — Реализовать Scheduler для фоновой генерации фактов приема

---

## 📄 Лицензия

Проект распространяется под лицензией MIT.