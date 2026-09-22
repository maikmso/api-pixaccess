package com.miqueias.pixaccess.event;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagamentoConfirmadoEvent {

    private Long id;
    private String cpfPagador;
    private BigDecimal valor;
    private String status;
    private LocalDateTime dataConfirmacao;
}
