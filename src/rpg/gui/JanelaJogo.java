package rpg.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import rpg.app.Aventura;
import rpg.combate.Batalha;
import rpg.combate.EstadoBatalha;
import rpg.personagem.Especializacao;

// A janela do jogo. Troca entre as telas com um CardLayout e guarda a Aventura atual
public class JanelaJogo extends JFrame {
    private static final String TELA_CRIACAO = "criacao";
    private static final String TELA_AVENTURA = "aventura";
    private static final String TELA_BATALHA = "batalha";
    private static final String TELA_FIM = "fim";

    private final CardLayout trocaDeTelas = new CardLayout();
    private final JPanel telas = new JPanel(trocaDeTelas);
    private final TelaCriacao telaCriacao;
    private final TelaAventura telaAventura;
    private final TelaBatalha telaBatalha;
    private final TelaFim telaFim;

    private Aventura aventura;

    public JanelaJogo() {
        super("A Fortaleza Tomada");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        telaCriacao = new TelaCriacao(this);
        telaAventura = new TelaAventura(this);
        telaBatalha = new TelaBatalha(this);
        telaFim = new TelaFim(this);

        telas.setBackground(Tema.FUNDO);
        telas.setPreferredSize(new Dimension(960, 640));
        telas.add(telaCriacao, TELA_CRIACAO);
        telas.add(telaAventura, TELA_AVENTURA);
        telas.add(telaBatalha, TELA_BATALHA);
        telas.add(telaFim, TELA_FIM);

        setContentPane(telas);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null); // abre no centro da tela
    }

    // Chamado pela TelaCriacao. Cada chamada cria uma Aventura nova: nada da anterior e reaproveitado
    public void iniciarAventura(String nome, Especializacao especializacao) {
        aventura = new Aventura(nome, especializacao);
        mostrarAventura(Collections.emptyList());
    }

    public void mostrarAventura(List<String> mensagens) {
        telaAventura.mostrar(aventura, mensagens);
        trocaDeTelas.show(telas, TELA_AVENTURA);
    }

    public void mostrarCriacao() {
        telaCriacao.limpar();
        trocaDeTelas.show(telas, TELA_CRIACAO);
    }

    // Chamado pelo botao "Enfrentar" da TelaAventura
    public void iniciarBatalha() {
        int numeroEtapa = aventura.getEtapaAtual() + 1;
        String descricao = aventura.getDescricaoLocal();
        Batalha batalha = aventura.iniciarBatalha();
        if (batalha == null) {
            return;
        }
        telaBatalha.mostrar(batalha, numeroEtapa, descricao);
        trocaDeTelas.show(telas, TELA_BATALHA);
    }

    // Chamado pelo botao "Continuar" da TelaBatalha, quando a batalha acabou
    public void finalizarBatalha(Batalha batalha) {
        if (batalha.getEstado() != EstadoBatalha.VITORIA) {
            telaFim.mostrarDerrota(aventura.getJogador(), batalha.getInimigo(),
                    aventura.getEtapaAtual() + 1, aventura.getTotalEtapas());
            trocaDeTelas.show(telas, TELA_FIM);
            return;
        }
        List<String> recompensas = aventura.registrarVitoria(batalha);
        if (aventura.foiConcluida()) {
            telaFim.mostrarVitoria(aventura.getJogador());
            trocaDeTelas.show(telas, TELA_FIM);
        } else {
            mostrarAventura(recompensas);
        }
    }
}