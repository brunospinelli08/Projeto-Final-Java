package rpg.personagem;

public class Guerreiro extends Jogador {
    private static final int CUSTO = 10;

    public Guerreiro(String nome) {
        super(nome, 140, 18, 8, 30);
    }

    @Override
    public boolean usarHabilidade(Personagem alvo) {
        if (!gastarEnergia(CUSTO)) {
            return false;
        }
        int dano = Math.max(1, 2 * getAtaqueTotal() - alvo.getDefesa());
        alvo.receberDano(dano);
        return true;
    }

    @Override
    public String getNomeHabilidade() {
        return "Golpe poderoso";
    }

    @Override
    public int getCustoHabilidade() {
        return CUSTO;
    }
}