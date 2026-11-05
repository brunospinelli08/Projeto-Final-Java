package rpg.personagem;

/**
 * Adversario comum. Goblin e Esqueleto sao objetos desta mesma classe,
 * diferenciados apenas pelos valores passados no construtor.
 */
public class Monstro extends Personagem {

    public Monstro(String nome, int vidaMaxima, int ataqueBase, int defesa) {
        super(nome, vidaMaxima, ataqueBase, defesa);
    }

    // Texto exibido na tela. O Chefe sobrescreve, entao a tela nunca precisa perguntar o tipo
    public String getCategoria() {
        return "Monstro";
    }
}