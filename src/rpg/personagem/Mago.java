package rpg.personagem;

public class Mago extends Jogador {
    private static final int CUSTO = 12;

    public Mago(String nome) {
        super(nome, 100, 22, 4, 50);
    }

    @Override
    public boolean usarHabilidade(Personagem alvo) {
        if (!gastarEnergia(CUSTO)) {
            return false;
        }
        // divisao inteira da defesa, como pede o roteiro
        int dano = Math.max(1, getAtaqueTotal() + 12 - alvo.getDefesa() / 2);
        alvo.receberDano(dano);
        return true;
    }

    @Override
    public String getNomeHabilidade() {
        return "Bola de fogo";
    }

    @Override
    public int getCustoHabilidade() {
        return CUSTO;
    }
}