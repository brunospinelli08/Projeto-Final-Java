package rpg.item;

public class Arma extends Item {
    private final int bonusAtaque;

    public Arma(String nome, String descricao, int bonusAtaque) {
        super(nome, descricao);
        this.bonusAtaque = bonusAtaque;
    }

    public int getBonusAtaque() {
        return bonusAtaque;
    }

    @Override
    public String getTipo() {
        return "Arma";
    }

    @Override
    public String getEfeito() {
        return "+" + bonusAtaque + " de ataque";
    }
}