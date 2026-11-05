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
        this.inventario = new Inventario(); // composicao: o inventario nasce com o jogador
    }

    // Cada especializacao implementa a sua regra
    public abstract boolean usarHabilidade(Personagem alvo);

    public abstract String getNomeHabilidade();

    public abstract int getCustoHabilidade();

    // Ataque base + bonus da arma equipada (0 se estiver sem arma)
    @Override
    public int getAtaqueTotal() {
        int bonus = (armaEquipada == null) ? 0 : armaEquipada.getBonusAtaque();
        return getAtaqueBase() + bonus;
    }

    // So equipa arma que esta no inventario. Troca a referencia, nao soma bonus
    public boolean equiparArma(Arma arma) {
        if (arma == null || !inventario.contem(arma)) {
            return false;
        }
        armaEquipada = arma;
        return true;
    }

    // Usa o item pela interface Consumivel e so remove se o uso deu certo
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

    // Gasta energia so se tiver o suficiente. Se nao tiver, nao altera nada
    protected boolean gastarEnergia(int custo) {
        if (custo < 0 || energiaAtual < custo) {
            return false;
        }
        energiaAtual -= custo;
        return true;
    }

    // Entre batalhas: vida cheia (super) e energia cheia
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
        String arma = (armaEquipada == null) ? "nenhuma" : armaEquipada.getNome();
        return super.toString() + " Energia " + energiaAtual + "/" + energiaMaxima + " | Arma: " + arma;
    }
}