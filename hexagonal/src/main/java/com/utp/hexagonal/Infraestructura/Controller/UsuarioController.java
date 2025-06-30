package com.utp.hexagonal.Infraestructura.Controller;

import com.utp.hexagonal.Infraestructura.Entity.UsuarioEntity;
import com.utp.hexagonal.Infraestructura.Repository.UsuarioRepository;
import com.utp.hexagonal.Infraestructura.dto.UsuarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Crear usuario (por defecto con rol USER)
    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioRequest request) {
        String correo = request.getEmail().toLowerCase().trim();
        String rol = request.getRol().toUpperCase().trim();

        // Validación según el rol
        if (rol.equals("ADMIN") && !correo.endsWith("@admin.com")) {
            return ResponseEntity.badRequest().body("Los administradores deben tener un correo que termine en @admin.com");
        } else if (rol.equals("TRABAJADOR") && !correo.endsWith("@empresa.com")) {
            return ResponseEntity.badRequest().body("Los trabajadores deben tener un correo que termine en @empresa.com");
        }

        if (usuarioRepository.findByEmail(correo).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        UsuarioEntity nuevo = new UsuarioEntity();
        nuevo.setEmail(correo);
        nuevo.setContrasena(passwordEncoder.encode(request.getContrasena()));
        nuevo.setRol(rol);

        usuarioRepository.save(nuevo);
        return ResponseEntity.ok("Usuario creado exitosamente");
    }


    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    //  Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setEmail(request.getEmail());
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
            usuario.setRol(request.getRol().toUpperCase());
            usuarioRepository.save(usuario);
            return ResponseEntity.ok("Usuario actualizado correctamente");
        }).orElse(ResponseEntity.notFound().build());
    }

    //  Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuarioRepository.delete(usuario);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        }).orElse(ResponseEntity.notFound().build());
    }
}
