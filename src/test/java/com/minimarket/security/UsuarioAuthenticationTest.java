package com.minimarket.security;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.model.CustomUserDetails;
import com.minimarket.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioAuthenticationTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void loadUserByUsername_conCredencialesValidas() {
        Usuario usuario = crearUsuario("cajero1", "password123", "CAJERO");
        when(usuarioRepository.findByUsername("cajero1")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("cajero1");

        assertEquals("cajero1", userDetails.getUsername());
        assertTrue(passwordEncoder.matches("password123", userDetails.getPassword()));
    }

    @Test
    void loadUserByUsername_usuarioInexistente_lanzaExcepcion() {
        when(usuarioRepository.findByUsername("desconocido")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("desconocido"));
    }

    @Test
    void customUserDetails_cargaAuthoritiesConPrefijoRole() {
        Usuario usuario = crearUsuario("admin1", "pass", "ADMINISTRADOR");
        CustomUserDetails userDetails = new CustomUserDetails(usuario);

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR")));
    }

    @Test
    void autenticacion_contrasenaIncorrecta_noCoincide() {
        Usuario usuario = crearUsuario("cliente1", "correcta", "CLIENTE");
        CustomUserDetails userDetails = new CustomUserDetails(usuario);

        assertFalse(passwordEncoder.matches("incorrecta", userDetails.getPassword()));
    }

    private Usuario crearUsuario(String username, String password, String rolNombre) {
        Rol rol = new Rol(rolNombre);
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRoles(Set.of(rol));
        return usuario;
    }
}
