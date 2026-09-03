package com.miqueias.pixaccess.controller;

import com.miqueias.pixaccess.dto.LoginRequest;
import com.miqueias.pixaccess.dto.LoginResponse;
import com.miqueias.pixaccess.dto.TrocarSenhaRequest;
import com.miqueias.pixaccess.entity.Usuario;
import com.miqueias.pixaccess.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody Usuario usuario) {
        Usuario salvo = usuarioService.cadastrar(usuario);
        salvo.setSenha(null); // nunca retornar a senha
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/trocar-senha")
    public ResponseEntity<Void> trocarSenha(@Valid @RequestBody TrocarSenhaRequest request) {
        usuarioService.trocarSenha(request);
        return ResponseEntity.noContent().build();
    }
}
