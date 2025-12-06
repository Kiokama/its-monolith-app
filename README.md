# ITS - Intelligent Testing System (Monolith App)

Một hệ thống quản lý bài kiểm tra trực tuyến được xây dựng bằng Spring Boot, PostgreSQL, và Vanilla JavaScript.

## Tính Năng

- **Quản lý Assessment**: Tạo, sửa, xóa các bài kiểm tra
- **Quản lý Question**: Hỗ trợ câu hỏi MCQ (Multiple Choice) và Essay
- **Làm Bài Test**: Học sinh có thể làm bài test và nhận điểm tự động cho MCQ
- **Quản lý Submission**: Lưu lịch sử nộp bài của học sinh
- **Auto Grading**: Tự động chấm điểm cho câu hỏi MCQ
- **RESTful API**: API đầy đủ cho các tính năng backend
- **Responsive UI**: Giao diện thân thiện với Tailwind CSS

## Yêu Cầu Hệ Thống

- **Java**: 17 trở lên
- **Maven**: 3.6+
- **PostgreSQL**: 12 trở lên
- **Git** (tùy chọn)

## Cài Đặt

### 1. Clone hoặc tải source code

```bash
git clone <repository-url>
cd its-monolith-app
```

### 2. Cài đặt PostgreSQL và tạo Database

**Windows/Mac/Linux:**
```bash
# Đăng nhập vào PostgreSQL
psql -U postgres

# Tạo database
CREATE DATABASE ITS;

# Xác nhận
\l
```

**Hoặc dùng pgAdmin:**
- Mở pgAdmin 4
- Đăng nhập (mặc định: postgres/password)
- Chuột phải → Databases → Create → Database...
- Nhập tên: `ITS` → Create

### 3. Chạy SQL script để tạo tables và insert dữ liệu mẫu

```bash
# Từ pgAdmin Query Tool hoặc dòng lệnh
psql -h localhost -U postgres -d ITS -f sql/test_setup.sql
```

**Hoặc từ pgAdmin:**
- Chọn database `ITS`
- Tools → Query Tool
- Paste nội dung file `sql/test_setup.sql`
- Execute (F5)

### 4. Cập nhật cấu hình database (nếu cần)

Mở file `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ITS
    username: postgres           # Thay bằng username của bạn
    password:                    # Thay bằng password của bạn
    driver-class-name: org.postgresql.Driver
  # ...existing config...
```

### 5. Build và chạy ứng dụng

```bash
# Build
mvn clean install

# Chạy
mvn spring-boot:run
```

**Hoặc từ IDE:**
- Mở project trong IntelliJ IDEA / Eclipse / VS Code
- Chạy `ItsApplication.java` với **Run** (Ctrl+F5 hoặc Shift+F10)

### 6. Truy cập ứng dụng

Mở trình duyệt và vào:
```
http://localhost:8080/
```

Sẽ thấy trang Assessment Manager với 3 nút chính:
- **Refresh**: Tải lại danh sách assessment
- **New Assessment**: Tạo assessment mới
- **Create Question**: Tạo câu hỏi
- **Take Test**: Làm bài test

---

## Hướng Dẫn Sử Dụng

### 1. Tạo Assessment (Bài Kiểm Tra)

1. Trên trang chủ, click nút **"New Assessment"**
2. Nhập:
   - **Title**: Tên bài kiểm tra (e.g., "Java Quiz")
   - **Description**: Mô tả (tuỳ chọn)
   - **Total Points**: Tổng điểm (e.g., 30)
3. Click **"Save"**
4. Assessment sẽ xuất hiện trong bảng danh sách

### 2. Tạo Câu Hỏi (Question)

1. Click nút **"Create Question"** ở trang chủ
2. Chọn **Assessment** từ dropdown
3. Chọn **Question Type**:
   - **MCQ**: Câu hỏi trắc nghiệm (tự động chấm)
   - **ESSAY**: Câu hỏi tự luận (cần chấm thủ công)

#### Tạo MCQ:
- **Content**: Nội dung câu hỏi
- **Points**: Điểm (e.g., 5)
- **Options**: Nhập các lựa chọn cách nhau bằng dấu phẩy
  ```
  Ví dụ: class, struct, object, def
  ```
