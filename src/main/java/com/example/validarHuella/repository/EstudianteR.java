package com.example.validarHuella.repository;

import com.example.validarHuella.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteR extends JpaRepository<Estudiante, Long> {

}
