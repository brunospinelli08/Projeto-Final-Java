package rpg.personagem;

/**
 * Chefe final. Mesmo construtor do Monstro, mas ataca com regra propria:
 * soma um bonus fixo antes de descontar a defesa do alvo.
 */
public class Chefe extends Monstro {
    private static final int BONUS_ATAQUE = 4;

    public Chefe(String nome, int vidaMaxima, int ataqueBase, int defesa) {
        super(nome, vidaMaxima, ataqueBase, defesa);
    }

    @Override
    public int atacar(Personagem alvo) {
        int dano = Math.max(1, getAtaqueTotal() + BONUS_ATAQUE - alvo.getDefesa());
        alvo.receberDano(dano);
        return dano;
    }

    @Override
    public String getCategoria() {
        return "Chefe";
    }
}