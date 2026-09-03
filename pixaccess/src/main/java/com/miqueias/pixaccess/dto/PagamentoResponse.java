package com.miqueias.pixaccess.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagamentoResponse {

    private Long id;
    private String cpfPagador;
    private BigDecimal valor;
    private String chavePix;
    private String descricao;
    private String status;
    private LocalDateTime dataCriacao;
}
