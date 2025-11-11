# 📖 API Documentation cho Frontend Team

## 🔧 Thông tin cơ bản

**Base URL:** `http://localhost:8081`

**Response format:** Tất cả API trả về theo format:
```json
{
  "statusCode": 200,
  "message": "Success message",
  "data": { ... }
}
```

**Headers cần thiết:**
```
Content-Type: application/json
Authorization: Bearer {JWT_TOKEN}  // Chỉ cho API cần xác thực
```

---

## 🔑 Phân quyền

| API | Method | Xác thực | Quyền |
|-----|--------|----------|-------|
| GET /sessions | ✅ Không | Ai cũng xem được |
| POST /sessions | ⚠️ Có | ROLE_TUTOR |
| PUT /sessions/{id} | ⚠️ Có | ROLE_TUTOR (chủ session) |
| DELETE /sessions/{id} | ⚠️ Có | ROLE_TUTOR (chủ session) |
| GET /tutors | ✅ Không | Ai cũng xem được |
| POST /tutors | ✅ Không | Ai cũng tạo được |
| PUT /tutors/{id} | ⚠️ Có | ROLE_TUTOR (chủ profile) |
| DELETE /tutors/{id} | ⚠️ Có | ROLE_TUTOR (chủ profile) |

---

## 1️⃣ SESSIONS API

### 1.1 GET - Lấy danh sách sessions

**Endpoint:** `GET /sessions`

**Xác thực:** ❌ Không cần

**Query params:**
- `page` (optional, default=0): Số trang
- `size` (optional, default=10): Số items/trang

**Request:**
```http
GET http://localhost:8081/sessions?page=0&size=10
```

**Response 200 OK:**
```json
{
  "statusCode": 200,
  "message": "Sessions retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "tutorName": "Nguyễn Văn A",
        "studentName": "Trần Thị B",
        "subjectName": "Toán Cao Cấp",
        "startTime": "2024-11-15T10:00:00Z",
        "endTime": "2024-11-15T12:00:00Z",
        "format": "online",
        "location": "Zoom Meeting",
        "status": "scheduled",
        "createdDate": "2024-11-12T08:00:00Z",
        "updatedDate": null
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true,
    "empty": false
  }
}
```

---

### 1.2 POST - Tạo session mới

**Endpoint:** `POST /sessions`

**Xác thực:** ✅ Cần JWT token + ROLE_TUTOR

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {JWT_TOKEN}
```

**Request body:**
```json
{
  "tutorId": 1,
  "studentId": 2,
  "subjectId": 3,
  "startTime": "2024-11-15T10:00:00Z",
  "endTime": "2024-11-15T12:00:00Z",
  "format": "online",
  "location": "Zoom: https://zoom.us/j/123456789",
  "status": "scheduled"
}
```

**Response 200 OK:**
```json
{
  "statusCode": 200,
  "message": "Session created successfully",
  "data": {
    "id": 1,
    "tutorName": "Nguyễn Văn A",
    "studentName": "Trần Thị B",
    "subjectName": "Toán Cao Cấp",
    "startTime": "2024-11-15T10:00:00Z",
    "endTime": "2024-11-15T12:00:00Z",
    "format": "online",
    "location": "Zoom: https://zoom.us/j/123456789",
    "status": "scheduled",
    "createdDate": "2024-11-12T08:00:00Z",
    "updatedDate": null
  }
}
```

**Response 403 Forbidden (không phải TUTOR):**
```json
{
  "statusCode": 403,
  "message": "Access Denied",
  "data": null
}
```

---

### 1.3 PUT - Cập nhật session

**Endpoint:** `PUT /sessions/{id}`

**Xác thực:** ✅ Cần JWT token + ROLE_TUTOR + là chủ session

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {JWT_TOKEN}
```

**Request body:**
```json
{
  "tutorId": 1,
  "studentId": 2,
  "subjectId": 3,
  "startTime": "2024-11-15T14:00:00Z",
  "endTime": "2024-11-15T16:00:00Z",
  "format": "offline",
  "location": "H1-101",
  "status": "completed"
}
```

**Response 200 OK:**
```json
{
  "statusCode": 200,
  "message": "Session updated successfully",
  "data": {
    "id": 1,
    "tutorName": "Nguyễn Văn A",
    "studentName": "Trần Thị B",
    "subjectName": "Toán Cao Cấp",
    "startTime": "2024-11-15T14:00:00Z",
    "endTime": "2024-11-15T16:00:00Z",
    "format": "offline",
    "location": "H1-101",
    "status": "completed",
    "createdDate": "2024-11-12T08:00:00Z",
    "updatedDate": "2024-11-12T09:00:00Z"
  }
}
```

**Response 403 Forbidden (không phải chủ session):**
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only update your own sessions",
  "data": null
}
```

---

### 1.4 DELETE - Xóa session

**Endpoint:** `DELETE /sessions/{id}`

**Xác thực:** ✅ Cần JWT token + ROLE_TUTOR + là chủ session

**Headers:**
```
Authorization: Bearer {JWT_TOKEN}
```

**Response 200 OK:**
```json
{
  "statusCode": 200,
  "message": "Session deleted successfully",
  "data": null
}
```

**Response 403 Forbidden:**
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only delete your own sessions",
  "data": null
}
```

---

## 2️⃣ TUTORS API

### 2.1 GET - Lấy danh sách tutors

**Endpoint:** `GET /tutors`

**Xác thực:** ❌ Không cần

**Query params:**
- `page` (optional, default=0)
- `size` (optional, default=10)

**Request:**
```http
GET http://localhost:8081/tutors?page=0&size=10
```

