# LMS API Testing Guide

> **Base URL:** `http://localhost:8095`  
> **Authorization:** Bearer Token (Admin login required)

---

## Prerequisites

1. Login as Admin: `POST /api/v1/auth/login`
   ```json
   {
     "email": "admin@artacademy.com",
     "password": "Admin@123"
   }
   ```
2. Copy the `accessToken` from response
3. Use in all requests: `Authorization: Bearer <accessToken>`

---

## Flow Overview

```
Student registers → Enrolls in class → Admin approves enrollment (rollNo generated)
    → Admin creates subscription → Admin creates sessions → Admin marks attendance
```

---

## Step 1: Student Enrollment

### 1.1 Register Student (if not exists)
**POST** `/api/v1/auth/register`
```json
{
  "firstName": "Test",
  "lastName": "Student",
  "email": "teststudent@test.com",
  "password": "Password@123",
  "phoneNumber": "+911234567890"
}
```

### 1.2 Create Enrollment
**POST** `/api/v1/lms/enrollments`
```json
{
  "userId": "USER_ID_FROM_REGISTRATION",
  "classId": "697df675b8841c6d9ad9889e",
  "parentGuardianName": "Parent Name",
  "studentAge": 12,
  "schedule": "WEEKEND_MORNING",
  "additionalMessage": "Looking forward to classes",
  "address": "123 Main Street, City",
  "emergencyContactName": "Emergency Contact",
  "emergencyContactPhone": "9876543210"
}
```

### 1.3 Approve Enrollment (Admin)
**PUT** `/api/v1/lms/enrollments/{enrollmentId}/status?status=APPROVED`

> ✅ After approval, `rollNo` is generated (e.g., AA-0001)

---

## Step 2: Create Subscription

**POST** `/api/v1/lms/subscriptions`

```json
{
  "enrollmentId": "697df7e9b8841c6d9ad988cf",
  "subscriptionMonth": 1,
  "subscriptionYear": 2026,
  "allowedSessions": 8,
  "notes": "January 2026 subscription"
}
```

### Expected Response
```json
{
  "id": "subscription_id_here",
  "enrollmentId": "697df7e9b8841c6d9ad988cf",
  "rollNo": "AA-0001",
  "studentId": "697df672b8841c6d9ad9886f",
  "studentName": "Fiona",
  "subscriptionMonth": 1,
  "subscriptionYear": 2026,
  "allowedSessions": 8,
  "attendedSessions": 0,
  "remainingSessions": 8,
  "status": "ACTIVE"
}
```

---

## Step 3: Create Session

**POST** `/api/v1/lms/sessions`

```json
{
  "sessionDate": "2026-01-31",
  "startTime": "10:00",
  "endTime": "12:00",
  "topic": "Watercolor Basics - Week 1",
  "description": "Introduction to watercolor techniques",
  "meetingLink": "https://meet.google.com/abc-defg-hij",
  "meetingPassword": "art123"
}
```

> 📝 **Save the `id` from response for attendance marking**

---

## Step 4: Get Eligible Students

**GET** `/api/v1/lms/attendance/eligible-students`

Returns students with:
- APPROVED enrollment
- Active subscription for current month

### Expected Response
```json
[
  {
    "studentId": "697df672b8841c6d9ad9886f",
    "enrollmentId": "697df7e9b8841c6d9ad988cf",
    "subscriptionId": "subscription_id",
    "rollNo": "AA-0001",
    "studentName": "Fiona",
    "studentEmail": "fiona@test.com",
    "attendedSessions": 0,
    "allowedSessions": 8,
    "remainingSessions": 8,
    "isOverLimit": false
  }
]
```

---

## Step 5: Mark Attendance

**POST** `/api/v1/lms/attendance`

```json
{
  "sessionId": "SESSION_ID_FROM_STEP_3",
  "attendanceList": [
    {
      "studentId": "697df672b8841c6d9ad9886f",
      "isPresent": true,
      "remarks": "On time"
    }
  ]
}
```

> ⚠️ Replace `SESSION_ID_FROM_STEP_3` with actual session ID

---

## Step 6: Verification Endpoints

### 6.1 Get Session Attendance
**GET** `/api/v1/lms/attendance/session/{sessionId}`

### 6.2 Get Student Attendance History
**GET** `/api/v1/lms/attendance/student/{studentId}?page=0&size=100`

### 6.3 Get Monthly Summary
**GET** `/api/v1/lms/attendance/student/{studentId}/monthly/{year}/{month}`

Example: `/api/v1/lms/attendance/student/697df672b8841c6d9ad9886f/monthly/2026/1`

### 6.4 Check Over-Limit Students
**GET** `/api/v1/lms/subscriptions/over-limit`

### 6.5 View Subscription Status
**GET** `/api/v1/lms/subscriptions/student/{studentId}/active`

---

## Test Scenarios

### Scenario 1: Normal Attendance Flow
1. Create subscription ✓
2. Create session ✓
3. Get eligible students (should see student) ✓
4. Mark attendance as PRESENT ✓
5. Check subscription - attendedSessions should be 1

### Scenario 2: Over-Limit Detection
1. Mark attendance for 9 sessions
2. Call `/api/v1/lms/subscriptions/over-limit`
3. Should see student in over-limit list

### Scenario 3: No Subscription
1. Try marking attendance without subscription
2. Should still work but no subscription tracking

---

## Quick Reference

| Action | Method | Endpoint |
|--------|--------|----------|
| Create Subscription | POST | `/api/v1/lms/subscriptions` |
| Create Session | POST | `/api/v1/lms/sessions` |
| Eligible Students | GET | `/api/v1/lms/attendance/eligible-students` |
| Mark Attendance | POST | `/api/v1/lms/attendance` |
| Session Attendance | GET | `/api/v1/lms/attendance/session/{id}` |
| Student History | GET | `/api/v1/lms/attendance/student/{id}` |
| Over-Limit | GET | `/api/v1/lms/subscriptions/over-limit` |

---

## Sample Test Data

### Already Approved Enrollment
```
Enrollment ID: 697df7e9b8841c6d9ad988cf
User ID: 697df672b8841c6d9ad9886f
Roll No: AA-0001
Student: Fiona
Email: fiona@test.com
Class ID: 697df675b8841c6d9ad9889e
```
