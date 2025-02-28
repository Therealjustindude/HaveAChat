# Use official Go image as base
FROM golang:1.23.4 AS builder

# Install bash in the image temporarily for debugging
RUN apt-get update && apt-get install -y bash

# Set the working directory for the container build context
WORKDIR /app

# Copy go.mod and go.sum files to download dependencies
COPY go.mod go.sum ./

# Download Go dependencies
RUN go mod download

# Copy the application code into the container (this includes cmd/api where main.go is)
COPY . .

# Set the working directory to where main.go resides
WORKDIR /app/cmd/api

# Build the Go application binary
RUN go build -o /app/main .

# Use a smaller base image for production
FROM gcr.io/distroless/base-debian12

# Set working directory
WORKDIR /root/

# Copy the built binary from the builder stage
COPY --from=builder /app/main .

# Expose the application's port
EXPOSE 8080

# Command to run the application
CMD ["./main"]