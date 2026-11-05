package rpg.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import rpg.personagem.Especializacao;
import rpg.personagem.Jogador;

// Tela 1: nome do aventureiro e escolha da especializacao
public class TelaCriacao extends JPanel {
    private final JanelaJogo janela;
    private final JTextField campoNome;
    private final JLabel descricaoHabilidade;
    private final JLabel mensagemErro;
    private final Map<JToggleButton, Especializacao> cartoes = new LinkedHashMap<>();

    public TelaCriacao(JanelaJogo janela) {
        this.janela = janela;
        this.campoNome = criarCampoNome();
        this.descricaoHabilidade = Tema.rotulo(" ", Tema.texto(13), Tema.TEXTO_FRACO);
        this.mensagemErro = Tema.rotulo(" ", Tema.texto(13), Tema.ALERTA);

        setLayout(new BorderLayout(48, 0));
        setBackground(Tema.FUNDO);
        setBorder(BorderFactory.createEmptyBorder(44, 48, 40, 48));
        add(criarColunaApresentacao(), BorderLayout.WEST);
        add(criarFormulario(), BorderLayout.CENTER);

        cartoes.keySet().iterator().next().setSelected(true); // Guerreiro comeca marcado
    }

    private JPanel criarColunaApresentacao() {
        JPanel coluna = new JPanel();
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
        coluna.setOpaque(false);
        coluna.setPreferredSize(new Dimension(290, 0));

        coluna.add(Tema.rotulo("RPG POR TURNOS", Tema.negrito(12), Tema.DOURADO));
        coluna.add(Box.createVerticalStrut(14));
        coluna.add(Tema.rotulo("<html>A Fortaleza<br>Tomada</html>", Tema.titulo(44), Tema.TEXTO));
        coluna.add(Box.createVerticalStrut(18));

        JPanel traco = new JPanel();
        traco.setBackground(Tema.DOURADO);
        traco.setMaximumSize(new Dimension(56, 2));
        traco.setPreferredSize(new Dimension(56, 2));
        traco.setAlignmentX(Component.LEFT_ALIGNMENT);
        coluna.add(traco);
        coluna.add(Box.createVerticalStrut(18));

        coluna.add(Tema.rotulo("<html><body style='width: 210px'>Uma antiga fortaleza foi tomada por criaturas. "
                        + "Atravesse a entrada, o corredor e a câmara final e derrote o guardião.</body></html>",
                Tema.texto(15), Tema.TEXTO_SUAVE));
        coluna.add(Box.createVerticalStrut(14));
        coluna.add(Tema.rotulo("<html><body style='width: 210px'>Escolha um nome e uma especialização para começar.</body></html>",
                Tema.texto(14), Tema.TEXTO_FRACO));
        return coluna;
    }

    private JPanel criarFormulario() {
        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setOpaque(false);

        JLabel rotuloNome = Tema.rotulo("Nome do aventureiro", Tema.negrito(13), Tema.TEXTO_SUAVE);
        rotuloNome.setLabelFor(campoNome);
        alinharEsquerda(formulario, rotuloNome);
        formulario.add(Box.createVerticalStrut(8));
        alinharEsquerda(formulario, campoNome);
        formulario.add(Box.createVerticalStrut(22));

        alinharEsquerda(formulario, Tema.rotulo("Especialização", Tema.negrito(13), Tema.TEXTO_SUAVE));
        formulario.add(Box.createVerticalStrut(10));
        alinharEsquerda(formulario, criarCartoes());
        formulario.add(Box.createVerticalStrut(12));
        alinharEsquerda(formulario, descricaoHabilidade);

        formulario.add(Box.createVerticalGlue());
        alinharEsquerda(formulario, mensagemErro);
        formulario.add(Box.createVerticalStrut(8));
        alinharEsquerda(formulario, criarRodape());
        return formulario;
    }

    private JTextField criarCampoNome() {
        JTextField campo = new JTextField();
        campo.setFont(Tema.texto(16));
        campo.setBackground(Tema.PAINEL);
        campo.setForeground(Tema.TEXTO);
        campo.setCaretColor(Tema.TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDA_FORTE, 1, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        campo.setPreferredSize(new Dimension(300, 44));
        campo.addActionListener(e -> comecar()); // Enter no campo tambem comeca
        return campo;
    }

    // Um cartao por especializacao, gerado a partir do enum: nenhuma classe escrita a mao aqui
    private JPanel criarCartoes() {
        JPanel grade = new JPanel(new GridLayout(1, 0, 14, 0));
        grade.setOpaque(false);
        grade.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));

