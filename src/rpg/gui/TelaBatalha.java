package rpg.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import rpg.combate.Batalha;
import rpg.combate.EstadoBatalha;
import rpg.combate.ResultadoTurno;
import rpg.item.Item;
import rpg.personagem.Jogador;
import rpg.personagem.Monstro;

// Tela 3: a batalha. Cada botao chama UM metodo da Batalha e mostra as mensagens que voltaram
public class TelaBatalha extends JPanel {
    private static final Color INIMIGO_BORDA = new Color(0x6b3a33);
    private static final String ACOES = "acoes";
    private static final String FIM = "fim";

    private final JanelaJogo janela;
    private Batalha batalha;
    private int turno;

    private final JLabel rotuloEtapa = Tema.rotulo(" ", Tema.negrito(12), Tema.DOURADO);
    private final JLabel rotuloTurno = Tema.rotulo(" ", Tema.texto(13), Tema.TEXTO_FRACO);

    private final JLabel nomeJogador = Tema.rotulo(" ", Tema.titulo(24), Tema.TEXTO);
    private final JLabel infoJogador = Tema.rotulo(" ", Tema.texto(12), Tema.TEXTO_SUAVE);
    private final BarraStatus vidaJogador = new BarraStatus("Vida", Tema.VIDA, Tema.VIDA_FUNDO);
    private final BarraStatus energiaJogador = new BarraStatus("Energia", Tema.ENERGIA, Tema.ENERGIA_FUNDO);

    private final JLabel nomeInimigo = Tema.rotulo(" ", Tema.titulo(24), Tema.TEXTO);
    private final JLabel categoriaInimigo = Tema.rotulo(" ", Tema.negrito(11), Tema.ALERTA);
    private final JLabel infoInimigo = Tema.rotulo(" ", Tema.texto(12), Tema.TEXTO_SUAVE);
    private final BarraStatus vidaInimigo = new BarraStatus("Vida", Tema.VIDA, Tema.VIDA_FUNDO);

    private final JTextArea registro = new JTextArea();

    private final CardLayout trocaRodape = new CardLayout();
    private final JPanel rodape = new JPanel(trocaRodape);
    private final JButton botaoHabilidade = Tema.botaoPrincipal("Habilidade");
    private final JButton botaoItem = Tema.botaoSecundario("Usar item");
    private final JLabel resultadoFinal = Tema.rotulo(" ", Tema.titulo(22), Tema.TEXTO);

