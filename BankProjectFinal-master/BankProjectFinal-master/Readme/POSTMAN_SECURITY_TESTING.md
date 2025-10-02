# Testing Bank Project Security Using Postman

## Prerequisites
1. Start your Spring Boot application: `mvn spring-boot:run`
2. Open Postman application
3. Ensure your app is running on `http://localhost:8080`

---

## Part 1: Setting Up Postman for Security Testing

### Step 1: Create a New Collection
1. Open Postman
2. Click "Collections" in the left sidebar
3. Click "Create Collection"
4. Name it "Bank Security Tests"
5. Save the collection

### Step 2: Set Up Environment Variables (Optional but Recommended)
1. Click the gear icon (⚙️) in top right
2. Select "Manage Environments"
3. Click "Add"
4. Environment name: "Bank Local"
5. Add variables:
   - `base_url` = `http://localhost:8080`
   - `admin_user` = `admin`
   - `admin_pass` = `admin123`
   - `manager_user` = `manager`
   - `manager_pass` = `manager123`
   - `user_user` = `user`
   - `user_pass` = `user123`
6. Save and select this environment

---

## Part 2: Security Test Cases

### Test Case 1: No Authentication (Should Fail - 401)

**Request Setup:**
- Method: `GET`
- URL: `{{base_url}}/banks` or `http://localhost:8080/banks`
- Authorization: None (No Auth)

**Steps:**
1. Create new request
2. Set method to GET
3. Enter URL: `http://localhost:8080/banks`
4. Authorization tab → Select "No Auth"
5. Click "Send"

**Expected Result:** 
- Status: `401 Unauthorized`
- Response body might contain authentication error

---

### Test Case 2: Admin Access (Should Work - 200)

**Request Setup:**
- Method: `GET`
- URL: `{{base_url}}/banks`
- Authorization: Basic Auth
- Username: `admin`
- Password: `admin123`

**Steps:**
1. Create new request
2. Set method to GET
3. Enter URL: `http://localhost:8080/banks`
4. Authorization tab → Select "Basic Auth"
5. Username: `admin`
6. Password: `admin123`
7. Click "Send"

**Expected Result:**
- Status: `200 OK`
- Response body: JSON array with bank data

---

### Test Case 3: Admin Creating Bank (Should Work - 200/201)

**Request Setup:**
- Method: `POST`
- URL: `{{base_url}}/banks`
- Authorization: Basic Auth (admin/admin123)
- Headers: `Content-Type: application/json`
- Body: JSON

**Steps:**
1. Create new request
2. Set method to POST
3. URL: `http://localhost:8080/banks`
4. Authorization → Basic Auth → admin/admin123
5. Headers tab → Add `Content-Type: application/json`
6. Body tab → Select "raw" → Choose "JSON"
7. Add JSON body:
```json
{
    "bankId": 999,
    "bankName": "Postman Test Bank",
    "branchName": "Test Branch",
    "ifscCode": "POST999"
}
```
8. Click "Send"

**Expected Result:**
- Status: `200 OK` or `201 Created`
- Response: Created bank object

---

### Test Case 4: Manager Trying to Create Bank (Should Fail - 403)

**Request Setup:**
- Method: `POST`
- URL: `{{base_url}}/banks`
- Authorization: Basic Auth (manager/manager123)
- Headers: `Content-Type: application/json`
- Body: Same JSON as above

**Steps:**
1. Duplicate the previous request
2. Change Authorization to manager/manager123
3. Click "Send"

**Expected Result:**
- Status: `403 Forbidden`
- Manager cannot create banks (admin-only operation)

---

### Test Case 5: Manager Managing Customers (Should Work - 200)

**Request Setup:**
- Method: `GET`
- URL: `{{base_url}}/customers`
- Authorization: Basic Auth (manager/manager123)

**Steps:**
1. Create new GET request
2. URL: `http://localhost:8080/customers`
3. Authorization → manager/manager123
4. Click "Send"

**Expected Result:**
- Status: `200 OK`
- Response: JSON array with customer data

---

### Test Case 6: User Viewing All Customers (Should Fail - 403)

**Request Setup:**
- Method: `GET`
- URL: `{{base_url}}/customers`
- Authorization: Basic Auth (user/user123)

**Steps:**
1. Create new GET request
2. URL: `http://localhost:8080/customers`
3. Authorization → user/user123
4. Click "Send"

