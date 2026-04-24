package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.TurnoRequest;
import com.estetica.estetica.dto.response.TurnoResponse;
import com.estetica.estetica.model.*;
import com.estetica.estetica.repository.PacienteRepository;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.estetica.estetica.repository.ServicioRepository;
import com.estetica.estetica.repository.TurnoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio que contiene la lógica de negocio de {@link Turno}.
 *
 * <p>Responsabilidades:</p>
 * <ul>
 *     <li>Crear turnos validando que profesional, paciente y servicios existan y estén
 *         bajo la misma profesional (aislamiento multi-tenant).</li>
 *     <li>Congelar el precio de cada servicio en la entidad intermedia {@link TurnoServicio}.</li>
 *     <li>Calcular el {@code montoTotal} sumando los precios congelados.</li>
 *     <li>Gestionar transiciones de estado según la máquina de estados definida.</li>
 *     <li>Listar turnos por profesional, paciente, estado y rango de fechas.</li>
 * </ul>
 *
 * <h3>Transiciones de estado permitidas:</h3>
 * <ul>
 *     <li>{@code PENDIENTE → CONFIRMADO | CANCELADO}</li>
 *     <li>{@code CONFIRMADO → REALIZADO | CANCELADO}</li>
 *     <li>{@code REALIZADO}, {@code CANCELADO}: finales, no se pueden cambiar.</li>
 * </ul>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-24
 */
