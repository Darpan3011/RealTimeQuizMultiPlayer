# Real-Time Multiplayer Quiz

This project is a **real-time quiz management system** where users can:
- **Create an account**
- **Create a quiz with a unique quiz code**
- **Join a quiz using the quiz code**
- **Attempt the quiz in real-time using WebSockets**

## 🚀 Features
- **User Authentication** (Not implemented yet)
- **Quiz Creation & Management**
- **WebSocket-based real-time quiz participation**
- **STOMP WebSockets for live communication**

---

## 📌 **Tech Stack**
- **Backend:** Java, Spring Boot
- **WebSockets:** STOMP with SockJS
- **Database:** MySQL / PostgreSQL (change in `application.properties`)
- **Frontend:** Any WebSocket-supported client (Postman, WebSocket King)

---

## 🔧 **Setup Instructions**
### 1️⃣ Clone the Repo  
```sh
git clone https://github.com/your-repo/realtimemultiplayerquiz.git
cd realtimemultiplayerquiz
```

### 2️⃣ Configure Database  
Update **`src/main/resources/application.properties`** with your database settings:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quiz_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

### 3️⃣ Run the Application  
```sh
mvn spring-boot:run
```
The server will start at **http://localhost:8081**.

---

## 🌐 **WebSocket API Endpoints**
| Action          | Destination        | Payload Example | Response |
|----------------|------------------|----------------|-----------|
| **Connect**    | `ws://localhost:8081/ws` | _N/A_ | WebSocket connection established |
| **Subscribe**  | `/topic/quiz` | _N/A_ | Receives quiz updates |
| **Fetch Quiz** | `/rtquiz/fetch` | `{}` | `Quiz questions will be sent!` |
| **Submit Answers** | `/rtquiz/submit` | `{"quizCode": 1234, "playerName": "John", "answers": ["A", "B"]}` | `Answers submitted!` |

---

## 🛠 **Testing WebSockets with Postman**
1. Open **Postman** → Go to **WebSocket Request**
2. Connect to **`ws://localhost:8081/ws`**
3. **Subscribe** to `/topic/quiz`:
   ```
   SUBSCRIBE
   id:sub-1
   destination:/topic/quiz

   
