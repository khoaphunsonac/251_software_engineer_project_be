# 📘 Hướng Dẫn Sử Dụng API - Tutor System

**Dành cho Frontend Developer**  
**Ngày cập nhật**: 12/11/2025  
**Database Version**: V10

---

## 🌐 Thông Tin Chung

### Base URL
```
http://localhost:8081
```

### Format Response Chung

Tất cả API đều trả về theo format sau:

```json
{
  "statusCode": 200,
  "message": "Thông báo thành công hoặc lỗi",
  "data": { /* Dữ liệu hoặc null */ }
}
```

**Giải thích các trường:**
- `statusCode`: Mã trạng thái (200 = thành công, 400 = lỗi request, 403 = không có quyền, 404 = không tìm thấy)
- `message`: Thông báo bằng tiếng Anh mô tả kết quả
- `data`: Chứa dữ liệu trả về, có thể là object, array, hoặc null

### Format Phân Trang

Khi API trả về danh sách có phân trang, `data` sẽ có format:

```json
{
  "content": [ /* Mảng các items */ ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 50,
  "totalPages": 5,
  "first": true,
  "last": false,
  "empty": false
}
```

**Giải thích:**
- `content`: Mảng chứa các item trong trang hiện tại
- `pageNumber`: Số trang hiện tại (bắt đầu từ 0)
- `pageSize`: Số lượng items mỗi trang
- `totalElements`: Tổng số items trong database
- `totalPages`: Tổng số trang
- `first`: `true` nếu đây là trang đầu tiên
- `last`: `true` nếu đây là trang cuối cùng
- `empty`: `true` nếu không có item nào

---

## 🔐 Xác Thực (Authentication)

### Các Endpoint Công Khai (Không cần token)

Những endpoint sau **KHÔNG** cần gửi token:
- `GET /sessions` - Xem danh sách sessions
- `GET /tutors` - Xem danh sách tutors

### Các Endpoint Cần Xác Thực

Những endpoint sau **BẮT BUỘC** phải có token:
- `POST /sessions` - Tạo session mới (chỉ role TUTOR)
- `PUT /sessions/{id}` - Sửa session (chỉ tutor chủ sở hữu)
- `DELETE /sessions/{id}` - Xóa session (chỉ tutor chủ sở hữu)
- `POST /tutors` - Tạo tutor profile (bất kỳ ai đã login)
- `PUT /tutors/{id}` - Sửa tutor profile (chỉ chủ sở hữu)
- `DELETE /tutors/{id}` - Xóa tutor profile (chỉ chủ sở hữu)

### Cách Gửi Token

Thêm vào Header của HTTP request:

```
Authorization: Bearer <your_jwt_token_here>
```

**Ví dụ:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Lỗi Khi Thiếu Token hoặc Token Không Hợp Lệ

Response sẽ có `statusCode: 401`:

```json
{
  "statusCode": 401,
  "message": "Authentication required",
  "data": null
}
```

### Lỗi Khi Không Đủ Quyền

Ví dụ: Bạn cố sửa session của người khác

Response sẽ có `statusCode: 403`:

```json
{
  "statusCode": 403,
  "message": "Access denied: You can only update your own sessions",
  "data": null
}
```

---

## 📖 PHẦN 0: LOOKUP APIs (Dữ liệu tra cứu)

### Giới thiệu

Các API này cung cấp dữ liệu tham chiếu để Frontend tạo dropdown, form select, v.v. Tất cả đều **PUBLIC** (không cần token) và **KHÔNG phân trang** (trả về toàn bộ list).

---

### 0.1 Lấy Danh Sách Môn Học

**Endpoint:** `GET /subjects`

**Authentication:** KHÔNG cần

**Response:**
```json
{
  "statusCode": 200,
  "message": "Subjects retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Giải tích 1"
    },
    {
      "id": 2,
      "name": "Vật lý 1"
    },
    {
      "id": 11,
      "name": "Kỹ thuật lập trình"
    }
  ]
}
```

**Sử dụng:**
- Tạo dropdown chọn môn học khi tạo Session
- Tạo multi-select chọn môn dạy khi tạo Tutor Profile

---

### 0.2 Lấy Danh Sách Khoa

**Endpoint:** `GET /departments`

**Authentication:** KHÔNG cần

**Response:**
```json
{
  "statusCode": 200,
  "message": "Departments retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Khoa Cơ khí"
    },
    {
      "id": 5,
      "name": "Khoa Khoa học và Kỹ thuật Máy tính"
    },
    {
      "id": 7,
      "name": "Khoa Khoa học Ứng dụng"
    }
  ]
}
```

**Sử dụng:**
- Tạo dropdown chọn khoa để filter
- Bước 1 trước khi chọn chuyên ngành

---

### 0.3 Lấy Danh Sách Chuyên Ngành

**Endpoint:** `GET /majors`

**Authentication:** KHÔNG cần

**Response:**
```json
{
  "statusCode": 200,
  "message": "Majors retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Khoa học Máy tính",
      "majorCode": "7480101",
      "programCode": "106",
      "note": null,
      "departmentId": 5,
      "departmentName": "Khoa Khoa học và Kỹ thuật Máy tính"
    },
    {
      "id": 2,
      "name": "Kỹ thuật Máy tính",
      "majorCode": "7480106",
      "programCode": "107",
      "note": null,
      "departmentId": 5,
      "departmentName": "Khoa Khoa học và Kỹ thuật Máy tính"
    }
  ]
}
```

**Giải thích fields:**
- `majorCode`: Mã ngành (7 ký tự)
- `programCode`: Mã chương trình (3 ký tự)
- `note`: Ghi chú (ví dụ: "CLC", "Tiên tiến")
- `departmentId`, `departmentName`: Khoa quản lý chuyên ngành này

**Sử dụng:**
- Tạo dropdown chọn chuyên ngành khi tạo Tutor Profile
- Hiển thị thông tin chuyên ngành của tutor

---

### 0.4 Lấy Chuyên Ngành Theo Khoa

**Endpoint:** `GET /majors/by-department/{departmentId}`

**Authentication:** KHÔNG cần

**URL Parameter:**
- `{departmentId}`: ID của khoa

**Ví dụ:**
```
GET http://localhost:8081/majors/by-department/5
```

**Response:** Giống như GET /majors nhưng chỉ trả về majors của khoa đó

**Sử dụng:**
- Cascading dropdown: User chọn khoa → Load majors của khoa đó
- Giảm số lượng options trong dropdown

**Workflow khuyến nghị:**
1. User chọn Department từ dropdown
2. Frontend gọi `/majors/by-department/{departmentId}`
3. Hiển thị dropdown Majors với kết quả đã filter

---

### 0.5 Lấy Danh Sách Session Status

**Endpoint:** `GET /session-statuses`

**Authentication:** KHÔNG cần

**Response:**
```json
{
  "statusCode": 200,
  "message": "Session statuses retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "SCHEDULED",
      "description": "Đã lên lịch"
    },
    {
      "id": 2,
      "name": "IN_PROGRESS",
      "description": "Đang diễn ra"
    },
    {
      "id": 3,
      "name": "COMPLETED",
      "description": "Hoàn thành"
    },
    {
      "id": 4,
      "name": "CANCELLED",
      "description": "Đã hủy"
    }
  ]
}
```

