package com.estetica.estetica.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * DTO de entrada para crear o actualizar una {@code HistoriaClinicaFacial}.
 *
 * <p>Todos los campos son <b>opcionales</b>: la ficha se puede completar progresivamente
 * a medida que la profesional recopila la información del paciente. No se incluye
 * {@code id}, {@code pacienteId} ni fechas de auditoría (los maneja el sistema o se
 * envían por path variable).</p>
 *
 * <p>Las únicas validaciones presentes son rangos de los campos numéricos de clasificación
 * ({@code fototipoFitzpatrick} 1-6, {@code gradoGlogau} 1-4).</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 * @see com.estetica.estetica.dto.response.HistoriaClinicaFacialResponse
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinicaFacialRequest {

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

    /** Fototipo de Fitzpatrick. Valores válidos: 1 a 6. */
    @Min(value = 1, message = "El fototipo de Fitzpatrick debe estar entre 1 y 6")
    @Max(value = 6, message = "El fototipo de Fitzpatrick debe estar entre 1 y 6")
    private Integer fototipoFitzpatrick;

    /** Grado de Glogau. Valores válidos: 1 a 4. */
    @Min(value = 1, message = "El grado de Glogau debe estar entre 1 y 4")
    @Max(value = 4, message = "El grado de Glogau debe estar entre 1 y 4")
    private Integer gradoGlogau;

    // ---- Tratamiento y seguimiento ----
    private String diagnosticoYTratamiento;
    private String observacionesPosteriores;
}

