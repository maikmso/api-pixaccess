package com.miqueias.pixaccess.controller;

import com.miqueias.pixaccess.entity.Pagamento;
import com.miqueias.pixaccess.service.PagamentoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    // Criar pagamento
    @PostMapping
    public Pagamento criarPagamento(@RequestBody Pagamento pagamento) {
        return pagamentoService.criarPagamento(
                pagamento.getCpf(),
                pagamento.getNome(),
                pagamento.getValor()
        );
    }

    // Confirmar pagamento
    @PostMapping("/confirmar/{id}")
    public void confirmarPagamento(@PathVariable Long id) {
        pagamentoService.confirmarPagamento(id);
    }
}
