package rpg.app;

import java.util.ArrayList;
import java.util.Scanner;

import rpg.combate.SistemaCombate;
import rpg.item.Arma;
import rpg.item.Inventario;
import rpg.item.Item;
import rpg.item.Pocao;
import rpg.personagem.Arqueiro;
import rpg.personagem.Chefe;
import rpg.personagem.Guerreiro;
import rpg.personagem.Jogador;
import rpg.personagem.Mago;
import rpg.personagem.Monstro;
import rpg.util.Entrada;

public class Jogo {
    private Entrada entrada;
    private Jogador jogador;
    private String classeJogador;
    private Arma armaReforcada;
    private ArrayList<Monstro> inimigos;
    private int etapa;

    public Jogo() {
        this.entrada = new Entrada(new Scanner(System.in));
    }

    public void iniciar() {
        System.out.println("==========================================");
        System.out.println("          A FORTALEZA TOMADA");
        System.out.println("==========================================");

        while (true) {
            System.out.println();
            System.out.println("=== MENU INICIAL ===");
            System.out.println("1 - Novo jogo");
            System.out.println("0 - Sair");
            int opcao = entrada.lerOpcao("Opcao: ", 0, 1);
            if (opcao == 0) {
                System.out.println("Ate a proxima, aventureiro!");
                return;
            }
            novaPartida();
            menuAventura();
        }
    }

    private void novaPartida() {
        System.out.println();
        System.out.println("=== CRIACAO DO PERSONAGEM ===");
        String nome = entrada.lerTexto("Nome do aventureiro: ");

        System.out.println("Escolha a especializacao:");
        System.out.println("1 - Guerreiro (Vida 140, Ataque 18, Defesa 8, Energia 30 - Golpe poderoso)");
        System.out.println("2 - Mago (Vida 100, Ataque 22, Defesa 4, Energia 50 - Bola de fogo)");
        System.out.println("3 - Arqueiro (Vida 120, Ataque 20, Defesa 6, Energia 40 - Tiro preciso)");
        int escolha = entrada.lerOpcao("Especializacao: ", 1, 3);

        Arma armaInicial;
        switch (escolha) {
            case 1:
                jogador = new Guerreiro(nome);
                classeJogador = "Guerreiro";
                armaInicial = new Arma("Espada curta", "Arma inicial", 4);
                armaReforcada = new Arma("Espada reforcada", "Recompensa do corredor", 8);
                break;
            case 2:
                jogador = new Mago(nome);
                classeJogador = "Mago";
                armaInicial = new Arma("Cajado", "Arma inicial", 4);
                armaReforcada = new Arma("Cajado reforcado", "Recompensa do corredor", 8);
                break;
            default:
                jogador = new Arqueiro(nome);
                classeJogador = "Arqueiro";
                armaInicial = new Arma("Arco", "Arma inicial", 4);
                armaReforcada = new Arma("Arco reforcado", "Recompensa do corredor", 8);
                break;
        }

        jogador.getInventario().adicionar(armaInicial);
        jogador.equiparArma(armaInicial);
        jogador.getInventario().adicionar(new Pocao("Pocao de cura", "Frasco de liquido vermelho", 30));
        jogador.getInventario().adicionar(new Pocao("Pocao de cura", "Frasco de liquido vermelho", 30));

        inimigos = new ArrayList<Monstro>();
        inimigos.add(new Monstro("Goblin da entrada", 50, 12, 3));
        inimigos.add(new Monstro("Esqueleto do corredor", 75, 16, 5));
        inimigos.add(new Chefe("Guardiao da Fortaleza", 130, 20, 7));
        etapa = 0;

        System.out.println();
        System.out.println("Uma antiga fortaleza foi tomada por criaturas.");
        System.out.println(nome + " precisa atravessa-la e derrotar o seu guardiao.");
    }

