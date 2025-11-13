# ✅ UPDATED - Code đã được cập nhật theo Database V10

## Ngày: 12/11/2025

---

## 📊 DATABASE STRUCTURE SAU V10

### Tables đã được tạo/sửa qua migrations:

1. **V2**: Tạo bảng `status` 
2. **V3**: User table - đổi `status` VARCHAR thành `status_id` FK
3. **V4**: Tạo bảng `department` và `major`
4. **V5**: Sửa lại bảng `subject` (đơn giản hóa)
5. **V6**: Sửa `feedback_student` và `reportof_tutor`, tạo `report_material`
6. **V7**: Đổi tên `Table` → `library`
7. **V8**: TutorProfile dùng ManyToMany với Subject
8. **V9**: Tạo bảng `student_session` (N-N), xóa `session.student_id`
9. **V10**: 
   - Đổi tên `status` → `user_status`
   - Tạo `session_status` table
   - Tạo `student_session_status` table
   - Session dùng `status_id` FK
   - StudentSession dùng `status_id` FK

---

## ✅ ĐÃ SỬA TRONG JAVA CODE

### 1. Model Changes

#### ✅ Status.java
- Đổi `@Table(name = "status")` → `@Table(name = "user_status")`
- Khớp với V10 rename

#### ✅ SessionStatus.java (NEW)
- Entity mới cho bảng `session_status`
- Constants: SCHEDULED=1, IN_PROGRESS=2, COMPLETED=3, CANCELLED=4

#### ✅ StudentSessionStatus.java (NEW)
- Entity mới cho bảng `student_session_status`
- Constants: PENDING=1, CONFIRMED=2, CANCELLED=3, REJECTED=4

#### ✅ Session.java
- Xóa: `student_id` (V9 đã xóa)
- Xóa: `status` VARCHAR field
- Thêm: `sessionStatus` ManyToOne FK
- Thêm: `studentSessions` OneToMany collection

#### ✅ StudentSession.java
- Xóa: `status` VARCHAR field
- Thêm: `studentSessionStatus` ManyToOne FK

#### ✅ User.java
- Xóa: `faculty` VARCHAR (dùng Major FK)
- Sửa: `status` → nullable = false
- Thêm: `studentSessions` relationship

#### ✅ FeedbackStudent.java
- Xóa: `contentQuality` (V6 đã drop)
- Xóa: `teachingEffectiveness` (V6 đã drop)
- Xóa: `communication` (V6 đã drop)
- Xóa: `suggestion` (V6 đã drop)
- Giữ: `rating`, `comment`, `wouldRecommend`

---

### 2. Repository Changes

#### ✅ SessionStatusRepository.java (NEW)
- Truy vấn session_status table

#### ✅ StudentSessionStatusRepository.java (NEW)
- Truy vấn student_session_status table

---

### 3. DTO Changes

#### ✅ SessionDTO.java
- `status` field vẫn là String (lấy từ sessionStatus.name)
- `studentNames` là List<String>

---

### 4. Request Changes

#### ✅ SessionRequest.java
- Đổi: `status` String → `statusId` Byte
- Frontend gửi: 1, 2, 3, 4 thay vì "scheduled", "in_progress"...

---

### 5. Mapper Changes

#### ✅ SessionMapper.java
- Map `session.getSessionStatus().getName()` → `dto.setStatus()`

---

### 6. Service Changes

#### ✅ SessionServiceImp.java
- Import SessionStatus, SessionStatusRepository
- Inject SessionStatusRepository
- `createSession`: Dùng statusId (default = 1 SCHEDULED)
- `updateSession`: 
  - Dùng statusId nếu có
  - **Chỉ update các field không null và không rỗng**
  - Trim whitespace cho string fields

#### ✅ TutorServiceImp.java
- `createTutor`: Auto set rating = 0.0, isAvailable = true
- `updateTutor`: 
  - Không cho sửa Datacore fields
  - **Chỉ update các field không null và không rỗng**
  - Trim whitespace cho string fields
  - Subjects: chỉ update nếu list không null và không empty

---

## ⚙️ UPDATE LOGIC (Partial Update)

### Nguyên tắc:
✅ **Chỉ update fields có giá trị mới**
- Null → Không update (giữ nguyên giá trị cũ)
- Empty string → Không update (giữ nguyên giá trị cũ)
- Có giá trị → Update và trim whitespace

