package com.miqueias.pixaccess.service;

import com.miqueias.pixaccess.dto.LoginRequest;
import com.miqueias.pixaccess.dto.LoginResponse;
import com.miqueias.pixaccess.dto.TrocarSenhaRequest;
import com.miqueias.pixaccess.entity.Usuario;
import com.miqueias.pixaccess.repository.UsuarioRepository;
import com.miqueias.pixaccess.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Usuario cadastrar(Usuario usuario) {
        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + usuario.getCpf());
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Usuario salvo = usuarioRepository.save(usuario);
        logger.info("Usuário cadastrado: {}", salvo.getCpf());
        return salvo;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCpf(request.getCpf())
                .orElseThrow(() -> new RuntimeException("CPF ou senha inválidos"));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("CPF ou senha inválidos");
        }

        String token = jwtUtil.gerarToken(usuario.getCpf());
        logger.info("Login realizado: {}", usuario.getCpf());
        return new LoginResponse(token, usuario.getCpf());
    }

    public void trocarSenha(TrocarSenhaRequest request) {
        Usuario usuario = usuarioRepository.findByCpf(request.getCpf())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getSenhaAtual(), usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        usuarioRepository.save(usuario);
        logger.info("Senha alterada para o usuário: {}", request.getCpf());
    }
}
