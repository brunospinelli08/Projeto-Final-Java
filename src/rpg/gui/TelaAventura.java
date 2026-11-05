package rpg.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import rpg.app.Aventura;
import rpg.item.Arma;
import rpg.item.Item;
import rpg.personagem.Jogador;
import rpg.personagem.Monstro;

// Tela 2: menu entre as batalhas. Mostra o personagem e o inventario e deixa trocar a arma
public class TelaAventura extends JPanel {
    private static final Color VENCIDA_FUNDO = new Color(0x2a2a1d);
    private static final Color VENCIDA_BORDA = new Color(0x5d6b3d);
    private static final Color VENCIDA_TEXTO = new Color(0xc8d6a0);
    private static final Color AVISO_BORDA = new Color(0x6b5630);
    private static final Color ITEM_FUNDO = new Color(0x1b1712);
    private static final String DICA = "Selecione uma arma e clique em Equipar, ou enfrente o próximo inimigo.";

    private final JanelaJogo janela;
    private Aventura aventura;

    private final JLabel rotuloEtapa = Tema.rotulo(" ", Tema.negrito(12), Tema.DOURADO);
    private final JLabel nomeInimigo = Tema.rotulo(" ", Tema.titulo(30), Tema.TEXTO);
    private final JLabel descricaoLocal = Tema.rotulo(" ", Tema.texto(14), Tema.TEXTO_SUAVE);
    private final JPanel progresso = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
    private final JLabel aviso = Tema.rotulo(" ", Tema.texto(14), Tema.TEXTO);

    private final JLabel nomeJogador = Tema.rotulo(" ", Tema.titulo(24), Tema.TEXTO);
    private final JLabel classeJogador = Tema.rotulo(" ", Tema.texto(12), Tema.TEXTO_SUAVE);
    private final BarraStatus barraVida = new BarraStatus("Vida", Tema.VIDA, Tema.VIDA_FUNDO);
    private final BarraStatus barraEnergia = new BarraStatus("Energia", Tema.ENERGIA, Tema.ENERGIA_FUNDO);
    private final JLabel valorAtaque = Tema.rotulo(" ", Tema.texto(14), Tema.TEXTO);
    private final JLabel valorDefesa = Tema.rotulo(" ", Tema.texto(14), Tema.TEXTO);
    private final JLabel valorHabilidade = Tema.rotulo(" ", Tema.texto(14), Tema.TEXTO);
    private final JLabel valorArma = Tema.rotulo(" ", Tema.texto(14), Tema.TEXTO);

    private final DefaultListModel<Item> modeloItens = new DefaultListModel<>();
    private final JList<Item> listaItens = new JList<>(modeloItens);
    private final JLabel contagemItens = Tema.rotulo(" ", Tema.texto(13), Tema.TEXTO_FRACO);
    private final JButton botaoEnfrentar = Tema.botaoPrincipal("Enfrentar");

