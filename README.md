# taubakul-zhanibek-student-api

Author: Taubakul Zhanibek

Student API by Taubakul Zhanibek — a REST API for managing students (create, read, update, delete) built with Spring Boot and PostgreSQL; DevOps midterm project: Linux, Git, Docker Compose and Jenkins CI.

## Tech stack

- Java 21 (Gradle toolchain)
- Spring Boot 4.1.2: Web MVC, Data JPA, Validation, Actuator
- PostgreSQL (runtime), H2 in-memory (tests only)
- Gradle 9.8.2 via the Gradle wrapper (`./gradlew`)

## Build and test

```bash
./gradlew clean build      # compile, run all tests, build the jar
./gradlew test             # tests only (H2, no PostgreSQL or Docker needed)
```

The executable jar is created in `build/libs/taubakul-zhanibek-student-api-<version>.jar`.

## Run locally

PostgreSQL must be running. Set the environment variables (see `.env.example`) and start the app:

```bash
export DB_HOST=localhost DB_PORT=5432 DB_NAME=studentdb DB_USERNAME=<user>
export DB_PASSWORD=<password>
SERVER_PORT=8081 ./gradlew bootRun
# or
SERVER_PORT=8081 java -jar build/libs/taubakul-zhanibek-student-api-1.0.1.jar
```

On first start, if the `students` table is empty, three test students are inserted
(the first one is Zhanibek Taubakul).

## Environment variables

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `localhost` | PostgreSQL host (Compose service name inside Docker) |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `studentdb` | Database name |
| `DB_USERNAME` | — (required) | Database user |
| `DB_PASSWORD` | — (required) | Database password, never stored in the repository |
| `SERVER_PORT` | `8080` | HTTP port of the application |

Copy `.env.example` to `.env` for local values; `.env` must not be committed.

## Run with Docker

Requirements: Docker with Docker Compose v2.

```bash
cp .env.example .env              # then set DB_USERNAME and a real DB_PASSWORD
                                  # e.g. DB_PASSWORD=$(openssl rand -hex 24)
docker compose up -d --build      # build the image and start PostgreSQL + the app
docker compose ps                 # db must be "healthy", app "Up"
curl http://localhost:8081/api/info
./scripts/api_test.sh             # end-to-end API test (default URL http://localhost:8081)
docker compose logs -f app        # follow application logs
docker compose down               # stop and remove containers (data is kept in the volume)
docker compose down -v            # ...and also delete the database volume (all data!)
```

| Item | Value |
|---|---|
| Image | `taubakul-zhanibek-student-api:v1.0-taubakul-zhanibek` (multi-stage: JDK build → JRE runtime) |
| Compose project | `taubakul-zhanibek-student-api` |
| Containers | `taubakul-zhanibek-app`, `taubakul-zhanibek-postgres` |
| App URL | `http://localhost:8081` (host 8081 → container 8080) |
| Database | PostgreSQL 16, not published to the host, reachable as `db:5432` inside the network |
| Network / volume | `taubakul-zhanibek-net` / `taubakul-zhanibek-pgdata` |

Variables from `.env` (see `.env.example`): `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`.
Inside Compose the app gets `DB_HOST=db` and `DB_PORT=5432` from `docker-compose.yml`.

A Postman collection with the same requests is in
`docs/postman/Taubakul_Zhanibek_Student_API.postman_collection.json` (variable `baseUrl`).

## REST API

Base URL: `http://localhost:<SERVER_PORT>`

| Method | Path | Description | Responses |
|---|---|---|---|
| GET | `/api/info` | Project name, author, version | 200 |
| GET | `/api/students` | List all students | 200 |
| GET | `/api/students/{id}` | Get one student | 200, 404 |
| POST | `/api/students` | Create a student | 201 + `Location`, 400, 409 |
| PUT | `/api/students/{id}` | Update a student | 200, 400, 404, 409 |
| DELETE | `/api/students/{id}` | Delete a student | 204, 404 |
| GET | `/actuator/health` | Health check | 200 |
| GET | `/actuator/info` | Application info | 200 |

Student JSON:

```json
{
  "firstName": "Zhanibek",
  "lastName": "Taubakul",
  "email": "taubakulzhanibek@gmail.com",
  "groupName": "DevOps-2026",
  "course": 3
}
```

Validation: all fields are required, `email` must be a valid and unique address, `course` is 1–4.

Errors use one JSON format:

```json
{
  "timestamp": "2026-10-27T17:15:24.675Z",
  "status": 404,
  "error": "Not Found",
  "message": "Student with id 999999 not found",
  "path": "/api/students/999999"
}
```

Validation errors (400) also contain `errors` with a message per field; a duplicate email returns 409.

## Changelog

### 1.0.1 — Taubakul Zhanibek
- Docker: multi-stage Dockerfile, Docker Compose with PostgreSQL 16 (health check, named volume,
  dedicated network), API test script `scripts/api_test.sh` and a Postman collection.
- Version bumped to 1.0.1 (shown by `GET /api/info`).

### 1.0.0 — Taubakul Zhanibek
- Added `PUT /api/students/{id}` (update a student, branch `feature/taubakul-zhanibek-crud`):
  returns 200 with the updated student, 404 if the student does not exist,
  400 for invalid data and 409 if the new email already belongs to another student
  (a student may keep their own email).
- Initial version: create, list, get by id and delete students; `/api/info`; seed data.
