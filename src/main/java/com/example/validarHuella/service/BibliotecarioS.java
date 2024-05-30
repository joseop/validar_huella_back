package com.example.validarHuella.service;


import com.example.validarHuella.entity.Bibliotecario;
import com.example.validarHuella.repository.BibliotecarioR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BibliotecarioS {
    @Autowired
    BibliotecarioR bibliotecarioR;

    public void save(Bibliotecario bibliotecario) {
        bibliotecario.setBibliotecaId(123456789);
        bibliotecarioR.save(bibliotecario);
    }

    public Bibliotecario userBibliotecario(String usuario) {
        List<Bibliotecario> bibliotecarios = bibliotecarioR.findAll();
        for (Bibliotecario bibliotecario : bibliotecarios) {
            if (bibliotecario.getUsuario().equals(usuario)) {
                return bibliotecario;
            }
        }
        return null;
    }

    public boolean isFinalizado(Bibliotecario bibliotecario) {
        return LocalDate.now().isAfter(bibliotecario.getFechaVencimiento().toLocalDate());
    }

    public String validarCredenciales(String usuario, String contrasena) {
        Bibliotecario bibliotecario = userBibliotecario(usuario);
        if (bibliotecario != null && bibliotecario.getContrasena().equals(contrasena) && !isFinalizado(bibliotecario)){
            return bibliotecario.getNombre();
        } else if (bibliotecario != null && isFinalizado(bibliotecario) && bibliotecario.getContrasena().equals(contrasena)) {
            return "1";
        }
        return "2";
    }

    public Bibliotecario findById(int id) {
        return bibliotecarioR.findById((long) id).orElse(null);
    }
}
