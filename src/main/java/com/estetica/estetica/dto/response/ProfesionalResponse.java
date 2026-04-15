package com.estetica.estetica.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesionalResponse {

    private UUID id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String especialidad;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}

