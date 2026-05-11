package com.miqueias.pixaccess.controller;

import com.miqueias.pixaccess.dto.LoginRequest;
import com.miqueias.pixaccess.dto.TrocarSenhaRequest;
import com.miqueias.pixaccess.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return usuarioService.login(request);
    }

    @PostMapping("/trocar-senha")
    public String trocarSenha(@RequestBody TrocarSenhaRequest request) {
        return  usuarioService.trocarSenha(request);
    }

}
