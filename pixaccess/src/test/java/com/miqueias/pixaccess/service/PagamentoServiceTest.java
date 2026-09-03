package com.miqueias.pixaccess.service;

import com.miqueias.pixaccess.dto.CriarPagamentoRequest;
import com.miqueias.pixaccess.dto.PagamentoResponse;
import com.miqueias.pixaccess.entity.Pagamento;
import com.miqueias.pixaccess.repository.PagamentoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Pagamento pagamentoMock(Long id, String status) {
        Pagamento p = new Pagamento();
        p.setId(id);
        p.setCpfPagador("12345678901");
        p.setValor(new BigDecimal("150.00"));
        p.setChavePix("email@exemplo.com");
        p.setStatus(status);
        p.setDataCriacao(LocalDateTime.now());
        return p;
    }

    @Test
    @DisplayName("Deve criar pagamento com status PENDENTE")
    void deveCriarPagamentoComSucesso() {
        CriarPagamentoRequest request = new CriarPagamentoRequest();
        request.setCpfPagador("12345678901");
        request.setValor(new BigDecimal("150.00"));
        request.setChavePix("email@exemplo.com");

        Pagamento salvo = pagamentoMock(1L, "PENDENTE");
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(salvo);

        PagamentoResponse response = pagamentoService.criarPagamento(request);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("PENDENTE");
        assertThat(response.getValor()).isEqualByComparingTo("150.00");
        verify(pagamentoRepository).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve buscar pagamento por ID com sucesso")
    void deveBuscarPagamentoPorId() {
        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamentoMock(1L, "PENDENTE")));

        PagamentoResponse response = pagamentoService.buscarPorId(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void deveLancarExcecaoParaIdInexistente() {
        when(pagamentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagamentoService.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Pagamento não encontrado");
    }

    @Test
    @DisplayName("Deve confirmar pagamento PENDENTE")
    void deveConfirmarPagamentoPendente() {
        Pagamento pendente = pagamentoMock(1L, "PENDENTE");
        Pagamento confirmado = pagamentoMock(1L, "CONFIRMADO");

        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pendente));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(confirmado);

        PagamentoResponse response = pagamentoService.confirmarPagamento(1L);

        assertThat(response.getStatus()).isEqualTo("CONFIRMADO");
    }

    @Test
    @DisplayName("Deve lançar exceção ao confirmar pagamento já confirmado")
    void deveLancarExcecaoAoConfirmarPagamentoJaConfirmado() {
        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamentoMock(1L, "CONFIRMADO")));

        assertThatThrownBy(() -> pagamentoService.confirmarPagamento(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("PENDENTE");
    }

    @Test
    @DisplayName("Deve cancelar pagamento PENDENTE")
    void deveCancelarPagamentoPendente() {
        Pagamento pendente = pagamentoMock(1L, "PENDENTE");
        Pagamento cancelado = pagamentoMock(1L, "CANCELADO");

        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pendente));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(cancelado);

        PagamentoResponse response = pagamentoService.cancelarPagamento(1L);

        assertThat(response.getStatus()).isEqualTo("CANCELADO");
    }
}