@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final ProfesionalRepository profesionalRepository;
    private final PacienteRepository pacienteRepository;
    private final ServicioRepository servicioRepository;

    // ============================================================
    // CREAR
    // ============================================================

    /**
     * Crea un nuevo turno vinculado a una profesional y un paciente con uno o más servicios.
     *
     * <p>Validaciones:</p>
     * <ul>
     *     <li>La profesional debe existir.</li>
     *     <li>El paciente debe existir y pertenecer a esa profesional.</li>
     *     <li>Todos los servicios deben existir, pertenecer a esa profesional y estar activos.</li>
     *     <li>La fecha no puede ser en el pasado (reforzado por {@code @Future} del DTO).</li>
     *     <li>TODO: validar que la fecha no caiga en un rango bloqueado de agenda
     *         (pendiente de implementar la entidad BloqueoAgenda).</li>
     * </ul>
     *
     * @param profesionalId UUID de la profesional dueña del turno
     * @param request       datos del turno a crear
     * @return {@link TurnoResponse} del turno creado con sus servicios y monto total
     */
    @Transactional
    public TurnoResponse crear(UUID profesionalId, TurnoRequest request) {
        // 1. Validar profesional
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional con ID: " + profesionalId));

        // 2. Validar paciente y que pertenezca a la profesional
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el paciente con ID: " + request.getPacienteId()));

        if (!paciente.getProfesional().getId().equals(profesionalId)) {
            throw new IllegalArgumentException(
                    "El paciente no pertenece a la profesional con ID: " + profesionalId);
        }

        // 3. Validar fecha no pasada (defensa en profundidad; el DTO también lo valida)
        if (request.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del turno no puede ser en el pasado");
        }

        // 4. Validar servicios: existencia, pertenencia y que estén activos
        List<UUID> servicioIds = request.getServicioIds();
        List<Servicio> servicios = servicioRepository.findAllById(servicioIds);

        if (servicios.size() != Set.copyOf(servicioIds).size()) {
            throw new EntityNotFoundException(
                    "Uno o más servicios no existen. IDs solicitados: " + servicioIds);
        }

        for (Servicio s : servicios) {
            if (!s.getProfesional().getId().equals(profesionalId)) {
                throw new IllegalArgumentException(
                        "El servicio " + s.getId() + " no pertenece a la profesional con ID: " + profesionalId);
            }
            if (Boolean.FALSE.equals(s.getActivo())) {
                throw new IllegalArgumentException(
                        "El servicio '" + s.getNombre() + "' está inactivo y no puede agendarse");
            }
        }

        // TODO: validar que la fechaHora no caiga en un rango de BloqueoAgenda
        // cuando se implemente esa entidad (sprint posterior).

        // 5. Construir el turno (sin TurnoServicio todavía, necesitamos el ID)
        Turno turno = Turno.builder()
                .profesional(profesional)
                .paciente(paciente)
                .fechaHora(request.getFechaHora())
                .estado(Turno.ESTADO_PENDIENTE)
                .observaciones(request.getObservaciones())
                .montoTotal(BigDecimal.ZERO)
                .turnoServicios(new ArrayList<>())
                .build();

        // 6. Crear los TurnoServicio con el precio congelado y calcular monto total
        BigDecimal montoTotal = BigDecimal.ZERO;
        for (Servicio s : servicios) {
            TurnoServicio ts = TurnoServicio.builder()
                    .turno(turno)
                    .servicio(s)
                    .precioMomento(s.getPrecio())
                    .build();
            turno.getTurnoServicios().add(ts);
            montoTotal = montoTotal.add(s.getPrecio());
        }
        turno.setMontoTotal(montoTotal);

        Turno guardado = turnoRepository.save(turno);
        return toResponse(guardado);
    }

    // ============================================================
    // BUSCAR / LISTAR
    // ============================================================

    @Transactional(readOnly = true)
    public TurnoResponse buscarPorId(UUID id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el turno con ID: " + id));
        return toResponse(turno);
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarPorProfesional(UUID profesionalId) {
        validarProfesionalExiste(profesionalId);
        return turnoRepository.findByProfesionalId(profesionalId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarPorProfesionalYEstado(UUID profesionalId, String estado) {
        validarProfesionalExiste(profesionalId);
        validarEstadoConocido(estado);
        return turnoRepository.findByProfesionalIdAndEstado(profesionalId, estado)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarPorProfesionalYRango(UUID profesionalId, LocalDateTime desde, LocalDateTime hasta) {
        validarProfesionalExiste(profesionalId);
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Las fechas 'desde' y 'hasta' son obligatorias");
        }
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha 'hasta' no puede ser anterior a 'desde'");
        }
        return turnoRepository.findByProfesionalIdAndFechaHoraBetween(profesionalId, desde, hasta)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarPorPaciente(UUID pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new EntityNotFoundException("No se encontró el paciente con ID: " + pacienteId);
        }
        return turnoRepository.findByPacienteId(pacienteId)
                .stream().map(this::toResponse).toList();
    }

    // ============================================================
    // CAMBIO DE ESTADO
    // ============================================================

    /**
     * Cambia el estado del turno validando la transición contra la máquina de estados.
     *
     * @param id          UUID del turno
     * @param nuevoEstado estado destino (PENDIENTE, CONFIRMADO, REALIZADO, CANCELADO)
     * @return {@link TurnoResponse} con el turno actualizado
     * @throws IllegalArgumentException si la transición no está permitida
     */
    @Transactional
    public TurnoResponse cambiarEstado(UUID id, String nuevoEstado) {
        validarEstadoConocido(nuevoEstado);
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el turno con ID: " + id));

        String actual = turno.getEstado();
        if (!esTransicionValida(actual, nuevoEstado)) {
            throw new IllegalArgumentException(
                    "Transición de estado inválida: " + actual + " → " + nuevoEstado);
        }

        turno.setEstado(nuevoEstado);
        Turno actualizado = turnoRepository.saveAndFlush(turno);
        return toResponse(actualizado);
    }

    private boolean esTransicionValida(String actual, String nuevo) {
        if (actual.equals(nuevo)) {
            return false;
        }
        return switch (actual) {
            case Turno.ESTADO_PENDIENTE ->
                    nuevo.equals(Turno.ESTADO_CONFIRMADO) || nuevo.equals(Turno.ESTADO_CANCELADO);
            case Turno.ESTADO_CONFIRMADO ->
                    nuevo.equals(Turno.ESTADO_REALIZADO) || nuevo.equals(Turno.ESTADO_CANCELADO);
            // REALIZADO y CANCELADO son estados finales
            default -> false;
        };
    }

    private void validarEstadoConocido(String estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        if (!estado.equals(Turno.ESTADO_PENDIENTE)
                && !estado.equals(Turno.ESTADO_CONFIRMADO)
                && !estado.equals(Turno.ESTADO_REALIZADO)
                && !estado.equals(Turno.ESTADO_CANCELADO)) {
            throw new IllegalArgumentException(
                    "Estado inválido: " + estado + ". Valores válidos: PENDIENTE, CONFIRMADO, REALIZADO, CANCELADO");
        }
    }

    private void validarProfesionalExiste(UUID profesionalId) {
        if (!profesionalRepository.existsById(profesionalId)) {
            throw new EntityNotFoundException("No se encontró la profesional con ID: " + profesionalId);
        }
    }

    // ============================================================
    // MAPEO
    // ============================================================

    private TurnoResponse toResponse(Turno turno) {
        List<TurnoResponse.TurnoServicioResponse> servicios = turno.getTurnoServicios()
                .stream()
                .map(ts -> TurnoResponse.TurnoServicioResponse.builder()
                        .servicioId(ts.getServicio().getId())
                        .nombre(ts.getServicio().getNombre())
                        .precioMomento(ts.getPrecioMomento())
                        .build())
                .collect(Collectors.toList());

        return TurnoResponse.builder()
                .id(turno.getId())
                .profesionalId(turno.getProfesional().getId())
                .pacienteId(turno.getPaciente().getId())
                .fechaHora(turno.getFechaHora())
                .estado(turno.getEstado())
                .montoTotal(turno.getMontoTotal())
                .observaciones(turno.getObservaciones())
                .servicios(servicios)
                .creadoEn(turno.getCreadoEn())
                .actualizadoEn(turno.getActualizadoEn())
                .build();
    }
}

