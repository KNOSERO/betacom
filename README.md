# betacom

## Run locally

Install JDK 21, then run the Spring Boot app from the project root:

```powershell
.\gradlew.bat :app:bootRun
```

On macOS or Linux, use `./gradlew :app:bootRun` instead. The app starts at <http://localhost:8080>.

## Run with Docker Compose

Compose starts the application and MySQL, and keeps database files in a named volume:

```sh
docker compose up --build
```

Open <http://localhost:8080>. Stop the services with `Ctrl+C`, then remove the containers with:

```sh
docker compose down
```

To remove the database data too, run `docker compose down --volumes`.

## Run with Podman Compose

In Podman Desktop or Linux with Podman installed, install a Compose provider (`podman-compose` or `docker-compose`) if one is not already available. Podman's `podman compose` command uses an installed provider.

From the project root, start the application and MySQL:

```sh
podman compose up --build
```

Open <http://localhost:8080>. Stop the services with `Ctrl+C`, then remove the containers with:

```sh
podman compose down
```

To remove the database data too, run `podman compose down --volumes`.
