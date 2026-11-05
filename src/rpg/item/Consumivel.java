package rpg.item;

import rpg.personagem.Jogador;

public interface Consumivel {
    // Retorna true somente se o efeito foi aplicado
    boolean usar(Jogador jogador);
}