        ButtonGroup grupo = new ButtonGroup(); // so um cartao marcado por vez
        for (Especializacao especializacao : Especializacao.values()) {
            JToggleButton cartao = criarCartao(especializacao);
            grupo.add(cartao);
            cartoes.put(cartao, especializacao);
            grade.add(cartao);
        }
        return grade;
    }

    private JToggleButton criarCartao(Especializacao especializacao) {
        // Le os numeros de um jogador de exemplo: a tela nao repete valores das classes
        Jogador modelo = especializacao.criarJogador(especializacao.getNome());
        String html = "<html>"
                + "<div style='font-family: " + Tema.titulo(20).getFamily() + "; font-size: 17pt'>" + especializacao.getNome() + "</div>"
                + "<div style='color: #d4a94e; font-size: 10pt'>" + modelo.getNomeHabilidade() + "</div><br>"
                + "<table cellpadding='1' cellspacing='0' style='font-size: 10pt; color: #c9bca4'>"
                + "<tr><td>Vida <b style='color: #efe7d6'>" + modelo.getVidaMaxima() + "</b>&nbsp;&nbsp;&nbsp;</td>"
                + "<td>Ataque <b style='color: #efe7d6'>" + modelo.getAtaqueBase() + "</b></td></tr>"
                + "<tr><td>Defesa <b style='color: #efe7d6'>" + modelo.getDefesa() + "</b></td>"
                + "<td>Energia <b style='color: #efe7d6'>" + modelo.getEnergiaMaxima() + "</b></td></tr>"
                + "</table><br>"
                + "<div style='color: #b3a58c; font-size: 9pt'>" + especializacao.getNomeArmaInicial() + " · +4 de ataque</div>"
                + "</html>";

        JToggleButton cartao = new JToggleButton(html);
        cartao.setFont(Tema.texto(13));
        cartao.setForeground(Tema.TEXTO);
        cartao.setVerticalAlignment(JToggleButton.TOP);
        cartao.setHorizontalAlignment(JToggleButton.LEFT);
        cartao.setContentAreaFilled(false); // primeiro desliga o desenho padrao
        cartao.setOpaque(true);             // depois liga a nossa cor de fundo
        cartao.setFocusPainted(false);
        cartao.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        cartao.getAccessibleContext().setAccessibleName(especializacao.getNome());
        pintarCartao(cartao, false);

        cartao.addItemListener(e -> {
            boolean marcado = e.getStateChange() == ItemEvent.SELECTED;
            pintarCartao(cartao, marcado);
            if (marcado) {
                descricaoHabilidade.setText("Habilidade: " + modelo.getNomeHabilidade()
                        + " · custa " + modelo.getCustoHabilidade() + " de energia");
            }
        });
        return cartao;
    }

    private void pintarCartao(JToggleButton cartao, boolean marcado) {
        cartao.setBackground(marcado ? Tema.PAINEL_ATIVO : Tema.PAINEL);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(marcado ? Tema.DOURADO : Tema.BORDA, 2, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        rodape.add(Tema.rotulo("Começa com a arma equipada e 2 poções de cura.", Tema.texto(13), Tema.TEXTO_FRACO),
                BorderLayout.WEST);

        JButton botaoComecar = Tema.botaoPrincipal("Começar aventura");
        botaoComecar.addActionListener(e -> comecar());
        rodape.add(botaoComecar, BorderLayout.EAST);
        return rodape;
    }

    // Valida o nome e pede para a janela criar a Aventura
    private void comecar() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            mensagemErro.setText("Digite um nome para o aventureiro.");
            campoNome.requestFocusInWindow();
            return;
        }
        mensagemErro.setText(" ");
        janela.iniciarAventura(nome, especializacaoEscolhida());
    }

    private Especializacao especializacaoEscolhida() {
        for (Map.Entry<JToggleButton, Especializacao> par : cartoes.entrySet()) {
            if (par.getKey().isSelected()) {
                return par.getValue();
            }
        }
        return Especializacao.GUERREIRO;
    }

    // Volta a tela ao estado inicial, para uma nova partida
    public void limpar() {
        campoNome.setText("");
        mensagemErro.setText(" ");
        cartoes.keySet().iterator().next().setSelected(true);
    }

    private static void alinharEsquerda(JPanel painel, Component componente) {
        if (componente instanceof javax.swing.JComponent) {
            ((javax.swing.JComponent) componente).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        painel.add(componente);
    }
}