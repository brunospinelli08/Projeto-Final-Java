package rpg.personagem;

public class Chefe extends Monstro {

    public Chefe(String nome, int vidaMaxima, int ataqueBase, int defesa) {
        super(nome, vidaMaxima, ataqueBase, defesa);
    }

    @Override
    public int atacar(Personagem alvo) {
        int dano = Math.max(1, getAtaqueTotal() + 4 - alvo.getDefesa());
        alvo.receberDano(dano);
        return dano;
    }
}
