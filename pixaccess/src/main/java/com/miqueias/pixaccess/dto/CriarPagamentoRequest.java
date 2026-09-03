package com.miqueias.pixaccess.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CriarPagamentoRequest {

    @NotBlank(message = "CPF do pagador é obrigatório")
    private String cpfPagador;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @NotBlank(message = "Chave PIX é obrigatória")
    private String chavePix;

    private String descricao;
}