    public TelaBatalha(JanelaJogo janela) {
        this.janela = janela;

        setLayout(new BorderLayout(0, 18));
        setBackground(Tema.FUNDO);
        setBorder(BorderFactory.createEmptyBorder(24, 36, 28, 36));
        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarCentro(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    // Chamado pela janela no comeco de cada batalha
    public void mostrar(Batalha batalha, int numeroEtapa, String descricaoLocal) {
        this.batalha = batalha;
        this.turno = 1;
        rotuloEtapa.setText("BATALHA · ETAPA " + numeroEtapa);
        registro.setText(descricaoLocal + "\n");
        trocaRodape.show(rodape, ACOES);
        atualizar();
    }

    // ---------- acoes do jogador: cada uma chama um metodo da Batalha ----------

    private void atacar() {
        mostrarResultado(batalha.atacar());
    }

    private void usarHabilidade() {
        mostrarResultado(batalha.usarHabilidade());
    }

    private void usarItem(Item item) {
        mostrarResultado(batalha.usarItem(item));
    }

    // Abre um menu acima do botao com um item do inventario por linha
    private void abrirMenuDeItens() {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(Tema.PAINEL);
        menu.setBorder(BorderFactory.createLineBorder(Tema.BORDA_FORTE, 1));

        for (Item item : batalha.getJogador().getInventario().getItens()) {
            JMenuItem opcao = new JMenuItem(item.getNome() + "  ·  " + item.getEfeito());
            opcao.setFont(Tema.texto(14));
            opcao.setBackground(Tema.PAINEL);
            opcao.setForeground(Tema.TEXTO);
            opcao.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            opcao.addActionListener(e -> usarItem(item));
            menu.add(opcao);
        }
        if (menu.getComponentCount() == 0) {
            mostrarResultado(batalha.usarItem(null)); // a Batalha responde "nenhum item"
            return;
        }

        // Mostra o menu logo acima do botao, alinhado pela direita (y negativo = para cima)
        Dimension tamanho = menu.getPreferredSize();
        menu.show(botaoItem, botaoItem.getWidth() - tamanho.width, -tamanho.height - 4);
    }

    // Escreve o resultado no registro. So conta o turno se a acao foi aceita
    private void mostrarResultado(ResultadoTurno resultado) {
        if (resultado.foiAceito()) {
            registro.append("\nTurno " + turno + "\n");
            turno++;
        } else {
            registro.append("\nAção recusada, o turno não passou:\n");
        }
        for (String mensagem : resultado.getMensagens()) {
            registro.append("   " + mensagem + "\n");
        }
        registro.setCaretPosition(registro.getDocument().getLength()); // rola para a ultima linha

        atualizar();
        if (batalha.terminou()) {
            mostrarFim();
        }
    }

    private void mostrarFim() {
        if (batalha.getEstado() == EstadoBatalha.VITORIA) {
            resultadoFinal.setText("Vitória! " + batalha.getInimigo().getNome() + " foi derrotado.");
            resultadoFinal.setForeground(Tema.DOURADO_CLARO);
        } else {
            resultadoFinal.setText("Derrota. " + batalha.getJogador().getNome() + " caiu em batalha.");
            resultadoFinal.setForeground(Tema.ALERTA);
        }
        trocaRodape.show(rodape, FIM);
    }

    // Le tudo de novo da Batalha. A tela nunca calcula dano nem guarda vida
    private void atualizar() {
        Jogador jogador = batalha.getJogador();
        Monstro inimigo = batalha.getInimigo();

        rotuloTurno.setText(batalha.terminou() ? "Fim da batalha" : "Turno " + turno + " · sua vez");

        nomeJogador.setText(jogador.getNome());
        infoJogador.setText(jogador.getClass().getSimpleName() + " · Ataque " + jogador.getAtaqueTotal()
                + " · Defesa " + jogador.getDefesa());
        vidaJogador.setValores(jogador.getVidaAtual(), jogador.getVidaMaxima());
        energiaJogador.setValores(jogador.getEnergiaAtual(), jogador.getEnergiaMaxima());

        nomeInimigo.setText(inimigo.getNome());
        categoriaInimigo.setText(inimigo.getCategoria().toUpperCase()); // polimorfismo: Monstro ou Chefe
        infoInimigo.setText("Ataque " + inimigo.getAtaqueTotal() + " · Defesa " + inimigo.getDefesa());
        vidaInimigo.setValores(inimigo.getVidaAtual(), inimigo.getVidaMaxima());

        botaoHabilidade.setText("<html><center>" + jogador.getNomeHabilidade() + "<br>"
                + "<font size='-1'>custa " + jogador.getCustoHabilidade() + " de energia</font></center></html>");
        botaoItem.setText("<html><center>Usar item<br><font size='-1'>"
                + jogador.getInventario().tamanho() + " no inventário</font></center></html>");
    }

    // ---------- montagem da tela ----------

    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.add(rotuloEtapa, BorderLayout.WEST);
        cabecalho.add(rotuloTurno, BorderLayout.EAST);
        return cabecalho;
    }

    private JPanel criarCentro() {
        JPanel cartoes = new JPanel(new GridBagLayout());
        cartoes.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        c.gridx = 0;
        c.weightx = 1;
        cartoes.add(criarCartaoJogador(), c);

        c.gridx = 1;
        c.weightx = 0;
        c.insets = new Insets(0, 16, 0, 16);
        cartoes.add(Tema.rotulo("vs", Tema.titulo(22), new Color(0x6b5d48)), c);

        c.gridx = 2;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 0, 0);
        cartoes.add(criarCartaoInimigo(), c);

        registro.setEditable(false);
        registro.setLineWrap(true);
        registro.setWrapStyleWord(true);
        registro.setFont(Tema.texto(14));
        registro.setBackground(Tema.FUNDO_ESCURO);
        registro.setForeground(Tema.TEXTO);
        registro.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JScrollPane rolagem = Tema.rolagem(registro, Tema.BORDA);

        JPanel centro = new JPanel(new BorderLayout(0, 18));
        centro.setOpaque(false);
        centro.add(cartoes, BorderLayout.NORTH);
        centro.add(rolagem, BorderLayout.CENTER);
        return centro;
    }

