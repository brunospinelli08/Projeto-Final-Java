package rpg.personagem;

public class Arqueiro extends Jogador {
    private static final int CUSTO = 8;

    public Arqueiro(String nome) {
        super(nome, 120, 20, 6, 40);
    }

    @Override
    public boolean usarHabilidade(Personagem alvo) {
        if (!gastarEnergia(CUSTO)) {
            return false;
        }
        // ignora a defesa do alvo
        int dano = getAtaqueTotal() + 4;
        alvo.receberDano(dano);
        return true;
    }

    @Override
    public String getNomeHabilidade() {
        return "Tiro preciso";
    }

    @Override
    public int getCustoHabilidade() {
        return CUSTO;
    }
}