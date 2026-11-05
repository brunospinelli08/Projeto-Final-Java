package rpg.personagem;

// As tres opcoes de classe do jogo, com o nome das armas de cada uma
public enum Especializacao {
    GUERREIRO("Guerreiro", "Espada curta", "Espada reforcada"),
    MAGO("Mago", "Cajado", "Cajado reforcado"),
    ARQUEIRO("Arqueiro", "Arco", "Arco reforcado");

    private final String nome;
    private final String nomeArmaInicial;
    private final String nomeArmaReforcada;

    Especializacao(String nome, String nomeArmaInicial, String nomeArmaReforcada) {
        this.nome = nome;
        this.nomeArmaInicial = nomeArmaInicial;
        this.nomeArmaReforcada = nomeArmaReforcada;
    }

    // Unico lugar do jogo que decide qual classe instanciar
    public Jogador criarJogador(String nomeJogador) {
        switch (this) {
            case GUERREIRO:
                return new Guerreiro(nomeJogador);
            case MAGO:
                return new Mago(nomeJogador);
            default:
                return new Arqueiro(nomeJogador);
        }
    }

    // Monta o resumo a partir de um jogador de exemplo: os numeros vem das proprias classes
    public String getResumo() {
        Jogador modelo = criarJogador(nome);
        return "Vida " + modelo.getVidaMaxima() + ", Ataque " + modelo.getAtaqueBase()
                + ", Defesa " + modelo.getDefesa() + ", Energia " + modelo.getEnergiaMaxima()
                + " - " + modelo.getNomeHabilidade();
    }

    public String getNome() { return nome; }
    public String getNomeArmaInicial() { return nomeArmaInicial; }
    public String getNomeArmaReforcada() { return nomeArmaReforcada; }
}