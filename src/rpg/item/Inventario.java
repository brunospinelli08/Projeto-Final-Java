package rpg.item;

import java.util.ArrayList;

public class Inventario {
    private ArrayList<Item> itens = new ArrayList<Item>();

    public void adicionar(Item item) {
        if (item != null) {
            itens.add(item);
        }
    }

    public boolean remover(Item item) {
        return itens.remove(item);
    }

    public boolean contem(Item item) {
        return itens.contains(item);
    }

    public Item getItem(int posicao) {
        if (posicao < 0 || posicao >= itens.size()) {
            return null;
        }
        return itens.get(posicao);
    }

    public int tamanho() {
        return itens.size();
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }
}
