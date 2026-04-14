package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.ProfesionalRequest;
import com.estetica.estetica.dto.response.ProfesionalResponse;
import com.estetica.estetica.service.ProfesionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profesionales")
@RequiredArgsConstructor
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    @PostMapping
    public ResponseEntity<ProfesionalResponse> crear(@Valid @RequestBody ProfesionalRequest request) {
        ProfesionalResponse response = profesionalService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalResponse> buscarPorId(@PathVariable UUID id) {
        ProfesionalResponse response = profesionalService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProfesionalResponse>> listarTodos() {
        List<ProfesionalResponse> response = profesionalService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalResponse> actualizar(@PathVariable UUID id,
                                                          @Valid @RequestBody ProfesionalRequest request) {
        ProfesionalResponse response = profesionalService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        profesionalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

