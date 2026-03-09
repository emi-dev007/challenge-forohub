package com.alura.forohub.controller;

import com.alura.forohub.DTO.ActualizarTopicoDTO;
import com.alura.forohub.DTO.CrearTopicoDTO;
import com.alura.forohub.DTO.TopicoResponseDTO;
import com.alura.forohub.model.Topico;
import com.alura.forohub.model.Usuario;
import com.alura.forohub.repository.TopicoRepository;
import com.alura.forohub.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoService service;

    @Autowired
    private TopicoRepository repository;

    @PostMapping
    public ResponseEntity crear(
            @RequestBody @Valid CrearTopicoDTO dto,
            @AuthenticationPrincipal Usuario usuario,
            UriComponentsBuilder uriBuilder
            ){
        Topico topico = service.crear(dto, usuario);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoResponseDTO(topico));
    }

    @GetMapping
    public ResponseEntity<Page> listar(
            @PageableDefault(size = 10, sort = "fechaCreacion")Pageable paginacion){
        var page = repository.findAll(paginacion).map(TopicoResponseDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalle(@PathVariable Long id){
        Topico topico = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topico no encontrado"));
        return ResponseEntity.ok(new TopicoResponseDTO(topico));
    }

    @PutMapping("/{id}")
    public ResponseEntity actualizar(
            @PathVariable Long id,
            @RequestBody @Valid ActualizarTopicoDTO dto,
            @AuthenticationPrincipal Usuario usuario
            ){
        Topico topico = service.actualizar(id, dto, usuario);
        return ResponseEntity.ok(new TopicoResponseDTO(topico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(
            @PathVariable Long id,
            @RequestBody @Valid ActualizarTopicoDTO dto,
            @AuthenticationPrincipal Usuario usuario
    ){
        service.eliminar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