    public TelaAventura(JanelaJogo janela) {
        this.janela = janela;

        setLayout(new BorderLayout(0, 20));
        setBackground(Tema.FUNDO);
        setBorder(BorderFactory.createEmptyBorder(26, 36, 28, 36));
        add(criarTopo(), BorderLayout.NORTH);
        add(criarCentro(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    // Chamado pela janela sempre que esta tela aparece. As mensagens sao as recompensas da ultima vitoria
    public void mostrar(Aventura aventura, List<String> mensagens) {
        this.aventura = aventura;
        listaItens.clearSelection();
        mostrarAviso(mensagens.isEmpty() ? DICA : String.join(" ", mensagens));
        atualizar();
    }

    // Le tudo de novo da Aventura e do Jogador. A tela nunca guarda uma copia dos numeros
    private void atualizar() {
        Jogador jogador = aventura.getJogador();
        Monstro proximo = aventura.getProximoInimigo();

        rotuloEtapa.setText("PRÓXIMA ETAPA · " + (aventura.getEtapaAtual() + 1) + " DE " + aventura.getTotalEtapas());
        nomeInimigo.setText(proximo.getNome());
        descricaoLocal.setText("<html><body style='width: 470px'>" + aventura.getDescricaoLocal() + "</body></html>");
        atualizarProgresso();

        nomeJogador.setText(jogador.getNome());
        classeJogador.setText(jogador.getClass().getSimpleName());
        barraVida.setValores(jogador.getVidaAtual(), jogador.getVidaMaxima());
        barraEnergia.setValores(jogador.getEnergiaAtual(), jogador.getEnergiaMaxima());

        Arma arma = jogador.getArmaEquipada();
        int bonus = (arma == null) ? 0 : arma.getBonusAtaque();
        valorAtaque.setText(jogador.getAtaqueBase() + " + " + bonus + " = " + jogador.getAtaqueTotal());
        valorDefesa.setText(String.valueOf(jogador.getDefesa()));
        valorHabilidade.setText(jogador.getNomeHabilidade() + " · " + jogador.getCustoHabilidade() + " de energia");
        valorArma.setText((arma == null) ? "nenhuma" : arma.getNome());

        modeloItens.clear();
        for (Item item : jogador.getInventario().getItens()) {
            modeloItens.addElement(item);
        }
        contagemItens.setText(modeloItens.size() + " itens");
        botaoEnfrentar.setText("Enfrentar " + proximo.getNome());

        revalidate();
        repaint();
    }

    // Uma "pilula" por etapa: vencida, proxima ou futura
    private void atualizarProgresso() {
        progresso.removeAll();
        for (int i = 0; i < aventura.getTotalEtapas(); i++) {
            JLabel pilula;
            if (i < aventura.getEtapaAtual()) {
                pilula = criarPilula((i + 1) + " · vencida", VENCIDA_FUNDO, VENCIDA_BORDA, VENCIDA_TEXTO);
            } else if (i == aventura.getEtapaAtual()) {
                pilula = criarPilula((i + 1) + " · próxima", Tema.PAINEL_ATIVO, Tema.DOURADO, Tema.TEXTO);
            } else {
                pilula = criarPilula(String.valueOf(i + 1), Tema.FUNDO, Tema.BORDA, Tema.TEXTO_FRACO);
            }
            progresso.add(pilula);
        }
    }

    private JLabel criarPilula(String texto, Color fundo, Color borda, Color letra) {
        JLabel pilula = Tema.rotulo(texto, Tema.texto(13), letra);
        pilula.setOpaque(true);
        pilula.setBackground(fundo);
        pilula.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borda, 1, true),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        return pilula;
    }

    private void mostrarAviso(String texto) {
        aviso.setText("<html>" + texto + "</html>");
    }

    // Equipa a arma selecionada. A regra (so arma do inventario, sem acumular bonus) fica no Jogador
    private void equiparSelecionada() {
        Item item = listaItens.getSelectedValue();
        if (item == null) {
            mostrarAviso("Selecione uma arma na lista.");
            return;
        }
        if (!(item instanceof Arma)) {
            mostrarAviso("Só armas podem ser equipadas. Poções são usadas durante a batalha.");
            return;
        }
        Jogador jogador = aventura.getJogador();
        jogador.equiparArma((Arma) item);

        int posicao = listaItens.getSelectedIndex();
        atualizar();
        listaItens.setSelectedIndex(posicao);
        mostrarAviso("Arma equipada: " + item.getNome() + ". Ataque total: " + jogador.getAtaqueTotal() + ".");
    }

    // ---------- montagem da tela ----------

    private JPanel criarTopo() {
        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        textos.add(rotuloEtapa);
        textos.add(Box.createVerticalStrut(6));
        textos.add(nomeInimigo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(descricaoLocal);

        progresso.setOpaque(false);
        JPanel cabecalho = new JPanel(new BorderLayout(24, 0));
        cabecalho.setOpaque(false);
        cabecalho.add(textos, BorderLayout.WEST);
        cabecalho.add(progresso, BorderLayout.EAST);

        JPanel caixaAviso = new JPanel(new BorderLayout());
        caixaAviso.setBackground(Tema.PAINEL_ATIVO);
        caixaAviso.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AVISO_BORDA, 1, true),
                BorderFactory.createEmptyBorder(11, 16, 11, 16)));
        caixaAviso.add(aviso, BorderLayout.CENTER);

        JPanel topo = new JPanel(new BorderLayout(0, 16));
        topo.setOpaque(false);
        topo.add(cabecalho, BorderLayout.CENTER);
        topo.add(caixaAviso, BorderLayout.SOUTH);
        return topo;
    }

    private JPanel criarCentro() {
        JPanel centro = new JPanel(new BorderLayout(20, 0));
        centro.setOpaque(false);
        centro.add(criarPainelPersonagem(), BorderLayout.WEST);
        centro.add(criarPainelInventario(), BorderLayout.CENTER);
        return centro;
    }

