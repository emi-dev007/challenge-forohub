package com.alura.forohub.DTO;

import jakarta.validation.constraints.NotBlank;

public record CrearTopicoDTO(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotBlank String curso
) {}
