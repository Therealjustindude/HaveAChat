# HaveAChat
A chat application that allows users to communicate in real-time. The application includes a frontend, backend, and a MySQL database for persistent storage.

## Table of Contents
- [Project Setup](#project-setup)
- [Running Migrations](#running-migrations)


## Project Setup
The project uses Docker to containerize the backend, frontend, and MySQL database. 
The Docker Compose file is configured to launch the following services:
- **MySQL**: A MySQL database for persistent storage.
- **Backend**: The backend service that interacts with the database.
    - golang-migrate is used to create migrations
- **Frontend**: The frontend service that interacts with the backend.

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/HaveAChat.git
    cd HaveAChat
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
    MYSQL_ROOT_PASSWORD=your_root_password
    MYSQL_USER=your_mysql_user
    MYSQL_PASSWORD=your_mysql_password
    MYSQL_DATABASE=your_database_name
    ```

5. Build and start the containers using Makefile commands:
    ```bash
    make up-build
    ```

## Running Migrations

Once the containers are built and up...

5. Apply the database migrations using the following command:
    ```bash
    make migrate-up
    ```

#### Note on migrate-up
This make command is used to apply database migrations to the MySQL container running in the haveachat-network. It does the following:

* Runs the migrate/migrate Docker container: This container is responsible for managing the database migrations.
* Mounts the migration files: It mounts the local migrations folder (haveachat-backend/migrations) to the /migrations folder inside the container.
* Uses environment variables for database credentials: The command connects to the MySQL container using the provided database credentials (MYSQL_USER, MYSQL_PASSWORD, MYSQL_DATABASE), which are set in the environment.
* Applies the migrations: It runs the up command to apply all the pending migrations to the database schema.

This command helps keep the database schema in sync with the application code and ensures that necessary database changes are automatically applied.

#### Local Development Notes


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
