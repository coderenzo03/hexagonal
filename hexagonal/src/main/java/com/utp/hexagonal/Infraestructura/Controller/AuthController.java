package com.utp.hexagonal.Infraestructura.Controller;

import com.utp.hexagonal.Infraestructura.dto.LoginRequest;
import com.utp.hexagonal.Infraestructura.Entity.UsuarioEntity;
import com.utp.hexagonal.Infraestructura.Repository.UsuarioRepository;
import com.utp.hexagonal.Seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(request.getCorreo())
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        if (!usuario.getContrasena().equals(request.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Contraseña incorrecta"));
        }

        if (!usuario.getRol().equalsIgnoreCase(request.getRol())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Rol no autorizado"));
        }

        // Generar token
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());

        // Devolver token y rol
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Acceso permitido");
        response.put("token", token);
        response.put("rol", usuario.getRol());

        return ResponseEntity.ok(response);
    }
}
