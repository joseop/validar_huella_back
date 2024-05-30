package com.example.validarHuella.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
public class Bibliotecario {
    @Id
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String usuario;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false)
    private String vinculo;

    @Column(nullable = false)
    private Date fechaVencimiento;

    @Column(nullable = false)
    private String correo;

    @Column(name = "biblioteca_id", nullable = false)
    private long bibliotecaId;
}
