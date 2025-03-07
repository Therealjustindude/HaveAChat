# Load variables from .env
ifneq (,$(wildcard .env))
    include .env
    export $(shell sed 's/=.*//' .env)
endif

########
# ALL  #
########
build:
	@echo "Building all containers..."
	docker compose build
	@echo "Containers have been built!"

up:
	@echo "Starting all containers..."
	docker compose up -d
	@echo "Containers are up and running!"

up-build:
	@echo "Building and starting all containers..."
	docker compose up --build -d
	@echo "All containers have been built and are up and running!"

down:
	@echo "Stopping and removing all containers..."
	docker compose down
	@echo "Containers have been stopped and removed!"

reset:
	@echo "Stopping and removing all containers and volumes..."
	docker compose down --volumes
	@echo "Rebuilding and starting containers..."
	docker compose up --build -d
	@echo "All containers have been reset and are running!"

migrate-up:
	@echo "Running migrations..."
	docker run --rm \
		--network haveachat-network \
		-v $(PWD)/haveachat-backend/migrations:/migrations \
		migrate/migrate \
		-path=/migrations \
		-database "mysql://$(MYSQL_USER):$(MYSQL_PASSWORD)@tcp(haveachat-mysql:3306)/$(MYSQL_DATABASE)" \
		up
	@echo "Migrations have been applied!"

get-current-migration-ver:
	docker run --rm \
		--network haveachat-network \
		-v /Users/justindavies/Development/my_code/HaveAChat/haveachat-backend/migrations:/migrations \
		migrate/migrate \
		-path=/migrations \
		-database "mysql://justin_davies:th1s_i5_4_M3@tcp(haveachat-mysql:3306)/haveachat_db" \
		version

force-clean-db:
	docker run --rm \
		--network haveachat-network \
		-v /Users/justindavies/Development/my_code/HaveAChat/haveachat-backend/migrations:/migrations \
		migrate/migrate \
		-path=/migrations \
		-database "mysql://justin_davies:th1s_i5_4_M3@tcp(haveachat-mysql:3306)/haveachat_db" \
		force $(version)

migrate-rollback:
	@echo "Rolling back migrations..."
	docker run --rm \
		--network haveachat-network \
		-v $(PWD)/haveachat-backend/migrations:/migrations \
		migrate/migrate \
		-path=/migrations \
		-database "mysql://$(MYSQL_USER):$(MYSQL_PASSWORD)@tcp(haveachat-mysql:3306)/$(MYSQL_DATABASE)" \
		down $(arg)
	@echo "Migrations have been rolled back!"

#########
# DB    #
#########
build-db:
	@echo "Building the database container..."
	docker compose build db
	@echo "Database container has been built!"

up-db:
	@echo "Starting the database container..."
	docker compose up -d db
	@echo "Database container is running!"

down-db:
	@echo "Stopping and removing the database container..."
	docker compose down db
	@echo "Database container has been stopped and removed!"

reset-db:
	@echo "Stopping and removing the database container and volumes..."
	docker compose down --volumes db
	@echo "Rebuilding and starting the database container..."
	docker compose up -d --build db
	@echo "Database container has been reset and is running!"


########### 
# Backend #
###########
build-backend:
	@echo "Building the backend container..."
	docker compose build backend
	@echo "Backend container has been built!"

up-backend:
	@echo "Starting the backend container..."
	docker compose up -d backend
	@echo "Backend container is running!"

down-backend:
	@echo "Stopping and removing the backend container..."
	docker compose down backend
	@echo "Backend container has been stopped and removed!"

reset-backend:
	@echo "Stopping and removing the backend container..."
	docker compose down --volumes backend
	@echo "Rebuilding and starting the backend container..."
	docker compose up -d --build backend
	@echo "Backend container has been reset and is running!"

############
# Frontend #
############
build-frontend:
	@echo "Building the frontend container..."
	docker compose build frontend
	@echo "Frontend container has been built!"

up-frontend:
	@echo "Starting the frontend container..."
	docker compose up -d frontend
	@echo "Frontend container is running!"

down-frontend:
	@echo "Stopping and removing the frontend container..."
	docker compose down frontend
	@echo "Frontend container has been stopped and removed!"

reset-frontend:
	@echo "Stopping and removing the frontend container..."
	docker compose down --volumes frontend
	@echo "Rebuilding and starting the frontend container..."
	docker compose up -d --build frontend
	@echo "Frontend container has been reset and is running!"