**Sử dụng:**
- Tạo dropdown chọn status khi tạo/update Session
- Hiển thị badge màu theo status

**Lưu ý:**
- Frontend GỬI `id` (number: 1, 2, 3, 4) trong request
- Backend TRẢ VỀ `name` (string: "SCHEDULED") trong response

---

### 0.6 Lấy Danh Sách Student Session Status

**Endpoint:** `GET /student-session-statuses`

**Authentication:** KHÔNG cần

**Response:**
```json
{
  "statusCode": 200,
  "message": "Student session statuses retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "PENDING",
      "description": "Chờ tutor xác nhận"
    },
    {
      "id": 2,
      "name": "CONFIRMED",
      "description": "Đã xác nhận"
    },
    {
      "id": 3,
      "name": "CANCELLED",
      "description": "Đã hủy"
    },
    {
      "id": 4,
      "name": "REJECTED",
      "description": "Bị từ chối"
    }
  ]
}
```

**Sử dụng:**
- Hiển thị trạng thái đăng ký của student vào session
- Sẽ dùng khi có API StudentSession (tương lai)

---

## 📚 PHẦN 1: SESSION APIs

### Giới Thiệu

Session là một buổi học/gia sư do tutor tạo ra. Mỗi session có:
- Một tutor (người dạy)
- Một môn học
- Thời gian bắt đầu và kết thúc
- Hình thức (online/offline)
- Địa điểm
- Trạng thái (scheduled, in_progress, completed, cancelled)
- Danh sách students đã đăng ký (có thể rỗng)

**Lưu ý quan trọng:** Khi tạo session, bạn **KHÔNG** gửi danh sách students. Students sẽ đăng ký vào session sau đó thông qua API khác.

---

### 1.1 Xem Danh Sách Sessions

**Endpoint:** `GET /sessions`

**Authentication:** KHÔNG cần (công khai)

**Query Parameters:**
- `page`: Số trang (bắt đầu từ 0, mặc định: 0)
- `size`: Số items mỗi trang (mặc định: 10)

**Ví dụ request:**
```
GET http://localhost:8080/sessions?page=0&size=10
```

Lấy trang thứ 2 với 20 items:
```
GET http://localhost:8080/sessions?page=1&size=20
```

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Sessions retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "tutorName": "Nguyen Van A",
        "studentNames": ["Tran Thi B", "Le Van C"],
        "subjectName": "Giải tích 1",
        "startTime": "2025-11-20T10:00:00Z",
        "endTime": "2025-11-20T12:00:00Z",
        "format": "online",
        "location": "Google Meet",
        "status": "SCHEDULED",
        "createdDate": "2025-11-12T08:00:00Z",
        "updatedDate": null
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 15,
    "totalPages": 2,
    "first": true,
    "last": false,
    "empty": false
  }
}
```

**Giải thích các trường trong Session:**
- `id`: ID của session (dùng để update/delete)
- `tutorName`: Tên đầy đủ của tutor (firstName + lastName)
- `studentNames`: Mảng tên các students đã đăng ký (có thể rỗng `[]`)
- `subjectName`: Tên môn học (ví dụ: "Giải tích 1", "Kỹ thuật lập trình")
- `startTime`, `endTime`: Thời gian bắt đầu/kết thúc, format ISO 8601 UTC
- `format`: Hình thức ("online" hoặc "offline")
- `location`: Địa điểm (ví dụ: "Google Meet", "H1-101")
- `status`: Trạng thái hiện tại (giải thích bên dưới)
- `createdDate`: Ngày tạo session
- `updatedDate`: Ngày cập nhật lần cuối (null nếu chưa update)

**Các giá trị của `status`:**
- `SCHEDULED` - Đã lên lịch, chưa bắt đầu
- `IN_PROGRESS` - Đang diễn ra
- `COMPLETED` - Đã hoàn thành
- `CANCELLED` - Đã bị hủy

---

### 1.2 Tạo Session Mới

**Endpoint:** `POST /sessions`

**Authentication:** BẮT BUỘC - Chỉ user có role TUTOR

**Headers cần gửi:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**

```json
{
  "tutorId": 5,
  "subjectId": 3,
  "startTime": "2025-11-20T14:00:00Z",
  "endTime": "2025-11-20T16:00:00Z",
  "format": "offline",
  "location": "H1-101",
  "statusId": 1
}
```

**Giải thích các trường:**

**Bắt buộc:**
- `tutorId`: ID của tutor tạo session (thường là ID của user đang login)
- `subjectId`: ID của môn học (lấy từ danh sách subjects)
- `startTime`: Thời gian bắt đầu, format: `YYYY-MM-DDTHH:mm:ssZ` (UTC timezone)
- `endTime`: Thời gian kết thúc

**Không bắt buộc (có thể null):**
- `format`: "online" hoặc "offline"
- `location`: Địa điểm (Google Meet link, phòng học, v.v.)
- `statusId`: ID trạng thái (xem bảng bên dưới). **Mặc định là 1 (SCHEDULED) nếu không gửi**

**Bảng Status IDs:**

| ID | Tên | Nghĩa |
|----|-----|-------|
| 1 | SCHEDULED | Đã lên lịch |
| 2 | IN_PROGRESS | Đang diễn ra |
| 3 | COMPLETED | Hoàn thành |
| 4 | CANCELLED | Đã hủy |

**⚠️ LƯU Ý QUAN TRỌNG:**
- **KHÔNG gửi** field `studentId` hoặc `students` khi tạo session
- Students sẽ tự đăng ký vào session sau thông qua API StudentSession
- Khi mới tạo, `studentNames` sẽ là mảng rỗng `[]`

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Session created successfully",
  "data": {
    "id": 15,
    "tutorName": "Nguyen Van A",
    "studentNames": [],
    "subjectName": "Đại số tuyến tính",
    "startTime": "2025-11-20T14:00:00Z",
    "endTime": "2025-11-20T16:00:00Z",
    "format": "offline",
    "location": "H1-101",
    "status": "SCHEDULED",
    "createdDate": "2025-11-12T10:30:00Z",
    "updatedDate": null
  }
}
```

**Lỗi có thể gặp:**

1. **Thiếu token hoặc không phải TUTOR:**
```json
{
  "statusCode": 401,
  "message": "Authentication required",
  "data": null
}
```

2. **Tutor không tồn tại:**
```json
{
  "statusCode": 404,
  "message": "Tutor not found with id: 999",
  "data": null
}
```

3. **Subject không tồn tại:**
```json
{
  "statusCode": 404,
  "message": "Subject not found with id: 999",
  "data": null
}
```

---

### 1.3 Cập Nhật Session

**Endpoint:** `PUT /sessions/{id}`

**Authentication:** BẮT BUỘC - Chỉ tutor chủ sở hữu session

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**URL Parameter:**
- `{id}`: ID của session cần update (ví dụ: `/sessions/15`)

**Request Body (TẤT CẢ đều không bắt buộc):**

