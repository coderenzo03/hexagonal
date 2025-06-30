package com.utp.hexagonal.Infraestructura.Controller;

import com.utp.hexagonal.Infraestructura.dto.LoginRequest;
import com.utp.hexagonal.Infraestructura.Entity.UsuarioEntity;
import com.utp.hexagonal.Infraestructura.Repository.UsuarioRepository;
import com.utp.hexagonal.Seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permitir peticiones desde el frontend en localhost
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(request.getCorreo()).orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        // Verifica la contraseña cifrada
        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Contraseña incorrecta"));
        }

        // Verifica el rol si lo estás enviando desde el frontend
        if (request.getRol() != null && !usuario.getRol().equalsIgnoreCase(request.getRol())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Rol no autorizado"));
        }

        // Generar JWT
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().toUpperCase());

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Acceso permitido");
        response.put("token", token);
        response.put("rol", usuario.getRol());
        response.put("email", usuario.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioEntity nuevoUsuario) {
        if (usuarioRepository.findByEmail(nuevoUsuario.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El correo ya está registrado"));
        }

        // Cifra la contraseña
        nuevoUsuario.setContrasena(passwordEncoder.encode(nuevoUsuario.getContrasena()));

        // Si no se envía el rol desde el frontend, se puede poner uno por defecto
        if (nuevoUsuario.getRol() == null || nuevoUsuario.getRol().isEmpty()) {
            nuevoUsuario.setRol("USER");
        }

        UsuarioEntity usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "mensaje", "Usuario registrado correctamente",
                        "usuario", usuarioGuardado.getEmail(),
                        "rol", usuarioGuardado.getRol()
                ));
    }
}
