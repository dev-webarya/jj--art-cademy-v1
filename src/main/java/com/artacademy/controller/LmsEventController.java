package com.artacademy.controller;

import com.artacademy.dto.request.LmsEventRequestDto;
import com.artacademy.dto.response.LmsEventResponseDto;
import com.artacademy.enums.EventType;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.service.LmsEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for event management.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/v1/lms/events")
@RequiredArgsConstructor
@Tag(name = "LMS - Events", description = "Event management (Admin only)")
@AdminOnly
public class LmsEventController {

    private final LmsEventService eventService;

    @PostMapping
    @Operation(summary = "Create a new event")
    public ResponseEntity<LmsEventResponseDto> create(@Valid @RequestBody LmsEventRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<LmsEventResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an event")
    public ResponseEntity<LmsEventResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody LmsEventRequestDto request) {
        return ResponseEntity.ok(eventService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all events (paginated)")
    public ResponseEntity<Page<LmsEventResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(eventService.getAll(pageable));
    }

    @GetMapping("/type/{eventType}")
    @Operation(summary = "Get events by type")
    public ResponseEntity<Page<LmsEventResponseDto>> getByType(
            @PathVariable EventType eventType, Pageable pageable) {
        return ResponseEntity.ok(eventService.getByType(eventType, pageable));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming events")
    public ResponseEntity<Page<LmsEventResponseDto>> getUpcoming(Pageable pageable) {
        return ResponseEntity.ok(eventService.getUpcoming(pageable));
    }

    @GetMapping("/public")
    @Operation(summary = "Get public events")
    public ResponseEntity<Page<LmsEventResponseDto>> getPublicEvents(Pageable pageable) {
        return ResponseEntity.ok(eventService.getPublicEvents(pageable));
    }
}