```json
{
  "startTime": "2025-11-20T15:00:00Z",
  "endTime": "2025-11-20T17:00:00Z",
  "statusId": 4,
  "location": "H1-201"
}
```

**🎯 Logic Update (Partial Update):**

Hệ thống chỉ cập nhật những field bạn gửi lên:
- Nếu field là `null` → KHÔNG update (giữ nguyên giá trị cũ)
- Nếu field là string rỗng (`""` hoặc chỉ có khoảng trắng) → KHÔNG update
- Nếu field có giá trị → Update và tự động xóa khoảng trắng thừa

**Ví dụ 1 - Chỉ update địa điểm:**

Request:
```json
{
  "location": "Google Meet"
}
```

Kết quả: Chỉ `location` bị thay đổi, các field khác giữ nguyên.

**Ví dụ 2 - Update nhiều fields:**

Request:
```json
{
  "location": "  H1-201  ",
  "statusId": 2,
  "format": "offline"
}
```

Kết quả:
- `location` = "H1-201" (đã trim khoảng trắng)
- `statusId` = 2 (IN_PROGRESS)
- `format` = "offline"
- Các field khác không đổi

**Ví dụ 3 - String rỗng không update:**

Request:
```json
{
  "location": "",
  "format": "   "
}
```

Kết quả: Không có field nào được update vì cả 2 đều rỗng.

**⚠️ LƯU Ý:**
- Chỉ tutor tạo session mới có quyền update
- KHÔNG thể update danh sách students (quản lý qua StudentSession API)
- KHÔNG thể update tutorId hoặc subjectId

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Session updated successfully",
  "data": {
    "id": 15,
    "tutorName": "Nguyen Van A",
    "studentNames": [],
    "subjectName": "Đại số tuyến tính",
    "startTime": "2025-11-20T15:00:00Z",
    "endTime": "2025-11-20T17:00:00Z",
    "format": "offline",
    "location": "H1-201",
    "status": "CANCELLED",
    "createdDate": "2025-11-12T10:30:00Z",
    "updatedDate": "2025-11-12T11:00:00Z"
  }
}
```

**Lỗi có thể gặp:**

1. **Không phải chủ sở hữu:**
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only update your own sessions",
  "data": null
}
```

2. **Session không tồn tại:**
```json
{
  "statusCode": 404,
  "message": "Session not found with id: 999",
  "data": null
}
```

---

### 1.4 Xóa Session

**Endpoint:** `DELETE /sessions/{id}`

**Authentication:** BẮT BUỘC - Chỉ tutor chủ sở hữu

**Headers:**
```
Authorization: Bearer <token>
```

**URL Parameter:**
- `{id}`: ID của session cần xóa

**Ví dụ:**
```
DELETE http://localhost:8080/sessions/15
```

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Session deleted successfully",
  "data": null
}
```

**Lỗi có thể gặp:**

Tương tự như UPDATE - không có quyền hoặc session không tồn tại.

---

## 👨‍🏫 PHẦN 2: TUTOR APIs

### Giới Thiệu

Tutor Profile là hồ sơ của người dạy/gia sư. Bao gồm:
- Thông tin cá nhân từ User (tên, ảnh, ngày sinh, SĐT) - TỪ HỆ THỐNG DATACORE
- Thông tin tutor chuyên biệt (chuyên môn, kinh nghiệm, đánh giá)

**Lưu ý quan trọng:**
- Thông tin cá nhân (tên, SĐT, ngày sinh) KHÔNG thể sửa qua API này
- Rating (đánh giá) tự động tính từ feedback của students
- isAvailable tự động set = true khi tạo

---

### 2.1 Xem Danh Sách Tutors

**Endpoint:** `GET /tutors`

**Authentication:** KHÔNG cần (công khai)

**Query Parameters:**
- `page`: Số trang (mặc định: 0)
- `size`: Số items mỗi trang (mặc định: 10)

**Ví dụ:**
```
GET http://localhost:8080/tutors?page=0&size=10
```

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Tutors retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "hcmutId": "1952001",
        "firstName": "Nguyen",
        "lastName": "Van A",
        "profileImage": "https://example.com/avatar.jpg",
        "academicStatus": "Senior",
        "dob": "2002-05-15",
        "phone": "0901234567",
        "otherMethodContact": "facebook.com/nguyenvana",
        "role": "tutor",
        "createdDate": "2025-01-10T08:00:00Z",
        "updateDate": "2025-11-10T14:30:00Z",
        "lastLogin": "2025-11-12T09:00:00Z",
        "title": "Senior",
        "majorId": 5,
        "majorName": "Khoa học Máy tính",
        "department": "Khoa Khoa học và Kỹ thuật Máy tính",
        "description": "2 năm kinh nghiệm dạy lập trình cho sinh viên năm 1, 2",
        "specializations": ["Giải tích 1", "Kỹ thuật lập trình", "Cơ sở dữ liệu"],
        "rating": 4.8,
        "reviewCount": 0,
        "studentCount": 25,
        "experienceYears": 2,
        "isAvailable": true
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 20,
    "totalPages": 2,
    "first": true,
    "last": false,
    "empty": false
  }
}
```

**Giải thích các trường:**

**Từ User (KHÔNG thể sửa qua Tutor API):**
- `hcmutId`: Mã số sinh viên/giảng viên HCMUT
- `firstName`, `lastName`: Tên (từ hệ thống Datacore)
- `profileImage`: URL ảnh đại diện
- `academicStatus`: Trạng thái học vấn (Senior, Graduate, v.v.)
- `dob`: Ngày sinh (format: YYYY-MM-DD)
- `phone`: Số điện thoại
- `otherMethodContact`: Liên hệ khác (Facebook, Zalo, v.v.)
- `role`: Vai trò ("tutor")
- `createdDate`: Ngày tạo tài khoản
- `updateDate`: Ngày cập nhật lần cuối
- `lastLogin`: Lần login cuối

**Từ TutorProfile (Có thể sửa):**
- `id`: ID của tutor profile (dùng để update/delete)
- `title`: Chức danh (ví dụ: "Senior Student", "Graduate Student")
- `majorId`: ID của chuyên ngành
- `majorName`: Tên chuyên ngành (ví dụ: "Khoa học Máy tính")
- `department`: Tên khoa (ví dụ: "Khoa Khoa học và Kỹ thuật Máy tính")
- `description`: Mô tả bản thân, kinh nghiệm
- `specializations`: Mảng tên các môn học tutor có thể dạy
- `rating`: Đánh giá trung bình (0.0 - 5.0), **tự động tính**
- `reviewCount`: Số lượng đánh giá (hiện tại luôn = 0)
- `studentCount`: Số học sinh đã dạy (từ sessions completed)
- `experienceYears`: Số năm kinh nghiệm
- `isAvailable`: Có sẵn sàng nhận học sinh không (true/false)

---

### 2.2 Tạo Tutor Profile

**Endpoint:** `POST /tutors`

**Authentication:** BẮT BUỘC - Bất kỳ user đã login

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**

