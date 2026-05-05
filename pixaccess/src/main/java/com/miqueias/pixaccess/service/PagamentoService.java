package com.miqueias.pixaccess.service;

import com.miqueias.pixaccess.entity.Pagamento;
import com.miqueias.pixaccess.entity.Usuario;
import com.miqueias.pixaccess.repository.PagamentoRepository;
import com.miqueias.pixaccess.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public PagamentoService(PagamentoRepository pagamentoRepository,
                            UsuarioRepository usuarioRepository,
                            PasswordEncoder passwordEncoder) {
        this.pagamentoRepository = pagamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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

            // Gera uma senha temporária aleatória para o primeiro acesso
            String senhaTemporaria = gerarSenhaTemporaria();
            usuario.setSenha(passwordEncoder.encode(senhaTemporaria));

            System.out.println("Senha temporária do usuário: " + senhaTemporaria);

            usuario.setSenhaAlterada(false);

            usuarioRepository.save(usuario);
        }
    }

    private String gerarSenhaTemporaria() {
        return "Pix@" + java.util.UUID.randomUUID()
                .toString()
                .substring(0, 8);
    }
}