    private void menuAventura() {
        while (true) {
            Monstro proximo = inimigos.get(etapa);
            System.out.println();
            System.out.println("=== AVENTURA - proxima etapa: " + (etapa + 1) + " de " + inimigos.size()
                    + " (" + proximo.getNome() + ") ===");
            System.out.println("1 - Consultar personagem");
            System.out.println("2 - Consultar inventario e trocar equipamento");
            System.out.println("3 - Iniciar a proxima batalha");
            System.out.println("0 - Encerrar a aventura");
            int opcao = entrada.lerOpcao("Opcao: ", 0, 3);

            if (opcao == 1) {
                consultarPersonagem();
            } else if (opcao == 2) {
                consultarInventario();
            } else if (opcao == 3) {
                SistemaCombate combate = new SistemaCombate(jogador, proximo, entrada);
                boolean venceu = combate.executar();
                if (!venceu) {
                    System.out.println();
                    System.out.println("A fortaleza continua tomada. Fim da aventura.");
                    return;
                }
                concederRecompensa();
                if (etapa == inimigos.size()) {
                    System.out.println();
                    System.out.println("*** O Guardiao caiu. A fortaleza foi libertada! ***");
                    System.out.println("Parabens, " + jogador.getNome() + "!");
                    return;
                }
            } else {
                System.out.println("Aventura encerrada.");
                return;
            }
        }
    }

    private void concederRecompensa() {
        if (etapa == 0) {
            jogador.getInventario().adicionar(new Pocao("Pocao de cura", "Frasco de liquido vermelho", 30));
            System.out.println("Recompensa: uma Pocao de cura foi adicionada ao inventario.");
        } else if (etapa == 1) {
            jogador.getInventario().adicionar(armaReforcada);
            System.out.println("Recompensa: " + armaReforcada.getNome() + " (+" + armaReforcada.getBonusAtaque()
                    + " de ataque). Equipe pelo inventario antes da proxima batalha.");
        }
        etapa++;

        jogador.restaurar();
        if (etapa < inimigos.size()) {
            System.out.println("Vida e energia restauradas.");
        }
    }

    private void consultarPersonagem() {
        Arma arma = jogador.getArmaEquipada();
        int bonus = 0;
        String nomeArma = "nenhuma";
        if (arma != null) {
            bonus = arma.getBonusAtaque();
            nomeArma = arma.getNome();
        }
        System.out.println();
        System.out.println("Nome: " + jogador.getNome() + " (" + classeJogador + ")");
        System.out.println("Vida: " + jogador.getVidaAtual() + "/" + jogador.getVidaMaxima());
        System.out.println("Energia: " + jogador.getEnergiaAtual() + "/" + jogador.getEnergiaMaxima());
        System.out.println("Ataque: " + jogador.getAtaqueBase() + " + " + bonus + " = " + jogador.getAtaqueTotal());
        System.out.println("Defesa: " + jogador.getDefesa());
        System.out.println("Habilidade: " + jogador.getNomeHabilidade() + " (custo " + jogador.getCustoHabilidade() + ")");
        System.out.println("Arma: " + nomeArma);
    }

    private void consultarInventario() {
        Inventario inventario = jogador.getInventario();
        while (true) {
            System.out.println();
            System.out.println("=== INVENTARIO ===");
            for (int i = 0; i < inventario.tamanho(); i++) {
                Item item = inventario.getItem(i);
                if (item == jogador.getArmaEquipada()) {
                    System.out.println((i + 1) + " - " + item + " [equipada]");
                } else {
                    System.out.println((i + 1) + " - " + item);
                }
            }
            System.out.println("Digite o numero de uma arma para equipar, ou 0 para voltar.");
            int opcao = entrada.lerOpcao("Opcao: ", 0, inventario.tamanho());
            if (opcao == 0) {
                return;
            }

            Item escolhido = inventario.getItem(opcao - 1);
            if (escolhido instanceof Arma) {
                jogador.equiparArma((Arma) escolhido);
                System.out.println("Arma equipada: " + escolhido.getNome() + ". Ataque total: " + jogador.getAtaqueTotal());
            } else {
                System.out.println("So armas podem ser equipadas. Pocoes sao usadas durante o combate.");
            }
        }
    }
}
