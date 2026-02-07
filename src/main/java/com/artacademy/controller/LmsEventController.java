package com.artacademy.controller;

import com.artacademy.dto.request.LmsEventRequestDto;
import com.artacademy.dto.response.LmsEventResponseDto;
import com.artacademy.enums.EventType;
import com.artacademy.security.annotations.AdminOnly;
import com.artacademy.security.annotations.PublicEndpoint;
import com.artacademy.service.LmsEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for event management.
 * Public events accessible to all, CRUD for admins.
 */
@RestController
@RequestMapping("/api/v1/lms/events")
@RequiredArgsConstructor
@Tag(name = "LMS - Events", description = "Event management")
@SecurityRequirement(name = "bearerAuth")
public class LmsEventController {

    private final LmsEventService eventService;

    // ==================== PUBLIC ENDPOINTS ====================

    @GetMapping("/public")
    @PublicEndpoint
    @Operation(summary = "Get public events")
    public ResponseEntity<Page<LmsEventResponseDto>> getPublicEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getPublicEvents(pageable));
    }

    @GetMapping("/upcoming")
    @PublicEndpoint
    @Operation(summary = "Get upcoming events")
    public ResponseEntity<Page<LmsEventResponseDto>> getUpcoming(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getUpcoming(pageable));
    }

    // ==================== ADMIN ENDPOINTS ====================

    @PostMapping
    @AdminOnly
    @Operation(summary = "Create a new event (Admin)")
    public ResponseEntity<LmsEventResponseDto> create(@Valid @RequestBody LmsEventRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(request));
    }

    @GetMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Get event by ID (Admin)")
    public ResponseEntity<LmsEventResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @PutMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Update an event (Admin)")
    public ResponseEntity<LmsEventResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody LmsEventRequestDto request) {
        return ResponseEntity.ok(eventService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    @Operation(summary = "Delete an event (Admin)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @AdminOnly
    @Operation(summary = "Get all events (Admin)")
    public ResponseEntity<Page<LmsEventResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getAll(pageable));
    }

    @GetMapping("/type/{eventType}")
    @AdminOnly
    @Operation(summary = "Get events by type (Admin)")
    public ResponseEntity<Page<LmsEventResponseDto>> getByType(
            @PathVariable EventType eventType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getByType(eventType, pageable));
    }
}
