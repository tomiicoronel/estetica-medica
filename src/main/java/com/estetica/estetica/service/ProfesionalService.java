package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.ProfesionalRequest;
import com.estetica.estetica.dto.response.ProfesionalResponse;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.repository.ProfesionalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Servicio que contiene la lógica de negocio para gestionar profesionales.
 *
 * <p>Actúa como intermediario entre el controller (capa web) y el repository (capa de datos).
 * Aquí se aplican las reglas de negocio como la validación de email duplicado
 * y se realiza el mapeo entre DTOs y entidades.</p>
 *
 * <h3>Anotaciones utilizadas:</h3>
 * <ul>
 *     <li>{@code @Service} — Marca la clase como un bean de servicio de Spring. Se detecta
 *         automáticamente por el component scan.</li>
 *     <li>{@code @RequiredArgsConstructor} (Lombok) — Genera un constructor con todos los campos
 *         {@code final}, permitiendo la inyección de dependencias por constructor
 *         (la forma recomendada por Spring).</li>
 *     <li>{@code @Transactional} — Envuelve cada método en una transacción de base de datos.
 *         Si ocurre una excepción, se hace rollback automáticamente.</li>
 * </ul>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-14
 * @see ProfesionalRepository
 * @see com.estetica.estetica.controller.ProfesionalController
 */
@Service
@RequiredArgsConstructor
public class ProfesionalService {

    /** Repositorio inyectado por constructor para acceder a la tabla de profesionales. */
    private final ProfesionalRepository profesionalRepository;

    /**
     * Lista todas las profesionales registradas en el sistema.
     *
     * <p>{@code @Transactional(readOnly = true)} optimiza la conexión a la base de datos
     * indicando que esta operación solo lee datos y no modifica nada.</p>
     *
     * @return lista de {@link ProfesionalResponse} con todas las profesionales
     */
    @Transactional(readOnly = true)
    public List<ProfesionalResponse> listarTodos() {
        return profesionalRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Busca una profesional por su identificador UUID.
     *
     * @param id el UUID de la profesional a buscar
     * @return {@link ProfesionalResponse} con los datos de la profesional encontrada
     * @throws EntityNotFoundException si no existe una profesional con el ID proporcionado
     */
    @Transactional(readOnly = true)
    public ProfesionalResponse buscarPorId(UUID id) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional con ID: " + id));
        return toResponse(profesional);
    }

    /**
     * Crea una nueva profesional en el sistema.
     *
     * <p>Antes de guardar, verifica que no exista otra profesional con el mismo email.
     * Si ya existe, lanza {@link IllegalArgumentException}.</p>
     *
     * @param request los datos de la profesional a crear
     * @return {@link ProfesionalResponse} con los datos de la profesional creada (incluyendo el ID generado)
     * @throws IllegalArgumentException si ya existe una profesional con el mismo email
     */
    @Transactional
    public ProfesionalResponse crear(ProfesionalRequest request) {
        if (profesionalRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe una profesional con el email: " + request.getEmail());
        }

        Profesional profesional = toEntity(request);
        Profesional guardado = profesionalRepository.save(profesional);
        return toResponse(guardado);
    }

    /**
     * Actualiza los datos de una profesional existente.
     *
     * <p>Busca la profesional por ID, verifica que el nuevo email no esté en uso
     * por otra profesional (si cambió), actualiza todos los campos y persiste los cambios.
     * Se usa {@code saveAndFlush()} para forzar la escritura inmediata en la base de datos,
     * garantizando que {@code @PreUpdate} se ejecute y {@code actualizadoEn} se actualice.</p>
     *
     * @param id      el UUID de la profesional a actualizar
     * @param request los nuevos datos de la profesional
     * @return {@link ProfesionalResponse} con los datos actualizados
     * @throws EntityNotFoundException  si no existe una profesional con el ID proporcionado
     * @throws IllegalArgumentException si el nuevo email ya está en uso por otra profesional
     */
    @Transactional
    public ProfesionalResponse actualizar(UUID id, ProfesionalRequest request) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional con ID: " + id));

        // Si cambió el email, verificar que no esté en uso por otra profesional
        if (!profesional.getEmail().equals(request.getEmail())
                && profesionalRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe una profesional con el email: " + request.getEmail());
        }

        profesional.setNombre(request.getNombre());
        profesional.setApellido(request.getApellido());
        profesional.setEmail(request.getEmail());
        profesional.setTelefono(request.getTelefono());
        profesional.setEspecialidad(request.getEspecialidad());

        Profesional actualizado = profesionalRepository.saveAndFlush(profesional);
        return toResponse(actualizado);
    }

    /**
     * Elimina una profesional del sistema por su ID.
     *
     * @param id el UUID de la profesional a eliminar
     * @throws EntityNotFoundException si no existe una profesional con el ID proporcionado
     */
    @Transactional
    public void eliminar(UUID id) {
        if (!profesionalRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "No se encontró la profesional con ID: " + id);
        }
        profesionalRepository.deleteById(id);
    }

    // ---- Métodos de mapeo (privados) ----

    /**
     * Convierte una entidad {@link Profesional} a un DTO {@link ProfesionalResponse}.
     *
     * <p>Se usa para transformar los datos de la base de datos al formato
     * que se devuelve al cliente en las respuestas HTTP.</p>
     *
     * @param profesional la entidad JPA a convertir
     * @return el DTO de respuesta con los datos mapeados
     */
    private ProfesionalResponse toResponse(Profesional profesional) {
        return ProfesionalResponse.builder()
                .id(profesional.getId())
                .nombre(profesional.getNombre())
                .apellido(profesional.getApellido())
                .email(profesional.getEmail())
                .telefono(profesional.getTelefono())
                .especialidad(profesional.getEspecialidad())
                .creadoEn(profesional.getCreadoEn())
                .actualizadoEn(profesional.getActualizadoEn())
                .build();
    }

    /**
     * Convierte un DTO {@link ProfesionalRequest} a una entidad {@link Profesional}.
     *
     * <p>Se usa al crear una nueva profesional. No setea {@code id} ni fechas
     * porque esos campos los genera el sistema automáticamente.</p>
     *
     * @param request el DTO de entrada con los datos del cliente
     * @return la entidad JPA lista para persistir
     */
    private Profesional toEntity(ProfesionalRequest request) {
        return Profesional.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .especialidad(request.getEspecialidad())
                .build();
    }
}
