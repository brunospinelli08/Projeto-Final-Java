package rpg.gui;

import javax.swing.SwingUtilities;

// Inicia a versao com janela. A versao de console continua em rpg.app.Main
public class MainGui {
    public static void main(String[] args) {
        // Toda tela Swing deve ser criada na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> new JanelaJogo().setVisible(true));
    }
}