    private JPanel criarPainelPersonagem() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Tema.PAINEL);
        painel.setBorder(Tema.bordaPainel());
        painel.setPreferredSize(new Dimension(340, 0));

        classeJogador.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDA_FORTE, 1, true),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)));
        JPanel linhaNome = new JPanel(new BorderLayout());
        linhaNome.setOpaque(false);
        linhaNome.add(nomeJogador, BorderLayout.WEST);
        linhaNome.add(classeJogador, BorderLayout.EAST);
        linhaNome.setAlignmentX(Component.LEFT_ALIGNMENT);
        linhaNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel atributos = new JPanel(new GridBagLayout());
        atributos.setOpaque(false);
        adicionarAtributo(atributos, 0, "Ataque", valorAtaque);
        adicionarAtributo(atributos, 1, "Defesa", valorDefesa);
        adicionarAtributo(atributos, 2, "Habilidade", valorHabilidade);
        adicionarAtributo(atributos, 3, "Arma", valorArma);
        atributos.setAlignmentX(Component.LEFT_ALIGNMENT);
        atributos.setMaximumSize(new Dimension(Integer.MAX_VALUE, atributos.getPreferredSize().height));

        painel.add(linhaNome);
        painel.add(Box.createVerticalStrut(18));
        painel.add(barraVida);
        painel.add(Box.createVerticalStrut(12));
        painel.add(barraEnergia);
        painel.add(Box.createVerticalStrut(20));
        painel.add(atributos);
        painel.add(Box.createVerticalGlue());
        return painel;
    }

    private void adicionarAtributo(JPanel grade, int linha, String nome, JLabel valor) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = linha;
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.insets = new Insets(0, 0, 10, 16);
        grade.add(Tema.rotulo(nome, Tema.texto(14), Tema.TEXTO_FRACO), c);

        c.gridx = 1;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 10, 0);
        grade.add(valor, c);
    }

    private JPanel criarPainelInventario() {
        JPanel painel = new JPanel(new BorderLayout(0, 12));
        painel.setBackground(Tema.PAINEL);
        painel.setBorder(Tema.bordaPainel());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.add(Tema.rotulo("Inventário", Tema.titulo(20), Tema.TEXTO), BorderLayout.WEST);
        cabecalho.add(contagemItens, BorderLayout.EAST);

        listaItens.setBackground(Tema.PAINEL);
        listaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaItens.setCellRenderer(criarDesenhoDoItem());
        listaItens.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // clique duplo tambem equipa
                    equiparSelecionada();
                }
            }
        });
        JScrollPane rolagem = new JScrollPane(listaItens);
        rolagem.setBorder(null);
        rolagem.getViewport().setBackground(Tema.PAINEL);

        JButton botaoEquipar = Tema.botaoSecundario("Equipar arma selecionada");
        botaoEquipar.addActionListener(e -> equiparSelecionada());
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.add(botaoEquipar, BorderLayout.EAST);

        painel.add(cabecalho, BorderLayout.NORTH);
        painel.add(rolagem, BorderLayout.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);
        return painel;
    }

    // Diz ao JList como desenhar cada item: nome, tipo e efeito, e a marca EQUIPADA
    private ListCellRenderer<Item> criarDesenhoDoItem() {
        return (lista, item, indice, selecionado, focado) -> {
            boolean equipada = (item == aventura.getJogador().getArmaEquipada());

            JLabel texto = Tema.rotulo("<html>" + item.getNome() + " <font color='#b3a58c'>· "
                    + item.getTipo() + " · " + item.getEfeito() + "</font></html>", Tema.texto(14), Tema.TEXTO);
            JLabel marca = Tema.rotulo(equipada ? "EQUIPADA" : "", Tema.negrito(11), Tema.DOURADO);

            JPanel caixa = new JPanel(new BorderLayout(12, 0));
            caixa.setBackground(selecionado ? Tema.PAINEL_ATIVO : ITEM_FUNDO);
            caixa.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(selecionado ? Tema.DOURADO : Tema.BORDA, 1, true),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            caixa.add(texto, BorderLayout.CENTER);
            caixa.add(marca, BorderLayout.EAST);

            JPanel espaco = new JPanel(new BorderLayout()); // so para deixar 6 px entre um item e outro
            espaco.setOpaque(false);
            espaco.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
            espaco.add(caixa);
            return espaco;
        };
    }

    private JPanel criarRodape() {
        JButton botaoEncerrar = Tema.botaoSecundario("Encerrar aventura");
        botaoEncerrar.addActionListener(e -> janela.mostrarCriacao());
        botaoEnfrentar.addActionListener(e -> janela.iniciarBatalha());

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.add(botaoEncerrar, BorderLayout.WEST);
        rodape.add(botaoEnfrentar, BorderLayout.EAST);
        return rodape;
    }
}