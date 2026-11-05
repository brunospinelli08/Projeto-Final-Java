package rpg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

// Barra desenhada a mao: nome a esquerda, "atual / maximo" a direita e a barra embaixo.
// Serve para vida e energia, so mudando as cores
public class BarraStatus extends JComponent {
    private static final int ALTURA_BARRA = 10;

    private final String nome;
    private final Color cor;
    private final Color fundo;
    private int atual;
    private int maximo = 1;

    public BarraStatus(String nome, Color cor, Color fundo) {
        this.nome = nome;
        this.cor = cor;
        this.fundo = fundo;
        setFont(Tema.texto(13));
        setAlignmentX(LEFT_ALIGNMENT);
    }

    public void setValores(int atual, int maximo) {
        this.atual = Math.max(0, atual);
        this.maximo = Math.max(1, maximo);
        setToolTipText(nome + ": " + this.atual + " de " + this.maximo);
        repaint(); // pede para o Swing desenhar de novo com os valores novos
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 36);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, 36);
    }

    // O Swing chama este metodo sempre que precisa desenhar o componente
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(getFont());
        FontMetrics medidas = g2.getFontMetrics();

        int linhaTexto = medidas.getAscent();
        g2.setColor(Tema.TEXTO_SUAVE);
        g2.drawString(nome, 0, linhaTexto);

        String valor = atual + " / " + maximo;
        g2.setColor(Tema.TEXTO);
        g2.drawString(valor, getWidth() - medidas.stringWidth(valor), linhaTexto);

        int topoBarra = medidas.getHeight() + 6;
        g2.setColor(fundo);
        g2.fillRoundRect(0, topoBarra, getWidth(), ALTURA_BARRA, ALTURA_BARRA, ALTURA_BARRA);

        int preenchido = (int) Math.round(getWidth() * (atual / (double) maximo));
        if (preenchido > 0) {
            g2.setColor(cor);
            g2.fillRoundRect(0, topoBarra, preenchido, ALTURA_BARRA, ALTURA_BARRA, ALTURA_BARRA);
        }
        g2.dispose();
    }
}