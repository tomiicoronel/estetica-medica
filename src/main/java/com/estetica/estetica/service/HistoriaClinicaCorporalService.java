package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.HistoriaClinicaCorporalRequest;
import com.estetica.estetica.dto.response.HistoriaClinicaCorporalResponse;
import com.estetica.estetica.exception.ResourceAlreadyExistsException;
import com.estetica.estetica.model.HistoriaClinicaCorporal;
import com.estetica.estetica.model.Paciente;
import com.estetica.estetica.repository.HistoriaClinicaCorporalRepository;
import com.estetica.estetica.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Servicio con la lógica de negocio para gestionar la {@code HistoriaClinicaCorporal}.
 *
 * <p>Reglas principales:</p>
 * <ul>
 *     <li>La ficha se crea vinculada a un paciente existente.</li>
 *     <li>Un paciente puede tener, como máximo, <b>una</b> ficha corporal.</li>
 *     <li>Todos los campos clínicos son opcionales y pueden completarse progresivamente.</li>
 * </ul>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-22
 */
@Service
@RequiredArgsConstructor
public class HistoriaClinicaCorporalService {

    private final HistoriaClinicaCorporalRepository historiaRepository;
    private final PacienteRepository pacienteRepository;

    /**
     * Crea una nueva ficha clínica corporal para un paciente.
     *
     * @param pacienteId UUID del paciente dueño de la ficha
     * @param request    datos clínicos a cargar
     * @return {@link HistoriaClinicaCorporalResponse} con la ficha creada
     * @throws EntityNotFoundException        si el paciente no existe
     * @throws ResourceAlreadyExistsException si el paciente ya tiene una ficha corporal
     */
    @Transactional
    public HistoriaClinicaCorporalResponse crearFicha(UUID pacienteId, HistoriaClinicaCorporalRequest request) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el paciente con ID: " + pacienteId));

        if (historiaRepository.existsByPacienteId(pacienteId)) {
            throw new ResourceAlreadyExistsException(
                    "El paciente con ID " + pacienteId + " ya tiene una ficha clínica corporal");
        }

        HistoriaClinicaCorporal ficha = toEntity(new HistoriaClinicaCorporal(), request);
        ficha.setPaciente(paciente);

