# 📖 Tài liệu Test Postman - Tutor System APIs

## 🎯 Base URL
```
http://localhost:8080
```

---

## 🔐 Authorization Matrix

| Endpoint | Method | Permission | Note |
|----------|--------|------------|------|
| `/sessions` | GET | permitAll | Ai cũng xem được |
| `/sessions` | POST | ROLE_TUTOR | Chỉ tutor tạo được |
| `/sessions/{id}` | PUT | ROLE_TUTOR (owner) | Chỉ tutor tạo session mới sửa được |
| `/sessions/{id}` | DELETE | ROLE_TUTOR (owner) | Chỉ tutor tạo session mới xóa được |
| `/tutors` | GET | permitAll | Ai cũng xem được |
| `/tutors` | POST | permitAll | Ai cũng đăng ký tutor được |
| `/tutors/{id}` | PUT | ROLE_TUTOR (owner) | Chỉ tutor chủ profile mới sửa được |
| `/tutors/{id}` | DELETE | ROLE_TUTOR (owner) | Chỉ tutor chủ profile mới xóa được |

---

## 1️⃣ SESSIONS API (`/sessions`)

### 1.1 GET All Sessions
**Endpoint:** `GET /sessions`

**Permission:** ✅ permitAll (không cần authentication)

**Query Params:**
- `page` (optional, default=0): Số trang
- `size` (optional, default=10): Số items mỗi trang

**Request:**
```http
GET http://localhost:8080/sessions?page=0&size=10
```

**Response:** `200 OK`
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

### 1.2 POST Create Session
**Endpoint:** `POST /sessions`

**Permission:** 🔒 ROLE_TUTOR required

**Headers:**
```
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>
```

**Request Body:**
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

**Response:** `200 OK`
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

**Error Response:** `403 Forbidden` (nếu không phải TUTOR)
```json
{
  "statusCode": 403,
  "message": "Access Denied",
  "data": null
}
```

---

### 1.3 PUT Update Session
**Endpoint:** `PUT /sessions/{id}`

**Permission:** 🔒 ROLE_TUTOR (owner only)

**Headers:**
```
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>
```

**Path Params:**
- `id`: Session ID

**Request Body:**
```json
{
  "tutorId": 1,
  "studentId": 2,
  "subjectId": 3,
  "startTime": "2024-11-15T14:00:00Z",
  "endTime": "2024-11-15T16:00:00Z",
  "format": "offline",
  "location": "H1-101",
  "status": "scheduled"
}
```

**Response:** `200 OK`
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
    "status": "scheduled",
    "createdDate": "2024-11-12T08:00:00Z",
    "updatedDate": "2024-11-12T09:00:00Z"
  }
}
```

**Error Response:** `403 Forbidden` (nếu không phải chủ session)
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only update your own sessions",
  "data": null
}
```

---

### 1.4 DELETE Session
**Endpoint:** `DELETE /sessions/{id}`

**Permission:** 🔒 ROLE_TUTOR (owner only)

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Path Params:**
- `id`: Session ID

**Request:**
```http
DELETE http://localhost:8080/sessions/1
```

**Response:** `200 OK`
```json
{
  "statusCode": 200,
  "message": "Session deleted successfully",
  "data": null
}
```

**Error Response:** `403 Forbidden` (nếu không phải chủ session)
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only delete your own sessions",
  "data": null
}
```

---

## 2️⃣ TUTORS API (`/tutors`)

### 2.1 GET All Tutors
**Endpoint:** `GET /tutors`

**Permission:** ✅ permitAll (không cần authentication)

**Query Params:**
- `page` (optional, default=0): Số trang
- `size` (optional, default=10): Số items mỗi trang

**Request:**
```http
GET http://localhost:8080/tutors?page=0&size=10
```

**Response:** `200 OK`
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
        "description": "Giảng viên với 5 năm kinh nghiệm",
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

### 2.2 POST Create Tutor
**Endpoint:** `POST /tutors`

**Permission:** ✅ permitAll (ai cũng có thể đăng ký tutor)

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Nguyễn Văn A",
  "title": "Thạc sĩ",
  "majorId": 5,
  "description": "Giảng viên với 5 năm kinh nghiệm giảng dạy Toán",
  "subjects": [1, 3, 7],
  "rating": 4.5,
  "experienceYears": 5,
  "isAvailable": true
}
```

**Field Descriptions:**
- `name`: Họ tên đầy đủ
- `title`: Học vị (Thạc sĩ, Tiến sĩ, etc.)
- `majorId`: ID của major (xác định department/faculty)
- `description`: Mô tả về tutor
- `subjects`: Array các subject IDs (ManyToMany)
- `rating`: Đánh giá (0.0 - 5.0)
- `experienceYears`: Số năm kinh nghiệm
- `isAvailable`: Có sẵn sàng nhận học sinh không

**Response:** `200 OK`
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
    "description": "Giảng viên với 5 năm kinh nghiệm giảng dạy Toán",
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

### 2.3 PUT Update Tutor
**Endpoint:** `PUT /tutors/{id}`

**Permission:** 🔒 ROLE_TUTOR (owner only)

**Headers:**
```
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>
```

**Path Params:**
- `id`: Tutor Profile ID

**Request Body:**
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

**Response:** `200 OK`
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

