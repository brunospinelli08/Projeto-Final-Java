package rpg.personagem;

import rpg.item.Arma;
import rpg.item.Consumivel;
import rpg.item.Inventario;
import rpg.item.Item;

public abstract class Jogador extends Personagem {
    private final int energiaMaxima;
    private int energiaAtual;
    private final Inventario inventario;
    private Arma armaEquipada;

    protected Jogador(String nome, int vidaMaxima, int ataqueBase, int defesa, int energiaMaxima) {
        super(nome, vidaMaxima, ataqueBase, defesa);
        this.energiaMaxima = energiaMaxima;
        this.energiaAtual = energiaMaxima;
        this.inventario = new Inventario();
    }

    public abstract boolean usarHabilidade(Personagem alvo);

    public abstract String getNomeHabilidade();

    public abstract int getCustoHabilidade();

    @Override
    public int getAtaqueTotal() {
        int bonus = 0;
        if (armaEquipada != null) {
            bonus = armaEquipada.getBonusAtaque();
        }
        return getAtaqueBase() + bonus;
    }

    public boolean equiparArma(Arma arma) {
        if (arma == null || !inventario.contem(arma)) {
            return false;
        }
        armaEquipada = arma;
        return true;
    }

    public boolean usarItem(Item item) {
        if (item == null || !inventario.contem(item)) {
            return false;
        }
        if (!(item instanceof Consumivel)) {
            return false;
        }
        Consumivel consumivel = (Consumivel) item;
        if (!consumivel.usar(this)) {
            return false;
        }
        inventario.remover(item);
        return true;
    }

    protected boolean gastarEnergia(int custo) {
        if (custo < 0 || energiaAtual < custo) {
            return false;
        }
        energiaAtual -= custo;
        return true;
    }

    @Override
    public void restaurar() {
        super.restaurar();
        energiaAtual = energiaMaxima;
    }

    public int getEnergiaMaxima() { return energiaMaxima; }
    public int getEnergiaAtual() { return energiaAtual; }
    public Inventario getInventario() { return inventario; }
    public Arma getArmaEquipada() { return armaEquipada; }

    @Override
    public String toString() {
        String arma = "nenhuma";
        if (armaEquipada != null) {
            arma = armaEquipada.getNome();
        }
        return super.toString() + " Energia " + energiaAtual + "/" + energiaMaxima + " | Arma: " + arma;
    }
}