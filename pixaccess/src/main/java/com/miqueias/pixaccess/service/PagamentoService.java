package com.miqueias.pixaccess.service;

import com.miqueias.pixaccess.entity.Pagamento;
import com.miqueias.pixaccess.entity.Usuario;
import com.miqueias.pixaccess.repository.PagamentoRepository;
import com.miqueias.pixaccess.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository,
                            UsuarioRepository usuarioRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Pagamento criarPagamento(String cpf, String nome, Double valor) {

        Pagamento pagamento = new Pagamento();

        pagamento.setCpf(cpf);
        pagamento.setNome(nome);
        pagamento.setValor(valor);
        pagamento.setStatus("PENDENTE");

        return pagamentoRepository.save(pagamento);
    }

    public void confirmarPagamento(Long id) {

        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamento.setStatus("PAGO");

        pagamentoRepository.save(pagamento);

        criarUsuario(pagamento);
    }

    private void criarUsuario(Pagamento pagamento) {

        boolean existe = usuarioRepository
                .findByCpf(pagamento.getCpf())
                .isPresent();

        if (!existe) {

            Usuario usuario = new Usuario();

            usuario.setCpf(pagamento.getCpf());
            usuario.setNome(pagamento.getNome());

            usuario.setSenha(pagamento.getCpf()
                    .substring(pagamento.getCpf()
                            .length() - 4));

            usuario.setSenhaAlterada(false);

            usuarioRepository.save(usuario);
        }
    }
}
