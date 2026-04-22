package com.estetica.estetica.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de salida que representa una {@code HistoriaClinicaCorporal} en la API.
 *
 * <p>Incluye {@code id}, {@code pacienteId}, todos los campos clínicos y los
 * timestamps de auditoría ({@code creadoEn}, {@code actualizadoEn}).</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 * @see com.estetica.estetica.dto.request.HistoriaClinicaCorporalRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinicaCorporalResponse {

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
    private Boolean cancer;
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
    private Boolean lactancia;

    // ---- Otros antecedentes ----
    private Boolean herpesLabial;
    private String medicacionHabitual;
    private Boolean aspirinaSemana;

    // ---- Hábitos corporales ----
    private Boolean alimentacionSaludable;
    private Boolean bebeAgua;
    private String sedentarismoGimnasia;
    private Boolean ortostatismoProlongado;
    private Boolean mediasCompresion;
    private Boolean tratamientosPrevios;
    private String tratamientosPreviosCuales;
    private String tratamientosPreviosCuando;
    private String tratamientosPreviosRespuesta;
    private Boolean viajeProximoMes;

    // ---- Examen corporal ----
    private String presenciaOtrosMateriales;
    private String secuelasTratamientosPrevios;
    private Boolean aranasVasculares;
    private Boolean telangiectasias;
    private Boolean varices;
    private Boolean celulitis;
    private Boolean flacidez;
    private Boolean estrias;
    private String adiposidadLocalizada;

    // ---- Medidas antropométricas ----
    private BigDecimal pesoActual;
    private BigDecimal pesoHabitual;
    private BigDecimal imc;
    private BigDecimal perimetroCintura;

    // ---- Fotografía ----
    private Boolean seTomaFotografia;

    // ---- Tratamiento y seguimiento ----
    private String diagnosticoYTratamiento;
    private String observacionesPosteriores;

    // ---- Auditoría ----
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}

