# SSO Spring Boot 3 + Angular 16 + Keycloak 22

A complete Single Sign-On (SSO) implementation using Spring Boot 3, Angular 16, and Keycloak 22. This project demonstrates modern authentication patterns with OAuth2/OpenID Connect.

📘 Blog Post: [Single Sign-On with Angular 16 Keycloak 22 Spring Boot 3](https://jarmx.blogspot.com/2023/07/single-sign-on-with-angular-16-keycloak.html)

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Angular 16    │───▶│   Keycloak 22   │◀───│ Spring Boot 3   │
│   (Frontend)    │    │ (Identity Provider)│    │   (Backend)     │
│   Port: 4200    │    │   Port: 8080    │    │   Port: 8081    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## ✨ Features

- **Single Sign-On**: Login once, access multiple services
- **JWT Token Authentication**: Secure stateless authentication
- **Role-based Authorization**: Fine-grained access control
- **CORS Configuration**: Proper cross-origin resource sharing
- **Docker Deployment**: Easy setup with Docker Compose
- **Modern Tech Stack**: Latest versions of Spring Boot 3, Angular 16, and Keycloak 22

## 🛠️ Technology Stack

### Frontend (Angular)
- **Angular**: 16.x
- **Node.js**: 18.x
- **NPM**: 9.x
- **Keycloak Angular**: Latest
- **Angular CLI**: 16.x

### Backend (Spring Boot)
- **Spring Boot**: 3.1.2
- **Spring Security**: 6.1
- **OAuth2 Resource Server**: JWT support
- **Java**: 17
- **Maven**: 3.x

### Identity Provider
- **Keycloak**: 22.0.0
- **Docker**: Latest
- **Docker Compose**: v3

## 🚀 Quick Start

### Prerequisites

Make sure you have the following installed:
- **Java 17** or higher
- **Node.js 18** or higher
- **Docker & Docker Compose**
- **Git**

### 1. Clone the Repository

```bash
git clone <repository-url>
cd sso-spring-boot3-angular16-keycloak22
```

### 2. Start Keycloak with Docker Compose

```bash
# Create and start Keycloak container
docker-compose up -d

# Wait for Keycloak to be ready (usually takes 30-60 seconds)
docker-compose logs -f keycloak
```

**Keycloak Admin Console**: http://localhost:8080
- Username: `admin`
- Password: `admin`

### 3. Configure Keycloak

#### Create Realm
1. Access Keycloak Admin Console
2. Create new realm: `demo`

#### Create Client
1. Go to Clients → Create Client
2. Client ID: `spring-boot-angular-keycloak`
3. Client authentication: **OFF** (public client)
4. Valid redirect URIs: `http://localhost:4200/*`
5. Web origins: `http://localhost:4200`

#### Create Test User
1. Go to Users → Add user
2. Username: `testuser`
3. Set password in Credentials tab
4. Assign roles as needed

### 4. Start Spring Boot Backend

```bash
# Navigate to backend directory
cd backend

# Run the application
./mvnw spring-boot:run

# Or with Maven wrapper
mvn spring-boot:run
```

**Backend API**: http://localhost:8081

### 5. Start Angular Frontend

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
ng serve

# Or run with specific port
ng serve --port 4200
```

**Frontend Application**: http://localhost:4200

## 📁 Project Structure

```
sso-spring-boot3-angular16-keycloak22/
├── backend/                          # Spring Boot application
│   ├── src/main/java/com/henry/
│   │   ├── config/
│   │   │   ├── SecurityConfig.java   # Security & CORS configuration
│   │   │   └── WebConfig.java        # Web MVC configuration
│   │   ├── controller/
│   │   │   └── UserController.java   # REST API endpoints
│   │   ├── model/
│   │   │   └── User.java            # User entity
│   │   └── Application.java         # Main application class
│   ├── src/main/resources/
│   │   └── application.yml          # Application configuration
│   └── pom.xml                      # Maven dependencies
├── frontend/                        # Angular application
│   ├── src/
│   │   ├── app/
│   │   │   ├── app.module.ts       # App module with Keycloak setup
│   │   │   └── app.component.ts    # Main app component
│   │   └── assets/
│   │       └── silent-check-sso.html # SSO silent check
│   ├── package.json                 # NPM dependencies
│   └── angular.json                 # Angular configuration
├── docker-compose.yml               # Keycloak Docker setup
└── README.md                        # This file
```

## ⚙️ Configuration Details

### Angular Configuration (app.module.ts)

```typescript
function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        realm: 'demo',
        url: 'http://localhost:8080',
        clientId: 'spring-boot-angular-keycloak'
      },
      initOptions: {
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html'
      }
    });
}
```

### Spring Boot Configuration (application.yml)

```yaml
server:
  port: 8081
  servlet:
    context-path: /

