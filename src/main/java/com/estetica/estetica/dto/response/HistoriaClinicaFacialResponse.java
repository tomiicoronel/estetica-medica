package com.estetica.estetica.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de salida que representa una {@code HistoriaClinicaFacial} en la API.
 *
 * <p>Incluye {@code id}, {@code pacienteId}, todos los campos clínicos y los
 * timestamps de auditoría ({@code creadoEn}, {@code actualizadoEn}).</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 * @see com.estetica.estetica.dto.request.HistoriaClinicaFacialRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinicaFacialResponse {

    private UUID id;
    private UUID pacienteId;

    // ---- Antecedentes patológicos ----
    private Boolean hta;
    private Boolean dbt;
    private Boolean hipotiroidismo;
    private Boolean hipertiroidismo;
    private Boolean anemia;
    private Boolean enfermedadesAutoinmunes;
    private Boolean glaucoma;
    private Boolean enfermedadNeuromuscular;
    private Boolean trastornosCoagulacion;
    private Boolean alteracionCicatrizacion;
    private Boolean marcapasos;
    private Boolean protesisMetalica;
    private String otroAntecedentePatologico;

    // ---- Antecedentes tóxicos ----
    private Boolean tbq;
    private Boolean alcohol;
    private String otrasToxico;

    // ---- Antecedentes alérgicos ----
    private Boolean alergicoHuevo;
    private Boolean alergicoAnestesia;
    private Boolean alergicoFish;
    private String otrasAlergias;

    // ---- Antecedentes quirúrgicos ----
    private String antecedentesQuirurgicos;

    // ---- Antecedentes ginecológicos ----
    private String fum;
    private Boolean embarazo;

    // ---- Otros antecedentes ----
    private Boolean herpesLabial;
    private String medicacionHabitual;
    private Boolean aspirinaSemana;

    // ---- Hábitos ----
    private Boolean exposicionSolar;
    private Boolean usaProteccionSolar;
    private String proteccionSolarCual;
    private String proteccionSolarVecesDia;
    private String habitosHigieneFacial;
    private String tratamientoDomiciliario;
    private Boolean tratamientosPrevios;
    private String tratamientosPreviosCuales;
    private String tratamientosPreviosRespuesta;
    private Boolean viajeProximoMes;

    // ---- Examen ----
    private String presenciaOtrosMateriales;
    private String secuelasTratamientosPrevios;
    private Boolean seTomaFotografia;

    // ---- Clasificaciones ----
    private Integer fototipoFitzpatrick;
    private Integer gradoGlogau;

    // ---- Tratamiento y seguimiento ----
    private String diagnosticoYTratamiento;
    private String observacionesPosteriores;

    // ---- Auditoría ----
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}

