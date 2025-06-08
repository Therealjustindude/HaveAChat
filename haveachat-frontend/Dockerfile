# Base image
FROM node:23.5.0-alpine AS base
WORKDIR /app
COPY package*.json ./
RUN npm ci

# --- Dev Stage ---
FROM base AS dev
COPY . .
EXPOSE 3000
CMD ["npm", "run", "dev"]

# --- Build Stage (for production assets) ---
FROM base AS build
COPY . .
RUN npm run build

# --- Prod Stage ---
FROM node:23.5.0-alpine AS prod
WORKDIR /app
COPY --from=build /app/dist ./dist
COPY --from=build /app/package*.json ./
RUN npm ci --omit=dev
EXPOSE 3000
CMD ["npm", "start"]