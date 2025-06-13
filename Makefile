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

build-up:
	@echo "Building and starting all containers..."
	docker compose up --build -d
	@echo "All containers have been built and are up and running!"

down:
	@echo "Stopping and removing all containers..."
	docker compose down --remove-orphans
	@echo "Containers have been stopped and removed!"

reset:
	@echo "Stopping and removing all containers and volumes..."
	docker compose down --volumes
	@echo "Rebuilding and starting containers..."
	docker compose up --build -d
	@echo "All containers have been reset and are running!"

reset-f-b:
	@echo "Stopping and removing the backend and frontend containers..."
	docker compose rm -sf backend frontend
	@echo "Rebuilding and starting the backend and frontend containers..."
	docker compose up -d --build backend frontend
	@echo "Backend and frontend containers have been reset and are running!"

	

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
	docker compose rm -sf db
	@echo "Database container has been stopped and removed!"

reset-db:
	@echo "Stopping and removing the database container..."
	docker compose rm -sf db
	@echo "Removing the database volume..."
	docker volume rm postgres-haveachat-data || true
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
	docker compose rm -sf backend
	@echo "Backend container has been stopped and removed!"

reset-backend:
	@echo "Stopping and removing the backend container..."
	docker compose rm -sf backend
	@echo "Rebuilding and starting the backend container..."
	docker compose up -d --build backend
	@echo "Backend container has been reset and is running!"

test-backend: build
	docker run --rm $(IMAGE_NAME) gradle test

############
# Frontend #
############
build-frontend:
	@echo "Building the frontend container (no cache)..."
	docker compose build --no-cache frontend
	@echo "Frontend container has been built!"

up-frontend:
	@echo "Starting the frontend container..."
	docker compose up -d frontend
	@echo "Frontend container is running!"

down-frontend:
	@echo "Stopping and removing the frontend container..."
	docker compose rm -sf frontend
	@echo "Frontend container has been stopped and removed!"

reset-frontend:
	@echo "Stopping and removing the frontend container..."
	docker compose rm -sf frontend
	@echo "Rebuilding and starting the frontend container (no cache)..."
	docker compose build --no-cache frontend
	docker compose up -d frontend
	@echo "Frontend container has been reset and is running!"
