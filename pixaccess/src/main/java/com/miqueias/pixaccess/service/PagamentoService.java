package com.miqueias.pixaccess.service;

import com.miqueias.pixaccess.dto.CriarPagamentoRequest;
import com.miqueias.pixaccess.dto.PagamentoResponse;
import com.miqueias.pixaccess.entity.Pagamento;
import com.miqueias.pixaccess.repository.PagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {

    private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);

    @Autowired
    private PagamentoRepository pagamentoRepository;

    public PagamentoResponse criarPagamento(CriarPagamentoRequest request) {
        Pagamento pagamento = new Pagamento();
        pagamento.setCpfPagador(request.getCpfPagador());
        pagamento.setValor(request.getValor());
        pagamento.setChavePix(request.getChavePix());
        pagamento.setDescricao(request.getDescricao());
        pagamento.setStatus("PENDENTE");
        pagamento.setDataCriacao(LocalDateTime.now());

        Pagamento salvo = pagamentoRepository.save(pagamento);
        logger.info("Pagamento criado com ID: {}", salvo.getId());
        return toResponse(salvo);
    }

    public PagamentoResponse buscarPorId(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado: " + id));
        return toResponse(pagamento);
    }

    public List<PagamentoResponse> listarPorCpf(String cpfPagador) {
        return pagamentoRepository.findByCpfPagador(cpfPagador)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PagamentoResponse confirmarPagamento(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado: " + id));

        if (!"PENDENTE".equals(pagamento.getStatus())) {
            throw new RuntimeException("Pagamento não está com status PENDENTE");
        }

        pagamento.setStatus("CONFIRMADO");
        Pagamento atualizado = pagamentoRepository.save(pagamento);
        logger.info("Pagamento ID {} confirmado", id);
        return toResponse(atualizado);
    }

    public PagamentoResponse cancelarPagamento(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado: " + id));

        if ("CONFIRMADO".equals(pagamento.getStatus())) {
            throw new RuntimeException("Não é possível cancelar um pagamento já confirmado");
        }

        pagamento.setStatus("CANCELADO");
        Pagamento atualizado = pagamentoRepository.save(pagamento);
        logger.info("Pagamento ID {} cancelado", id);
        return toResponse(atualizado);
    }

    private PagamentoResponse toResponse(Pagamento p) {
        PagamentoResponse response = new PagamentoResponse();
        response.setId(p.getId());
        response.setCpfPagador(p.getCpfPagador());
        response.setValor(p.getValor());
        response.setChavePix(p.getChavePix());
        response.setDescricao(p.getDescricao());
        response.setStatus(p.getStatus());
        response.setDataCriacao(p.getDataCriacao());
        return response;
    }
}