```json
{
  "title": "Senior Student",
  "majorId": 5,
  "description": "Mình có 2 năm kinh nghiệm dạy lập trình cho sinh viên năm 1, 2. Chuyên về Java, Python và cơ sở dữ liệu.",
  "subjects": [11, 20, 22],
  "experienceYears": 2
}
```

**Giải thích các trường:**

**Không bắt buộc (có thể null):**
- `title`: Chức danh (ví dụ: "Senior Student", "Graduate Student")
- `majorId`: ID của chuyên ngành (lấy từ bảng major)
- `description`: Giới thiệu bản thân, kinh nghiệm dạy học
- `subjects`: **Mảng các ID môn học** (KHÔNG phải tên môn học). Ví dụ: [11, 20, 22] tương ứng với ["Kỹ thuật lập trình", "Công nghệ phần mềm", "Cơ sở dữ liệu"]
- `experienceYears`: Số năm kinh nghiệm (số nguyên)

**❌ KHÔNG gửi các field sau (tự động xử lý):**
- `name`, `firstName`, `lastName` → Lấy từ User (Datacore)
- `phone`, `dob`, `hcmutId` → Lấy từ User (Datacore)
- `rating` → Tự động set = 0.0, sẽ tính sau từ feedback
- `isAvailable` → Tự động set = true
- `studentCount` → Tự động set = 0

**📚 Lấy danh sách Subject IDs:**

Hiện tại bạn cần biết trước ID của các môn học. Ví dụ một số môn:
- 1: Giải tích 1
- 2: Vật lý 1
- 11: Kỹ thuật lập trình
- 20: Công nghệ phần mềm
- 22: Cơ sở dữ liệu
- ...

(Trong tương lai sẽ có API GET /subjects để lấy danh sách đầy đủ)

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Tutor created successfully",
  "data": {
    "id": 10,
    "hcmutId": "1952050",
    "firstName": "Tran",
    "lastName": "Thi B",
    "profileImage": null,
    "academicStatus": "Senior",
    "dob": "2002-08-20",
    "phone": "0912345678",
    "otherMethodContact": null,
    "role": "tutor",
    "createdDate": "2025-11-12T10:45:00Z",
    "updateDate": "2025-11-12T10:45:00Z",
    "lastLogin": "2025-11-12T10:30:00Z",
    "title": "Senior Student",
    "majorId": 5,
    "majorName": "Khoa học Máy tính",
    "department": "Khoa Khoa học và Kỹ thuật Máy tính",
    "description": "Mình có 2 năm kinh nghiệm dạy lập trình...",
    "specializations": ["Kỹ thuật lập trình", "Công nghệ phần mềm", "Cơ sở dữ liệu"],
    "rating": 0.0,
    "reviewCount": 0,
    "studentCount": 0,
    "experienceYears": 2,
    "isAvailable": true
  }
}
```

**Lưu ý response:**
- `specializations` trả về là **tên môn học** (mảng string), không phải ID
- `rating` = 0.0 ban đầu
- `studentCount` = 0 ban đầu
- `isAvailable` = true tự động

---

### 2.3 Cập Nhật Tutor Profile

**Endpoint:** `PUT /tutors/{id}`

**Authentication:** BẮT BUỘC - Chỉ chủ sở hữu profile

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**URL Parameter:**
- `{id}`: ID của tutor profile (ví dụ: `/tutors/10`)

**Request Body (TẤT CẢ không bắt buộc):**

```json
{
  "title": "Graduate Student",
  "description": "Updated description với nhiều kinh nghiệm hơn",
  "subjects": [11, 20, 22, 25],
  "experienceYears": 3
}
```

**🎯 Logic Update (Partial Update):**

Giống như Session, chỉ update những field có giá trị:
- Field `null` → Không update
- String rỗng → Không update
- List rỗng (`[]`) → Không update
- Có giá trị → Update và trim whitespace

**Ví dụ 1 - Chỉ update mô tả:**

Request:
```json
{
  "description": "Mô tả mới"
}
```

Chỉ `description` thay đổi, các field khác giữ nguyên.

**Ví dụ 2 - Update subjects:**

Request:
```json
{
  "subjects": [11, 20, 22, 25, 30]
}
```

Danh sách môn học được thay thế hoàn toàn bằng list mới.

**⚠️ QUAN TRỌNG - KHÔNG thể update:**
- Thông tin từ User/Datacore: `name`, `firstName`, `lastName`, `phone`, `dob`, `hcmutId`, `profileImage`
- Thông tin tự động tính: `rating`, `studentCount`
- Nếu muốn update thông tin cá nhân, phải thông qua hệ thống User/Datacore

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Tutor updated successfully",
  "data": {
    "id": 10,
    "hcmutId": "1952050",
    "firstName": "Tran",
    "lastName": "Thi B",
    ...
    "title": "Graduate Student",
    "description": "Updated description với nhiều kinh nghiệm hơn",
    "specializations": ["Kỹ thuật lập trình", "Công nghệ phần mềm", "Cơ sở dữ liệu", "Lập trình web", "Mạng máy tính"],
    "experienceYears": 3,
    ...
  }
}
```

**Lỗi có thể gặp:**

1. **Không phải chủ sở hữu:**
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only update your own tutor profile",
  "data": null
}
```

---

### 2.4 Xóa Tutor Profile

**Endpoint:** `DELETE /tutors/{id}`

**Authentication:** BẮT BUỘC - Chỉ chủ sở hữu

**Headers:**
```
Authorization: Bearer <token>
```

**Ví dụ:**
```
DELETE http://localhost:8080/tutors/10
```

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Tutor deleted successfully",
  "data": null
}
```

---

## 📝 Phụ Lục: Thông Tin Thêm

### Format Thời Gian

Tất cả thời gian đều dùng format **ISO 8601 với UTC timezone**:

```
YYYY-MM-DDTHH:mm:ssZ
```

**Ví dụ:**
- `2025-11-20T14:00:00Z` = Ngày 20/11/2025, 14:00 giờ UTC
- `2025-12-01T08:30:00Z` = Ngày 01/12/2025, 08:30 giờ UTC

**Lưu ý:**
- Luôn có chữ `Z` ở cuối (nghĩa là UTC)
- Không có timezone offset (+07:00)
- Frontend cần convert sang giờ địa phương khi hiển thị

### Subject IDs Tham Khảo

Dựa theo database V5 (một số môn phổ biến):

| ID | Tên Môn Học |
|----|-------------|
| 1 | Giải tích 1 |
| 2 | Vật lý 1 |
| 3 | Xác suất thống kê |
| 4 | Đại số tuyến tính |
| 5 | Hóa đại cương |
| 11 | Kỹ thuật lập trình |
| 20 | Công nghệ phần mềm |
| 22 | Cơ sở dữ liệu |
| 23 | Lập trình web |
| 25 | Mạng máy tính |

### Status IDs Tham Khảo

**Session Status:**
| ID | Tên | Nghĩa |
|----|-----|-------|
| 1 | SCHEDULED | Đã lên lịch |
| 2 | IN_PROGRESS | Đang diễn ra |
| 3 | COMPLETED | Hoàn thành |
| 4 | CANCELLED | Đã hủy |

