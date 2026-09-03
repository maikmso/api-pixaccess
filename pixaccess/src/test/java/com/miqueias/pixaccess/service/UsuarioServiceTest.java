package com.miqueias.pixaccess.service;

import com.miqueias.pixaccess.dto.LoginRequest;
import com.miqueias.pixaccess.dto.LoginResponse;
import com.miqueias.pixaccess.entity.Usuario;
import com.miqueias.pixaccess.repository.UsuarioRepository;
import com.miqueias.pixaccess.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setCpf("12345678901");
        usuario.setSenha("senhaHash");
    }

    @Test
    @DisplayName("Deve cadastrar um novo usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaHash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario novo = new Usuario();
        novo.setNome("João Silva");
        novo.setCpf("12345678901");
        novo.setSenha("senha123");

        Usuario resultado = usuarioService.cadastrar(novo);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCpf()).isEqualTo("12345678901");
        verify(passwordEncoder).encode("senha123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar CPF já existente")
    void deveLancarExcecaoParaCpfDuplicado() {
        when(usuarioRepository.existsByCpf("12345678901")).thenReturn(true);

        Usuario novo = new Usuario();
        novo.setCpf("12345678901");
        novo.setSenha("senha123");

        assertThatThrownBy(() -> usuarioService.cadastrar(novo))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("CPF já cadastrado");
    }

    @Test
    @DisplayName("Deve realizar login com credenciais corretas")
    void deveRealizarLoginComSucesso() {
        when(usuarioRepository.findByCpf("12345678901")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", "senhaHash")).thenReturn(true);
        when(jwtUtil.gerarToken("12345678901")).thenReturn("token.jwt.aqui");

        LoginRequest request = new LoginRequest();
        request.setCpf("12345678901");
        request.setSenha("senha123");

        LoginResponse response = usuarioService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("token.jwt.aqui");
        assertThat(response.getCpf()).isEqualTo("12345678901");
        assertThat(response.getTipo()).isEqualTo("Bearer");
    }

    @Test
    @DisplayName("Deve lançar exceção ao fazer login com senha incorreta")
    void deveLancarExcecaoParaSenhaIncorreta() {
        when(usuarioRepository.findByCpf("12345678901")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaErrada", "senhaHash")).thenReturn(false);

        LoginRequest request = new LoginRequest();
        request.setCpf("12345678901");
        request.setSenha("senhaErrada");

        assertThatThrownBy(() -> usuarioService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("CPF ou senha inválidos");
    }

    @Test
    @DisplayName("Deve lançar exceção ao fazer login com CPF inexistente")
    void deveLancarExcecaoParaCpfInexistente() {
        when(usuarioRepository.findByCpf("99999999999")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setCpf("99999999999");
        request.setSenha("qualquerSenha");

        assertThatThrownBy(() -> usuarioService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("CPF ou senha inválidos");
    }
}
