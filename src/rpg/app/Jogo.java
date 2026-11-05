package rpg.app;

import java.util.Scanner;

import rpg.combate.Batalha;
import rpg.combate.SistemaCombate;
import rpg.item.Arma;
import rpg.item.Inventario;
import rpg.item.Item;
import rpg.personagem.Especializacao;
import rpg.personagem.Jogador;
import rpg.personagem.Monstro;
import rpg.util.Entrada;

// Versao de CONSOLE do jogo: menus e mensagens. As regras da partida ficam na Aventura
public class Jogo {
    private final Entrada entrada;
    private Aventura aventura;

    public Jogo() {
        this.entrada = new Entrada(new Scanner(System.in)); // unico Scanner do programa
    }

    // Menu inicial: novo jogo ou sair
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
        }
    }

    private void novaPartida() {
        System.out.println();
        System.out.println("=== CRIACAO DO PERSONAGEM ===");
        String nome = entrada.lerTexto("Nome do aventureiro: ");

        // A lista de classes vem do enum: nenhum texto de classe escrito a mao aqui
        Especializacao[] opcoes = Especializacao.values();
        System.out.println("Escolha a especializacao:");
        for (int i = 0; i < opcoes.length; i++) {
            System.out.println((i + 1) + " - " + opcoes[i].getNome() + " (" + opcoes[i].getResumo() + ")");
        }
        int escolha = entrada.lerOpcao("Especializacao: ", 1, opcoes.length);

        aventura = new Aventura(nome, opcoes[escolha - 1]); // tudo novo a cada partida

        System.out.println();
        System.out.println("Uma antiga fortaleza foi tomada por criaturas.");
        System.out.println(nome + " precisa atravessa-la e derrotar o seu guardiao.");
        menuAventura();
    }

    // Menu entre as batalhas. Sai quando a aventura termina (vitoria, derrota ou opcao 0)
    private void menuAventura() {
        while (true) {
            Monstro proximo = aventura.getProximoInimigo();
            System.out.println();
            System.out.println("=== AVENTURA - proxima etapa: " + (aventura.getEtapaAtual() + 1) + " de "
                    + aventura.getTotalEtapas() + " (" + proximo.getNome() + ") ===");
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
                boolean venceu = jogarBatalha();
                if (!venceu) {
                    System.out.println();
                    System.out.println("A fortaleza continua tomada. Fim da aventura.");
                    return;
                }
                if (aventura.foiConcluida()) {
                    System.out.println();
                    System.out.println("*** O Guardiao caiu. A fortaleza foi libertada! ***");
                    System.out.println("Parabens, " + aventura.getJogador().getNome() + "!");
                    return;
                }
            } else {
                System.out.println("Aventura encerrada.");
                return;
            }
        }
    }

    private boolean jogarBatalha() {
        System.out.println();
        System.out.println(aventura.getDescricaoLocal());
        Batalha batalha = aventura.iniciarBatalha();
        boolean venceu = new SistemaCombate(batalha, entrada).executar();
        if (venceu) {
            for (String mensagem : aventura.registrarVitoria(batalha)) {
                System.out.println(mensagem);
            }
        }
        return venceu;
    }

    private void consultarPersonagem() {
        Jogador jogador = aventura.getJogador();
        Arma arma = jogador.getArmaEquipada();
        int bonus = (arma == null) ? 0 : arma.getBonusAtaque();
        System.out.println();
        System.out.println("Nome: " + jogador.getNome() + " (" + jogador.getClass().getSimpleName() + ")");
        System.out.println("Vida: " + jogador.getVidaAtual() + "/" + jogador.getVidaMaxima());
        System.out.println("Energia: " + jogador.getEnergiaAtual() + "/" + jogador.getEnergiaMaxima());
        System.out.println("Ataque: " + jogador.getAtaqueBase() + " + " + bonus + " = " + jogador.getAtaqueTotal());
        System.out.println("Defesa: " + jogador.getDefesa());
        System.out.println("Habilidade: " + jogador.getNomeHabilidade() + " (custo " + jogador.getCustoHabilidade() + ")");
        System.out.println("Arma: " + ((arma == null) ? "nenhuma" : arma.getNome()));
    }

    private void consultarInventario() {
        Jogador jogador = aventura.getJogador();
        Inventario inventario = jogador.getInventario();
        while (true) {
            System.out.println();
            System.out.println("=== INVENTARIO ===");
            for (int i = 0; i < inventario.tamanho(); i++) {
                Item item = inventario.getItem(i);
                String marca = (item == jogador.getArmaEquipada()) ? " [equipada]" : "";
                System.out.println((i + 1) + " - " + item + marca);
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