package com.alura.forohub.repository;

import com.alura.forohub.model.Topico;
import org.aspectj.apache.bcel.util.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository <Topico, Long>{
    Optional findByTituloAndMensaje(String titulo, String mensaje);

}