    private JPanel criarCartaoJogador() {
        JPanel cartao = criarCartao(Tema.BORDA);
        cartao.add(alinhar(nomeJogador));
        cartao.add(alinhar(infoJogador));
        cartao.add(Box.createVerticalStrut(14));
        cartao.add(vidaJogador);
        cartao.add(Box.createVerticalStrut(10));
        cartao.add(energiaJogador);
        return cartao;
    }

    private JPanel criarCartaoInimigo() {
        categoriaInimigo.setOpaque(true);
        categoriaInimigo.setBackground(Tema.VIDA_FUNDO);
        categoriaInimigo.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        JPanel linhaNome = new JPanel(new BorderLayout(10, 0));
        linhaNome.setOpaque(false);
        linhaNome.add(nomeInimigo, BorderLayout.WEST);
        linhaNome.add(categoriaInimigo, BorderLayout.EAST);
        linhaNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JPanel cartao = criarCartao(INIMIGO_BORDA);
        cartao.add(alinhar(linhaNome));
        cartao.add(alinhar(infoInimigo));
        cartao.add(Box.createVerticalStrut(14));
        cartao.add(vidaInimigo);
        cartao.add(Box.createVerticalGlue());
        return cartao;
    }

    private JPanel criarCartao(Color borda) {
        JPanel cartao = new JPanel() {
            // Largura preferida fixa: assim os dois cartoes dividem o espaco por igual,
            // mesmo quando o nome do inimigo e comprido
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, super.getPreferredSize().height);
            }
        };
        cartao.setLayout(new BoxLayout(cartao, BoxLayout.Y_AXIS));
        cartao.setBackground(Tema.PAINEL);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borda, 1, true),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        return cartao;
    }

    private JPanel criarRodape() {
        JButton botaoAtacar = Tema.botaoSecundario("Ataque comum");
        botaoAtacar.addActionListener(e -> atacar());
        botaoHabilidade.addActionListener(e -> usarHabilidade());
        botaoItem.addActionListener(e -> abrirMenuDeItens());

        JPanel acoes = new JPanel(new GridLayout(1, 3, 14, 0));
        acoes.setOpaque(false);
        acoes.add(botaoAtacar);
        acoes.add(botaoHabilidade);
        acoes.add(botaoItem);

        JButton botaoContinuar = Tema.botaoPrincipal("Continuar");
        botaoContinuar.addActionListener(e -> janela.finalizarBatalha(batalha));
        JPanel fim = new JPanel(new BorderLayout());
        fim.setOpaque(false);
        fim.add(resultadoFinal, BorderLayout.CENTER);
        fim.add(botaoContinuar, BorderLayout.EAST);

        rodape.setOpaque(false);
        rodape.setPreferredSize(new Dimension(0, 64));
        rodape.add(acoes, ACOES);
        rodape.add(fim, FIM);
        return rodape;
    }

    // No BoxLayout todos os filhos precisam do mesmo alinhamento, senao ficam desencontrados
    private static JComponent alinhar(JComponent componente) {
        componente.setAlignmentX(Component.LEFT_ALIGNMENT);
        return componente;
    }
}