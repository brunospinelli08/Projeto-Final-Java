package rpg.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

// Cores, fontes e componentes padronizados da interface. Nenhuma regra de jogo aqui
public final class Tema {
    public static final Color FUNDO = new Color(0x17140f);
    public static final Color FUNDO_ESCURO = new Color(0x120f0c);
    public static final Color PAINEL = new Color(0x221d17);
    public static final Color PAINEL_ATIVO = new Color(0x2c251c);
    public static final Color BORDA = new Color(0x3a3228);
    public static final Color BORDA_FORTE = new Color(0x4a4034);
    public static final Color TEXTO = new Color(0xefe7d6);
    public static final Color TEXTO_SUAVE = new Color(0xc9bca4);
    public static final Color TEXTO_FRACO = new Color(0xb3a58c);
    public static final Color DOURADO = new Color(0xd4a94e);
    public static final Color DOURADO_CLARO = new Color(0xe8c77a);
    public static final Color TEXTO_NO_DOURADO = new Color(0x1b160f);
    public static final Color VIDA = new Color(0xc0493d);
    public static final Color VIDA_FUNDO = new Color(0x3a2622);
    public static final Color ENERGIA = new Color(0x5b93d6);
    public static final Color ENERGIA_FUNDO = new Color(0x24303e);
    public static final Color ALERTA = new Color(0xf0b3aa);

    // Bloco estatico: roda uma vez, quando a classe Tema e usada pela primeira vez.
    // Troca o azul padrao do Swing na linha destacada dos menus pelas cores do tema
    static {
        UIManager.put("MenuItem.selectionBackground", PAINEL_ATIVO);
        UIManager.put("MenuItem.selectionForeground", DOURADO_CLARO);
    }

    private static final String SERIFA = escolherFonte("Georgia", Font.SERIF);
    private static final String SEM_SERIFA = escolherFonte("Segoe UI", Font.SANS_SERIF);

    private Tema() {
        // so constantes e metodos estaticos: ninguem cria um Tema
    }

    // Usa a fonte preferida se ela existir no computador; senao, a generica do Java
    private static String escolherFonte(String preferida, String reserva) {
        String[] instaladas = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        List<String> lista = Arrays.asList(instaladas);
        return lista.contains(preferida) ? preferida : reserva;
    }

    public static Font titulo(int tamanho) {
        return new Font(SERIFA, Font.PLAIN, tamanho);
    }

    public static Font texto(int tamanho) {
        return new Font(SEM_SERIFA, Font.PLAIN, tamanho);
    }

    public static Font negrito(int tamanho) {
        return new Font(SEM_SERIFA, Font.BOLD, tamanho);
    }

    public static JLabel rotulo(String texto, Font fonte, Color cor) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(fonte);
        rotulo.setForeground(cor);
        return rotulo;
    }

    public static Border bordaPainel() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA, 1, true),
                BorderFactory.createEmptyBorder(18, 20, 18, 20));
    }

    // Botao dourado, para a acao principal de cada tela
    public static JButton botaoPrincipal(String texto) {
        JButton botao = criarBotao(texto, DOURADO, TEXTO_NO_DOURADO, DOURADO);
        botao.setFont(negrito(16));
        botao.getModel().addChangeListener(e ->
                botao.setBackground(botao.getModel().isRollover() ? DOURADO_CLARO : DOURADO));
        return botao;
    }

    // Botao escuro com borda, para as outras acoes
    public static JButton botaoSecundario(String texto) {
        JButton botao = criarBotao(texto, PAINEL, TEXTO, BORDA_FORTE);
        botao.setFont(texto(15));
        botao.getModel().addChangeListener(e ->
                botao.setBackground(botao.getModel().isRollover() ? PAINEL_ATIVO : PAINEL));
        return botao;
    }

    // Area com rolagem e barra escura (a barra padrao do Java e clara e destoa do tema)
    public static JScrollPane rolagem(JComponent conteudo, Color borda) {
        JScrollPane rolagem = new JScrollPane(conteudo);
        rolagem.setBorder(BorderFactory.createLineBorder(borda, 1, true));
        rolagem.getViewport().setBackground(conteudo.getBackground());
        rolagem.getVerticalScrollBar().setUI(new BarraDeRolagemEscura());
        rolagem.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        rolagem.getVerticalScrollBar().setUnitIncrement(16);
        return rolagem;
    }

    // Heranca: muda so as cores e some com as setinhas da barra de rolagem padrao
    private static class BarraDeRolagemEscura extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            thumbColor = BORDA_FORTE;
            trackColor = FUNDO_ESCURO;
        }

        @Override
        protected JButton createDecreaseButton(int orientacao) {
            return botaoInvisivel();
        }

        @Override
        protected JButton createIncreaseButton(int orientacao) {
            return botaoInvisivel();
        }

        private JButton botaoInvisivel() {
            JButton botao = new JButton();
            botao.setPreferredSize(new Dimension(0, 0));
            return botao;
        }
    }

    private static JButton criarBotao(String texto, Color fundo, Color letra, Color borda) {
        JButton botao = new JButton(texto);
        botao.setBackground(fundo);
        botao.setForeground(letra);
        // A ordem importa: setContentAreaFilled(false) desliga o opaque, entao o opaque vem DEPOIS
        botao.setContentAreaFilled(false); // desliga o desenho padrao do Java
        botao.setOpaque(true);             // e pinta a nossa cor de fundo
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borda, 1, true),
                BorderFactory.createEmptyBorder(12, 26, 12, 26)));
        return botao;
    }
}