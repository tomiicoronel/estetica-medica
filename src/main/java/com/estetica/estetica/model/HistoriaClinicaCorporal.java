package com.estetica.estetica.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa la <b>Historia Clínica Corporal</b> de un paciente.
 *
 * <p>Se mapea a la tabla {@code historias_clinicas_corporales} en PostgreSQL. Cada paciente
 * tiene como máximo una única historia clínica corporal (relación {@code OneToOne} con
 * {@link Paciente}).</p>
 *
 * <p>La ficha contiene el relevamiento clínico completo previo a cualquier tratamiento
 * corporal: antecedentes patológicos (incluye cáncer, a diferencia de la facial),
 * tóxicos, alérgicos, quirúrgicos, ginecológicos (agrega lactancia), hábitos corporales
 * (alimentación, hidratación, actividad física, ortostatismo, etc.), examen corporal
 * (várices, celulitis, flacidez, estrías, adiposidad localizada) y medidas antropométricas
 * (peso, IMC, perímetro de cintura).</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 * @see Paciente
 * @see com.estetica.estetica.repository.HistoriaClinicaCorporalRepository
 */
@Entity
@Table(name = "historias_clinicas_corporales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinicaCorporal {

    /** Identificador único de la ficha corporal (UUID v4). */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Paciente al que pertenece esta ficha corporal.
     *
     * <p>Relación {@code OneToOne}: un paciente tiene como máximo una ficha corporal.
     * La columna {@code paciente_id} es FK única (impide duplicados a nivel DB).</p>
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    // ============================================================
    // ANTECEDENTES PATOLÓGICOS
    // ============================================================

    @Column(name = "hta") private Boolean hta;
    @Column(name = "dbt") private Boolean dbt;
    @Column(name = "hipotiroidismo") private Boolean hipotiroidismo;
    @Column(name = "hipertiroidismo") private Boolean hipertiroidismo;
    @Column(name = "anemia") private Boolean anemia;
    @Column(name = "enfermedades_autoinmunes") private Boolean enfermedadesAutoinmunes;
    @Column(name = "glaucoma") private Boolean glaucoma;
    @Column(name = "enfermedad_neuromuscular") private Boolean enfermedadNeuromuscular;
    @Column(name = "trastornos_coagulacion") private Boolean trastornosCoagulacion;
    @Column(name = "alteracion_cicatrizacion") private Boolean alteracionCicatrizacion;
    @Column(name = "marcapasos") private Boolean marcapasos;
    @Column(name = "protesis_metalica") private Boolean protesisMetalica;

    /** Antecedente oncológico (exclusivo de la ficha corporal, contraindica muchos tratamientos). */
    @Column(name = "cancer") private Boolean cancer;

    @Column(name = "otro_antecedente_patologico", columnDefinition = "TEXT")
    private String otroAntecedentePatologico;

    // ============================================================
    // ANTECEDENTES TÓXICOS
    // ============================================================

    @Column(name = "tbq") private Boolean tbq;
    @Column(name = "alcohol") private Boolean alcohol;

    @Column(name = "otras_toxico", columnDefinition = "TEXT")
    private String otrasToxico;

    // ============================================================
    // ANTECEDENTES ALÉRGICOS
    // ============================================================

    @Column(name = "alergico_huevo") private Boolean alergicoHuevo;
    @Column(name = "alergico_anestesia") private Boolean alergicoAnestesia;
    @Column(name = "alergico_fish") private Boolean alergicoFish;

    @Column(name = "otras_alergias", columnDefinition = "TEXT")
    private String otrasAlergias;

    // ============================================================
    // ANTECEDENTES QUIRÚRGICOS
    // ============================================================

    @Column(name = "antecedentes_quirurgicos", columnDefinition = "TEXT")
    private String antecedentesQuirurgicos;

    // ============================================================
    // ANTECEDENTES GINECOLÓGICOS
    // ============================================================

    /** Fecha de la última menstruación (texto libre). */
    @Column(name = "fum")
    private String fum;

    /** Embarazo actual (contraindica muchos tratamientos corporales). */
    @Column(name = "embarazo") private Boolean embarazo;

    /** Lactancia actual (contraindicación específica de la ficha corporal). */
    @Column(name = "lactancia") private Boolean lactancia;

    // ============================================================
    // OTROS ANTECEDENTES
    // ============================================================

    @Column(name = "herpes_labial") private Boolean herpesLabial;

    @Column(name = "medicacion_habitual", columnDefinition = "TEXT")
    private String medicacionHabitual;

    @Column(name = "aspirina_semana") private Boolean aspirinaSemana;

    // ============================================================
    // HÁBITOS CORPORALES
    // ============================================================

    /** Lleva una alimentación saludable. */
    @Column(name = "alimentacion_saludable") private Boolean alimentacionSaludable;

    /** Consume suficiente agua diariamente. */
    @Column(name = "bebe_agua") private Boolean bebeAgua;

    /** Nivel de sedentarismo o actividad física (texto libre). */
    @Column(name = "sedentarismo_gimnasia", columnDefinition = "TEXT")
    private String sedentarismoGimnasia;

    /** Permanece muchas horas de pie (afecta circulación de miembros inferiores). */
    @Column(name = "ortostatismo_prolongado") private Boolean ortostatismoProlongado;

    /** Usa medias de compresión. */
    @Column(name = "medias_compresion") private Boolean mediasCompresion;

    /** Realizó tratamientos estéticos corporales previos. */
    @Column(name = "tratamientos_previos") private Boolean tratamientosPrevios;

    @Column(name = "tratamientos_previos_cuales", columnDefinition = "TEXT")
    private String tratamientosPreviosCuales;

    /** Cuándo se realizaron los tratamientos previos (texto libre). */
    @Column(name = "tratamientos_previos_cuando", columnDefinition = "TEXT")
    private String tratamientosPreviosCuando;

    @Column(name = "tratamientos_previos_respuesta", columnDefinition = "TEXT")
    private String tratamientosPreviosRespuesta;

    @Column(name = "viaje_proximo_mes") private Boolean viajeProximoMes;

    // ============================================================
    // EXAMEN CORPORAL
    // ============================================================

    @Column(name = "presencia_otros_materiales", columnDefinition = "TEXT")
    private String presenciaOtrosMateriales;

    @Column(name = "secuelas_tratamientos_previos", columnDefinition = "TEXT")
    private String secuelasTratamientosPrevios;

    /** Arañas vasculares (capilares dilatados). */
    @Column(name = "aranas_vasculares") private Boolean aranasVasculares;

    @Column(name = "telangiectasias") private Boolean telangiectasias;
    @Column(name = "varices") private Boolean varices;
    @Column(name = "celulitis") private Boolean celulitis;
    @Column(name = "flacidez") private Boolean flacidez;
    @Column(name = "estrias") private Boolean estrias;

    /** Descripción de la adiposidad localizada (abdomen, flancos, muslos, etc.). */
    @Column(name = "adiposidad_localizada", columnDefinition = "TEXT")
    private String adiposidadLocalizada;

    // ============================================================
    // MEDIDAS ANTROPOMÉTRICAS
    // ============================================================

    /** Peso actual en kilogramos. */
    @Column(name = "peso_actual", precision = 6, scale = 2)
    private BigDecimal pesoActual;

    /** Peso habitual / histórico en kilogramos. */
    @Column(name = "peso_habitual", precision = 6, scale = 2)
    private BigDecimal pesoHabitual;

    /** Índice de Masa Corporal. */
    @Column(name = "imc", precision = 5, scale = 2)
    private BigDecimal imc;

    /** Perímetro de cintura en centímetros (indicador de riesgo cardiovascular). */
    @Column(name = "perimetro_cintura", precision = 5, scale = 2)
    private BigDecimal perimetroCintura;

    // ============================================================
    // EXAMEN — FOTOGRAFÍA
    // ============================================================

    /** Consentimiento para registro fotográfico. */
    @Column(name = "se_toma_fotografia") private Boolean seTomaFotografia;

    // ============================================================
    // TRATAMIENTO Y SEGUIMIENTO
    // ============================================================

    @Column(name = "diagnostico_y_tratamiento", columnDefinition = "TEXT")
    private String diagnosticoYTratamiento;

    @Column(name = "observaciones_posteriores", columnDefinition = "TEXT")
    private String observacionesPosteriores;

    // ============================================================
    // AUDITORÍA
    // ============================================================

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}

