package rpg.item;

import rpg.personagem.Jogador;

public class Pocao extends Item implements Consumivel {
    private final int pontosCura;

    public Pocao(String nome, String descricao, int pontosCura) {
        super(nome, descricao);
        this.pontosCura = pontosCura;
    }

    @Override
    public boolean usar(Jogador jogador) {
        return jogador.recuperarVida(pontosCura) > 0;
    }

    public int getPontosCura() {
        return pontosCura;
    }

    @Override
    public String getTipo() {
        return "Pocao";
    }

    @Override
    public String getEfeito() {
        return "recupera ate " + pontosCura + " de vida";
    }
}