package com.estetica.estetica.dto.request;

import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO de entrada para crear o actualizar una {@code HistoriaClinicaCorporal}.
 *
 * <p>Todos los campos son <b>opcionales</b>: la ficha se puede completar progresivamente.
 * No se incluye {@code id}, {@code pacienteId} ni fechas de auditoría (los maneja el sistema
 * o se envían por path variable).</p>
 *
 * <p>Las únicas validaciones presentes son {@code DecimalMin("0.0")} sobre los campos
 * antropométricos ({@code pesoActual}, {@code pesoHabitual}, {@code imc},
 * {@code perimetroCintura}) para impedir valores negativos.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 * @see com.estetica.estetica.dto.response.HistoriaClinicaCorporalResponse
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinicaCorporalRequest {

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

    /** Peso actual en kg. No puede ser negativo. */
    @DecimalMin(value = "0.0", inclusive = true, message = "El peso actual no puede ser negativo")
    private BigDecimal pesoActual;

    /** Peso habitual en kg. No puede ser negativo. */
    @DecimalMin(value = "0.0", inclusive = true, message = "El peso habitual no puede ser negativo")
    private BigDecimal pesoHabitual;

    /** Índice de masa corporal. No puede ser negativo. */
    @DecimalMin(value = "0.0", inclusive = true, message = "El IMC no puede ser negativo")
    private BigDecimal imc;

    /** Perímetro de cintura en cm. No puede ser negativo. */
    @DecimalMin(value = "0.0", inclusive = true, message = "El perímetro de cintura no puede ser negativo")
    private BigDecimal perimetroCintura;

    // ---- Fotografía ----
    private Boolean seTomaFotografia;

    // ---- Tratamiento y seguimiento ----
    private String diagnosticoYTratamiento;
    private String observacionesPosteriores;
}