spring:
  security:
    oauth2:
      resource-server:
        jwt:
          issuer-uri: http://localhost:8080/realms/demo
          jwk-set-uri: http://localhost:8080/realms/demo/protocol/openid-connect/certs
```

### Docker Compose Configuration

```yaml
version: '3'
services:
  keycloak:
    image: quay.io/keycloak/keycloak:22.0.0
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
    ports:
      - 8080:8080
    volumes:
      - ./config/:/opt/keycloak/data/import:ro
    entrypoint: '/opt/keycloak/bin/kc.sh start-dev --import-realm'
```

## 🔧 API Endpoints

### User Management
- **POST** `/api/users` - Create new user
- **GET** `/api/users/{id}` - Get user by ID

### Example API Usage

```bash
# Get JWT token from Keycloak (replace with actual token)
TOKEN="your-jwt-token-here"

# Create user
curl -X POST http://localhost:8081/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"username": "john", "email": "john@example.com"}'

# Get user
curl -X GET http://localhost:8081/api/users/1 \
  -H "Authorization: Bearer $TOKEN"
```

## 🔐 Security Features

### JWT Token Validation
- Automatic JWT validation using Spring Security
- Token signature verification via JWK Set URI
- Role-based access control from Keycloak roles

### CORS Configuration
- Configured for cross-origin requests
- Allows Angular frontend to communicate with Spring Boot backend
- Supports preflight requests

### Role Mapping
- Keycloak roles automatically mapped to Spring Security authorities
- Roles prefixed with `ROLE_` for Spring Security compatibility

## 🧪 Testing

### Manual Testing
1. Access http://localhost:4200
2. Click login (redirects to Keycloak)
3. Login with your Keycloak user
4. Access protected resources
5. Verify JWT token in browser developer tools

### Integration Testing
```bash
# Run backend tests
cd backend
./mvnw test

# Run frontend tests
cd frontend
npm test
```

## 🐛 Troubleshooting

### Common Issues

#### Keycloak Not Starting
```bash
# Check container logs
docker-compose logs keycloak

# Restart container
docker-compose restart keycloak
```

#### CORS Errors
- Verify `http://localhost:4200` is added to Keycloak client's valid redirect URIs
- Check Web Origins setting in Keycloak client configuration

#### JWT Token Issues
- Ensure issuer-uri matches your Keycloak realm
- Verify JWK Set URI is accessible
- Check token expiration time

#### Angular Build Errors
```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Update Angular CLI
npm install -g @angular/cli@latest
```

### Debug Mode

#### Enable Spring Boot Debug Logging
```yaml
logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.web: DEBUG
```

#### Enable Keycloak Debug Logging
```bash
# Add to docker-compose environment
KC_LOG_LEVEL: DEBUG
```

## 📦 Production Deployment

### Environment Configuration

#### Production application.yml
```yaml
spring:
  security:
    oauth2:
      resource-server:
        jwt:
          issuer-uri: https://your-keycloak-domain/realms/demo
          jwk-set-uri: https://your-keycloak-domain/realms/demo/protocol/openid-connect/certs
```

#### Production Angular Configuration
```typescript
config: {
  realm: 'demo',
  url: 'https://your-keycloak-domain',
  clientId: 'spring-boot-angular-keycloak'
}
```

### Security Considerations
- Use HTTPS in production
- Set proper CORS origins (avoid wildcards)
- Configure proper session timeouts
- Implement proper logging and monitoring
- Use production Keycloak setup with proper database

## 📚 Additional Resources

### Documentation
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [Angular Keycloak Integration](https://www.npmjs.com/package/keycloak-angular)

### Related Projects
- [Spring Boot OAuth2 Examples](https://github.com/spring-projects/spring-security-samples)
- [Keycloak Quickstarts](https://github.com/keycloak/keycloak-quickstarts)

