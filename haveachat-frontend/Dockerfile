# Stage 1: Build the application
FROM node:23.5.0-alpine AS builder

# Set working directory
WORKDIR /app

# Install dependencies
COPY package.json package-lock.json ./
RUN npm ci

# Remove genereate route tree file before building to make sure any new routes get added
RUN rm -f src/routeTree.gen.ts

# Copy the rest of the application code
COPY . .

# Build the application
RUN npm run build

# Stage 2: Run the application
FROM node:23.5.0-alpine

# Set working directory
WORKDIR /app

# Copy built assets from the builder stage
COPY --from=builder /app /app

# Expose the port the app runs on
EXPOSE 3000

# Start the application
CMD ["npm", "start"]