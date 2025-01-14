package com.example.SportyRest.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.SportyRest.model.Equipo;
import com.example.SportyRest.model.Equipo_miembro;
import com.example.SportyRest.service.EquipoMiembroService;
import com.example.SportyRest.service.EquipoService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    // Crear un equipo
    @PostMapping
    public ResponseEntity<Equipo> createEquipo(@RequestBody Equipo equipo) {
        return ResponseEntity.ok(equipoService.createEquipo(equipo));
    }

    // Obtener todos los equipos
    @GetMapping
    public ResponseEntity<List<Equipo>> getAllEquipos() {
        return ResponseEntity.ok(equipoService.getAllEquipos());
    }

    // Obtener un equipo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Equipo> getEquipoById(@PathVariable int id) {
        return equipoService.getEquipoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar un equipo
    @PutMapping("/{id}")
    public ResponseEntity<Equipo> updateEquipo(@PathVariable int id, @RequestBody Equipo equipoDetails) {
        try {
            return ResponseEntity.ok(equipoService.updateEquipo(id, equipoDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un equipo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipo(@PathVariable int id) {
        equipoService.deleteEquipo(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/subirImagen")
    public ResponseEntity<String> subirImagen(@RequestParam("image") MultipartFile file) {
        try {
            Dotenv dotenv = Dotenv.load();
            Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));

            Map params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            );

            // Convertir MultipartFile a File temporal
            File tempFile = File.createTempFile("upload", file.getOriginalFilename());
            file.transferTo(tempFile);

            Map uploadResult = cloudinary.uploader().upload(tempFile, params1); // Sube la imagen a Cloudinary u otro servicio

            // Eliminar el archivo temporal después de subir
            tempFile.delete();

            // Obtener la URL de la imagen subida
            String imageUrl = (String) uploadResult.get("url");
            System.out.println("URL de la imagen subida: " + imageUrl);

            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir la imagen");
        }
    }


}