**Student Session Status (dùng sau khi có API StudentSession):**
| ID | Tên | Nghĩa |
|----|-----|-------|
| 1 | PENDING | Chờ tutor xác nhận |
| 2 | CONFIRMED | Đã xác nhận |
| 3 | CANCELLED | Đã hủy |
| 4 | REJECTED | Bị từ chối |

**User Status:**
| ID | Tên | Nghĩa |
|----|-----|-------|
| 1 | ACTIVE | Đang hoạt động |
| 2 | INACTIVE | Tạm ngừng |

### Mã Lỗi HTTP Phổ Biến

| Code | Nghĩa | Khi nào xảy ra |
|------|-------|----------------|
| 200 | OK | Request thành công |
| 400 | Bad Request | Dữ liệu gửi lên không hợp lệ |
| 401 | Unauthorized | Thiếu token hoặc token không hợp lệ |
| 403 | Forbidden | Không có quyền thực hiện hành động |
| 404 | Not Found | Không tìm thấy resource (session, tutor, subject...) |
| 500 | Internal Server Error | Lỗi server (báo cho Backend) |

---

## ✅ Checklist Test Cho Frontend

### Sessions
- [ ] GET /sessions - Lấy danh sách không cần token
- [ ] GET /sessions?page=1&size=5 - Test phân trang
- [ ] POST /sessions - Tạo session với token (role TUTOR)
- [ ] POST /sessions - Test lỗi 401 khi không có token
- [ ] PUT /sessions/{id} - Update session của mình
- [ ] PUT /sessions/{id} - Test lỗi 403 khi update session người khác
- [ ] DELETE /sessions/{id} - Xóa session của mình
- [ ] Test partial update (chỉ gửi 1-2 fields)

### Tutors
- [ ] GET /tutors - Lấy danh sách không cần token
- [ ] GET /tutors?page=1&size=5 - Test phân trang
- [ ] POST /tutors - Tạo tutor profile với token
- [ ] Verify response có đầy đủ info từ User
- [ ] PUT /tutors/{id} - Update profile của mình
- [ ] PUT /tutors/{id} - Test lỗi 403 khi update profile người khác
- [ ] DELETE /tutors/{id} - Xóa profile của mình
- [ ] Test không thể sửa name, phone qua Tutor API

---

**HẾT TÀI LIỆU**

Nếu có thắc mắc hoặc gặp lỗi không có trong tài liệu này, vui lòng liên hệ Backend team.
# 📘 Hướng Dẫn Sử Dụng API - Tutor System

**Dành cho Frontend Developer**  
**Ngày cập nhật**: 12/11/2025  
**Database Version**: V10

---

## 🌐 Thông Tin Chung

### Base URL
```
http://localhost:8081
```

### Format Response Chung

Tất cả API đều trả về theo format sau:

```json
{
  "statusCode": 200,
  "message": "Thông báo thành công hoặc lỗi",
  "data": { /* Dữ liệu hoặc null */ }
}
```

**Giải thích các trường:**
- `statusCode`: Mã trạng thái (200 = thành công, 400 = lỗi request, 403 = không có quyền, 404 = không tìm thấy)
- `message`: Thông báo bằng tiếng Anh mô tả kết quả
- `data`: Chứa dữ liệu trả về, có thể là object, array, hoặc null

### Format Phân Trang

Khi API trả về danh sách có phân trang, `data` sẽ có format:

```json
{
  "content": [ /* Mảng các items */ ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 50,
  "totalPages": 5,
  "first": true,
  "last": false,
  "empty": false
}
```

**Giải thích:**
- `content`: Mảng chứa các item trong trang hiện tại
- `pageNumber`: Số trang hiện tại (bắt đầu từ 0)
- `pageSize`: Số lượng items mỗi trang
- `totalElements`: Tổng số items trong database
- `totalPages`: Tổng số trang
- `first`: `true` nếu đây là trang đầu tiên
- `last`: `true` nếu đây là trang cuối cùng
- `empty`: `true` nếu không có item nào

---

## 🔐 Xác Thực (Authentication)

### Các Endpoint Công Khai (Không cần token)

Những endpoint sau **KHÔNG** cần gửi token:
- `GET /sessions` - Xem danh sách sessions
- `GET /tutors` - Xem danh sách tutors
- `GET /subjects` - Lấy danh sách môn học
- `GET /departments` - Lấy danh sách khoa
- `GET /majors` - Lấy danh sách chuyên ngành
- `GET /majors/by-department/{departmentId}` - Lấy chuyên ngành theo khoa
- `GET /session-statuses` - Lấy danh sách trạng thái session
- `GET /student-session-statuses` - Lấy danh sách trạng thái đăng ký

### Các Endpoint Cần Xác Thực

Những endpoint sau **BẮT BUỘC** phải có token:
- `POST /sessions` - Tạo session mới (chỉ role TUTOR)
- `PUT /sessions/{id}` - Sửa session (chỉ tutor chủ sở hữu)
- `DELETE /sessions/{id}` - Xóa session (chỉ tutor chủ sở hữu)
- `POST /tutors` - Tạo tutor profile (bất kỳ ai đã login)
- `PUT /tutors/{id}` - Sửa tutor profile (chỉ chủ sở hữu)
- `DELETE /tutors/{id}` - Xóa tutor profile (chỉ chủ sở hữu)

### Cách Gửi Token

Thêm vào Header của HTTP request:

```
Authorization: Bearer <your_jwt_token_here>
```

**Ví dụ:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Lỗi Khi Thiếu Token hoặc Token Không Hợp Lệ

Response sẽ có `statusCode: 401`:

```json
{
  "statusCode": 401,
  "message": "Authentication required",
  "data": null
}
```

### Lỗi Khi Không Đủ Quyền

Ví dụ: Bạn cố sửa session của người khác

Response sẽ có `statusCode: 403`:

```json
{
  "statusCode": 403,
  "message": "Access denied: You can only update your own sessions",
  "data": null
}
```

---

## 📚 PHẦN 1: SESSION APIs

### Giới Thiệu

Session là một buổi học/gia sư do tutor tạo ra. Mỗi session có:
- Một tutor (người dạy)
- Một môn học
- Thời gian bắt đầu và kết thúc
- Hình thức (online/offline)
- Địa điểm
- Trạng thái (scheduled, in_progress, completed, cancelled)
- Danh sách students đã đăng ký (có thể rỗng)

**Lưu ý quan trọng:** Khi tạo session, bạn **KHÔNG** gửi danh sách students. Students sẽ đăng ký vào session sau đó thông qua API khác.

---

### 1.1 Xem Danh Sách Sessions

**Endpoint:** `GET /sessions`

**Authentication:** KHÔNG cần (công khai)

**Query Parameters:**
- `page`: Số trang (bắt đầu từ 0, mặc định: 0)
- `size`: Số items mỗi trang (mặc định: 10)

**Ví dụ request:**
```
GET http://localhost:8080/sessions?page=0&size=10
```

