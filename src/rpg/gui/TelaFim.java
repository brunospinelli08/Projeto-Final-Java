package rpg.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rpg.item.Arma;
import rpg.personagem.Jogador;
import rpg.personagem.Monstro;

// Tela 4: fim da aventura. A mesma tela serve para a vitoria final e para a derrota,
// so mudando os textos e as cores
public class TelaFim extends JPanel {
    private final JLabel rotulo = Tema.rotulo(" ", Tema.negrito(13), Tema.DOURADO);
    private final JLabel titulo = Tema.rotulo(" ", Tema.titulo(52), Tema.TEXTO);
    private final JPanel traco = new JPanel();
    private final JLabel texto = Tema.rotulo(" ", Tema.texto(16), Tema.TEXTO_SUAVE);
    private final JLabel resumo = Tema.rotulo(" ", Tema.texto(14), Tema.TEXTO_FRACO);

    public TelaFim(JanelaJogo janela) {
        setLayout(new GridBagLayout()); // com um filho so, o GridBagLayout centraliza ele na tela
        setBackground(Tema.FUNDO);

        traco.setPreferredSize(new Dimension(56, 2));
        traco.setMaximumSize(new Dimension(56, 2));

        JButton botaoNovaPartida = Tema.botaoPrincipal("Nova partida");
        botaoNovaPartida.addActionListener(e -> janela.mostrarCriacao());
        JButton botaoSair = Tema.botaoSecundario("Sair do jogo");
        botaoSair.addActionListener(e -> System.exit(0));
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        botoes.setOpaque(false);
        botoes.add(botaoSair);
        botoes.add(botaoNovaPartida);

        JPanel coluna = new JPanel();
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
        coluna.setOpaque(false);
        adicionarCentralizado(coluna, rotulo);
        coluna.add(Box.createVerticalStrut(14));
        adicionarCentralizado(coluna, titulo);
        coluna.add(Box.createVerticalStrut(20));
        adicionarCentralizado(coluna, traco);
        coluna.add(Box.createVerticalStrut(22));
        adicionarCentralizado(coluna, texto);
        coluna.add(Box.createVerticalStrut(14));
        adicionarCentralizado(coluna, resumo);
        coluna.add(Box.createVerticalStrut(40));
        adicionarCentralizado(coluna, botoes);
        add(coluna);
    }

    public void mostrarVitoria(Jogador jogador) {
        preencher("FORTALEZA LIBERTADA", "Vitória!", Tema.DOURADO,
                "O Guardião caiu e a fortaleza está livre outra vez. "
                        + jogador.getNome() + " atravessou a entrada, o corredor e a câmara final.",
                jogador);
    }

    public void mostrarDerrota(Jogador jogador, Monstro inimigo, int etapa, int totalEtapas) {
        preencher("FIM DA AVENTURA · ETAPA " + etapa + " DE " + totalEtapas, "Derrota", Tema.ALERTA,
                jogador.getNome() + " caiu diante de " + inimigo.getNome()
                        + ". A fortaleza continua tomada. Uma nova partida começa do zero.",
                jogador);
    }

    private void preencher(String textoRotulo, String textoTitulo, Color cor, String descricao, Jogador jogador) {
        rotulo.setText(textoRotulo);
        rotulo.setForeground(cor);
        titulo.setText(textoTitulo);
        traco.setBackground(cor);
        texto.setText("<html><div style='width: 460px; text-align: center'>" + descricao + "</div></html>");

        Arma arma = jogador.getArmaEquipada();
        resumo.setText(jogador.getClass().getSimpleName()
                + "  ·  Arma: " + ((arma == null) ? "nenhuma" : arma.getNome()));
    }

    private static void adicionarCentralizado(JPanel coluna, javax.swing.JComponent componente) {
        componente.setAlignmentX(Component.CENTER_ALIGNMENT);
        coluna.add(componente);
    }
}