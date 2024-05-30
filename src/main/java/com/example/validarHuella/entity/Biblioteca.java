package com.example.validarHuella.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Biblioteca {
    @Id
    private Long id;

    @Column(nullable = false)
    private String sede;

    @Column(nullable = false)
    private String nombre;
}