**Error Response:** `403 Forbidden` (nếu không phải chủ profile)
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only update your own tutor profile",
  "data": null
}
```

---

### 2.4 DELETE Tutor
**Endpoint:** `DELETE /tutors/{id}`

**Permission:** 🔒 ROLE_TUTOR (owner only)

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Path Params:**
- `id`: Tutor Profile ID

**Request:**
```http
DELETE http://localhost:8080/tutors/1
```

**Response:** `200 OK`
```json
{
  "statusCode": 200,
  "message": "Tutor deleted successfully",
  "data": null
}
```

**Error Response:** `403 Forbidden` (nếu không phải chủ profile)
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only delete your own tutor profile",
  "data": null
}
```

---

## 🔑 Authentication Setup (Postman)

### Cách test với JWT Token:

1. **Lấy JWT Token** (từ login API - chưa implement trong tài liệu này)
2. **Set Authorization Header:**
   - Vào tab "Authorization" trong Postman
   - Type: "Bearer Token"
   - Token: Paste JWT token vào

hoặc

3. **Manual Header:**
   ```
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

### Test scenarios:

#### Scenario 1: Guest User (không đăng nhập)
- ✅ GET /sessions → Success
- ✅ GET /tutors → Success
- ✅ POST /tutors → Success (đăng ký tutor)
- ❌ POST /sessions → 403 Forbidden
- ❌ PUT/DELETE sessions → 403 Forbidden
- ❌ PUT/DELETE tutors → 403 Forbidden

#### Scenario 2: Logged in as TUTOR (role: TUTOR)
- ✅ GET /sessions → Success
- ✅ GET /tutors → Success
- ✅ POST /sessions → Success
- ✅ PUT /sessions/{own_session_id} → Success
- ❌ PUT /sessions/{other_tutor_session_id} → 403 Forbidden
- ✅ DELETE /sessions/{own_session_id} → Success
- ❌ DELETE /sessions/{other_tutor_session_id} → 403 Forbidden
- ✅ PUT /tutors/{own_profile_id} → Success
- ❌ PUT /tutors/{other_tutor_profile_id} → 403 Forbidden

---

## 📝 Postman Collection Structure

```
Tutor System APIs/
├── Sessions/
│   ├── GET All Sessions
│   ├── POST Create Session (TUTOR only)
│   ├── PUT Update Session (Owner only)
│   └── DELETE Session (Owner only)
└── Tutors/
    ├── GET All Tutors
    ├── POST Create Tutor (Public)
    ├── PUT Update Tutor (Owner only)
    └── DELETE Tutor (Owner only)
```

---

## 🧪 Test Cases

### Test Case 1: Public Access
1. GET /sessions → Expect 200 OK
2. GET /tutors → Expect 200 OK
3. POST /tutors với valid data → Expect 200 OK

### Test Case 2: Create Session Without Auth
1. POST /sessions without token → Expect 403 Forbidden

### Test Case 3: Create Session With TUTOR Role
1. Login as TUTOR, get token
2. POST /sessions with token → Expect 200 OK

### Test Case 4: Update Own Session
1. Login as TUTOR A, create session
2. PUT /sessions/{created_session_id} → Expect 200 OK

### Test Case 5: Update Other's Session
1. Login as TUTOR B
2. PUT /sessions/{tutor_A_session_id} → Expect 403 Forbidden

### Test Case 6: Update Own Tutor Profile
1. Login as TUTOR A
2. PUT /tutors/{own_profile_id} → Expect 200 OK

### Test Case 7: Update Other's Tutor Profile
1. Login as TUTOR B
2. PUT /tutors/{tutor_A_profile_id} → Expect 403 Forbidden

---

## ⚠️ Important Notes

1. **getCurrentUserId()** hiện tại return placeholder (1L)
   - Cần implement extraction từ JWT token
   - Phụ thuộc vào AuthenticationPrincipal structure

2. **Role Format:** `ROLE_TUTOR` (Spring Security convention)
   - JWT phải chứa role với prefix "ROLE_"
   - Example: `roles: ["ROLE_TUTOR"]`

3. **Ownership Check:**
   - Session: Check tutorId
   - Tutor: Check userId from tutor profile

4. **Subject IDs:**
   - Subjects phải tồn tại trong DB trước
   - Không tự động tạo subject mới
   - Invalid IDs sẽ bị skip (silent)

---

## 🎯 Quick Test Commands (cURL)

### GET All Sessions:
```bash
curl -X GET http://localhost:8080/sessions?page=0&size=10
```

### POST Create Session (with auth):
```bash
curl -X POST http://localhost:8080/sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "tutorId": 1,
    "studentId": 2,
    "subjectId": 3,
    "startTime": "2024-11-15T10:00:00Z",
    "endTime": "2024-11-15T12:00:00Z",
    "format": "online",
    "location": "Zoom",
    "status": "scheduled"
  }'
```

### POST Create Tutor (no auth needed):
```bash
curl -X POST http://localhost:8080/tutors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyễn Văn A",
    "title": "Thạc sĩ",
    "majorId": 5,
    "description": "Giảng viên kinh nghiệm",
    "subjects": [1, 3, 7],
    "experienceYears": 5,
    "isAvailable": true
  }'
```

---

## 🚀 Ready to Test!

Import vào Postman và bắt đầu test! 🎉

