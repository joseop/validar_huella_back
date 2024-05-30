package com.example.validarHuella.repository;

import com.example.validarHuella.entity.Bibliotecario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BibliotecarioR extends JpaRepository<Bibliotecario, Long> {
}
