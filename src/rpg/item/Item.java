package rpg.item;

public abstract class Item {
    private final String nome;
    private final String descricao;

    protected Item(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // Cada tipo de item diz o que e e o que faz
    public abstract String getTipo();

    public abstract String getEfeito();

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        return nome + " (" + getTipo() + ") - " + getEfeito();
    }
}