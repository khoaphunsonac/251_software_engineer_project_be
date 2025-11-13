# ✅ Phân trang (Pagination) cho List APIs

## 🎯 Đã triển khai

Đã áp dụng phân trang cho tất cả các API trả về list với **mặc định 10 items/trang**.

---

## 📦 Files đã tạo mới

### 1. PageDTO.java
```java
package HCMUT.TutorSytem.dto;

@Data
public class PageDTO<T> {
    private List<T> content;           // Dữ liệu của trang hiện tại
    private int pageNumber;            // Số trang hiện tại (bắt đầu từ 0)
    private int pageSize;              // Số items mỗi trang
    private long totalElements;        // Tổng số items trong database
    private int totalPages;            // Tổng số trang
    private boolean first;             // Có phải trang đầu không
    private boolean last;              // Có phải trang cuối không
    private boolean empty;             // Trang có rỗng không
}
```

---

## 🔄 Files đã cập nhật

### Service Layer
1. ✅ `TutorService.java`
2. ✅ `TutorServiceImp.java`
3. ✅ `CourseService.java`
4. ✅ `CourseServiceImp.java`

### Controller Layer
5. ✅ `TutorController.java`
6. ✅ `CourseController.java`

---

## 📖 API Usage

### GET /tutors (với phân trang)

#### Request
```http
GET /tutors?page=0&size=10
```

**Query Parameters:**
- `page` (optional, default=0): Số trang (bắt đầu từ 0)
- `size` (optional, default=10): Số items mỗi trang

#### Ví dụ:
```http
# Trang đầu tiên, 10 items (default)
GET /tutors

# Trang đầu tiên, 10 items (explicit)
GET /tutors?page=0&size=10

# Trang thứ 2, 10 items
GET /tutors?page=1&size=10

# Trang đầu tiên, 20 items
GET /tutors?page=0&size=20

# Trang thứ 3, 5 items
GET /tutors?page=2&size=5
```

#### Response
```json
{
  "statusCode": 200,
  "message": "Tutors retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "TS. Nguyễn Văn Minh",
        "title": "Tiến sĩ",
        "department": "Khoa Toán - Tin học",
        "description": "Giảng viên với hơn 8 năm kinh nghiệm",
        "specializations": ["Toán Cao Cấp", "Giải tích"],
        "rating": 4.9,
        "reviewCount": 124,
        "studentCount": 45,
        "experienceYears": 8,
        "isAvailable": true,
        "faculty": "Khoa Toán - Tin học"
      }
      // ... 9 items nữa (tổng 10 items)
    ],
    "pageNumber": 0,        // Trang hiện tại (0-based)
    "pageSize": 10,         // Số items/trang
    "totalElements": 45,    // Tổng số tutors trong DB
    "totalPages": 5,        // Tổng số trang (45/10 = 5)
    "first": true,          // Đây là trang đầu
    "last": false,          // Không phải trang cuối
    "empty": false          // Không rỗng
  }
}
```

---

### GET /courses (với phân trang)

#### Request
```http
GET /courses?page=0&size=10
```

**Query Parameters:**
- `page` (optional, default=0): Số trang (bắt đầu từ 0)
- `size` (optional, default=10): Số items mỗi trang

#### Ví dụ:
```http
# Trang đầu tiên, 10 items (default)
GET /courses

# Trang thứ 2, 15 items
GET /courses?page=1&size=15

# Trang thứ 5, 5 items
GET /courses?page=4&size=5
```

#### Response
```json
{
  "statusCode": 200,
  "message": "Courses retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Toán Cao Cấp 1",
        "code": "MT1003",
        "timeslots": [
          { "day": "Mon", "start": "07:30", "end": "09:30" },
          { "day": "Wed", "start": "07:30", "end": "09:30" }
        ],
        "teacher": "TS. Nguyễn Văn Minh",
        "faculty": "Khoa Toán - Tin học",
        "weeks": "15 tuần (30 buổi)",
        "enrolled": 45,
        "capacity": 50,
        "rating": 4.8,
        "ratingCount": 28
      }
      // ... 9 items nữa
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 120,
    "totalPages": 12,
    "first": true,
    "last": false,
    "empty": false
  }
}
```

---

## 🔧 Implementation Details

### Controller Layer
```java
@GetMapping
public ResponseEntity<BaseResponse> getAllTutors(
        @RequestParam(defaultValue = "0") int page,      // Mặc định trang 0
        @RequestParam(defaultValue = "10") int size) {   // Mặc định 10 items
    
    PageDTO<TutorDTO> tutorPage = tutorService.getAllTutors(page, size);
    
    BaseResponse response = new BaseResponse();
    response.setStatusCode(200);
    response.setMessage("Tutors retrieved successfully");
    response.setData(tutorPage);  // PageDTO được nhét vào data
    
    return ResponseEntity.ok(response);
}
```