**Response 200 OK:**
```json
{
  "statusCode": 200,
  "message": "Tutors retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Nguyễn Văn A",
        "title": "Thạc sĩ",
        "majorId": 5,
        "majorName": "Khoa học Máy tính",
        "department": "Khoa Khoa học & Kỹ thuật Máy tính",
        "description": "Giảng viên 5 năm kinh nghiệm",
        "specializations": ["Toán Cao Cấp", "Giải Tích", "Đại Số"],
        "rating": 4.5,
        "reviewCount": 0,
        "studentCount": 12,
        "experienceYears": 5,
        "isAvailable": true
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true,
    "empty": false
  }
}
```

---

### 2.2 POST - Đăng ký tutor

**Endpoint:** `POST /tutors`

**Xác thực:** ❌ Không cần (ai cũng có thể đăng ký)

**Headers:**
```
Content-Type: application/json
```

**Request body:**
```json
{
  "name": "Nguyễn Văn A",
  "title": "Thạc sĩ",
  "majorId": 5,
  "description": "Giảng viên với 5 năm kinh nghiệm",
  "subjects": [1, 3, 7],
  "rating": 4.5,
  "experienceYears": 5,
  "isAvailable": true
}
```

**Field descriptions:**
- `name`: Họ tên đầy đủ
- `title`: Học vị (Thạc sĩ, Tiến sĩ, etc.)
- `majorId`: ID của major (xác định khoa/department)
- `description`: Mô tả về tutor
- `subjects`: **Array các ID của subjects** (không phải tên)
- `rating`: Đánh giá ban đầu
- `experienceYears`: Số năm kinh nghiệm
- `isAvailable`: Có sẵn sàng nhận học sinh không

**Response 200 OK:**
```json
{
  "statusCode": 200,
  "message": "Tutor created successfully",
  "data": {
    "id": 1,
    "name": "Nguyễn Văn A",
    "title": "Thạc sĩ",
    "majorId": 5,
    "majorName": "Khoa học Máy tính",
    "department": "Khoa Khoa học & Kỹ thuật Máy tính",
    "description": "Giảng viên với 5 năm kinh nghiệm",
    "specializations": ["Toán Cao Cấp", "Giải Tích", "Đại Số"],
    "rating": 4.5,
    "reviewCount": 0,
    "studentCount": 0,
    "experienceYears": 5,
    "isAvailable": true
  }
}
```

---

### 2.3 PUT - Cập nhật tutor profile

**Endpoint:** `PUT /tutors/{id}`

**Xác thực:** ✅ Cần JWT token + ROLE_TUTOR + là chủ profile

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {JWT_TOKEN}
```

**Request body:**
```json
{
  "name": "Nguyễn Văn A",
  "title": "Tiến sĩ",
  "majorId": 5,
  "description": "Giảng viên với 7 năm kinh nghiệm",
  "subjects": [1, 3, 7, 9],
  "rating": 4.8,
  "experienceYears": 7,
  "isAvailable": true
}
```

**Response 200 OK:**
```json
{
  "statusCode": 200,
  "message": "Tutor updated successfully",
  "data": {
    "id": 1,
    "name": "Nguyễn Văn A",
    "title": "Tiến sĩ",
    "majorId": 5,
    "majorName": "Khoa học Máy tính",
    "department": "Khoa Khoa học & Kỹ thuật Máy tính",
    "description": "Giảng viên với 7 năm kinh nghiệm",
    "specializations": ["Toán Cao Cấp", "Giải Tích", "Đại Số", "Hình Học"],
    "rating": 4.8,
    "reviewCount": 0,
    "studentCount": 0,
    "experienceYears": 7,
    "isAvailable": true
  }
}
```

**Response 403 Forbidden:**
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only update your own tutor profile",
  "data": null
}
```

---

### 2.4 DELETE - Xóa tutor profile

**Endpoint:** `DELETE /tutors/{id}`

**Xác thực:** ✅ Cần JWT token + ROLE_TUTOR + là chủ profile

**Headers:**
```
Authorization: Bearer {JWT_TOKEN}
```

**Response 200 OK:**
```json
{
  "statusCode": 200,
  "message": "Tutor deleted successfully",
  "data": null
}
```

**Response 403 Forbidden:**
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only delete your own tutor profile",
  "data": null
}
```

---

## 📝 Lưu ý quan trọng

### 1. Date/Time Format
**Tất cả dates dùng ISO 8601 format (UTC):**
```
2024-11-15T10:00:00Z
```

### 2. Pagination
Tất cả list API đều có pagination:
- `page`: Số trang (bắt đầu từ 0)
- `size`: Số items mỗi trang (default 10)

Response pagination fields:
- `content`: Array data
- `pageNumber`: Trang hiện tại
- `pageSize`: Số items/trang
- `totalElements`: Tổng số items
- `totalPages`: Tổng số trang
- `first`: Có phải trang đầu?
- `last`: Có phải trang cuối?
- `empty`: Có rỗng không?

### 3. Subjects Field
**⚠️ QUAN TRỌNG:**
- **Request:** `subjects` là **Array of IDs** (e.g., `[1, 3, 7]`)
- **Response:** `specializations` là **Array of Names** (e.g., `["Toán", "Lý"]`)

### 4. JWT Token
**Lưu token sau khi login:**
```javascript
localStorage.setItem('authToken', token);
```

**Gửi token trong header:**
```javascript
headers: {
  'Authorization': `Bearer ${localStorage.getItem('authToken')}`
}
```

### 5. Error Handling
**Status codes:**
- `200`: Success
- `400`: Bad request (dữ liệu không hợp lệ)
- `401`: Unauthorized (không có token hoặc token hết hạn)
- `403`: Forbidden (không có quyền)
- `404`: Not found
- `500`: Server error

---

**Last Updated:** November 12, 2024
**API Version:** 1.0.0
**Port:** 8081

