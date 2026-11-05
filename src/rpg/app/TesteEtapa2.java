package rpg.app;

import rpg.personagem.Arqueiro;
import rpg.personagem.Guerreiro;
import rpg.personagem.Jogador;
import rpg.personagem.Mago;
import rpg.personagem.Monstro;

public class TesteEtapa2 {
    public static void main(String[] args) {
        // Referencias do tipo base Jogador: polimorfismo
        Jogador guerreiro = new Guerreiro("Teste G");
        Jogador mago = new Mago("Teste M");
        Jogador arqueiro = new Arqueiro("Teste A");

        System.out.println("--- Cada um usa a SUA habilidade pela mesma chamada ---");
        Jogador[] jogadores = {guerreiro, mago, arqueiro};
        for (Jogador j : jogadores) {
            System.out.println(j.getNome() + " -> " + j.getNomeHabilidade() + " (custo " + j.getCustoHabilidade() + ")");
        }

        System.out.println("--- Dano das habilidades no Goblin (defesa 3) ---");
        Monstro goblin1 = new Monstro("Goblin", 50, 12, 3);
        guerreiro.usarHabilidade(goblin1);
        conferir("Golpe poderoso: 2*18 - 3", 50 - goblin1.getVidaAtual(), 33);
        conferir("Energia do Guerreiro 30 - 10", guerreiro.getEnergiaAtual(), 20);

        Monstro goblin2 = new Monstro("Goblin", 50, 12, 3);
        mago.usarHabilidade(goblin2);
        conferir("Bola de fogo: 22 + 12 - 3/2", 50 - goblin2.getVidaAtual(), 33);
        conferir("Energia do Mago 50 - 12", mago.getEnergiaAtual(), 38);

        Monstro goblin3 = new Monstro("Goblin", 50, 12, 3);
        arqueiro.usarHabilidade(goblin3);
        conferir("Tiro preciso: 20 + 4, ignora defesa", 50 - goblin3.getVidaAtual(), 24);
        conferir("Energia do Arqueiro 40 - 8", arqueiro.getEnergiaAtual(), 32);

        System.out.println("--- Sem energia ---");
        Monstro boneco = new Monstro("Boneco", 999, 1, 0);
        guerreiro.usarHabilidade(boneco); // energia 20 -> 10
        guerreiro.usarHabilidade(boneco); // energia 10 -> 0
        int vidaAntes = boneco.getVidaAtual();
        conferir("Habilidade sem energia retorna false", guerreiro.usarHabilidade(boneco), false);
        conferir("Energia nao fica negativa", guerreiro.getEnergiaAtual(), 0);
        conferir("Sem dano quando recusada", boneco.getVidaAtual(), vidaAntes);
        conferir("Ataque comum funciona sem energia (18 - 0)", guerreiro.atacar(boneco), 18);

        System.out.println("--- Restaurar entre batalhas ---");
        guerreiro.receberDano(50);
        guerreiro.restaurar();
        conferir("Vida volta ao maximo", guerreiro.getVidaAtual(), 140);
        conferir("Energia volta ao maximo", guerreiro.getEnergiaAtual(), 30);
    }

    private static void conferir(String descricao, int obtido, int esperado) {
        String status = obtido == esperado ? "OK    " : "FALHOU";
        System.out.println(status + " " + descricao + ": obtido " + obtido + ", esperado " + esperado);
    }

    private static void conferir(String descricao, boolean obtido, boolean esperado) {
        String status = obtido == esperado ? "OK    " : "FALHOU";
        System.out.println(status + " " + descricao + ": obtido " + obtido + ", esperado " + esperado);
    }
}