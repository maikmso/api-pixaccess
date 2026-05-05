package com.miqueias.pixaccess.controller;

import com.miqueias.pixaccess.dto.LoginRequest;
import com.miqueias.pixaccess.entity.Usuario;
import com.miqueias.pixaccess.repository.UsuarioRepository;
import com.miqueias.pixaccess.dto.TrocarSenhaRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioRepository.findByCpf(request.getCpf())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getSenha(),
                usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        if (!usuario.isSenhaAlterada()) {
            return "Login válido, mas é necessário trocar a senha no primeiro acesso";
        }

        return "Login realizado com sucesso";
    }

    @PostMapping("/trocar-senha")
    public String trocarSenha(@RequestBody TrocarSenhaRequest request) {

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
