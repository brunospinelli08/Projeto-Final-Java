package rpg.app;

import rpg.item.Arma;
import rpg.item.Item;
import rpg.item.Pocao;
import rpg.personagem.Guerreiro;
import rpg.personagem.Jogador;
import rpg.personagem.Monstro;

public class TesteEtapa3 {
    public static void main(String[] args) {
        Jogador guerreiro = new Guerreiro("Teste G");
        Arma espada = new Arma("Espada curta", "Lamina simples", 4);
        Arma reforcada = new Arma("Espada reforcada", "Lamina temperada", 8);
        Pocao pocao1 = new Pocao("Pocao de cura", "Frasco vermelho", 30);
        Pocao pocao2 = new Pocao("Pocao de cura", "Frasco vermelho", 30);

        System.out.println("--- Equipamento ---");
        conferir("Equipar arma fora do inventario", guerreiro.equiparArma(espada), false);
        conferir("Ataque total sem arma", guerreiro.getAtaqueTotal(), 18);

        guerreiro.getInventario().adicionar(espada);
        guerreiro.getInventario().adicionar(pocao1);
        guerreiro.getInventario().adicionar(pocao2);
        conferir("Equipar espada curta", guerreiro.equiparArma(espada), true);
        conferir("Ataque total 18 + 4", guerreiro.getAtaqueTotal(), 22);
        conferir("Ataque comum no Goblin (22 - 3)", guerreiro.atacar(new Monstro("Goblin", 50, 12, 3)), 19);

        guerreiro.getInventario().adicionar(reforcada);
        guerreiro.equiparArma(reforcada);
        conferir("Troca para reforcada: 18 + 8", guerreiro.getAtaqueTotal(), 26);
        guerreiro.equiparArma(espada);
        guerreiro.equiparArma(reforcada);
        guerreiro.equiparArma(espada);
        guerreiro.equiparArma(reforcada);
        conferir("Varias trocas nao acumulam bonus", guerreiro.getAtaqueTotal(), 26);
        conferir("Arma equipada continua no inventario", guerreiro.getInventario().contem(reforcada), true);

        System.out.println("--- Itens consumiveis ---");
        int qtdAntes = guerreiro.getInventario().tamanho();
        conferir("Usar arma como item e recusado", guerreiro.usarItem(espada), false);
        conferir("Pocao com vida cheia e recusada", guerreiro.usarItem(pocao1), false);
        conferir("Inventario nao mudou", guerreiro.getInventario().tamanho(), qtdAntes);

        guerreiro.receberDano(12); // 140 -> 128
        conferir("Pocao com vida parcial funciona", guerreiro.usarItem(pocao1), true);
        conferir("Cura so ate o maximo", guerreiro.getVidaAtual(), 140);
        conferir("Saiu exatamente uma unidade", guerreiro.getInventario().tamanho(), qtdAntes - 1);
        conferir("A pocao usada saiu", guerreiro.getInventario().contem(pocao1), false);
        conferir("A outra pocao ficou", guerreiro.getInventario().contem(pocao2), true);

        guerreiro.receberDano(10);
        Pocao deFora = new Pocao("Pocao de cura", "Frasco vermelho", 30);
        conferir("Pocao fora do inventario e recusada", guerreiro.usarItem(deFora), false);

        guerreiro.receberDano(999);
        conferir("Pocao nao revive", guerreiro.usarItem(pocao2), false);
        conferir("Vida continua zero", guerreiro.getVidaAtual(), 0);

        System.out.println("--- Listagem do inventario ---");
        for (int i = 0; i < guerreiro.getInventario().tamanho(); i++) {
            Item item = guerreiro.getInventario().getItem(i);
            System.out.println((i + 1) + " - " + item);
        }
        System.out.println(guerreiro);
    }

    private static void conferir(String descricao, int obtido, int esperado) {
        String status = obtido == esperado ? "OK    " : "FALHOU";
        System.out.println(status + " " + descricao + ": obtido " + obtido + ", esperado " + esperado);
    }

    private static void conferir(String descricao, boolean obtido, boolean esperado) {
        String status = obtido == esperado ? "OK    " : "FALHOU";
        System.out.println(status + " " + descricao + ": obtido " + obtido + ", esperado " + esperado);
    }
}