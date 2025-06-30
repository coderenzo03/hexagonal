package com.utp.hexagonal.Infraestructura.Config;

import com.utp.hexagonal.Infraestructura.Entity.UsuarioEntity;
import com.utp.hexagonal.Infraestructura.Repository.UsuarioRepository;
import com.utp.hexagonal.Seguridad.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UsuarioRepository usuarioRepository;
    private final JwtFilter jwtFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getContrasena())
                    .roles(usuario.getRol()) // Usa "ADMIN" o "TRABAJADOR"
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Rutas permitidas para todos sin autenticación
                        .requestMatchers(
                                "/", // Permitir acceso a la raíz de la aplicación
                                "/login.html", "/register.html",
                                "/css/**", "/js/**",
                                "/images/**", // Mantener si tienes una carpeta 'images'
                                "/imagen/**"  // <-- ¡IMPORTANTE! Permitir acceso a la carpeta 'imagen'
                        ).permitAll()
                        .requestMatchers("/admin/**.html", "/usuario/**.html").permitAll()

                        // Rutas de autenticación (login, registro)
                        .requestMatchers("/api/auth/**").permitAll()

                        // (incluye los KPIs de productos, accesibles por ADMIN)
                        .requestMatchers("/api/productos/**").hasAnyRole("ADMIN", "TRABAJADOR")

                        // Permisos para endpoints de usuarios: Solo ADMIN puede gestionar usuarios
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                        // Permisos para endpoints de pedidos: TRABAJADOR puede crear, ADMIN puede ver KPIs e historial, CLIENTE si aplica.
                        // Incluye los KPIs de pedidos ("/api/pedidos/kpi/**")
                        .requestMatchers("/api/pedidos/**").hasAnyRole("TRABAJADOR", "CLIENTE", "ADMIN")

                        // Cualquier otra petición debe ser autenticada
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
