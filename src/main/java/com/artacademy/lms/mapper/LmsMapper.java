package com.artacademy.lms.mapper;

import com.artacademy.lms.dto.response.*;
import com.artacademy.lms.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for LMS entities
 */
@Mapper(componentModel = "spring")
public interface LmsMapper {

    // Instructor mappings
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", expression = "java(instructor.getUser().getFirstName() + \" \" + instructor.getUser().getLastName())")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userPhone", source = "user.phoneNumber")
    @Mapping(target = "batchCount", expression = "java(instructor.getBatches().size())")
    InstructorResponseDto toInstructorDto(Instructor instructor);

    List<InstructorResponseDto> toInstructorDtoList(List<Instructor> instructors);

    // Batch mappings
    @Mapping(target = "artClassId", source = "artClass.id")
    @Mapping(target = "artClassName", source = "artClass.name")
    @Mapping(target = "instructorId", source = "instructor.id")
    @Mapping(target = "instructorName", expression = "java(batch.getInstructor() != null ? batch.getInstructor().getUser().getFirstName() + \" \" + batch.getInstructor().getUser().getLastName() : \"Unassigned\")")
    @Mapping(target = "scheduleDisplayName", expression = "java(batch.getSchedule().getDisplayName())")
    @Mapping(target = "currentStudentCount", expression = "java(batch.getStudents().size())")
    @Mapping(target = "needsInstructor", expression = "java(batch.needsInstructor())")
    BatchResponseDto toBatchDto(Batch batch);

    List<BatchResponseDto> toBatchDtoList(List<Batch> batches);

    // BatchStudent mappings
    @Mapping(target = "batchId", source = "batch.id")
    @Mapping(target = "batchName", source = "batch.name")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "studentName", expression = "java(student.getUser().getFirstName() + \" \" + student.getUser().getLastName())")
    @Mapping(target = "studentEmail", source = "user.email")
    @Mapping(target = "studentPhone", source = "user.phoneNumber")
    BatchStudentResponseDto toBatchStudentDto(BatchStudent student);

    List<BatchStudentResponseDto> toBatchStudentDtoList(List<BatchStudent> students);

    // ClassSession mappings
    @Mapping(target = "batchId", source = "batch.id")
    @Mapping(target = "batchName", source = "batch.name")
    @Mapping(target = "attendanceCount", expression = "java(session.getAttendanceRecords().size())")
    ClassSessionResponseDto toSessionDto(ClassSession session);

    List<ClassSessionResponseDto> toSessionDtoList(List<ClassSession> sessions);

    // Attendance mappings
    @Mapping(target = "sessionId", source = "session.id")
    @Mapping(target = "sessionTitle", source = "session.title")
    @Mapping(target = "batchStudentId", source = "batchStudent.id")
    @Mapping(target = "studentName", expression = "java(attendance.getBatchStudent().getUser().getFirstName() + \" \" + attendance.getBatchStudent().getUser().getLastName())")
    @Mapping(target = "studentEmail", source = "batchStudent.user.email")
    @Mapping(target = "markedByName", expression = "java(attendance.getMarkedBy() != null ? attendance.getMarkedBy().getUser().getFirstName() + \" \" + attendance.getMarkedBy().getUser().getLastName() : null)")
    AttendanceResponseDto toAttendanceDto(Attendance attendance);

    List<AttendanceResponseDto> toAttendanceDtoList(List<Attendance> attendances);

    // Assignment mappings
    @Mapping(target = "batchId", source = "batch.id")
    @Mapping(target = "batchName", source = "batch.name")
    @Mapping(target = "createdByName", expression = "java(assignment.getCreatedBy() != null ? assignment.getCreatedBy().getUser().getFirstName() + \" \" + assignment.getCreatedBy().getUser().getLastName() : null)")
    @Mapping(target = "submissionCount", expression = "java(assignment.getSubmissions().size())")
    @Mapping(target = "gradedCount", expression = "java((int) assignment.getSubmissions().stream().filter(s -> s.getScore() != null).count())")
    AssignmentResponseDto toAssignmentDto(Assignment assignment);

    List<AssignmentResponseDto> toAssignmentDtoList(List<Assignment> assignments);

    // Submission mappings
    @Mapping(target = "assignmentId", source = "assignment.id")
    @Mapping(target = "assignmentTitle", source = "assignment.title")
    @Mapping(target = "batchStudentId", source = "batchStudent.id")
    @Mapping(target = "studentName", expression = "java(submission.getBatchStudent().getUser().getFirstName() + \" \" + submission.getBatchStudent().getUser().getLastName())")
    @Mapping(target = "studentEmail", source = "batchStudent.user.email")
    @Mapping(target = "maxScore", source = "assignment.maxScore")
    @Mapping(target = "gradedByName", expression = "java(submission.getGradedBy() != null ? submission.getGradedBy().getUser().getFirstName() + \" \" + submission.getGradedBy().getUser().getLastName() : null)")
    SubmissionResponseDto toSubmissionDto(AssignmentSubmission submission);

    List<SubmissionResponseDto> toSubmissionDtoList(List<AssignmentSubmission> submissions);
}
