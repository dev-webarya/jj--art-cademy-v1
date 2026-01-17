package com.artacademy.lms.service.impl;

import com.artacademy.entity.ArtClasses;
import com.artacademy.entity.ClassEnrollment;
import com.artacademy.entity.User;
import com.artacademy.exception.InvalidRequestException;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.lms.dto.request.BatchRequestDto;
import com.artacademy.lms.dto.response.BatchResponseDto;
import com.artacademy.lms.dto.response.BatchStudentResponseDto;
import com.artacademy.lms.entity.Batch;
import com.artacademy.lms.entity.BatchStudent;
import com.artacademy.lms.entity.Instructor;
import com.artacademy.lms.enums.BatchStatus;
import com.artacademy.lms.mapper.LmsMapper;
import com.artacademy.lms.repository.BatchRepository;
import com.artacademy.lms.repository.BatchStudentRepository;
import com.artacademy.lms.repository.InstructorRepository;
import com.artacademy.lms.service.BatchService;
import com.artacademy.repository.ArtClassesRepository;
import com.artacademy.repository.ClassEnrollmentRepository;
import com.artacademy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final BatchStudentRepository batchStudentRepository;
    private final InstructorRepository instructorRepository;
    private final ArtClassesRepository artClassesRepository;
    private final ClassEnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final LmsMapper mapper;

    @Override
    public BatchResponseDto create(BatchRequestDto request) {
        log.info("Creating batch: {}", request.getName());

        ArtClasses artClass = artClassesRepository.findById(request.getArtClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Art class not found"));

        Instructor instructor = null;
        BatchStatus status = BatchStatus.NEEDS_INSTRUCTOR;

        if (request.getInstructorId() != null) {
            instructor = instructorRepository.findById(request.getInstructorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
            status = BatchStatus.ACTIVE;
        }

        Batch batch = Batch.builder()
                .name(request.getName())
                .artClass(artClass)
                .instructor(instructor)
                .schedule(request.getSchedule())
                .status(status)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .maxStudents(request.getMaxStudents())
                .description(request.getDescription())
                .build();

        return mapper.toBatchDto(batchRepository.save(batch));
    }

    @Override
    public BatchResponseDto update(UUID id, BatchRequestDto request) {
        Batch batch = findById(id);
        batch.setName(request.getName());
        batch.setSchedule(request.getSchedule());
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());
        batch.setMaxStudents(request.getMaxStudents());
        batch.setDescription(request.getDescription());

        if (request.getInstructorId() != null) {
            Instructor instructor = instructorRepository.findById(request.getInstructorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
            batch.setInstructor(instructor);
            if (batch.getStatus() == BatchStatus.NEEDS_INSTRUCTOR) {
                batch.setStatus(BatchStatus.ACTIVE);
            }
        }

        return mapper.toBatchDto(batchRepository.save(batch));
    }

    @Override
    @Transactional(readOnly = true)
    public BatchResponseDto getById(UUID id) {
        return mapper.toBatchDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BatchResponseDto> getAll(Pageable pageable) {
        return batchRepository.findAll(pageable).map(mapper::toBatchDto);
    }

    @Override
    public void delete(UUID id) {
        Batch batch = findById(id);
        batchRepository.delete(batch);
        log.info("Batch {} deleted", id);
    }

    @Override
    public BatchResponseDto assignInstructor(UUID batchId, UUID instructorId) {
        log.info("Assigning instructor {} to batch {}", instructorId, batchId);

        Batch batch = findById(batchId);
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        batch.setInstructor(instructor);
        batch.setStatus(BatchStatus.ACTIVE);

        return mapper.toBatchDto(batchRepository.save(batch));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BatchResponseDto> getByInstructor(UUID instructorId, Pageable pageable) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        return batchRepository.findByInstructor(instructor, pageable).map(mapper::toBatchDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BatchResponseDto> getMyBatches(User instructorUser, Pageable pageable) {
        Instructor instructor = instructorRepository.findByUser(instructorUser)
                .orElseThrow(() -> new ResourceNotFoundException("You are not an instructor"));
        return batchRepository.findByInstructor(instructor, pageable).map(mapper::toBatchDto);
    }

    @Override
    public BatchStudentResponseDto addStudent(UUID batchId, UUID userId, UUID enrollmentId) {
        log.info("Adding student {} to batch {}", userId, batchId);

        Batch batch = findById(batchId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (batchStudentRepository.existsByBatchAndUser(batch, user)) {
            throw new InvalidRequestException("Student already in this batch");
        }

        if (batch.getStudents().size() >= batch.getMaxStudents()) {
            throw new InvalidRequestException("Batch is full");
        }

        ClassEnrollment enrollment = null;
        if (enrollmentId != null) {
            enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        }

        BatchStudent student = BatchStudent.builder()
                .batch(batch)
                .user(user)
                .enrollment(enrollment)
                .build();

        return mapper.toBatchStudentDto(batchStudentRepository.save(student));
    }

    @Override
    public void removeStudent(UUID batchId, UUID userId) {
        Batch batch = findById(batchId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BatchStudent student = batchStudentRepository.findByBatchAndUser(batch, user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not in batch"));

        batchStudentRepository.delete(student);
        log.info("Removed student {} from batch {}", userId, batchId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BatchStudentResponseDto> getStudents(UUID batchId) {
        Batch batch = findById(batchId);
        return mapper.toBatchStudentDtoList(batchStudentRepository.findByBatch(batch));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BatchResponseDto> getStudentBatches(User user, Pageable pageable) {
        return batchStudentRepository.findByUser(user, pageable)
                .map(bs -> mapper.toBatchDto(bs.getBatch()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BatchResponseDto> getBatchesNeedingInstructor(Pageable pageable) {
        return batchRepository.findByStatus(BatchStatus.NEEDS_INSTRUCTOR, pageable)
                .map(mapper::toBatchDto);
    }

    private Batch findById(UUID id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));
    }
}
