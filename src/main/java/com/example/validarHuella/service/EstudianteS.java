package com.example.validarHuella.service;
import com.digitalpersona.uareu.*;
import com.example.validarHuella.entity.Estudiante;
import com.example.validarHuella.repository.EstudianteR;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class EstudianteS {
    @Autowired
    EstudianteR estudianteR;
    public String save(Estudiante estudiante) {
            estudiante.setBibliotecaId(123456789);
            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaFutura = fechaActual.plusMonths(6);
            estudiante.setFecha(Date.valueOf(fechaFutura));

        if (existe(estudiante.getId())) {
            return "1";
        }
        else {
            estudiante.setHuella(Objects.requireNonNull(registrar()).getData());
            estudianteR.save(estudiante);
            return "El estudiante " + estudiante.getNombre() + " se ha guardado correctamente";
        }
    }


    public Estudiante validar() {
        Fmd huella = registrar();
        List<Estudiante> estudiantes = estudianteR.findAll();
        for (Estudiante estudiante : estudiantes) {
            if (comparar(huella,estudiante.getHuella())) {
                return estudiante;
            }
        }
        return null;
    }

    public String saveI(String id) {
        try {
            Estudiante estudiante = new Estudiante();
            String urlApi = System.getenv("URL_API");
            URL url = new URL(urlApi);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");
            String psApi = System.getenv("PS_API");
            String data = "[{\"cedula\":\"" + id + "\",\"appKey\":\""+psApi+"\"}]";

            http.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
            if (existe(Long.parseLong(id))) {
                return "2";
            }else
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JsonNode rootNode = new ObjectMapper().readTree(new BufferedReader(new InputStreamReader(http.getInputStream())));

                estudiante.setId(Long.parseLong(id));
                estudiante.setNombre(rootNode.get(0).get("firstname").asText());
                estudiante.setApellido(rootNode.get(0).get("surname").asText());
                estudiante.setUniversidad("Universidad de Antioquia");
                estudiante.setPrograma(rootNode.get(0).get("respuesta").get(0).get("programa").asText());
                estudiante.setCategoria(rootNode.get(0).get("respuesta").get(0).get("categoria").asText());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    java.util.Date date = dateFormat.parse(rootNode.get(0).get("respuesta").get(0).get("fechamembresia").asText());
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    estudiante.setFecha(sqlDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                estudiante.setBibliotecaId(123456789);
                Fmd fmd = registrar();
                assert fmd != null;
                estudiante.setHuella(fmd.getData());
                estudianteR.save(estudiante);
                return estudiante.getNombre();
            } else {
                return "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }
    }

    private boolean comparar(Fmd fmd, byte[] huella) {
        Fmd fmd2;
        try {
            fmd2 = UareUGlobal.GetImporter().ImportFmd(huella, Fmd.Format.ANSI_378_2004, Fmd.Format.ANSI_378_2004);
        } catch (UareUException e) {
            throw new RuntimeException(e);
        }
        try {
            int falsematch_rate = UareUGlobal.GetEngine().Compare(fmd, 0, fmd2, 0);
            int target_falsematch_rate = Engine.PROBABILITY_ONE / 100000; // target rate is 0.00001
            if (falsematch_rate < target_falsematch_rate) {
                return true;
            } else {
                return false;
            }
        } catch (UareUException e) {
            System.err.println("Error al comparar las huellas digitales.");
            e.printStackTrace();
        }
        return false;
    }


    private Fmd registrar() {
        try {
            ReaderCollection readerCollection = UareUGlobal.GetReaderCollection();
            readerCollection.GetReaders();

            Reader reader = readerCollection.get(0);
            reader.Open(Reader.Priority.EXCLUSIVE);

            Reader.Capabilities capabilities = reader.GetCapabilities();
            if (capabilities != null && capabilities.resolutions != null && capabilities.resolutions.length > 0) {
                Reader.CaptureResult captureResult = reader.Capture(Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT, reader.GetCapabilities().resolutions[0], -1);
                if (captureResult != null && captureResult.image != null && Reader.CaptureQuality.GOOD == captureResult.quality) {
                    Fmd fmd = UareUGlobal.GetEngine().CreateFmd(captureResult.image, Fmd.Format.ANSI_378_2004);
                    reader.Close();
                    return fmd;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (UareUException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean existe(Long id) {
        if(estudianteR.findById(id).isPresent()){
            return true;
    }
        return false;}
}
