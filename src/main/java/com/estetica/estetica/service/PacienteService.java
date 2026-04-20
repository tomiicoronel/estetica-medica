package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.PacienteRequest;
import com.estetica.estetica.dto.response.PacienteResponse;
import com.estetica.estetica.model.Paciente;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.repository.PacienteRepository;
import com.estetica.estetica.repository.ProfesionalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Servicio con la lógica de negocio para gestionar pacientes.
 *
 * <p>Valida que la profesional exista antes de crear/listar pacientes y que no se duplique
 * el DNI/CUIT dentro de la misma profesional (modelo multi-tenant aislado).</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-20
 */
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ProfesionalRepository profesionalRepository;

    /**
     * Lista todos los pacientes de una profesional.
     *
     * @param profesionalId UUID de la profesional
     * @return lista de {@link PacienteResponse}
     * @throws EntityNotFoundException si la profesional no existe
     */
    @Transactional(readOnly = true)
    public List<PacienteResponse> listarPorProfesional(UUID profesionalId) {
        if (!profesionalRepository.existsById(profesionalId)) {
            throw new EntityNotFoundException(
                    "No se encontró la profesional con ID: " + profesionalId);
        }

        return pacienteRepository.findByProfesionalId(profesionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Lista solo los pacientes activos de una profesional.
     *
     * @param profesionalId UUID de la profesional
     * @return lista de {@link PacienteResponse} activos
     * @throws EntityNotFoundException si la profesional no existe
     */
    @Transactional(readOnly = true)
    public List<PacienteResponse> listarActivosPorProfesional(UUID profesionalId) {
        if (!profesionalRepository.existsById(profesionalId)) {
            throw new EntityNotFoundException(
                    "No se encontró la profesional con ID: " + profesionalId);
        }

        return pacienteRepository.findByProfesionalIdAndActivo(profesionalId, true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Busca un paciente por su ID.
     *
     * @param id UUID del paciente
     * @return {@link PacienteResponse} con los datos del paciente
     * @throws EntityNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(UUID id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el paciente con ID: " + id));
        return toResponse(paciente);
    }

    /**
     * Crea un nuevo paciente vinculado a una profesional.
     *
     * <p>Validaciones:
     * <ol>
     *     <li>La profesional debe existir.</li>
     *     <li>No debe existir otro paciente con el mismo DNI/CUIT para esa profesional.</li>
     * </ol>
     * </p>
     *
     * @param request datos del paciente a crear
     * @return {@link PacienteResponse} con los datos del paciente creado
     * @throws EntityNotFoundException  si la profesional no existe
     * @throws IllegalArgumentException si el DNI/CUIT ya está registrado para esa profesional
     */
    @Transactional
    public PacienteResponse crear(PacienteRequest request) {
        Profesional profesional = profesionalRepository.findById(request.getProfesionalId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional con ID: " + request.getProfesionalId()));

        if (pacienteRepository.existsByDniCuitAndProfesionalId(
                request.getDniCuit(), request.getProfesionalId())) {
            throw new IllegalArgumentException(
                    "Ya existe un paciente con DNI/CUIT " + request.getDniCuit()
                    + " para esta profesional");
        }

        Paciente paciente = toEntity(request, profesional);
        Paciente guardado = pacienteRepository.save(paciente);
        return toResponse(guardado);
    }

    /**
     * Actualiza los datos de un paciente existente.
     *
     * <p>Si el DNI/CUIT cambió, valida que no esté duplicado para la misma profesional.
     * El paciente siempre mantiene su profesional original (no se permite reasignar).</p>
     *
     * @param id      UUID del paciente a actualizar
     * @param request nuevos datos del paciente
     * @return {@link PacienteResponse} con los datos actualizados
     * @throws EntityNotFoundException  si el paciente no existe
     * @throws IllegalArgumentException si se intenta reasignar a otra profesional o el DNI está duplicado
     */
    @Transactional
    public PacienteResponse actualizar(UUID id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el paciente con ID: " + id));

        // No permitir reasignar a otra profesional
        if (!paciente.getProfesional().getId().equals(request.getProfesionalId())) {
            throw new IllegalArgumentException(
                    "No se puede reasignar un paciente a otra profesional. "
                    + "El paciente pertenece a la profesional con ID: "
                    + paciente.getProfesional().getId());
        }

        // Si cambió el DNI/CUIT, verificar que no esté duplicado
        if (!paciente.getDniCuit().equals(request.getDniCuit())
                && pacienteRepository.existsByDniCuitAndProfesionalId(
                request.getDniCuit(), paciente.getProfesional().getId())) {
            throw new IllegalArgumentException(
                    "Ya existe un paciente con DNI/CUIT " + request.getDniCuit()
                    + " para esta profesional");
        }

        // Actualizar todos los campos
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setDniCuit(request.getDniCuit());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setTelefono(request.getTelefono());
        paciente.setEmail(request.getEmail());
        paciente.setProfesion(request.getProfesion());
        paciente.setDomicilio(request.getDomicilio());
        paciente.setObraSocial(request.getObraSocial());
        paciente.setNumeroObraSocial(request.getNumeroObraSocial());
        paciente.setContactoEmergenciaNombre(request.getContactoEmergenciaNombre());
        paciente.setContactoEmergenciaTelefono(request.getContactoEmergenciaTelefono());
        paciente.setContactoEmergenciaParentesco(request.getContactoEmergenciaParentesco());
        paciente.setEntidadTraslado1(request.getEntidadTraslado1());
        paciente.setEntidadTraslado2(request.getEntidadTraslado2());

        Paciente actualizado = pacienteRepository.saveAndFlush(paciente);
        return toResponse(actualizado);
    }

    /**
     * Activa o desactiva (archiva) un paciente (soft delete).
     *
     * @param id     UUID del paciente
     * @param activo {@code true} para activar, {@code false} para archivar
     * @throws EntityNotFoundException si el paciente no existe
     */
    @Transactional
    public void cambiarEstado(UUID id, Boolean activo) {
        if (!pacienteRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "No se encontró el paciente con ID: " + id);
        }

        int filasAfectadas = pacienteRepository.cambiarEstado(id, activo);
        if (filasAfectadas == 0) {
            throw new EntityNotFoundException(
                    "No se pudo actualizar el estado del paciente con ID: " + id);
        }
    }

    // ---- Métodos de mapeo (privados) ----

    /**
     * Convierte una entidad {@link Paciente} a un DTO {@link PacienteResponse}.
     */
    private PacienteResponse toResponse(Paciente paciente) {
        return PacienteResponse.builder()
                .id(paciente.getId())
                .profesionalId(paciente.getProfesional().getId())
                .nombre(paciente.getNombre())
                .apellido(paciente.getApellido())
                .dniCuit(paciente.getDniCuit())
                .fechaNacimiento(paciente.getFechaNacimiento())
                .telefono(paciente.getTelefono())
                .email(paciente.getEmail())
                .profesion(paciente.getProfesion())
                .domicilio(paciente.getDomicilio())
                .obraSocial(paciente.getObraSocial())
                .numeroObraSocial(paciente.getNumeroObraSocial())
                .contactoEmergenciaNombre(paciente.getContactoEmergenciaNombre())
                .contactoEmergenciaTelefono(paciente.getContactoEmergenciaTelefono())
                .contactoEmergenciaParentesco(paciente.getContactoEmergenciaParentesco())
                .entidadTraslado1(paciente.getEntidadTraslado1())
                .entidadTraslado2(paciente.getEntidadTraslado2())
                .activo(paciente.getActivo())
                .creadoEn(paciente.getCreadoEn())
                .actualizadoEn(paciente.getActualizadoEn())
                .build();
    }

    /**
     * Convierte un DTO {@link PacienteRequest} a una entidad {@link Paciente}.
     */
    private Paciente toEntity(PacienteRequest request, Profesional profesional) {
        return Paciente.builder()
                .profesional(profesional)
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .dniCuit(request.getDniCuit())
                .fechaNacimiento(request.getFechaNacimiento())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .profesion(request.getProfesion())
                .domicilio(request.getDomicilio())
                .obraSocial(request.getObraSocial())
                .numeroObraSocial(request.getNumeroObraSocial())
                .contactoEmergenciaNombre(request.getContactoEmergenciaNombre())
                .contactoEmergenciaTelefono(request.getContactoEmergenciaTelefono())
                .contactoEmergenciaParentesco(request.getContactoEmergenciaParentesco())
                .entidadTraslado1(request.getEntidadTraslado1())
                .entidadTraslado2(request.getEntidadTraslado2())
                .build();
    }
}

