package com.utp.hexagonal.Infraestructura.Config;

import com.utp.hexagonal.Infraestructura.Entity.UsuarioEntity;
import com.utp.hexagonal.Infraestructura.Repository.UsuarioRepository;
import com.utp.hexagonal.Seguridad.JwtFilter; // Asegúrate de que esta importación sea correcta si el paquete es 'Seguridad'
import lombok.RequiredArgsConstructor; // Añadido para inyección por constructor

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager; // Añadido
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Añadido para el AuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Añadido
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Añadido para csrf.disable
import org.springframework.security.config.http.SessionCreationPolicy; // Añadido para gestión de sesión sin estado
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Añadido para el filtro JWT

@Configuration
@EnableWebSecurity // Habilita la seguridad web de Spring
@RequiredArgsConstructor // Genera un constructor con todos los campos 'final'
public class WebSecurityConfig {

    // Ya tienes @Autowired aquí, pero la inyección por constructor es preferida.
    // Vamos a ajustarlo para la inyección por constructor para mejor práctica.
    private final UsuarioRepository usuarioRepository;
    private final JwtFilter jwtFilter; // Inyecta tu filtro JWT

    // Este bean es crucial para que Spring Security sepa cómo obtener los detalles del usuario
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getContrasena())
                    // Spring Security espera roles con prefijo "ROLE_".
                    // Si tu base de datos guarda "ADMIN" o "USER" sin prefijo,
                    // Spring Security lo añadirá automáticamente si no lo tiene.
                    // Si ya lo tienen, asegúrate de que sea consistente.
                    .roles(usuario.getRol())
                    .build();
        };
    }

    // Este bean define el codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Este bean define el AuthenticationManager que el proveedor de autenticación usará
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Define un AuthenticationProvider (DaoAuthenticationProvider es común para UserDetailsService)
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // ¡Esta es la única cadena de filtros de seguridad HTTP!
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF para APIs REST que usan JWT
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                // Rutas que no requieren autenticación (permitidas para todos)
                                .requestMatchers("/api/auth/**").permitAll() // Para el login/registro
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Para Swagger UI y docs

                                // Rutas que requieren roles específicos
                                .requestMatchers("/api/pedidos/**").hasRole("USER")
                                .requestMatchers("/api/productos/**", "/admin/**").hasRole("ADMIN")

                                // Cualquier otra petición requiere autenticación
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sesiones sin estado para JWT
                )
                // Asegúrate de usar el proveedor de autenticación que definiste
                // No necesitas añadirlo explícitamente aquí si ya está en el AuthenticationManager
                // .authenticationProvider(authenticationProvider()) // Descomentar si tu AuthenticationManager no lo configura automáticamente

                // Agrega tu filtro JWT ANTES del filtro estándar de UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}