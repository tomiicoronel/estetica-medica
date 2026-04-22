package com.estetica.estetica.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa la <b>Historia Clínica Facial</b> de un paciente.
 *
 * <p>Se mapea a la tabla {@code historias_clinicas_faciales} en PostgreSQL. Cada paciente
 * tiene como máximo una única historia clínica facial (relación {@code OneToOne} con
 * {@link Paciente}).</p>
 *
 * <p>La ficha contiene el relevamiento clínico completo previo a cualquier tratamiento
 * facial: antecedentes patológicos, tóxicos, alérgicos, quirúrgicos, ginecológicos,
 * hábitos, examen y clasificaciones de piel (Fitzpatrick y Glogau).</p>
 *
 * <h3>Campos de auditoría</h3>
 * <p>Se usa {@link CreationTimestamp} y {@link UpdateTimestamp} de Hibernate para que
 * {@code creadoEn} y {@code actualizadoEn} se llenen automáticamente, sin callbacks manuales.</p>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 * @see Paciente
 * @see com.estetica.estetica.repository.HistoriaClinicaFacialRepository
 */
@Entity
@Table(name = "historias_clinicas_faciales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinicaFacial {

    /** Identificador único de la ficha facial (UUID v4). */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Paciente al que pertenece esta ficha facial.
     *
     * <p>Relación {@code OneToOne}: un paciente tiene como máximo una ficha facial.
     * La columna {@code paciente_id} es FK única (impide que un mismo paciente tenga
     * más de una ficha facial a nivel de base de datos).</p>
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    // ============================================================
    // ANTECEDENTES PATOLÓGICOS
    // ============================================================

    /** Hipertensión arterial. */
    @Column(name = "hta")
    private Boolean hta;

    /** Diabetes. */
    @Column(name = "dbt")
    private Boolean dbt;

    /** Hipotiroidismo. */
    @Column(name = "hipotiroidismo")
    private Boolean hipotiroidismo;

    /** Hipertiroidismo. */
    @Column(name = "hipertiroidismo")
    private Boolean hipertiroidismo;

    /** Anemia. */
    @Column(name = "anemia")
    private Boolean anemia;

    /** Enfermedades autoinmunes (lupus, artritis reumatoide, etc.). */
    @Column(name = "enfermedades_autoinmunes")
    private Boolean enfermedadesAutoinmunes;

    /** Glaucoma. */
    @Column(name = "glaucoma")
    private Boolean glaucoma;

    /** Enfermedad neuromuscular (miastenia gravis, etc.). */
    @Column(name = "enfermedad_neuromuscular")
    private Boolean enfermedadNeuromuscular;

    /** Trastornos de la coagulación. */
    @Column(name = "trastornos_coagulacion")
    private Boolean trastornosCoagulacion;

    /** Alteraciones en la cicatrización (queloides, cicatrices hipertróficas). */
    @Column(name = "alteracion_cicatrizacion")
    private Boolean alteracionCicatrizacion;

    /** Marcapasos. */
    @Column(name = "marcapasos")
    private Boolean marcapasos;

    /** Prótesis metálica. */
    @Column(name = "protesis_metalica")
    private Boolean protesisMetalica;

    /** Otros antecedentes patológicos no contemplados en los flags anteriores. */
    @Column(name = "otro_antecedente_patologico", columnDefinition = "TEXT")
    private String otroAntecedentePatologico;

    // ============================================================
    // ANTECEDENTES TÓXICOS
    // ============================================================

    /** Tabaquismo. */
    @Column(name = "tbq")
    private Boolean tbq;

    /** Consumo habitual de alcohol. */
    @Column(name = "alcohol")
    private Boolean alcohol;

    /** Otras sustancias tóxicas (drogas recreativas, etc.). */
    @Column(name = "otras_toxico", columnDefinition = "TEXT")
    private String otrasToxico;

    // ============================================================
    // ANTECEDENTES ALÉRGICOS
    // ============================================================

    /** Alergia al huevo (relevante para ciertos productos cosméticos). */
    @Column(name = "alergico_huevo")
    private Boolean alergicoHuevo;

    /** Alergia a anestésicos locales. */
    @Column(name = "alergico_anestesia")
    private Boolean alergicoAnestesia;

    /** Alergia al pescado (relevante para productos con colágeno marino). */
    @Column(name = "alergico_fish")
    private Boolean alergicoFish;

    /** Otras alergias relevantes para el tratamiento. */
    @Column(name = "otras_alergias", columnDefinition = "TEXT")
    private String otrasAlergias;

    // ============================================================
    // ANTECEDENTES QUIRÚRGICOS
    // ============================================================

    /** Cirugías previas relevantes (estéticas o no). */
    @Column(name = "antecedentes_quirurgicos", columnDefinition = "TEXT")
    private String antecedentesQuirurgicos;

    // ============================================================
    // ANTECEDENTES GINECOLÓGICOS
    // ============================================================

    /** Fecha de la última menstruación (texto libre para soportar respuestas tipo "irregular"). */
    @Column(name = "fum")
    private String fum;

    /** Embarazo actual (contraindica muchos tratamientos). */
    @Column(name = "embarazo")
    private Boolean embarazo;

    // ============================================================
    // OTROS ANTECEDENTES
    // ============================================================

    /** Antecedentes de herpes labial (puede reactivarse con ciertos tratamientos). */
    @Column(name = "herpes_labial")
    private Boolean herpesLabial;

    /** Medicación habitual del paciente. */
    @Column(name = "medicacion_habitual", columnDefinition = "TEXT")
    private String medicacionHabitual;

    /** Si tomó aspirina en la última semana (relevante para sangrado en tratamientos invasivos). */
    @Column(name = "aspirina_semana")
    private Boolean aspirinaSemana;

    // ============================================================
    // HÁBITOS
    // ============================================================

    /** Exposición solar habitual. */
    @Column(name = "exposicion_solar")
    private Boolean exposicionSolar;

    /** Usa protección solar. */
    @Column(name = "usa_proteccion_solar")
    private Boolean usaProteccionSolar;

    /** Marca/tipo de protector solar que utiliza. */
    @Column(name = "proteccion_solar_cual")
    private String proteccionSolarCual;

    /** Cantidad de veces al día que aplica el protector. */
    @Column(name = "proteccion_solar_veces_dia")
    private String proteccionSolarVecesDia;

    /** Hábitos de higiene facial (rutina actual). */
    @Column(name = "habitos_higiene_facial", columnDefinition = "TEXT")
    private String habitosHigieneFacial;

    /** Tratamiento domiciliario actual. */
    @Column(name = "tratamiento_domiciliario", columnDefinition = "TEXT")
    private String tratamientoDomiciliario;

    /** Realizó tratamientos estéticos faciales previos. */
    @Column(name = "tratamientos_previos")
    private Boolean tratamientosPrevios;

    /** Cuáles tratamientos realizó previamente. */
    @Column(name = "tratamientos_previos_cuales", columnDefinition = "TEXT")
    private String tratamientosPreviosCuales;

    /** Respuesta / resultado de los tratamientos previos. */
    @Column(name = "tratamientos_previos_respuesta", columnDefinition = "TEXT")
    private String tratamientosPreviosRespuesta;

    /** Tiene planificado un viaje el próximo mes (relevante para evitar exposición solar post-tratamiento). */
    @Column(name = "viaje_proximo_mes")
    private Boolean viajeProximoMes;

    // ============================================================
    // EXAMEN
    // ============================================================

    /** Presencia de otros materiales en el rostro (rellenos, hilos, etc.). */
    @Column(name = "presencia_otros_materiales", columnDefinition = "TEXT")
    private String presenciaOtrosMateriales;

    /** Secuelas visibles de tratamientos estéticos previos. */
    @Column(name = "secuelas_tratamientos_previos", columnDefinition = "TEXT")
    private String secuelasTratamientosPrevios;

    /** Consentimiento para registro fotográfico. */
    @Column(name = "se_toma_fotografia")
    private Boolean seTomaFotografia;

    // ============================================================
    // CLASIFICACIONES
    // ============================================================

    /**
     * Fototipo de Fitzpatrick (1 a 6).
     * Clasifica la piel según su respuesta a la radiación UV.
     */
    @Column(name = "fototipo_fitzpatrick")
    private Integer fototipoFitzpatrick;

    /**
     * Grado de fotoenvejecimiento de Glogau (1 a 4).
     * Clasifica la severidad del envejecimiento facial.
     */
    @Column(name = "grado_glogau")
    private Integer gradoGlogau;

    // ============================================================
    // TRATAMIENTO Y SEGUIMIENTO
    // ============================================================

    /** Diagnóstico clínico y plan de tratamiento indicado. */
    @Column(name = "diagnostico_y_tratamiento", columnDefinition = "TEXT")
    private String diagnosticoYTratamiento;

    /** Observaciones posteriores al tratamiento / evolución. */
    @Column(name = "observaciones_posteriores", columnDefinition = "TEXT")
    private String observacionesPosteriores;

    // ============================================================
    // AUDITORÍA
    // ============================================================

    /** Fecha y hora de creación. Seteada automáticamente por Hibernate. */
    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    /** Fecha y hora de última actualización. Seteada automáticamente por Hibernate. */
    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}

