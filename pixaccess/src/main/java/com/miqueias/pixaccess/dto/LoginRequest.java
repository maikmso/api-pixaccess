package com.miqueias.pixaccess.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String cpf;
    private String senha;
}