- **Correct Answer**: Đáp án đúng (e.g., "class")
- Click **"Save Question"**

#### Tạo Essay:
- **Content**: Nội dung câu hỏi
- **Points**: Điểm (e.g., 10)
- **Rubric**: Tiêu chí chấm điểm (tuỳ chọn)
- Click **"Save Question"**

**Ghi chú:** Các câu hỏi sẽ hiện ở cột "Questions Preview" bên phải, có thể xóa từ đó.

### 3. Làm Bài Test

1. Click nút **"Take Test"** ở trang chủ
2. Nhập **Student ID** (e.g., "student01")
3. Chọn **Assessment** từ dropdown
4. Bài test sẽ hiển thị tất cả câu hỏi:
   - **MCQ**: Chọn một đáp án bằng radio button
   - **ESSAY**: Nhập câu trả lời vào text area
5. Click **"Submit Test"**
6. Sẽ thấy kết quả: **"Your Score: X points"**

**Lưu ý:**
- Điểm MCQ được tính tự động (so sánh với Correct Answer)
- Điểm Essay mặc định là 0 (cần cập nhật thủ công ở database)

### 4. Quản Lý Assessment

**Xem chi tiết:** Click nút **"View"** → Sẽ hiển thị JSON của assessment ở cột phải

**Sửa:** Click nút **"Edit"** → Form sẽ điền dữ liệu cũ → Cập nhật → Click **"Save"**

**Xóa:** Click nút **"Delete"** → Xác nhận → Assessment và tất cả câu hỏi/submissions sẽ bị xóa

---

## API Endpoints

### Assessment API

| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| GET | `/api/assessments` | Lấy danh sách tất cả assessment |
| GET | `/api/assessments/{id}` | Lấy chi tiết một assessment |
| POST | `/api/assessments` | Tạo assessment mới |
| PUT | `/api/assessments/{id}` | Cập nhật assessment |
| DELETE | `/api/assessments/{id}` | Xóa assessment |
| POST | `/api/assessments/{id}/submissions` | Nộp bài test |

**Ví dụ POST `/api/assessments`:**
```json
{
  "title": "Java Quiz",
  "description": "Introduction to Java",
  "totalPoints": 30
}
```

### Question API

| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| GET | `/api/questions/assessment/{assessmentId}` | Lấy câu hỏi của assessment |
| GET | `/api/questions/{id}` | Lấy chi tiết một câu hỏi |
| POST | `/api/questions` | Tạo câu hỏi mới |
| PUT | `/api/questions/{id}` | Cập nhật câu hỏi |
| DELETE | `/api/questions/{id}` | Xóa câu hỏi |

**Ví dụ POST `/api/questions` (MCQ):**
```json
{
  "content": "What is the keyword to define a class in Java?",
  "points": 5,
  "assessmentId": 1,
  "questionType": "MCQ",
  "options": ["class", "struct", "object", "def"],
  "correctAnswer": "class"
}
```

**Ví dụ POST `/api/questions` (Essay):**
```json
{
  "content": "Explain the difference between == and equals() in Java.",
  "points": 10,
  "assessmentId": 1,
  "questionType": "ESSAY",
  "rubric": "Check for understanding of reference vs value comparison"
}
```

### Submission API

| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| POST | `/api/assessments/{id}/submissions` | Nộp bài test |

**Ví dụ POST `/api/assessments/1/submissions`:**
```json
{
  "studentId": "student01",
  "assessmentId": 1,
  "answers": [
    {
      "questionId": 1,
      "answer": "class"
    },
    {
      "questionId": 2,
      "answer": "String"
    }
  ]
}
```

**Response:**
```json
{
  "id": 1,
  "studentId": "student01",
  "assessmentId": 1,
  "score": 10,
  "submittedAt": "2025-12-06T22:30:00Z",
  "answers": [...]
}
```

---

## Cấu Trúc Project

