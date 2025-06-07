# HaveAChat
A chat application that allows users to communicate in real-time. The application includes a frontend, backend, and a Postgres database for persistent storage.

## Table of Contents
- [Project Setup](#project-setup)

## Project Setup
The project uses Docker to containerize the Java backend, frontend. 
The Docker Compose file is configured to launch the following services:
- **Backend**: The backend service that interacts with the database.
- **Frontend**: The frontend service that interacts with the backend.

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/HaveAChat-Java.git
    cd HaveAChat-Java
    ```

2. Make sure you have Docker and Docker Compose installed. You can check if they are installed by running:
    ```bash
    docker --version
    docker-compose --version
    ```

3. Copy the `.env.template` file to create your own `.env` file:
    ```bash
    cp .env.template .env
    ```

4. Open the `.env` file and customize it with your database credentials:
    - Ensure the values match your local or production database configuration.
    ```env
    POSTGRES_ROOT_PASSWORD=your_root_password
    POSTGRES_USER=your_postgres_user
    POSTGRES_PASSWORD=your_postgres_password
    POSTGRES_DATABASE=your_database_name
    ```

5. Build and start the containers using Makefile commands:
    ```bash
    make build-up
    ```

#### Local Development Notes
From backend directory run this command to generate openapi spec:
```bash
    openapi-generator-cli generate \
    -g typescript-fetch \
    -i ./openapi.json \
    -o ../haveachat-frontend/app/api
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