Lấy trang thứ 2 với 20 items:
```
GET http://localhost:8080/sessions?page=1&size=20
```

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Sessions retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "tutorName": "Nguyen Van A",
        "studentNames": ["Tran Thi B", "Le Van C"],
        "subjectName": "Giải tích 1",
        "startTime": "2025-11-20T10:00:00Z",
        "endTime": "2025-11-20T12:00:00Z",
        "format": "online",
        "location": "Google Meet",
        "status": "SCHEDULED",
        "createdDate": "2025-11-12T08:00:00Z",
        "updatedDate": null
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 15,
    "totalPages": 2,
    "first": true,
    "last": false,
    "empty": false
  }
}
```

**Giải thích các trường trong Session:**
- `id`: ID của session (dùng để update/delete)
- `tutorName`: Tên đầy đủ của tutor (firstName + lastName)
- `studentNames`: Mảng tên các students đã đăng ký (có thể rỗng `[]`)
- `subjectName`: Tên môn học (ví dụ: "Giải tích 1", "Kỹ thuật lập trình")
- `startTime`, `endTime`: Thời gian bắt đầu/kết thúc, format ISO 8601 UTC
- `format`: Hình thức ("online" hoặc "offline")
- `location`: Địa điểm (ví dụ: "Google Meet", "H1-101")
- `status`: Trạng thái hiện tại (giải thích bên dưới)
- `createdDate`: Ngày tạo session
- `updatedDate`: Ngày cập nhật lần cuối (null nếu chưa update)

**Các giá trị của `status`:**
- `SCHEDULED` - Đã lên lịch, chưa bắt đầu
- `IN_PROGRESS` - Đang diễn ra
- `COMPLETED` - Đã hoàn thành
- `CANCELLED` - Đã bị hủy

---

### 1.2 Tạo Session Mới

**Endpoint:** `POST /sessions`

**Authentication:** BẮT BUỘC - Chỉ user có role TUTOR

**Headers cần gửi:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**

```json
{
  "tutorId": 5,
  "subjectId": 3,
  "startTime": "2025-11-20T14:00:00Z",
  "endTime": "2025-11-20T16:00:00Z",
  "format": "offline",
  "location": "H1-101",
  "statusId": 1
}
```

**Giải thích các trường:**

**Bắt buộc:**
- `tutorId`: ID của tutor tạo session (thường là ID của user đang login)
- `subjectId`: ID của môn học (lấy từ danh sách subjects)
- `startTime`: Thời gian bắt đầu, format: `YYYY-MM-DDTHH:mm:ssZ` (UTC timezone)
- `endTime`: Thời gian kết thúc

**Không bắt buộc (có thể null):**
- `format`: "online" hoặc "offline"
- `location`: Địa điểm (Google Meet link, phòng học, v.v.)
- `statusId`: ID trạng thái (xem bảng bên dưới). **Mặc định là 1 (SCHEDULED) nếu không gửi**

**Bảng Status IDs:**

| ID | Tên | Nghĩa |
|----|-----|-------|
| 1 | SCHEDULED | Đã lên lịch |
| 2 | IN_PROGRESS | Đang diễn ra |
| 3 | COMPLETED | Hoàn thành |
| 4 | CANCELLED | Đã hủy |

**⚠️ LƯU Ý QUAN TRỌNG:**
- **KHÔNG gửi** field `studentId` hoặc `students` khi tạo session
- Students sẽ tự đăng ký vào session sau thông qua API StudentSession
- Khi mới tạo, `studentNames` sẽ là mảng rỗng `[]`

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Session created successfully",
  "data": {
    "id": 15,
    "tutorName": "Nguyen Van A",
    "studentNames": [],
    "subjectName": "Đại số tuyến tính",
    "startTime": "2025-11-20T14:00:00Z",
    "endTime": "2025-11-20T16:00:00Z",
    "format": "offline",
    "location": "H1-101",
    "status": "SCHEDULED",
    "createdDate": "2025-11-12T10:30:00Z",
    "updatedDate": null
  }
}
```

**Lỗi có thể gặp:**

1. **Thiếu token hoặc không phải TUTOR:**
```json
{
  "statusCode": 401,
  "message": "Authentication required",
  "data": null
}
```

2. **Tutor không tồn tại:**
```json
{
  "statusCode": 404,
  "message": "Tutor not found with id: 999",
  "data": null
}
```

3. **Subject không tồn tại:**
```json
{
  "statusCode": 404,
  "message": "Subject not found with id: 999",
  "data": null
}
```

---

### 1.3 Cập Nhật Session

**Endpoint:** `PUT /sessions/{id}`

**Authentication:** BẮT BUỘC - Chỉ tutor chủ sở hữu session

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**URL Parameter:**
- `{id}`: ID của session cần update (ví dụ: `/sessions/15`)

**Request Body (TẤT CẢ đều không bắt buộc):**

```json
{
  "startTime": "2025-11-20T15:00:00Z",
  "endTime": "2025-11-20T17:00:00Z",
  "statusId": 4,
  "location": "H1-201"
}
```

**🎯 Logic Update (Partial Update):**

Hệ thống chỉ cập nhật những field bạn gửi lên:
- Nếu field là `null` → KHÔNG update (giữ nguyên giá trị cũ)
- Nếu field là string rỗng (`""` hoặc chỉ có khoảng trắng) → KHÔNG update
- Nếu field có giá trị → Update và tự động xóa khoảng trắng thừa

**Ví dụ 1 - Chỉ update địa điểm:**

Request:
```json
{
  "location": "Google Meet"
}
```

Kết quả: Chỉ `location` bị thay đổi, các field khác giữ nguyên.

**Ví dụ 2 - Update nhiều fields:**

Request:
```json
{
  "location": "  H1-201  ",
  "statusId": 2,
  "format": "offline"
}
```

Kết quả:
- `location` = "H1-201" (đã trim khoảng trắng)
- `statusId` = 2 (IN_PROGRESS)
- `format` = "offline"
- Các field khác không đổi

**Ví dụ 3 - String rỗng không update:**

Request:
```json
{
  "location": "",
  "format": "   "
}
```

Kết quả: Không có field nào được update vì cả 2 đều rỗng.

**⚠️ LƯU Ý:**
- Chỉ tutor tạo session mới có quyền update
- KHÔNG thể update danh sách students (quản lý qua StudentSession API)
- KHÔNG thể update tutorId hoặc subjectId

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Session updated successfully",
  "data": {
    "id": 15,
    "tutorName": "Nguyen Van A",
    "studentNames": [],
    "subjectName": "Đại số tuyến tính",
    "startTime": "2025-11-20T15:00:00Z",
    "endTime": "2025-11-20T17:00:00Z",
    "format": "offline",
    "location": "H1-201",
    "status": "CANCELLED",
    "createdDate": "2025-11-12T10:30:00Z",
    "updatedDate": "2025-11-12T11:00:00Z"
  }
}
```

**Lỗi có thể gặp:**

1. **Không phải chủ sở hữu:**
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only update your own sessions",
  "data": null
}
```

2. **Session không tồn tại:**
```json
{
  "statusCode": 404,
  "message": "Session not found with id: 999",
  "data": null
}
```

---

### 1.4 Xóa Session

**Endpoint:** `DELETE /sessions/{id}`

**Authentication:** BẮT BUỘC - Chỉ tutor chủ sở hữu

