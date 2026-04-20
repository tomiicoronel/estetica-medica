package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.ServicioRequest;
import com.estetica.estetica.dto.response.ServicioResponse;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.model.Servicio;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.estetica.estetica.repository.ServicioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Servicio que contiene la lógica de negocio para gestionar servicios de estética.
 *
 * <p>Actúa como intermediario entre el controller (capa web) y los repositories (capa de datos).
 * Aquí se aplican las reglas de negocio como la validación de nombre duplicado por profesional,
 * la verificación de existencia de la profesional al crear un servicio,
 * y se realiza el mapeo entre DTOs y entidades.</p>
 *
 * <p>A diferencia de {@link ProfesionalService}, esta clase inyecta <b>dos repositorios</b>:
 * {@link ServicioRepository} para los servicios y {@link ProfesionalRepository} para buscar
 * la profesional al crear o actualizar un servicio.</p>
 *
 * <h3>Anotaciones utilizadas:</h3>
 * <ul>
 *     <li>{@code @Service} — Marca la clase como un bean de servicio de Spring.</li>
 *     <li>{@code @RequiredArgsConstructor} (Lombok) — Inyección de dependencias por constructor.</li>
 *     <li>{@code @Transactional} — Cada método se ejecuta dentro de una transacción de base de datos.</li>
 * </ul>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-15
 * @see ServicioRepository
 * @see ProfesionalRepository
 * @see com.estetica.estetica.controller.ServicioController
 */
@Service
@RequiredArgsConstructor
public class ServicioService {

    /** Repositorio para acceder a la tabla de servicios. */
    private final ServicioRepository servicioRepository;

    /** Repositorio para acceder a la tabla de profesionales (necesario para vincular servicios). */
    private final ProfesionalRepository profesionalRepository;

