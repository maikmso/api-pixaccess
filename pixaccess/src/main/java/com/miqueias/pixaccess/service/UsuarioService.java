package com.miqueias.pixaccess.service;

import com.miqueias.pixaccess.dto.LoginRequest;
import com.miqueias.pixaccess.dto.TrocarSenhaRequest;
import com.miqueias.pixaccess.entity.Usuario;
import com.miqueias.pixaccess.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByCpf(request.getCpf())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        if (!usuario.isSenhaAlterada()) {
            return "Login válido, mas é necessário trocar a senha no primeiro acesso";
        }

        return "Login realizado com sucesso";
    }

    public String trocarSenha(TrocarSenhaRequest request) {

        Usuario usuario = usuarioRepository.findByCpf(request.getCpf())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getSenhaAtual(), usuario.getSenha())) {
           throw new RuntimeException("Senha atual inválida");
        }

        usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        usuario.setSenhaAlterada(true);

        usuarioRepository.save(usuario);

        return "Senha alterada com sucesso";
    }
}
