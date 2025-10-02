# Bank Accounts Management System

This project is a **Spring Boot web application** for managing bank accounts with a **dynamic HTML (Thymeleaf) UI** and **RESTful APIs**.

---

## Features
- Responsive, modern account listing UI  
- View accounts with customer and bank details  
- Add new accounts (via API or UI form)  
- REST API for viewing and adding accounts  
- **Thymeleaf-based HTML rendering** for dynamic server-side UI  
- Oracle database (JPA/Hibernate)  
- Role-based security (`ADMIN`, `MANAGER`, `USER`)  

---

## Tech Stack
- **Backend:** Spring Boot, Spring Data JPA, Spring Security  
- **UI:** Thymeleaf, HTML, CSS (`/static/css/style.css`)  
- **Database:** Oracle (configurable)  
- **Build Tool:** Maven/Gradle  

---

## About Thymeleaf
**Thymeleaf** is a modern server-side Java template engine used to generate dynamic web pages in Spring Boot applications.  
It allows you to bind data from your backend directly to your HTML templates, enabling you to display database content (like customer/account details) in an interactive, secure, and maintainable way.

---

## UI Preview
> **Sample of the Accounts Listing Page:**  
<p align="center">
  <img src="Screenshot 2025-10-01 203944.png" alt="Accounts Listing Page Screenshot" style="border-radius: 10px; box-shadow: 0 2px 8px #e0e0e0;">
</p>

*  

---

## Project Structure
```bash
src/
 └─ main/
     ├─ java/
     │   └─ com.ofss.controller/
     │   └─ com.ofss.model/
     │   └─ com.ofss.services/
     ├─ resources/
     │   ├─ templates/          # Thymeleaf HTML templates
     │   │    └─ accounts.html
     │   ├─ static/css/         # CSS files
     │   │    └─ style.css
     │   └─ application.properties
```

---

## Setup & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/bank-accounts-management.git
   cd bank-accounts-management
   ```

2. Configure your database in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Build & run the project:
   ```bash
   mvn spring-boot:run
   ```

4. Open the app in your browser:
   ```
   http://localhost:8080/accounts
   ```

---

## Roles & Permissions
- **ADMIN** → Full access (manage users, accounts)  
- **MANAGER** → Manage accounts only  
- **USER** → View accounts only  

---

## Contribution
Feel free to fork the repo, create issues, or submit pull requests to improve this project.  

---

## License
This project is licensed under the MIT License.  
