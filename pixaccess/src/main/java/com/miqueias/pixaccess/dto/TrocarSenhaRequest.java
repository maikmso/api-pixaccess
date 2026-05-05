package com.miqueias.pixaccess.dto;

import lombok.Data;

@Data
public class TrocarSenhaRequest {

    private String cpf;
    private String senhaAtual;
    private String novaSenha;
}