**Headers:**
```
Authorization: Bearer <token>
```

**URL Parameter:**
- `{id}`: ID của session cần xóa

**Ví dụ:**
```
DELETE http://localhost:8080/sessions/15
```

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Session deleted successfully",
  "data": null
}
```

**Lỗi có thể gặp:**

Tương tự như UPDATE - không có quyền hoặc session không tồn tại.

---

## 👨‍🏫 PHẦN 2: TUTOR APIs

### Giới Thiệu

Tutor Profile là hồ sơ của người dạy/gia sư. Bao gồm:
- Thông tin cá nhân từ User (tên, ảnh, ngày sinh, SĐT) - TỪ HỆ THỐNG DATACORE
- Thông tin tutor chuyên biệt (chuyên môn, kinh nghiệm, đánh giá)

**Lưu ý quan trọng:**
- Thông tin cá nhân (tên, SĐT, ngày sinh) KHÔNG thể sửa qua API này
- Rating (đánh giá) tự động tính từ feedback của students
- isAvailable tự động set = true khi tạo

---

### 2.1 Xem Danh Sách Tutors

**Endpoint:** `GET /tutors`

**Authentication:** KHÔNG cần (công khai)

**Query Parameters:**
- `page`: Số trang (mặc định: 0)
- `size`: Số items mỗi trang (mặc định: 10)

**Ví dụ:**
```
GET http://localhost:8080/tutors?page=0&size=10
```

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Tutors retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "hcmutId": "1952001",
        "firstName": "Nguyen",
        "lastName": "Van A",
        "profileImage": "https://example.com/avatar.jpg",
        "academicStatus": "Senior",
        "dob": "2002-05-15",
        "phone": "0901234567",
        "otherMethodContact": "facebook.com/nguyenvana",
        "role": "tutor",
        "createdDate": "2025-01-10T08:00:00Z",
        "updateDate": "2025-11-10T14:30:00Z",
        "lastLogin": "2025-11-12T09:00:00Z",
        "title": "Senior",
        "majorId": 5,
        "majorName": "Khoa học Máy tính",
        "department": "Khoa Khoa học và Kỹ thuật Máy tính",
        "description": "2 năm kinh nghiệm dạy lập trình cho sinh viên năm 1, 2",
        "specializations": ["Giải tích 1", "Kỹ thuật lập trình", "Cơ sở dữ liệu"],
        "rating": 4.8,
        "reviewCount": 0,
        "studentCount": 25,
        "experienceYears": 2,
        "isAvailable": true
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 20,
    "totalPages": 2,
    "first": true,
    "last": false,
    "empty": false
  }
}
```

**Giải thích các trường:**

**Từ User (KHÔNG thể sửa qua Tutor API):**
- `hcmutId`: Mã số sinh viên/giảng viên HCMUT
- `firstName`, `lastName`: Tên (từ hệ thống Datacore)
- `profileImage`: URL ảnh đại diện
- `academicStatus`: Trạng thái học vấn (Senior, Graduate, v.v.)
- `dob`: Ngày sinh (format: YYYY-MM-DD)
- `phone`: Số điện thoại
- `otherMethodContact`: Liên hệ khác (Facebook, Zalo, v.v.)
- `role`: Vai trò ("tutor")
- `createdDate`: Ngày tạo tài khoản
- `updateDate`: Ngày cập nhật lần cuối
- `lastLogin`: Lần login cuối

**Từ TutorProfile (Có thể sửa):**
- `id`: ID của tutor profile (dùng để update/delete)
- `title`: Chức danh (ví dụ: "Senior Student", "Graduate Student")
- `majorId`: ID của chuyên ngành
- `majorName`: Tên chuyên ngành (ví dụ: "Khoa học Máy tính")
- `department`: Tên khoa (ví dụ: "Khoa Khoa học và Kỹ thuật Máy tính")
- `description`: Mô tả bản thân, kinh nghiệm
- `specializations`: Mảng tên các môn học tutor có thể dạy
- `rating`: Đánh giá trung bình (0.0 - 5.0), **tự động tính**
- `reviewCount`: Số lượng đánh giá (hiện tại luôn = 0)
- `studentCount`: Số học sinh đã dạy (từ sessions completed)
- `experienceYears`: Số năm kinh nghiệm
- `isAvailable`: Có sẵn sàng nhận học sinh không (true/false)

---

### 2.2 Tạo Tutor Profile

**Endpoint:** `POST /tutors`

**Authentication:** BẮT BUỘC - Bất kỳ user đã login

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**

```json
{
  "title": "Senior Student",
  "majorId": 5,
  "description": "Mình có 2 năm kinh nghiệm dạy lập trình cho sinh viên năm 1, 2. Chuyên về Java, Python và cơ sở dữ liệu.",
  "subjects": [11, 20, 22],
  "experienceYears": 2
}
```

**Giải thích các trường:**

**Không bắt buộc (có thể null):**
- `title`: Chức danh (ví dụ: "Senior Student", "Graduate Student")
- `majorId`: ID của chuyên ngành (lấy từ bảng major)
- `description`: Giới thiệu bản thân, kinh nghiệm dạy học
- `subjects`: **Mảng các ID môn học** (KHÔNG phải tên môn học). Ví dụ: [11, 20, 22] tương ứng với ["Kỹ thuật lập trình", "Công nghệ phần mềm", "Cơ sở dữ liệu"]
- `experienceYears`: Số năm kinh nghiệm (số nguyên)

**❌ KHÔNG gửi các field sau (tự động xử lý):**
- `name`, `firstName`, `lastName` → Lấy từ User (Datacore)
- `phone`, `dob`, `hcmutId` → Lấy từ User (Datacore)
- `rating` → Tự động set = 0.0, sẽ tính sau từ feedback
- `isAvailable` → Tự động set = true
- `studentCount` → Tự động set = 0

**📚 Lấy danh sách Subject IDs:**

Hiện tại bạn cần biết trước ID của các môn học. Ví dụ một số môn:
- 1: Giải tích 1
- 2: Vật lý 1
- 11: Kỹ thuật lập trình
- 20: Công nghệ phần mềm
- 22: Cơ sở dữ liệu
- ...