**Expected Result:**
- Status: `403 Forbidden`
- Users cannot view all customers

---

### Test Case 7: User Performing Deposit (Should Work - 200)

**Request Setup:**
- Method: `POST`
- URL: `{{base_url}}/accounts/deposit/accounts/id/1`
- Authorization: Basic Auth (user/user123)
- Headers: `Content-Type: application/json`
- Body: Deposit request

**Steps:**
1. Create new POST request
2. URL: `http://localhost:8080/accounts/deposit/accounts/id/1`
3. Authorization → user/user123
4. Headers → `Content-Type: application/json`
5. Body → raw → JSON:
```json
{
    "depositAmount": 500.00
}
```
6. Click "Send"

**Expected Result:**
- Status: `200 OK`
- Users can perform transactions

---

### Test Case 8: Wrong Credentials (Should Fail - 401)

**Request Setup:**
- Method: `GET`
- URL: `{{base_url}}/banks`
- Authorization: Basic Auth
- Username: `admin`
- Password: `wrongpassword`

**Steps:**
1. Create new GET request
2. URL: `http://localhost:8080/banks`
3. Authorization → Basic Auth
4. Username: `admin`
5. Password: `wrongpassword`
6. Click "Send"

**Expected Result:**
- Status: `401 Unauthorized`
- Authentication failed

---

## Part 3: Complete Test Suite

Create these requests in your Postman collection:

### Admin Tests (Should all work)
1. `GET /banks` - View banks
2. `POST /banks` - Create bank
3. `GET /customers` - View customers
4. `POST /customers` - Create customer
5. `GET /accounts` - View accounts
6. `POST /accounts` - Create account

### Manager Tests
1. `GET /banks` - ✅ Should work (read-only)
2. `POST /banks` - ❌ Should fail (403 Forbidden)
3. `GET /customers` - ✅ Should work
4. `POST /customers` - ✅ Should work
5. `GET /accounts` - ✅ Should work
6. `POST /accounts` - ✅ Should work

### User Tests
1. `GET /banks` - ✅ Should work (read-only)
2. `GET /customers` - ❌ Should fail (403 Forbidden)
3. `GET /accounts` - ❌ Should fail (403 Forbidden)
4. `POST /accounts/deposit/accounts/id/1` - ✅ Should work
5. `POST /accounts/withdraw/accounts/id/1` - ✅ Should work
6. `POST /accounts/transfer/1/2` - ✅ Should work

---

## Part 4: Quick Security Verification Checklist

Run these 5 tests in order:

### ✅ Test 1: No Auth Test
- Request: `GET /banks` (No authentication)
- Expected: `401 Unauthorized`

### ✅ Test 2: Admin Power Test
- Request: `POST /banks` (admin credentials)
- Expected: `200/201 Created`

### ✅ Test 3: Manager Limitation Test
- Request: `POST /banks` (manager credentials)  
- Expected: `403 Forbidden`

### ✅ Test 4: User Limitation Test
- Request: `GET /customers` (user credentials)
- Expected: `403 Forbidden`

### ✅ Test 5: User Transaction Test
- Request: `POST /accounts/deposit/accounts/id/1` (user credentials)
- Expected: `200 OK`

If all 5 tests give expected results, your security is working perfectly!

---

## Part 5: Postman Collection Export

After creating all tests, export your collection:
1. Right-click on "Bank Security Tests" collection
2. Select "Export"
3. Choose "Collection v2.1"
4. Save as `Bank_Security_Tests.postman_collection.json`
5. Share with team members for consistent testing

---

## Troubleshooting Common Issues

### Issue 1: All requests return 401
**Solution:** Check if you're using the correct credentials and Basic Auth is selected

### Issue 2: User can access admin endpoints
**Solution:** Verify @PreAuthorize annotations are present on controller methods

### Issue 3: Application not responding
**Solution:** Ensure Spring Boot app is running on localhost:8080

### Issue 4: Getting 404 instead of 401/403
**Solution:** Check URL paths and ensure endpoints exist

---

## Summary

Your security is working correctly if you see:
- **401** for unauthenticated requests
- **403** for insufficient permissions  
- **200/201** for authorized requests
- **Different behavior** for different user roles

The key is that **different users should get different results** for the same requests!
