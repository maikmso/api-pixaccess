package com.miqueias.pixaccess.controller;

import com.miqueias.pixaccess.dto.CriarPagamentoRequest;
import com.miqueias.pixaccess.dto.PagamentoResponse;
import com.miqueias.pixaccess.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoResponse> criarPagamento(@Valid @RequestBody CriarPagamentoRequest request) {
        PagamentoResponse response = pagamentoService.criarPagamento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscarPorId(@PathVariable Long id) {
        PagamentoResponse response = pagamentoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cpf/{cpfPagador}")
    public ResponseEntity<List<PagamentoResponse>> listarPorCpf(@PathVariable String cpfPagador) {
        List<PagamentoResponse> lista = pagamentoService.listarPorCpf(cpfPagador);
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<PagamentoResponse> confirmarPagamento(@PathVariable Long id) {
        PagamentoResponse response = pagamentoService.confirmarPagamento(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PagamentoResponse> cancelarPagamento(@PathVariable Long id) {
        PagamentoResponse response = pagamentoService.cancelarPagamento(id);
        return ResponseEntity.ok(response);
    }
}