(Trong tương lai sẽ có API GET /subjects để lấy danh sách đầy đủ)

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Tutor created successfully",
  "data": {
    "id": 10,
    "hcmutId": "1952050",
    "firstName": "Tran",
    "lastName": "Thi B",
    "profileImage": null,
    "academicStatus": "Senior",
    "dob": "2002-08-20",
    "phone": "0912345678",
    "otherMethodContact": null,
    "role": "tutor",
    "createdDate": "2025-11-12T10:45:00Z",
    "updateDate": "2025-11-12T10:45:00Z",
    "lastLogin": "2025-11-12T10:30:00Z",
    "title": "Senior Student",
    "majorId": 5,
    "majorName": "Khoa học Máy tính",
    "department": "Khoa Khoa học và Kỹ thuật Máy tính",
    "description": "Mình có 2 năm kinh nghiệm dạy lập trình...",
    "specializations": ["Kỹ thuật lập trình", "Công nghệ phần mềm", "Cơ sở dữ liệu"],
    "rating": 0.0,
    "reviewCount": 0,
    "studentCount": 0,
    "experienceYears": 2,
    "isAvailable": true
  }
}
```

**Lưu ý response:**
- `specializations` trả về là **tên môn học** (mảng string), không phải ID
- `rating` = 0.0 ban đầu
- `studentCount` = 0 ban đầu
- `isAvailable` = true tự động

---

### 2.3 Cập Nhật Tutor Profile

**Endpoint:** `PUT /tutors/{id}`

**Authentication:** BẮT BUỘC - Chỉ chủ sở hữu profile

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**URL Parameter:**
- `{id}`: ID của tutor profile (ví dụ: `/tutors/10`)

**Request Body (TẤT CẢ không bắt buộc):**

```json
{
  "title": "Graduate Student",
  "description": "Updated description với nhiều kinh nghiệm hơn",
  "subjects": [11, 20, 22, 25],
  "experienceYears": 3
}
```

**🎯 Logic Update (Partial Update):**

Giống như Session, chỉ update những field có giá trị:
- Field `null` → Không update
- String rỗng → Không update
- List rỗng (`[]`) → Không update
- Có giá trị → Update và trim whitespace

**Ví dụ 1 - Chỉ update mô tả:**

Request:
```json
{
  "description": "Mô tả mới"
}
```

Chỉ `description` thay đổi, các field khác giữ nguyên.

**Ví dụ 2 - Update subjects:**

Request:
```json
{
  "subjects": [11, 20, 22, 25, 30]
}
```

Danh sách môn học được thay thế hoàn toàn bằng list mới.

**⚠️ QUAN TRỌNG - KHÔNG thể update:**
- Thông tin từ User/Datacore: `name`, `firstName`, `lastName`, `phone`, `dob`, `hcmutId`, `profileImage`
- Thông tin tự động tính: `rating`, `studentCount`
- Nếu muốn update thông tin cá nhân, phải thông qua hệ thống User/Datacore

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Tutor updated successfully",
  "data": {
    "id": 10,
    "hcmutId": "1952050",
    "firstName": "Tran",
    "lastName": "Thi B",
    ...
    "title": "Graduate Student",
    "description": "Updated description với nhiều kinh nghiệm hơn",
    "specializations": ["Kỹ thuật lập trình", "Công nghệ phần mềm", "Cơ sở dữ liệu", "Lập trình web", "Mạng máy tính"],
    "experienceYears": 3,
    ...
  }
}
```

**Lỗi có thể gặp:**

1. **Không phải chủ sở hữu:**
```json
{
  "statusCode": 403,
  "message": "Access denied: You can only update your own tutor profile",
  "data": null
}
```

---

### 2.4 Xóa Tutor Profile

**Endpoint:** `DELETE /tutors/{id}`

**Authentication:** BẮT BUỘC - Chỉ chủ sở hữu

**Headers:**
```
Authorization: Bearer <token>
```

**Ví dụ:**
```
DELETE http://localhost:8080/tutors/10
```

**Response thành công:**

```json
{
  "statusCode": 200,
  "message": "Tutor deleted successfully",
  "data": null
}
```

---

## 📝 Phụ Lục: Thông Tin Thêm

### Format Thời Gian

Tất cả thời gian đều dùng format **ISO 8601 với UTC timezone**:

```
YYYY-MM-DDTHH:mm:ssZ
```

**Ví dụ:**
- `2025-11-20T14:00:00Z` = Ngày 20/11/2025, 14:00 giờ UTC
- `2025-12-01T08:30:00Z` = Ngày 01/12/2025, 08:30 giờ UTC

**Lưu ý:**
- Luôn có chữ `Z` ở cuối (nghĩa là UTC)
- Không có timezone offset (+07:00)
- Frontend cần convert sang giờ địa phương khi hiển thị

### Subject IDs Tham Khảo

Dựa theo database V5 (một số môn phổ biến):

| ID | Tên Môn Học |
|----|-------------|
| 1 | Giải tích 1 |
| 2 | Vật lý 1 |
| 3 | Xác suất thống kê |
| 4 | Đại số tuyến tính |
| 5 | Hóa đại cương |
| 11 | Kỹ thuật lập trình |
| 20 | Công nghệ phần mềm |
| 22 | Cơ sở dữ liệu |
| 23 | Lập trình web |
| 25 | Mạng máy tính |

### Status IDs Tham Khảo

**Session Status:**
| ID | Tên | Nghĩa |
|----|-----|-------|
| 1 | SCHEDULED | Đã lên lịch |
| 2 | IN_PROGRESS | Đang diễn ra |
| 3 | COMPLETED | Hoàn thành |
| 4 | CANCELLED | Đã hủy |

**Student Session Status (dùng sau khi có API StudentSession):**
| ID | Tên | Nghĩa |
|----|-----|-------|
| 1 | PENDING | Chờ tutor xác nhận |
| 2 | CONFIRMED | Đã xác nhận |
| 3 | CANCELLED | Đã hủy |
| 4 | REJECTED | Bị từ chối |

**User Status:**
| ID | Tên | Nghĩa |
|----|-----|-------|
| 1 | ACTIVE | Đang hoạt động |
| 2 | INACTIVE | Tạm ngừng |

### Mã Lỗi HTTP Phổ Biến

| Code | Nghĩa | Khi nào xảy ra |
|------|-------|----------------|
| 200 | OK | Request thành công |
| 400 | Bad Request | Dữ liệu gửi lên không hợp lệ |
| 401 | Unauthorized | Thiếu token hoặc token không hợp lệ |
| 403 | Forbidden | Không có quyền thực hiện hành động |
| 404 | Not Found | Không tìm thấy resource (session, tutor, subject...) |
| 500 | Internal Server Error | Lỗi server (báo cho Backend) |

---

## ✅ Checklist Test Cho Frontend

### Sessions
- [ ] GET /sessions - Lấy danh sách không cần token
- [ ] GET /sessions?page=1&size=5 - Test phân trang
- [ ] POST /sessions - Tạo session với token (role TUTOR)
- [ ] POST /sessions - Test lỗi 401 khi không có token
- [ ] PUT /sessions/{id} - Update session của mình
- [ ] PUT /sessions/{id} - Test lỗi 403 khi update session người khác
- [ ] DELETE /sessions/{id} - Xóa session của mình
- [ ] Test partial update (chỉ gửi 1-2 fields)

### Tutors
- [ ] GET /tutors - Lấy danh sách không cần token
- [ ] GET /tutors?page=1&size=5 - Test phân trang
- [ ] POST /tutors - Tạo tutor profile với token
- [ ] Verify response có đầy đủ info từ User
- [ ] PUT /tutors/{id} - Update profile của mình
- [ ] PUT /tutors/{id} - Test lỗi 403 khi update profile người khác
- [ ] DELETE /tutors/{id} - Xóa profile của mình
- [ ] Test không thể sửa name, phone qua Tutor API

---

**HẾT TÀI LIỆU**

Nếu có thắc mắc hoặc gặp lỗi không có trong tài liệu này, vui lòng liên hệ Backend team.

