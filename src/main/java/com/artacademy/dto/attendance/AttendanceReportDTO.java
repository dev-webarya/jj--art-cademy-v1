package com.artacademy.dto.attendance;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceReportDTO {
    private String classId;
    private String className;

    private String studentId;
    private String studentName;

    private Integer totalSessions;
    private Integer presentCount;
    private Integer absentCount;
    private Integer lateCount;
    private Integer excusedCount;

    private Double attendancePercentage;
}
