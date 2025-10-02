# Manual Security Testing Guide

## Quick Security Test Steps

### Step 1: Start Your Application
```cmd
cd C:\Batch32\BankProjectFinal
mvn spring-boot:run
```
Wait for the application to start (look for "Started BankProjectFinalApplication" in the logs).

### Step 2: Test Without Authentication (Should Fail)
Open a new command prompt and try:
```cmd
curl http://localhost:8080/banks
```
**Expected Result:** Should return 401 Unauthorized or prompt for authentication.

### Step 3: Test Admin Access (Should Work)
```cmd
curl -u admin:admin123 http://localhost:8080/banks
```
**Expected Result:** Should return JSON data with banks (200 OK status).

### Step 4: Test User Trying Admin Operations (Should Fail)
```cmd
curl -u user:user123 -X POST http://localhost:8080/banks -H "Content-Type: application/json" -d "{\"bankName\":\"Test\"}"
```
**Expected Result:** Should return 403 Forbidden.

### Step 5: Test User Transaction Access (Should Work)
First get an account ID, then:
```cmd
curl -u user:user123 -X POST http://localhost:8080/accounts/deposit/accounts/id/1 -H "Content-Type: application/json" -d "{\"depositAmount\":100.00}"
```
**Expected Result:** Should work (200 OK) - users can perform transactions.

## Using Postman to Test Security

### 1. Set up Authentication in Postman:
- Open Postman
- Create a new request
- Go to Authorization tab
- Select "Basic Auth"
- Enter username and password

### 2. Test Different Scenarios:

#### Admin Tests:
- **URL:** GET http://localhost:8080/banks
- **Auth:** admin / admin123
- **Expected:** 200 OK with data

- **URL:** POST http://localhost:8080/banks
- **Auth:** admin / admin123
- **Body:** {"bankName": "Test Bank", "branchName": "Test Branch", "ifscCode": "TEST123"}
- **Expected:** 201 Created

#### Manager Tests:
- **URL:** GET http://localhost:8080/customers
- **Auth:** manager / manager123
- **Expected:** 200 OK with data

- **URL:** POST http://localhost:8080/banks
- **Auth:** manager / manager123
- **Expected:** 403 Forbidden

#### User Tests:
- **URL:** GET http://localhost:8080/customers
- **Auth:** user / user123
- **Expected:** 403 Forbidden

- **URL:** POST http://localhost:8080/accounts/deposit/accounts/id/{accountId}
- **Auth:** user / user123
- **Body:** {"depositAmount": 500.00}
- **Expected:** 200 OK

## Security Test Results Interpretation

### HTTP Status Codes You Should See:
- **200 OK** - Request successful, user has permission
- **201 Created** - Resource created successfully, user has permission
- **401 Unauthorized** - No authentication provided or wrong credentials
- **403 Forbidden** - Valid authentication but insufficient permissions
- **404 Not Found** - Resource doesn't exist (but user is authenticated)

### What Each Status Means for Security:
- **401** = Authentication is working (rejecting unauthenticated requests)
- **403** = Authorization is working (user authenticated but lacks permission)
- **200/201** = User has proper permissions for the operation

## Browser Testing

### 1. Open your browser and go to:
```
http://localhost:8080/banks
```

### 2. You should see a login dialog box
- This means authentication is working!

### 3. Try different credentials:
- **admin/admin123** - should work
- **user/user123** - should work for viewing banks
- **wrong/password** - should be rejected

### 4. After logging in as 'user', try:
```
http://localhost:8080/customers
```
- Should show 403 Forbidden error (users can't view all customers)

## Automated Testing

Run the provided test script:
```cmd
cd C:\Batch32\BankProjectFinal
security_test.bat
```

This will automatically test all security scenarios and show you the results.

## Signs That Security is Working Correctly

✅ **Good Signs:**
- Requests without authentication return 401
- Users get 403 when trying admin operations
- Admins can access everything (200/201 responses)
- Managers can manage customers but not banks
- Users can only do transactions and view individual records

❌ **Bad Signs (Security Issues):**
- Any request works without authentication
- Users can access admin endpoints (should be 403)
- Wrong credentials are accepted
- All users have the same permissions

## Troubleshooting

### If Security Isn't Working:
1. Check if @EnableGlobalMethodSecurity is present in SecurityConfig
2. Verify @PreAuthorize annotations are on controller methods
3. Make sure Spring Security dependency is in pom.xml
4. Restart the application after security changes
5. Check application logs for security-related errors
