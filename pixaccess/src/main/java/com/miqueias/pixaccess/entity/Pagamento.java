package com.miqueias.pixaccess.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Data
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cpfPagador;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String chavePix;

    private String descricao;

    @Column(nullable = false)
    private String status; // PENDENTE, CONFIRMADO, CANCELADO

    @Column(nullable = false)
    private LocalDateTime dataCriacao;
}
