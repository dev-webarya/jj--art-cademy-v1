# Art Academy - MongoDB Schema Documentation

> **MongoDB Connection**: `mongodb+srv://devwebarya_db_user:WebArya1234#@cluster-jj.vlopquo.mongodb.net/artacademy?appName=Cluster-jj`

---

## Database Overview

| Collection | Description |
|------------|-------------|
| `users` | User accounts (students, teachers, admins) |
| `roles` | User roles for authorization |
| `art_classes` | Art class definitions |
| `art_classes_categories` | Categories for art classes |
| `art_classes_images` | Images associated with art classes |
| `art_works` | Artwork listings |
| `art_works_categories` | Categories for artworks |
| `art_gallery` | Gallery items |
| `art_gallery_categories` | Gallery categories |
| `art_materials` | Art materials/supplies |
| `art_materials_categories` | Material categories |
| `art_exhibitions` | Exhibition events |
| `art_exhibition_categories` | Exhibition categories |
| `orders` | Customer orders |
| `order_items` | Individual items in orders |
| `shopping_carts` | User shopping carts |
| `cart_items` | Items in shopping carts |
| `payments` | Payment records |
| `email_otps` | OTP verification records |
| `attendance_sessions` | Class session schedules |
| `attendance_records` | Student attendance records |

---

## Collection Schemas

### 1. Users Collection

```javascript
// Collection: users
{
  "_id": ObjectId,                    // MongoDB generated ID
  "firstName": String,                // Required
  "lastName": String,                 // Required
  "email": String,                    // Required, Unique index
  "password": String,                 // Required, Hashed
  "phoneNumber": String,              // Unique index
  "isEnabled": Boolean,               // Default: false
  "deleted": Boolean,                 // Default: false (soft delete)
  "roles": [                          // Embedded array of role references
    {
      "roleId": ObjectId,
      "name": String                  // e.g., "ROLE_USER", "ROLE_ADMIN", "ROLE_TEACHER"
    }
  ],
  "createdAt": ISODate,               // Auto-generated
  "updatedAt": ISODate                // Auto-updated
}

// Indexes
db.users.createIndex({ "email": 1 }, { unique: true })
db.users.createIndex({ "phoneNumber": 1 }, { unique: true, sparse: true })
db.users.createIndex({ "deleted": 1 })
```

---

### 2. Roles Collection

```javascript
// Collection: roles
{
  "_id": ObjectId,
  "name": String,                     // e.g., "ROLE_USER", "ROLE_ADMIN", "ROLE_TEACHER"
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.roles.createIndex({ "name": 1 }, { unique: true })
```

---

### 3. Art Classes Collection

```javascript
// Collection: art_classes
{
  "_id": ObjectId,
  "name": String,                     // Required
  "description": String,              // Text description
  "basePrice": Decimal128,            // Required, e.g., NumberDecimal("1500.00")
  "isActive": Boolean,                // Default: true
  "proficiency": String,              // "Beginner", "Intermediate", "Advanced"
  "deleted": Boolean,                 // Default: false
  "imageUrl": String,                 // Required
  "category": {                       // Embedded category reference
    "categoryId": ObjectId,
    "name": String
  },
  "images": [                         // Embedded images array
    {
      "imageUrl": String,
      "altText": String,
      "displayOrder": Number
    }
  ],
  "schedule": {                       // Class schedule info
    "duration": Number,               // Duration in minutes
    "maxStudents": Number,
    "recurringDays": [String]         // ["Monday", "Wednesday", "Friday"]
  },
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.art_classes.createIndex({ "name": 1 })
db.art_classes.createIndex({ "category.categoryId": 1 })
db.art_classes.createIndex({ "proficiency": 1 })
db.art_classes.createIndex({ "isActive": 1, "deleted": 1 })
```

---

### 4. Art Classes Categories Collection

```javascript
// Collection: art_classes_categories
{
  "_id": ObjectId,
  "name": String,                     // Required
  "description": String,
  "imageUrl": String,
  "deleted": Boolean,                 // Default: false
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.art_classes_categories.createIndex({ "name": 1 })
db.art_classes_categories.createIndex({ "deleted": 1 })
```

