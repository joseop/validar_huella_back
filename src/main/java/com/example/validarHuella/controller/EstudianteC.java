package com.example.validarHuella.controller;

import com.example.validarHuella.entity.Estudiante;
import com.example.validarHuella.service.EstudianteS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/estudiante")
public class EstudianteC {
    @Autowired
    private EstudianteS estudianteS;
    @PostMapping("/externo")
    public ResponseEntity<String> save(@RequestBody Estudiante estudiante) {
        String texto;
        texto=estudianteS.save(estudiante);
        if (texto.equals("1")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Este ID ya ha sido registrado");
        }
        else {
            return ResponseEntity.ok(texto);
        }
    }
    @PostMapping("/interno")
    public ResponseEntity<String> savei(@RequestBody Map<String, Object> json) {
        String id = (String) json.get("id");
        String texto;
        texto=estudianteS.saveI(id);
        if (texto.equals("1")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");

        }
        else if (texto.equals("2")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuario ya registrado");
        }
        else{
            return ResponseEntity.ok(texto);
        }
    }


    @GetMapping("/huella")
    public ResponseEntity<String> getEstudianteByHuella() {
        Estudiante estudiante = estudianteS.validar();
        if (estudiante != null) {
            String estudianteT = estudiante.getNombre() + " " + estudiante.getApellido() + " -  CC:" + estudiante.getId() + "<br>" +
                    "Programa: " + estudiante.getPrograma() + " -  " + estudiante.getCategoria() + "<br>" +
                    estudiante.getUniversidad() + " - fecha de membresia: " + estudiante.getFecha();

            if (estudiante.getFecha().toLocalDate().isAfter(java.time.LocalDate.now())) {
                return ResponseEntity.ok(estudianteT);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(estudianteT+"<br>Fecha de membresia vencida");
            }
        } else {
            return new ResponseEntity <>(HttpStatus.UNAUTHORIZED);
        }
    }
}