    /**
     * Lista todos los servicios de una profesional específica.
     *
     * <p>Devuelve todos los servicios (activos e inactivos) de la profesional.
     * Si se necesitan solo los activos, usar {@link #listarActivosPorProfesional(UUID)}.</p>
     *
     * @param profesionalId el UUID de la profesional cuyos servicios se quieren listar
     * @return lista de {@link ServicioResponse} con los servicios de la profesional
     * @throws EntityNotFoundException si no existe una profesional con el ID proporcionado
     */
    @Transactional(readOnly = true)
    public List<ServicioResponse> listarPorProfesional(UUID profesionalId) {
        if (!profesionalRepository.existsById(profesionalId)) {
            throw new EntityNotFoundException(
                    "No se encontró la profesional con ID: " + profesionalId);
        }

        return servicioRepository.findByProfesionalId(profesionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Lista solo los servicios activos de una profesional específica.
     *
     * <p>Útil para mostrar al cliente final los servicios disponibles para agendar,
     * excluyendo los que fueron desactivados (soft delete).</p>
     *
     * @param profesionalId el UUID de la profesional
     * @return lista de {@link ServicioResponse} con los servicios activos
     * @throws EntityNotFoundException si no existe una profesional con el ID proporcionado
     */
    @Transactional(readOnly = true)
    public List<ServicioResponse> listarActivosPorProfesional(UUID profesionalId) {
        if (!profesionalRepository.existsById(profesionalId)) {
            throw new EntityNotFoundException(
                    "No se encontró la profesional con ID: " + profesionalId);
        }

        return servicioRepository.findByProfesionalIdAndActivo(profesionalId, true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Busca un servicio por su identificador UUID.
     *
     * @param id el UUID del servicio a buscar
     * @return {@link ServicioResponse} con los datos del servicio encontrado
     * @throws EntityNotFoundException si no existe un servicio con el ID proporcionado
     */
    @Transactional(readOnly = true)
    public ServicioResponse buscarPorId(UUID id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el servicio con ID: " + id));
        return toResponse(servicio);
    }

    /**
     * Crea un nuevo servicio vinculado a una profesional existente.
     *
     * <p>Antes de guardar:
     * <ol>
     *     <li>Verifica que la profesional exista en la base de datos.</li>
     *     <li>Verifica que la profesional no tenga otro servicio con el mismo nombre.</li>
     * </ol>
     * </p>
     *
     * @param request los datos del servicio a crear (incluye {@code profesionalId})
     * @return {@link ServicioResponse} con los datos del servicio creado (incluyendo el ID generado)
     * @throws EntityNotFoundException  si no existe la profesional con el ID proporcionado
     * @throws IllegalArgumentException si la profesional ya tiene un servicio con el mismo nombre
     */
    @Transactional
    public ServicioResponse crear(ServicioRequest request) {
        Profesional profesional = profesionalRepository.findById(request.getProfesionalId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional con ID: " + request.getProfesionalId()));

        if (servicioRepository.existsByNombreAndProfesionalId(
                request.getNombre(), request.getProfesionalId())) {
            throw new IllegalArgumentException(
                    "La profesional ya tiene un servicio con el nombre: " + request.getNombre());
        }

        Servicio servicio = toEntity(request, profesional);
        Servicio guardado = servicioRepository.save(servicio);
        return toResponse(guardado);
    }

    /**
     * Actualiza los datos de un servicio existente.
     *
     * <p>Busca el servicio por ID, verifica que el nuevo nombre no esté duplicado
     * para la misma profesional (si cambió), actualiza los campos y persiste los cambios.
     * Se usa {@code saveAndFlush()} para forzar la escritura inmediata y que
     * {@code @PreUpdate} actualice {@code actualizadoEn}.</p>
     *
     * <p>El servicio siempre mantiene su profesional original. Si el {@code profesionalId}
     * del request no coincide con el de la profesional dueña del servicio, se lanza una excepción.
     * Esto garantiza el aislamiento por tenant: un servicio de María no puede pasarse a Laura.</p>
     *
     * @param id      el UUID del servicio a actualizar
     * @param request los nuevos datos del servicio
     * @return {@link ServicioResponse} con los datos actualizados
     * @throws EntityNotFoundException  si no existe el servicio
     * @throws IllegalArgumentException si se intenta reasignar el servicio a otra profesional
     *                                  o si el nuevo nombre ya existe para esa profesional
     */
    @Transactional
    public ServicioResponse actualizar(UUID id, ServicioRequest request) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el servicio con ID: " + id));

        // Validar que no se intente reasignar el servicio a otra profesional
        if (!servicio.getProfesional().getId().equals(request.getProfesionalId())) {
            throw new IllegalArgumentException(
                    "No se puede reasignar un servicio a otra profesional. "
                    + "El servicio pertenece a la profesional con ID: "
                    + servicio.getProfesional().getId());
        }

        // Si cambió el nombre, verificar que no esté duplicado para esa profesional
        if (!servicio.getNombre().equals(request.getNombre())
                && servicioRepository.existsByNombreAndProfesionalId(
                request.getNombre(), servicio.getProfesional().getId())) {
            throw new IllegalArgumentException(
                    "La profesional ya tiene un servicio con el nombre: " + request.getNombre());
        }

        servicio.setNombre(request.getNombre());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setPrecio(request.getPrecio());

        Servicio actualizado = servicioRepository.saveAndFlush(servicio);
        return toResponse(actualizado);
    }

    /**
     * Activa o desactiva un servicio (soft delete).
     *
     * <p>En lugar de eliminar el servicio de la base de datos, cambia su estado {@code activo}.
     * Los servicios desactivados no aparecen como disponibles para agendar pero se mantienen
     * para el historial.</p>
     *
     * @param id     el UUID del servicio a activar/desactivar
     * @param activo {@code true} para activar, {@code false} para desactivar
     * @throws EntityNotFoundException si no existe un servicio con el ID proporcionado
     */
    @Transactional
    public void cambiarEstado(UUID id, Boolean activo) {
        if (!servicioRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "No se encontró el servicio con ID: " + id);
        }

        int filasAfectadas = servicioRepository.cambiarEstado(id, activo);
        if (filasAfectadas == 0) {
            throw new EntityNotFoundException(
                    "No se pudo actualizar el estado del servicio con ID: " + id);
        }
    }

    /**
     * Actualiza únicamente el precio de un servicio.
     *
     * <p>Usa la query custom {@code actualizarPrecio} del repository que modifica
     * solo el campo {@code precio} y {@code actualizadoEn} directamente en la base de datos,
     * sin necesidad de cargar la entidad completa.</p>
     *
     * @param id          el UUID del servicio cuyo precio se quiere actualizar
     * @param nuevoPrecio el nuevo precio a establecer (debe ser mayor a 0)
     * @throws EntityNotFoundException  si no existe un servicio con el ID proporcionado
     * @throws IllegalArgumentException si el precio es nulo o menor/igual a 0
     */
    @Transactional
    public void actualizarPrecio(UUID id, java.math.BigDecimal nuevoPrecio) {
        if (!servicioRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "No se encontró el servicio con ID: " + id);
        }

        if (nuevoPrecio == null || nuevoPrecio.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        int filasAfectadas = servicioRepository.actualizarPrecio(id, nuevoPrecio);
        if (filasAfectadas == 0) {
            throw new EntityNotFoundException(
                    "No se pudo actualizar el precio del servicio con ID: " + id);
        }
    }

    // ---- Métodos de mapeo (privados) ----

    /**
     * Convierte una entidad {@link Servicio} a un DTO {@link ServicioResponse}.
     *
     * <p>Mapea el {@code profesionalId} desde la relación {@code ManyToOne} para evitar
     * exponer el objeto {@link Profesional} completo en la respuesta JSON.</p>
     *
     * @param servicio la entidad JPA a convertir
     * @return el DTO de respuesta con los datos mapeados
     */
    private ServicioResponse toResponse(Servicio servicio) {
        return ServicioResponse.builder()
                .id(servicio.getId())
                .profesionalId(servicio.getProfesional().getId())
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .precio(servicio.getPrecio())
                .activo(servicio.getActivo())
                .creadoEn(servicio.getCreadoEn())
                .actualizadoEn(servicio.getActualizadoEn())
                .build();
    }

    /**
     * Convierte un DTO {@link ServicioRequest} a una entidad {@link Servicio}.
     *
     * <p>Recibe la entidad {@link Profesional} ya buscada para asignarla directamente.
     * No setea {@code id}, {@code activo} ni fechas porque esos campos los genera
     * el sistema automáticamente ({@code activo} tiene default {@code true} por {@code @Builder.Default}).</p>
     *
     * @param request     el DTO de entrada con los datos del cliente
     * @param profesional la profesional a la que se vincula el servicio
     * @return la entidad JPA lista para persistir
     */
    private Servicio toEntity(ServicioRequest request, Profesional profesional) {
        return Servicio.builder()
                .profesional(profesional)
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .build();
    }
}

