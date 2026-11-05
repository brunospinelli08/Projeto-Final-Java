package rpg.personagem;

public abstract class Personagem {
    private final String nome;
    private final int vidaMaxima;
    private int vidaAtual;
    private final int ataqueBase;
    private final int defesa;

    protected Personagem(String nome, int vidaMaxima, int ataqueBase, int defesa) {
        this.nome = nome;
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.ataqueBase = ataqueBase;
        this.defesa = defesa;
    }

    // Jogador vai sobrescrever para somar o bonus da arma
    public int getAtaqueTotal() {
        return ataqueBase;
    }

    // Calcula o dano descontando a defesa uma unica vez e aplica no alvo
    public int atacar(Personagem alvo) {
        int dano = Math.max(1, getAtaqueTotal() - alvo.getDefesa());
        alvo.receberDano(dano);
        return dano;
    }

    // So aplica o dano. Nunca desconta defesa aqui
    public void receberDano(int dano) {
        if (dano <= 0) {
            return;
        }
        vidaAtual = Math.max(0, vidaAtual - dano);
    }

    // Recupera ate o maximo e devolve quanto curou. Nao revive
    public int recuperarVida(int pontos) {
        if (pontos <= 0 || !estaVivo()) {
            return 0;
        }
        int efetiva = Math.min(pontos, vidaMaxima - vidaAtual);
        vidaAtual += efetiva;
        return efetiva;
    }

    // Usado entre batalhas
    public void restaurar() {
        vidaAtual = vidaMaxima;
    }

    public boolean estaVivo() {
        return vidaAtual > 0;
    }

    public String getNome() { return nome; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getVidaAtual() { return vidaAtual; }
    public int getAtaqueBase() { return ataqueBase; }
    public int getDefesa() { return defesa; }

    @Override
    public String toString() {
        return nome + " [Vida " + vidaAtual + "/" + vidaMaxima
                + " | Ataque " + getAtaqueTotal() + " | Defesa " + defesa + "]";
    }
}