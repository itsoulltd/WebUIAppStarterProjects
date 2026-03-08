# =========================
# Build stage
# =========================
FROM node:18-alpine AS build
LABEL authors="towhid.islam"
MAINTAINER ${authors}

WORKDIR /app
# Copy package files
COPY package*.json .
# Install dependencies
RUN npm ci
# Copy source code
COPY src src
COPY public public
COPY tsconfig.json .
COPY .env* .

# Build React production bundle
RUN npm run build


# =========================
# Runtime stage
# =========================
FROM nginx:alpine
LABEL authors="towhid.islam"
MAINTAINER ${authors}

# Remove default nginx static files
RUN rm -rf /usr/share/nginx/html/*
# Copy build output from previous stage
COPY --from=build /app/build /usr/share/nginx/html
# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Copy ssl certificates
COPY ssl /etc/ssl

EXPOSE 80
EXPOSE 443

ENTRYPOINT ["nginx", "-g", "daemon off;"]