### Service Layer
```java
@Override
public PageDTO<TutorDTO> getAllTutors(int page, int size) {
    // 1. Tạo Pageable object
    Pageable pageable = PageRequest.of(page, size);
    
    // 2. Query với phân trang
    Page<TutorProfile> tutorPage = tutorProfileRepository.findAll(pageable);
    
    // 3. Convert entities sang DTOs
    List<TutorDTO> tutorDTOs = tutorPage.getContent().stream()
            .map(tutorMapper::toDTO)
            .collect(Collectors.toList());
    
    // 4. Tạo PageDTO với metadata
    PageDTO<TutorDTO> pageDTO = new PageDTO<>();
    pageDTO.setContent(tutorDTOs);
    pageDTO.setPageNumber(tutorPage.getNumber());
    pageDTO.setPageSize(tutorPage.getSize());
    pageDTO.setTotalElements(tutorPage.getTotalElements());
    pageDTO.setTotalPages(tutorPage.getTotalPages());
    pageDTO.setFirst(tutorPage.isFirst());
    pageDTO.setLast(tutorPage.isLast());
    pageDTO.setEmpty(tutorPage.isEmpty());
    
    return pageDTO;
}
```

---

## 💡 Lợi ích của Pagination

### 1. ⚡ Performance
- Không load toàn bộ data một lúc
- Giảm memory usage
- Query nhanh hơn với LIMIT/OFFSET

### 2. 🌐 Better UX
- Tải trang nhanh hơn
- Không bị lag khi có nhiều dữ liệu
- Dễ dàng điều hướng qua các trang

### 3. 📊 Bandwidth
- Giảm lượng data transfer
- Tiết kiệm băng thông
- Mobile-friendly

### 4. 🎯 Flexibility
- Client có thể tùy chỉnh page size
- Dễ dàng implement infinite scroll
- Support sorting và filtering (có thể thêm sau)

---

## 🔍 PageDTO Fields Explained

| Field | Type | Description | Example |
|-------|------|-------------|---------|
| `content` | `List<T>` | Dữ liệu của trang hiện tại | `[{...}, {...}]` |
| `pageNumber` | `int` | Số trang hiện tại (0-based) | `0`, `1`, `2` |
| `pageSize` | `int` | Số items mỗi trang | `10`, `20`, `50` |
| `totalElements` | `long` | Tổng số items trong DB | `125` |
| `totalPages` | `int` | Tổng số trang | `13` (125/10) |
| `first` | `boolean` | Có phải trang đầu? | `true`/`false` |
| `last` | `boolean` | Có phải trang cuối? | `true`/`false` |
| `empty` | `boolean` | Trang có rỗng không? | `true`/`false` |

---

## 📱 Frontend Integration Example

### React/Vue/Angular
```javascript
// Fetch tutors với phân trang
async function fetchTutors(page = 0, size = 10) {
  const response = await fetch(`/tutors?page=${page}&size=${size}`);
  const data = await response.json();
  
  // data.data chứa PageDTO
  const pageData = data.data;
  
  console.log('Items:', pageData.content);
  console.log('Current page:', pageData.pageNumber);
  console.log('Total pages:', pageData.totalPages);
  console.log('Is last page?', pageData.last);
  
  return pageData;
}

// Pagination controls
function navigateToPage(page) {
  fetchTutors(page, 10);
}

// Next page
if (!pageData.last) {
  fetchTutors(pageData.pageNumber + 1);
}

// Previous page
if (!pageData.first) {
  fetchTutors(pageData.pageNumber - 1);
}
```

---

## 🎨 Pagination UI Pattern

```
┌─────────────────────────────────────────┐
│  Showing 1-10 of 125 tutors             │
├─────────────────────────────────────────┤
│  [Item 1]                                │
│  [Item 2]                                │
│  ...                                     │
│  [Item 10]                               │
├─────────────────────────────────────────┤
│  [<< First] [< Prev] 1 2 3 4 5 [Next >] │
│                      └─ Current          │
└─────────────────────────────────────────┘
```

---

## ✅ Tóm lại

### Đã triển khai:
- ✅ PageDTO<T> - Generic pagination wrapper
- ✅ GET /tutors?page=0&size=10 - Phân trang tutors
- ✅ GET /courses?page=0&size=10 - Phân trang courses
- ✅ Mặc định: page=0, size=10
- ✅ Tích hợp Spring Data Pagination
- ✅ Metadata đầy đủ (totalElements, totalPages, first, last, empty)

### Response format:
```json
{
  "statusCode": 200,
  "message": "Success",
  "data": {
    "content": [...],        // Items của trang
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 125,
    "totalPages": 13,
    "first": true,
    "last": false,
    "empty": false
  }
}
```

🎉 **Pagination đã sẵn sàng sử dụng!**

