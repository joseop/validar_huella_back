package com.example.validarHuella.controller;


import com.example.validarHuella.entity.Bibliotecario;
import com.example.validarHuella.entity.Login;
import com.example.validarHuella.service.BibliotecarioS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/bibliotecario")
public class BibliotecarioC {
    @Autowired
    private BibliotecarioS bibliotecarioS;
    @PostMapping
    public ResponseEntity<String> save(@RequestBody Bibliotecario bibliotecario) {
        bibliotecarioS.save(bibliotecario);
        return ResponseEntity.ok(bibliotecario.getNombre());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        Bibliotecario bibliotecario = bibliotecarioS.findById(id);
        if (bibliotecario != null) {
            return ResponseEntity.ok(bibliotecario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login log) {
        String usuario = log.getUsuario();
        String contrasena = log.getContrasena();
        String bibliotecario = bibliotecarioS.validarCredenciales(usuario, contrasena);
        if (bibliotecario.equals("1")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Fecha de bibliotecario vencida");
        } else if (bibliotecario.equals("2")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credenciales incorrectas");
        } else {
            return ResponseEntity.ok(bibliotecario);
        }
    }
}
