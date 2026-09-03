package com.miqueias.pixaccess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tipo;
    private String cpf;

    public LoginResponse(String token, String cpf) {
        this.token = token;
        this.tipo = "Bearer";
        this.cpf = cpf;
    }
}