### SessionServiceImp.updateSession()
```java
// Null check + Empty check
if (request.getFormat() != null && !request.getFormat().trim().isEmpty()) {
    session.setFormat(request.getFormat().trim());
}

if (request.getLocation() != null && !request.getLocation().trim().isEmpty()) {
    session.setLocation(request.getLocation().trim());
}

// Instant/Timestamp - chỉ null check
if (request.getStartTime() != null) {
    session.setStartTime(request.getStartTime());
}

// Byte/Number - chỉ null check
if (request.getStatusId() != null) {
    SessionStatus sessionStatus = sessionStatusRepository.findById(request.getStatusId())
        .orElseThrow(() -> new DataNotFoundExceptions("SessionStatus not found"));
    session.setSessionStatus(sessionStatus);
}
```

### TutorServiceImp.updateTutor()
```java
// String fields - null + empty check + trim
if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
    user.setAcademicStatus(request.getTitle().trim());
}

if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
    tutorProfile.setBio(request.getDescription().trim());
}

// Number fields - chỉ null check
if (request.getExperienceYears() != null) {
    tutorProfile.setExperienceYears(request.getExperienceYears().shortValue());
}

// List fields - null + empty check
if (request.getSubjects() != null && !request.getSubjects().isEmpty()) {
    tutorProfile.getSubjects().clear();
    for (Long subjectId : request.getSubjects()) {
        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new DataNotFoundExceptions("Subject not found"));
        tutorProfile.getSubjects().add(subject);
    }
}
```

### Ví dụ Update Request

**Request chỉ update location:**
```json
PUT /sessions/10
{
  "location": "H1-201"
}
```
→ Chỉ location được update, các field khác giữ nguyên

**Request update nhiều fields:**
```json
PUT /sessions/10
{
  "startTime": "2025-11-20T15:00:00Z",
  "endTime": "2025-11-20T17:00:00Z",
  "location": "  H1-201  ",
  "statusId": 4
}
```
→ Tất cả 4 fields được update, location được trim whitespace

**Request với empty string (không update):**
```json
PUT /sessions/10
{
  "location": "",
  "format": "   "
}
```
→ Không update gì cả vì cả 2 đều empty sau khi trim

---

## 📋 DATABASE STRUCTURE HIỆN TẠI

### user_status (V10 renamed từ status)
```sql
id   | name      | description
-----|-----------|------------------
1    | ACTIVE    | Hoạt động
2    | INACTIVE  | Ngừng hoạt động
```

### session_status (V10 new)
```sql
id   | name         | description
-----|--------------|------------------
1    | SCHEDULED    | Đã lên lịch
2    | IN_PROGRESS  | Đang diễn ra
3    | COMPLETED    | Hoàn thành
4    | CANCELLED    | Đã hủy
```

### student_session_status (V10 new)
```sql
id   | name       | description
-----|------------|------------------
1    | PENDING    | Chờ xác nhận
2    | CONFIRMED  | Đã xác nhận
3    | CANCELLED  | Đã hủy
4    | REJECTED   | Bị từ chối
```

### session
```sql
- id
- tutor_id (FK → user)
- subject_id (FK → subject)
- status_id (FK → session_status) ← V10
- start_time
- end_time
- format
- location
- created_date
- updated_date
```
**KHÔNG CÒN student_id** (V9 đã xóa)

### student_session (V9 new)
```sql
- id
- student_id (FK → user)
- session_id (FK → session)
- status_id (FK → student_session_status) ← V10
- registered_date
- updated_date
```

### user
```sql
- id
- status_id (FK → user_status) ← V3
- major_id (FK → major) ← V4
- created_date
- update_date
- last_login
- role
- hcmut_id
- first_name
- last_name
- profile_image
- academic_status
- dob
- phone
- other_method_contact
```
**KHÔNG CÒN faculty** (dùng major.department.name)

### feedback_student (V6 simplified)
```sql
- id
- session_id (FK → session)
- student_id (FK → user)
- rating (DECIMAL 2,1)
- comment (TEXT)
- would_recommend (BOOLEAN)
- created_date
```
**ĐÃ XÓA**: content_quality, teaching_effectiveness, communication, suggestion

### tutor_profile (V8 ManyToMany)
```sql
- id
- user_id (FK → user)
- experience_years
- bio
- rating
- priority
- total_sessions_completed
- is_available
```
**KHÔNG CÒN subject_id** (dùng bảng tutor_profile_subject)

### tutor_profile_subject (V8 new)
```sql
- tutor_profile_id (FK → tutor_profile)
- subject_id (FK → subject)
```