        HistoriaClinicaCorporal guardada = historiaRepository.save(ficha);
        return toResponse(guardada);
    }

    /**
     * Busca la ficha clínica corporal de un paciente.
     *
     * @param pacienteId UUID del paciente
     * @return {@link HistoriaClinicaCorporalResponse} con la ficha
     * @throws EntityNotFoundException si el paciente no existe o no tiene ficha cargada
     */
    @Transactional(readOnly = true)
    public HistoriaClinicaCorporalResponse buscarPorPaciente(UUID pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new EntityNotFoundException(
                    "No se encontró el paciente con ID: " + pacienteId);
        }

        HistoriaClinicaCorporal ficha = historiaRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El paciente con ID " + pacienteId + " no tiene ficha clínica corporal cargada"));

        return toResponse(ficha);
    }

    /**
     * Actualiza los campos de una ficha clínica corporal existente.
     *
     * <p>La ficha mantiene siempre al mismo paciente (no se permite reasignar).</p>
     *
     * @param id      UUID de la ficha a actualizar
     * @param request nuevos datos clínicos (JSON completo desde el frontend)
     * @return {@link HistoriaClinicaCorporalResponse} actualizada
     * @throws EntityNotFoundException si la ficha no existe
     */
    @Transactional
    public HistoriaClinicaCorporalResponse actualizarFicha(UUID id, HistoriaClinicaCorporalRequest request) {
        HistoriaClinicaCorporal ficha = historiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la ficha clínica corporal con ID: " + id));

        toEntity(ficha, request);

        HistoriaClinicaCorporal actualizada = historiaRepository.saveAndFlush(ficha);
        return toResponse(actualizada);
    }

    // ============================================================
    // MÉTODOS PRIVADOS DE MAPEO
    // ============================================================

    /**
     * Vuelca los valores del {@link HistoriaClinicaCorporalRequest} sobre la entidad recibida
     * (pisa todos los campos, incluso los nulos). Se usa al crear y al actualizar (PUT).
     */
    private HistoriaClinicaCorporal toEntity(HistoriaClinicaCorporal ficha, HistoriaClinicaCorporalRequest r) {
        // Antecedentes patológicos
        ficha.setHta(r.getHta());
        ficha.setDbt(r.getDbt());
        ficha.setHipotiroidismo(r.getHipotiroidismo());
        ficha.setHipertiroidismo(r.getHipertiroidismo());
        ficha.setAnemia(r.getAnemia());
        ficha.setEnfermedadesAutoinmunes(r.getEnfermedadesAutoinmunes());
        ficha.setGlaucoma(r.getGlaucoma());
        ficha.setEnfermedadNeuromuscular(r.getEnfermedadNeuromuscular());
        ficha.setTrastornosCoagulacion(r.getTrastornosCoagulacion());
        ficha.setAlteracionCicatrizacion(r.getAlteracionCicatrizacion());
        ficha.setMarcapasos(r.getMarcapasos());
        ficha.setProtesisMetalica(r.getProtesisMetalica());
        ficha.setCancer(r.getCancer());
        ficha.setOtroAntecedentePatologico(r.getOtroAntecedentePatologico());

        // Tóxicos
        ficha.setTbq(r.getTbq());
        ficha.setAlcohol(r.getAlcohol());
        ficha.setOtrasToxico(r.getOtrasToxico());

        // Alérgicos
        ficha.setAlergicoHuevo(r.getAlergicoHuevo());
        ficha.setAlergicoAnestesia(r.getAlergicoAnestesia());
        ficha.setAlergicoFish(r.getAlergicoFish());
        ficha.setOtrasAlergias(r.getOtrasAlergias());

        // Quirúrgicos
        ficha.setAntecedentesQuirurgicos(r.getAntecedentesQuirurgicos());

        // Ginecológicos
        ficha.setFum(r.getFum());
        ficha.setEmbarazo(r.getEmbarazo());
        ficha.setLactancia(r.getLactancia());

        // Otros
        ficha.setHerpesLabial(r.getHerpesLabial());
        ficha.setMedicacionHabitual(r.getMedicacionHabitual());
        ficha.setAspirinaSemana(r.getAspirinaSemana());

        // Hábitos corporales
        ficha.setAlimentacionSaludable(r.getAlimentacionSaludable());
        ficha.setBebeAgua(r.getBebeAgua());
        ficha.setSedentarismoGimnasia(r.getSedentarismoGimnasia());
        ficha.setOrtostatismoProlongado(r.getOrtostatismoProlongado());
        ficha.setMediasCompresion(r.getMediasCompresion());
        ficha.setTratamientosPrevios(r.getTratamientosPrevios());
        ficha.setTratamientosPreviosCuales(r.getTratamientosPreviosCuales());
        ficha.setTratamientosPreviosCuando(r.getTratamientosPreviosCuando());
        ficha.setTratamientosPreviosRespuesta(r.getTratamientosPreviosRespuesta());
        ficha.setViajeProximoMes(r.getViajeProximoMes());

        // Examen corporal
        ficha.setPresenciaOtrosMateriales(r.getPresenciaOtrosMateriales());
        ficha.setSecuelasTratamientosPrevios(r.getSecuelasTratamientosPrevios());
        ficha.setAranasVasculares(r.getAranasVasculares());
        ficha.setTelangiectasias(r.getTelangiectasias());
        ficha.setVarices(r.getVarices());
        ficha.setCelulitis(r.getCelulitis());
        ficha.setFlacidez(r.getFlacidez());
        ficha.setEstrias(r.getEstrias());
        ficha.setAdiposidadLocalizada(r.getAdiposidadLocalizada());

        // Medidas antropométricas
        ficha.setPesoActual(r.getPesoActual());
        ficha.setPesoHabitual(r.getPesoHabitual());
        ficha.setImc(r.getImc());
        ficha.setPerimetroCintura(r.getPerimetroCintura());

        // Fotografía
        ficha.setSeTomaFotografia(r.getSeTomaFotografia());

        // Tratamiento y seguimiento
        ficha.setDiagnosticoYTratamiento(r.getDiagnosticoYTratamiento());
        ficha.setObservacionesPosteriores(r.getObservacionesPosteriores());

        return ficha;
    }

    /** Convierte la entidad a DTO de respuesta. */
    private HistoriaClinicaCorporalResponse toResponse(HistoriaClinicaCorporal f) {
        return HistoriaClinicaCorporalResponse.builder()
                .id(f.getId())
                .pacienteId(f.getPaciente().getId())
                .hta(f.getHta())
                .dbt(f.getDbt())
                .hipotiroidismo(f.getHipotiroidismo())
                .hipertiroidismo(f.getHipertiroidismo())
                .anemia(f.getAnemia())
                .enfermedadesAutoinmunes(f.getEnfermedadesAutoinmunes())
                .glaucoma(f.getGlaucoma())
                .enfermedadNeuromuscular(f.getEnfermedadNeuromuscular())
                .trastornosCoagulacion(f.getTrastornosCoagulacion())
                .alteracionCicatrizacion(f.getAlteracionCicatrizacion())
                .marcapasos(f.getMarcapasos())
                .protesisMetalica(f.getProtesisMetalica())
                .cancer(f.getCancer())
                .otroAntecedentePatologico(f.getOtroAntecedentePatologico())
                .tbq(f.getTbq())
                .alcohol(f.getAlcohol())
                .otrasToxico(f.getOtrasToxico())
                .alergicoHuevo(f.getAlergicoHuevo())
                .alergicoAnestesia(f.getAlergicoAnestesia())
                .alergicoFish(f.getAlergicoFish())
                .otrasAlergias(f.getOtrasAlergias())
                .antecedentesQuirurgicos(f.getAntecedentesQuirurgicos())
                .fum(f.getFum())
                .embarazo(f.getEmbarazo())
                .lactancia(f.getLactancia())
                .herpesLabial(f.getHerpesLabial())
                .medicacionHabitual(f.getMedicacionHabitual())
                .aspirinaSemana(f.getAspirinaSemana())
                .alimentacionSaludable(f.getAlimentacionSaludable())
                .bebeAgua(f.getBebeAgua())
                .sedentarismoGimnasia(f.getSedentarismoGimnasia())
                .ortostatismoProlongado(f.getOrtostatismoProlongado())
                .mediasCompresion(f.getMediasCompresion())
                .tratamientosPrevios(f.getTratamientosPrevios())
                .tratamientosPreviosCuales(f.getTratamientosPreviosCuales())
                .tratamientosPreviosCuando(f.getTratamientosPreviosCuando())
                .tratamientosPreviosRespuesta(f.getTratamientosPreviosRespuesta())
                .viajeProximoMes(f.getViajeProximoMes())
                .presenciaOtrosMateriales(f.getPresenciaOtrosMateriales())
                .secuelasTratamientosPrevios(f.getSecuelasTratamientosPrevios())
                .aranasVasculares(f.getAranasVasculares())
                .telangiectasias(f.getTelangiectasias())
                .varices(f.getVarices())
                .celulitis(f.getCelulitis())
                .flacidez(f.getFlacidez())
                .estrias(f.getEstrias())
                .adiposidadLocalizada(f.getAdiposidadLocalizada())
                .pesoActual(f.getPesoActual())
                .pesoHabitual(f.getPesoHabitual())
                .imc(f.getImc())
                .perimetroCintura(f.getPerimetroCintura())
                .seTomaFotografia(f.getSeTomaFotografia())
                .diagnosticoYTratamiento(f.getDiagnosticoYTratamiento())
                .observacionesPosteriores(f.getObservacionesPosteriores())
                .creadoEn(f.getCreadoEn())
                .actualizadoEn(f.getActualizadoEn())
                .build();
    }
}

