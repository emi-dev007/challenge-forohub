package com.alura.forohub.service;

import com.alura.forohub.DTO.ActualizarTopicoDTO;
import com.alura.forohub.DTO.CrearTopicoDTO;
import com.alura.forohub.model.EstadoTopico;
import com.alura.forohub.model.Topico;
import com.alura.forohub.model.Usuario;
import com.alura.forohub.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository repository;

    public Topico crear(CrearTopicoDTO dto, Usuario autor){
        Optional duplicado = repository.findByTituloAndMensaje(
                dto.titulo(), dto.mensaje()
        );
        if (duplicado.isPresent()){
            throw new IllegalArgumentException("Topico duplicado");
        }

        Topico topico = new Topico(dto.titulo(), dto.mensaje(), autor, dto.curso());
        return repository.save(topico);
    }

    public Topico actualizar(Long id, ActualizarTopicoDTO dto, Usuario usuario){
        Topico topico = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topico no encontrado"));

        if (!topico.getAutor().getId().equals(usuario.getId())){
            throw new IllegalArgumentException("Solo el autor puede actualizar");
        }
        if (dto.titulo() != null) topico.setTitulo(dto.titulo());
        if (dto.mensaje() != null) topico.setMensaje(dto.mensaje());

        return repository.save(topico);
    }

    public void eliminar(Long id, Usuario usuario){
        Topico topico = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topico no encontrado"));

        topico.setEstado(EstadoTopico.CERRADO);
        repository.save(topico);
    }

}
