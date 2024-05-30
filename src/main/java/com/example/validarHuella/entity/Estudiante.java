package com.example.validarHuella.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
    public class Estudiante {
    @Id
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String universidad;

    @Column(nullable = false)
    private String programa;

    @Column(nullable = false)
    private String categoria;

    @Column(name = "fecha_fin",nullable = false)
    private Date fecha;

    @Column(nullable = false,length = 5000)
    private byte[] huella;

    @Column(name = "biblioteca_id", nullable = false)
    private int bibliotecaId;
    }