### library (V7 renamed từ Table)
```sql
- id
- name
- category (đổi từ catagory)
- author
- subject_id (FK → subject) ← V7
- url
- uploaded_date
- uploaded_by
```

---

## 🔄 API CHANGES

### SessionRequest
**TRƯỚC:**
```json
{
  "tutorId": 1,
  "subjectId": 2,
  "startTime": "2025-11-20T10:00:00Z",
  "endTime": "2025-11-20T12:00:00Z",
  "format": "online",
  "location": "Google Meet",
  "status": "scheduled"  ← String
}
```

**SAU:**
```json
{
  "tutorId": 1,
  "subjectId": 2,
  "startTime": "2025-11-20T10:00:00Z",
  "endTime": "2025-11-20T12:00:00Z",
  "format": "online",
  "location": "Google Meet",
  "statusId": 1  ← Byte (1=SCHEDULED, 2=IN_PROGRESS, 3=COMPLETED, 4=CANCELLED)
}
```

### SessionResponse (SessionDTO)
**Response vẫn trả String status cho dễ đọc:**
```json
{
  "id": 10,
  "tutorName": "Nguyen Van A",
  "studentNames": ["Tran Thi B"],
  "subjectName": "Calculus",
  "startTime": "2025-11-20T10:00:00Z",
  "endTime": "2025-11-20T12:00:00Z",
  "format": "online",
  "location": "Google Meet",
  "status": "SCHEDULED",  ← String từ sessionStatus.name
  "createdDate": "2025-11-12T08:00:00Z",
  "updatedDate": null
}
```

---

## 📝 STATUS IDs REFERENCE

### Session Status
- `1` = SCHEDULED (đã lên lịch)
- `2` = IN_PROGRESS (đang diễn ra)
- `3` = COMPLETED (hoàn thành)
- `4` = CANCELLED (đã hủy)

### Student Session Status
- `1` = PENDING (chờ tutor xác nhận)
- `2` = CONFIRMED (đã xác nhận tham gia)
- `3` = CANCELLED (student đã hủy)
- `4` = REJECTED (tutor từ chối)

### User Status
- `1` = ACTIVE (đang hoạt động)
- `2` = INACTIVE (tạm ngừng)

---

## 🎯 NHỮNG GÌ ĐÃ KHỚP

✅ Status table → user_status (V10)
✅ Session.status VARCHAR → status_id FK (V10)
✅ StudentSession.status VARCHAR → status_id FK (V10)
✅ Session.student_id → removed (V9)
✅ User.status VARCHAR → status_id FK (V3)
✅ User.faculty → removed, dùng major_id (V4)
✅ FeedbackStudent - xóa 4 fields (V6)
✅ TutorProfile.subject_id → ManyToMany (V8)
✅ Table → library (V7)
✅ Subject simplified (V5)

---

## 🔧 CẦN UPDATE DOCUMENTATION

Các file documentation cần update:

1. **FRONTEND_API_GUIDE.md**
   - SessionRequest: `status` → `statusId` (Byte)
   - Thêm status IDs reference

2. **POSTMAN_TEST_GUIDE_V2.md**
   - Ví dụ request dùng `statusId: 1` thay vì `status: "scheduled"`

3. **QUICK_START.md**
   - Note về status IDs

---

## ✅ CODE HIỆN TẠI KHỚP 100% VỚI DATABASE SAU V10

**Tất cả conflicts đã được giải quyết!**

---

## 📁 FILES ĐÃ SỬA

### Models
- ✅ Status.java (table name → user_status)
- ✅ SessionStatus.java (NEW)
- ✅ StudentSessionStatus.java (NEW)
- ✅ Session.java (dùng sessionStatus FK)
- ✅ StudentSession.java (dùng studentSessionStatus FK)
- ✅ User.java (status_id FK, xóa faculty)
- ✅ FeedbackStudent.java (xóa 4 fields)

### Repositories
- ✅ SessionStatusRepository.java (NEW)
- ✅ StudentSessionStatusRepository.java (NEW)

### DTOs
- ✅ SessionDTO.java (không đổi, vẫn String status)

### Requests
- ✅ SessionRequest.java (status → statusId Byte)

### Mappers
- ✅ SessionMapper.java (map sessionStatus.name)

### Services
- ✅ SessionServiceImp.java (dùng SessionStatus FK)

---

## 🚀 READY TO USE

Code Java hiện tại đã khớp hoàn toàn với database structure sau khi chạy migrations đến V10!