---

### 5. Art Works Collection

```javascript
// Collection: art_works
{
  "_id": ObjectId,
  "title": String,                    // Required
  "description": String,
  "price": Decimal128,                // Required
  "isAvailable": Boolean,             // Default: true
  "artistName": String,
  "medium": String,                   // e.g., "Oil on Canvas", "Watercolor"
  "dimensions": {
    "width": Number,
    "height": Number,
    "unit": String                    // "cm", "inches"
  },
  "imageUrl": String,
  "category": {
    "categoryId": ObjectId,
    "name": String
  },
  "deleted": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.art_works.createIndex({ "title": "text", "artistName": "text" })
db.art_works.createIndex({ "category.categoryId": 1 })
db.art_works.createIndex({ "price": 1 })
```

---

### 6. Art Works Categories Collection

```javascript
// Collection: art_works_categories
{
  "_id": ObjectId,
  "name": String,
  "description": String,
  "imageUrl": String,
  "deleted": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

---

### 7. Art Gallery Collection

```javascript
// Collection: art_gallery
{
  "_id": ObjectId,
  "name": String,
  "description": String,
  "imageUrl": String,
  "category": {
    "categoryId": ObjectId,
    "name": String
  },
  "deleted": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

---

### 8. Art Gallery Categories Collection

```javascript
// Collection: art_gallery_categories
{
  "_id": ObjectId,
  "name": String,
  "description": String,
  "imageUrl": String,
  "deleted": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

---

### 9. Art Materials Collection

```javascript
// Collection: art_materials
{
  "_id": ObjectId,
  "name": String,
  "description": String,
  "price": Decimal128,
  "stockQuantity": Number,
  "imageUrl": String,
  "category": {
    "categoryId": ObjectId,
    "name": String
  },
  "deleted": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.art_materials.createIndex({ "name": "text" })
db.art_materials.createIndex({ "stockQuantity": 1 })
```

---

### 10. Art Materials Categories Collection

```javascript
// Collection: art_materials_categories
{
  "_id": ObjectId,
  "name": String,
  "description": String,
  "imageUrl": String,
  "deleted": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

---

### 11. Art Exhibitions Collection

```javascript
// Collection: art_exhibitions
{
  "_id": ObjectId,
  "name": String,
  "description": String,
  "venue": String,
  "startDate": ISODate,
  "endDate": ISODate,
  "entryFee": Decimal128,
  "imageUrl": String,
  "category": {
    "categoryId": ObjectId,
    "name": String
  },
  "deleted": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.art_exhibitions.createIndex({ "startDate": 1, "endDate": 1 })
```

---

### 12. Art Exhibition Categories Collection

```javascript
// Collection: art_exhibition_categories
{
  "_id": ObjectId,
  "name": String,
  "description": String,
  "imageUrl": String,
  "deleted": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

---

### 13. Orders Collection

```javascript
// Collection: orders
{
  "_id": ObjectId,
  "orderNumber": String,              // Unique, auto-generated
  "user": {                           // Embedded user reference
    "userId": ObjectId,
    "email": String,
    "firstName": String,
    "lastName": String
  },
  "items": [                          // Embedded order items
    {
      "itemType": String,             // "ART_WORK", "ART_MATERIAL", "ART_CLASS"
      "itemId": ObjectId,
      "itemName": String,
      "quantity": Number,
      "unitPrice": Decimal128,
      "totalPrice": Decimal128
    }
  ],
  "orderStatus": String,              // "PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"
  "statusHistory": [
    {
      "status": String,
      "changedAt": ISODate,
      "remarks": String
    }
  ],
  "shippingAddress": {
    "street": String,
    "city": String,
    "state": String,
    "postalCode": String,
    "country": String
  },
  "totalAmount": Decimal128,
  "deleted": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.orders.createIndex({ "orderNumber": 1 }, { unique: true })
db.orders.createIndex({ "user.userId": 1 })
db.orders.createIndex({ "orderStatus": 1 })
db.orders.createIndex({ "createdAt": -1 })
```

---

### 14. Shopping Carts Collection

```javascript
// Collection: shopping_carts
{
  "_id": ObjectId,
  "user": {
    "userId": ObjectId,
    "email": String
  },
  "items": [
    {
      "itemType": String,             // "ART_WORK", "ART_MATERIAL", "ART_CLASS"
      "itemId": ObjectId,
      "itemName": String,
      "quantity": Number,
      "unitPrice": Decimal128
    }
  ],
  "totalAmount": Decimal128,
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.shopping_carts.createIndex({ "user.userId": 1 }, { unique: true })
```

---

### 15. Payments Collection

```javascript
// Collection: payments
{
  "_id": ObjectId,
  "order": {
    "orderId": ObjectId,
    "orderNumber": String
  },
  "user": {
    "userId": ObjectId,
    "email": String
  },
  "razorpayOrderId": String,
  "razorpayPaymentId": String,
  "razorpaySignature": String,
  "amount": Decimal128,
  "currency": String,                 // Default: "INR"
  "status": String,                   // "PENDING", "SUCCESS", "FAILED"
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.payments.createIndex({ "razorpayOrderId": 1 })
db.payments.createIndex({ "order.orderId": 1 })
db.payments.createIndex({ "status": 1 })
```

---

### 16. Email OTPs Collection

```javascript
// Collection: email_otps
{
  "_id": ObjectId,
  "email": String,
  "otp": String,
  "expiresAt": ISODate,
  "verified": Boolean,
  "createdAt": ISODate
}

// Indexes
db.email_otps.createIndex({ "email": 1 })
db.email_otps.createIndex({ "expiresAt": 1 }, { expireAfterSeconds: 0 }) // TTL index
```

---

## NEW: Attendance Management Collections

### 17. Attendance Sessions Collection

```javascript
// Collection: attendance_sessions
{
  "_id": ObjectId,
  "sessionCode": String,              // Unique, e.g., "ART101-2026-01-17-AM"
  "artClass": {                       // Reference to art class
    "classId": ObjectId,
    "name": String,
    "proficiency": String
  },
  "teacher": {                        // Reference to teacher user
    "userId": ObjectId,
    "firstName": String,
    "lastName": String,
    "email": String
  },
  "sessionDate": ISODate,             // Date of the session
  "startTime": String,                // "10:00 AM"
  "endTime": String,                  // "12:00 PM"
  "status": String,                   // "SCHEDULED", "IN_PROGRESS", "COMPLETED", "CANCELLED"
  "totalEnrolled": Number,            // Total students enrolled
  "totalPresent": Number,             // Count of present students
  "totalAbsent": Number,              // Count of absent students
  "remarks": String,                  // Session notes
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.attendance_sessions.createIndex({ "sessionCode": 1 }, { unique: true })
db.attendance_sessions.createIndex({ "artClass.classId": 1 })
db.attendance_sessions.createIndex({ "teacher.userId": 1 })
db.attendance_sessions.createIndex({ "sessionDate": 1 })
db.attendance_sessions.createIndex({ "status": 1 })
```

---

### 18. Attendance Records Collection

```javascript
// Collection: attendance_records
{
  "_id": ObjectId,
  "session": {                        // Reference to attendance session
    "sessionId": ObjectId,
    "sessionCode": String,
    "sessionDate": ISODate
  },
  "artClass": {                       // Reference to art class
    "classId": ObjectId,
    "name": String
  },
  "student": {                        // Reference to student user
    "userId": ObjectId,
    "firstName": String,
    "lastName": String,
    "email": String,
    "phoneNumber": String
  },
  "status": String,                   // "PRESENT", "ABSENT", "LATE", "EXCUSED"
  "checkInTime": ISODate,             // Actual check-in time
  "checkOutTime": ISODate,            // Actual check-out time
  "markedBy": {                       // Who marked the attendance
    "userId": ObjectId,
    "firstName": String,
    "lastName": String,
    "role": String                    // "TEACHER" or "ADMIN"
  },
  "remarks": String,                  // Notes about this attendance
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.attendance_records.createIndex({ "session.sessionId": 1, "student.userId": 1 }, { unique: true })
db.attendance_records.createIndex({ "student.userId": 1 })
db.attendance_records.createIndex({ "artClass.classId": 1 })
db.attendance_records.createIndex({ "session.sessionDate": 1 })
db.attendance_records.createIndex({ "status": 1 })
```

---

### 19. Class Enrollments Collection (NEW)

```javascript
// Collection: class_enrollments
{
  "_id": ObjectId,
  "student": {
    "userId": ObjectId,
    "firstName": String,
    "lastName": String,
    "email": String
  },
  "artClass": {
    "classId": ObjectId,
    "name": String,
    "proficiency": String
  },
  "enrollmentDate": ISODate,
  "status": String,                   // "ACTIVE", "COMPLETED", "DROPPED", "SUSPENDED"
  "paymentStatus": String,            // "PAID", "PENDING", "PARTIAL"
  "startDate": ISODate,
  "endDate": ISODate,
  "createdAt": ISODate,
  "updatedAt": ISODate
}

// Indexes
db.class_enrollments.createIndex({ "student.userId": 1, "artClass.classId": 1 }, { unique: true })
db.class_enrollments.createIndex({ "artClass.classId": 1 })
db.class_enrollments.createIndex({ "status": 1 })
```

---

## Aggregation Examples

### Get Attendance Summary for a Class

```javascript
db.attendance_records.aggregate([
  { $match: { "artClass.classId": ObjectId("...") } },
  { $group: {
      _id: "$student.userId",
      studentName: { $first: { $concat: ["$student.firstName", " ", "$student.lastName"] } },
      totalSessions: { $sum: 1 },
      presentCount: { $sum: { $cond: [{ $eq: ["$status", "PRESENT"] }, 1, 0] } },
      absentCount: { $sum: { $cond: [{ $eq: ["$status", "ABSENT"] }, 1, 0] } },
      lateCount: { $sum: { $cond: [{ $eq: ["$status", "LATE"] }, 1, 0] } }
  }},
  { $addFields: {
      attendancePercentage: { 
        $multiply: [{ $divide: ["$presentCount", "$totalSessions"] }, 100] 
      }
  }},
  { $sort: { attendancePercentage: -1 } }
])
```

### Monthly Attendance Report

```javascript
db.attendance_records.aggregate([
  { $match: {
      "session.sessionDate": {
        $gte: ISODate("2026-01-01"),
        $lt: ISODate("2026-02-01")
      }
  }},
  { $group: {
      _id: {
        classId: "$artClass.classId",
        className: "$artClass.name"
      },
      totalRecords: { $sum: 1 },
      presentCount: { $sum: { $cond: [{ $eq: ["$status", "PRESENT"] }, 1, 0] } }
  }},
  { $project: {
      className: "$_id.className",
      totalRecords: 1,
      presentCount: 1,
      attendanceRate: { 
        $multiply: [{ $divide: ["$presentCount", "$totalRecords"] }, 100] 
      }
  }}
])
```

---

## Data Validation Rules

MongoDB schema validation can be applied using JSON Schema:

```javascript
db.createCollection("attendance_records", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["session", "student", "status", "createdAt"],
      properties: {
        status: {
          enum: ["PRESENT", "ABSENT", "LATE", "EXCUSED"],
          description: "Must be a valid attendance status"
        }
      }
    }
  }
})
```

---

## Notes

1. **Embedded vs Referenced**: This schema uses a hybrid approach:
   - **Embedded**: Frequently accessed together data (e.g., category name in art_classes)
   - **Referenced**: Data that changes independently (using ObjectId references)

2. **Soft Deletes**: All collections use a `deleted` field for soft deletes

3. **Audit Fields**: All collections include `createdAt` and `updatedAt` timestamps

4. **Indexes**: Critical indexes are defined for query performance