```
its-monolith-app/
├── src/
│   ├── main/
│   │   ├── java/com/hcmut/its/
│   │   │   ├── controller/       # REST Controllers
│   │   │   ├── service/          # Business logic
│   │   │   ├── mapper/           # DTO converters
│   │   │   ├── model/            # JPA entities
│   │   │   ├── repository/       # Data access
│   │   │   ├── dto/              # Data transfer objects
│   │   │   ├── exception/        # Custom exceptions
│   │   │   ├── config/           # Spring configuration
│   │   │   ├── handler/          # Exception handlers
│   │   │   └── ItsApplication.java
│   │   ├── resources/
│   │   │   ├── static/           # Frontend (HTML, JS, CSS)
│   │   │   ├── application.yml   # Spring config
│   │   │   └── .env              # Environment variables
│   │   └── sql/
│   │       └── test_setup.sql    # Database setup script
│   └── test/                     # Unit tests
├── pom.xml                       # Maven dependencies
├── .gitignore                    # Git ignore rules
└── README.md                     # This file
```

---

## Dữ Liệu Mẫu

Script `sql/test_setup.sql` chứa:
- **6 assessments** (Java, Algorithms, Database, Web Dev, Data Structures, Software Engineering)
- **27 questions** (MCQ + Essay)
- **15 submissions** từ 8 students
- **Sample answers** để test auto-grading

**Truy cập:**
- pgAdmin: http://localhost:5050/
- Database: `ITS` (user: `postgres`)

---

## Troubleshooting

### Lỗi: "Connection refused" hoặc "Database not found"

**Nguyên nhân:** PostgreSQL chưa chạy hoặc database chưa tạo

**Giải pháp:**
```bash
# Kiểm tra PostgreSQL đang chạy
sudo systemctl status postgresql

# Khởi động (nếu chưa)
sudo systemctl start postgresql

# Xác nhận database
psql -U postgres -l | grep ITS
```

### Lỗi: "Cannot create table" hoặc "Table already exists"

**Giải pháp:** Script `test_setup.sql` bắt đầu bằng `DROP TABLE IF EXISTS`, nên cứ chạy lại file.

### Frontend không load

**Nguyên nhân:** Backend chưa chạy

**Giải pháp:**
```bash
mvn spring-boot:run
# Hoặc khởi động từ IDE
# Mở http://localhost:8080/ (không phải file:// hoặc port khác)
```

### API trả 404 hoặc CORS error

**Giải pháp:** CORS đã được cấu hình ở `CorsConfig.java`. Nếu vẫn lỗi, kiểm tra:
1. Backend đang chạy trên `http://localhost:8080`
2. Frontend truy cập từ cùng domain (hoặc dùng relative path `/api/...`)

---

## Phát Triển Thêm

### Thêm loại question mới

1. Tạo class mới extends `Question`:
   ```java
   @Entity
   @DiscriminatorValue("MATCHING")
   public class MatchingQuestion extends Question {
       // fields, getters, setters
   }
   ```

2. Thêm DTO + Mapper
3. Cập nhật `QuestionMapper.toEntity()` để xử lý loại mới

### Thêm chấm điểm tự động cho Essay

1. Cập nhật `AutoGrader.grade()`:
   ```java
   if (question instanceof EssayQuestion) {
       // Implement keyword matching hoặc AI scoring
   }
   ```

### Thêm login / authentication

1. Thêm `spring-security`
2. Tạo `User` entity và repository
3. Thêm JWT token support

---

## Các Công Nghệ Sử Dụng

- **Backend**: Spring Boot 3.2, Spring Data JPA, Hibernate
- **Database**: PostgreSQL 12+
- **Frontend**: Vanilla JavaScript, Tailwind CSS, HTML5
- **Build**: Maven 3.6+
- **Java**: 17+

---

## Liên Hệ & Hỗ Trợ

Nếu có vấn đề, kiểm tra logs từ:
```bash
# Spring Boot logs (server console)
mvn spring-boot:run

# PostgreSQL logs
sudo tail -f /var/log/postgresql/postgresql.log  # Linux
```

Hoặc tham khảo file `application.yml` để enable debug logging:
```yaml
logging:
  level:
    org.springframework.web: DEBUG
    org.hibernate: DEBUG
```

---

**Happy Testing! 🚀**
