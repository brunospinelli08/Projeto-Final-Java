package rpg.app;

import rpg.personagem.Chefe;
import rpg.personagem.Monstro;
import rpg.personagem.Personagem;

public class TesteEtapa1 {
    public static void main(String[] args) {
        Monstro goblin = new Monstro("Goblin", 50, 12, 3);
        Monstro treino = new Monstro("Boneco", 999, 22, 8);
        Personagem chefe = new Chefe("Guardiao", 130, 20, 7);

        conferir("Dano comum no Goblin", treino.atacar(goblin), 19);
        conferir("Vida do Goblin apos o ataque", goblin.getVidaAtual(), 31);
        conferir("Cura efetiva (faltavam 19)", goblin.recuperarVida(30), 19);
        conferir("Vida apos a cura", goblin.getVidaAtual(), 50);

        treino.atacar(goblin);
        treino.atacar(goblin);
        treino.atacar(goblin);
        conferir("Vida nao fica negativa", goblin.getVidaAtual(), 0);
        conferir("Goblin derrotado (1 = sim)", goblin.estaVivo() ? 0 : 1, 1);
        conferir("Cura em derrotado", goblin.recuperarVida(30), 0);

        conferir("Dano do chefe via Personagem (20+4-8)", chefe.atacar(treino), 16);
        conferir("Dano minimo 1", new Monstro("Fraco", 10, 1, 0).atacar(treino), 1);
    }

    private static void conferir(String descricao, int obtido, int esperado) {
        String status = obtido == esperado ? "OK    " : "FALHOU";
        System.out.println(status + " " + descricao + ": obtido " + obtido + ", esperado " + esperado);
    }
}

