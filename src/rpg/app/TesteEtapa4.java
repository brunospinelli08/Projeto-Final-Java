package rpg.app;

import java.util.Scanner;

import rpg.combate.SistemaCombate;
import rpg.item.Arma;
import rpg.item.Pocao;
import rpg.personagem.Chefe;
import rpg.personagem.Guerreiro;
import rpg.personagem.Jogador;
import rpg.personagem.Mago;
import rpg.personagem.Monstro;
import rpg.util.Entrada;

public class TesteEtapa4 {
    public static void main(String[] args) {
        Entrada entrada = new Entrada(new Scanner(System.in));

        System.out.println("1 - Guerreiro armado x Goblin (testar vitoria)");
        System.out.println("2 - Mago sem arma x Guardiao (testar derrota)");
        int cenario = entrada.lerOpcao("Cenario: ", 1, 2);

        Jogador jogador;
        Monstro inimigo;
        if (cenario == 1) {
            jogador = new Guerreiro("Aldric");
            Arma espada = new Arma("Espada curta", "Lamina simples", 4);
            jogador.getInventario().adicionar(espada);
            jogador.equiparArma(espada);
            inimigo = new Monstro("Goblin da entrada", 50, 12, 3);
        } else {
            jogador = new Mago("Merlin");
            inimigo = new Chefe("Guardiao da Fortaleza", 130, 20, 7);
        }
        jogador.getInventario().adicionar(new Pocao("Pocao de cura", "Frasco vermelho", 30));
        jogador.getInventario().adicionar(new Pocao("Pocao de cura", "Frasco vermelho", 30));

        SistemaCombate combate = new SistemaCombate(jogador, inimigo, entrada);
        boolean venceu = combate.executar();
        System.out.println();
        if (venceu) {
            System.out.println("RESULTADO: vitoria");
        } else {
            System.out.println("RESULTADO: derrota");
        }
        System.out.println(jogador);
    }
}