package rpg.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventario {
    private final ArrayList<Item> itens = new ArrayList<>();

    public void adicionar(Item item) {
        if (item != null) {
            itens.add(item);
        }
    }

    // Remove exatamente este objeto (comparacao por referencia)
    public boolean remover(Item item) {
        return itens.remove(item);
    }

    public boolean contem(Item item) {
        return itens.contains(item);
    }

    // Posicao comeca em 0. Devolve null se a posicao nao existir
    public Item getItem(int posicao) {
        if (posicao < 0 || posicao >= itens.size()) {
            return null;
        }
        return itens.get(posicao);
    }

    // Lista somente leitura: quem esta fora nao consegue adicionar nem remover por aqui
    public List<Item> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public int tamanho() {
        return itens.size();
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }
}