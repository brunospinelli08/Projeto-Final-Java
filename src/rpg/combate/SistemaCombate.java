package rpg.combate;

import rpg.item.Consumivel;
import rpg.item.Inventario;
import rpg.item.Item;
import rpg.personagem.Jogador;
import rpg.personagem.Monstro;
import rpg.util.Entrada;

public class SistemaCombate {
    private Jogador jogador;
    private Monstro inimigo;
    private Entrada entrada;

    public SistemaCombate(Jogador jogador, Monstro inimigo, Entrada entrada) {
        this.jogador = jogador;
        this.inimigo = inimigo;
        this.entrada = entrada;
    }

    public boolean executar() {
        System.out.println();
        System.out.println("=== Batalha: " + jogador.getNome() + " x " + inimigo.getNome() + " ===");

        while (true) {
            mostrarStatus();
            boolean acaoFeita = escolherAcao();
            if (!acaoFeita) {
                continue;
            }

            if (!inimigo.estaVivo()) {
                System.out.println(inimigo.getNome() + " foi derrotado!");
                return true;
            }

            int dano = inimigo.atacar(jogador);
            System.out.println(inimigo.getNome() + " ataca e causa " + dano + " de dano.");

            if (!jogador.estaVivo()) {
                System.out.println(jogador.getNome() + " foi derrotado...");
                return false;
            }
        }
    }

    private void mostrarStatus() {
        System.out.println();
        System.out.println(jogador.getNome() + " - Vida " + jogador.getVidaAtual() + "/" + jogador.getVidaMaxima()
                + " | Energia " + jogador.getEnergiaAtual() + "/" + jogador.getEnergiaMaxima());
        System.out.println(inimigo.getNome() + " - Vida " + inimigo.getVidaAtual() + "/" + inimigo.getVidaMaxima());
        System.out.println("1 - Ataque comum");
        System.out.println("2 - " + jogador.getNomeHabilidade() + " (custo " + jogador.getCustoHabilidade() + " de energia)");
        System.out.println("3 - Usar item");
    }

    private boolean escolherAcao() {
        int opcao = entrada.lerOpcao("Sua acao: ", 1, 3);
        switch (opcao) {
            case 1:
                int dano = jogador.atacar(inimigo);
                System.out.println(jogador.getNome() + " ataca e causa " + dano + " de dano.");
                return true;
            case 2:
                return usarHabilidade();
            default:
                return usarItem();
        }
    }

    private boolean usarHabilidade() {
        int vidaAntes = inimigo.getVidaAtual();
        if (!jogador.usarHabilidade(inimigo)) {
            System.out.println("Energia insuficiente para " + jogador.getNomeHabilidade() + ".");
            return false;
        }
        int dano = vidaAntes - inimigo.getVidaAtual();
        System.out.println(jogador.getNome() + " usa " + jogador.getNomeHabilidade() + " e causa " + dano + " de dano.");
        return true;
    }

    private boolean usarItem() {
        Inventario inventario = jogador.getInventario();
        if (inventario.estaVazio()) {
            System.out.println("Inventario vazio.");
            return false;
        }

        System.out.println("Itens:");
        for (int i = 0; i < inventario.tamanho(); i++) {
            System.out.println((i + 1) + " - " + inventario.getItem(i));
        }
        System.out.println("0 - Voltar");

        int opcao = entrada.lerOpcao("Item: ", 0, inventario.tamanho());
        if (opcao == 0) {
            return false;
        }

        Item item = inventario.getItem(opcao - 1);
        int vidaAntes = jogador.getVidaAtual();
        if (!jogador.usarItem(item)) {
            if (item instanceof Consumivel) {
                System.out.println("Sua vida ja esta cheia. O item nao foi usado.");
            } else {
                System.out.println(item.getNome() + " nao pode ser consumido.");
            }
            return false;
        }
        int curado = jogador.getVidaAtual() - vidaAntes;
        System.out.println(jogador.getNome() + " usa " + item.getNome() + " e recupera " + curado + " de vida.");
        return true;
    }
}
