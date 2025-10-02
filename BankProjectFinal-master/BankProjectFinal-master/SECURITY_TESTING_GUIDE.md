# Spring Security Testing Guide for Bank Project

## Setup Instructions

### 1. Database Setup
Run the `users_setup.sql` script in your Oracle database to create the USERS table and insert sample data.

### 2. Test Credentials
The following users are available for testing:

| Username | Password    | Role    | Access Level |
|----------|-------------|---------|--------------|
| admin    | admin123    | ADMIN   | Full access to all endpoints |
| manager  | manager123  | MANAGER | Access to customers, accounts, transactions |
| sarah    | sarah123    | MANAGER | Access to customers, accounts, transactions |
| user     | user123     | USER    | Access only to transactions |
| john     | john123     | USER    | Access only to transactions |

## API Endpoint Security Rules

### Admin Only (ROLE_ADMIN)
- `GET/POST/PUT/DELETE /banks/**` - Bank management
- `GET/POST/PUT/DELETE /users/**` - User management

### Admin + Manager (ROLE_ADMIN, ROLE_MANAGER)
- `GET/POST/PUT/DELETE /customers/**` - Customer management
- `GET/POST /accounts` - Account management
- `GET /accounts/id/**` - View specific accounts

### All Authenticated Users (ROLE_ADMIN, ROLE_MANAGER, ROLE_USER)
- `POST /accounts/deposit/**` - Deposit money
- `POST /accounts/withdraw/**` - Withdraw money  
- `POST /accounts/transfer/**` - Transfer money
- `GET/POST /transactions/**` - View and create transactions

## Testing with Postman

### 1. Authentication Setup
- Use **Basic Auth** in Postman
- Add Authorization header: `Authorization: Basic <base64(username:password)>`

### 2. Test Scenarios

#### A. Test Admin Access
```
Method: GET
URL: http://localhost:8080/banks
Authorization: Basic Auth
Username: admin
Password: admin123
Expected: 200 OK - List of banks
```

#### B. Test Manager Access  
```
Method: GET
URL: http://localhost:8080/customers
Authorization: Basic Auth
Username: manager
Password: manager123
Expected: 200 OK - List of customers
```

#### C. Test User Access (Should Fail)
```
Method: GET
URL: http://localhost:8080/banks
Authorization: Basic Auth
Username: user
Password: user123
Expected: 403 Forbidden
```

#### D. Test Transaction Access (Should Work for All)
```
Method: POST
URL: http://localhost:8080/accounts/deposit/accounts/id/1
Authorization: Basic Auth
Username: user
Password: user123
Body: {"depositAmount": 100.00}
Expected: 200 OK or appropriate response
```

### 3. Expected Error Responses

#### Unauthorized (401)
```json
{
    "timestamp": "2025-09-24T...",
    "status": 401,
    "error": "Unauthorized",
    "message": "Full authentication is required to access this resource",
    "path": "/banks"
}
```

#### Forbidden (403)
```json
{
    "timestamp": "2025-09-24T...", 
    "status": 403,
    "error": "Forbidden",
    "message": "Access Denied",
    "path": "/banks"
}
```

## Quick Test Commands for Postman

### 1. Create New User (Admin Only)
```
POST http://localhost:8080/users
Authorization: Basic admin:admin123
Content-Type: application/json

{
    "username": "newuser",
    "password": "newpass123",
    "userType": "USER", 
    "firstName": "New",
    "lastName": "User",
    "email": "newuser@email.com"
}
```

### 2. Get All Users (Admin Only)
```
GET http://localhost:8080/users
Authorization: Basic admin:admin123
```

### 3. Test Account Creation (Admin/Manager Only)
```
POST http://localhost:8080/accounts
Authorization: Basic manager:manager123
Content-Type: application/json

{
    "accountType": "SAVINGS",
    "balance": 1000.00,
    "ifscCode": "SBI0001001",
    "customer": {"custId": 1},
    "bank": {"bankId": 101}
}
```

### 4. Test Deposit (All Users)
```
POST http://localhost:8080/accounts/deposit/accounts/id/1
Authorization: Basic user:user123
Content-Type: application/json

{
    "depositAmount": 500.00
}
```

## Troubleshooting

### Common Issues:
1. **401 Unauthorized**: Check username/password
2. **403 Forbidden**: User doesn't have required role
3. **500 Internal Server Error**: Check if USERS table exists and sequence issues are resolved

### Security Verification Steps:
1. Try accessing admin endpoints with user credentials (should fail)
2. Try accessing without authentication (should fail)  
3. Try with wrong credentials (should fail)
4. Try valid transactions with each role (should work)
