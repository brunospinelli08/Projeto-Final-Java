package rpg.combate;

import java.util.ArrayList;
import java.util.List;

import rpg.item.Consumivel;
import rpg.item.Item;
import rpg.personagem.Jogador;
import rpg.personagem.Monstro;

public class Batalha {
    private final Jogador jogador;
    private final Monstro inimigo;
    private EstadoBatalha estado;

    public Batalha(Jogador jogador, Monstro inimigo) {
        this.jogador = jogador;
        this.inimigo = inimigo;
        this.estado = EstadoBatalha.EM_ANDAMENTO;
    }

    public ResultadoTurno atacar() {
        if (terminou()) {
            return ResultadoTurno.recusado("A batalha ja terminou.", estado);
        }
        int dano = jogador.atacar(inimigo);
        List<String> mensagens = new ArrayList<>();
        mensagens.add(jogador.getNome() + " ataca e causa " + dano + " de dano.");
        return concluirTurno(mensagens);
    }

    public ResultadoTurno usarHabilidade() {
        if (terminou()) {
            return ResultadoTurno.recusado("A batalha ja terminou.", estado);
        }
        int vidaAntes = inimigo.getVidaAtual();
        // Polimorfismo: cada especializacao executa a sua formula
        if (!jogador.usarHabilidade(inimigo)) {
            return ResultadoTurno.recusado("Energia insuficiente para " + jogador.getNomeHabilidade()
                    + " (custo " + jogador.getCustoHabilidade() + ", energia atual " + jogador.getEnergiaAtual() + ").", estado);
        }
        int dano = vidaAntes - inimigo.getVidaAtual();
        List<String> mensagens = new ArrayList<>();
        mensagens.add(jogador.getNome() + " usa " + jogador.getNomeHabilidade() + " e causa " + dano + " de dano.");
        return concluirTurno(mensagens);
    }

    public ResultadoTurno usarItem(Item item) {
        if (terminou()) {
            return ResultadoTurno.recusado("A batalha ja terminou.", estado);
        }
        if (item == null) {
            return ResultadoTurno.recusado("Nenhum item foi escolhido.", estado);
        }
        int vidaAntes = jogador.getVidaAtual();
        if (!jogador.usarItem(item)) {
            // A regra ja foi aplicada no Jogador. Aqui so escolhemos a mensagem do motivo
            if (!(item instanceof Consumivel)) {
                return ResultadoTurno.recusado(item.getNome() + " nao pode ser consumido.", estado);
            }
            return ResultadoTurno.recusado("Sua vida ja esta cheia. O item nao foi usado.", estado);
        }
        int curado = jogador.getVidaAtual() - vidaAntes;
        List<String> mensagens = new ArrayList<>();
        mensagens.add(jogador.getNome() + " usa " + item.getNome() + " e recupera " + curado + " de vida.");
        return concluirTurno(mensagens);
    }

    // Parte comum a toda acao aceita: ve se o inimigo caiu, senao ele ataca
    private ResultadoTurno concluirTurno(List<String> mensagens) {
        if (!inimigo.estaVivo()) {
            mensagens.add(inimigo.getNome() + " foi derrotado!");
            estado = EstadoBatalha.VITORIA;
            return ResultadoTurno.aceito(mensagens, estado);
        }

        // Mesma chamada para qualquer inimigo. Se for o Chefe, o Java usa a versao dele
        int dano = inimigo.atacar(jogador);
        mensagens.add(inimigo.getNome() + " ataca e causa " + dano + " de dano.");

        if (!jogador.estaVivo()) {
            mensagens.add(jogador.getNome() + " foi derrotado...");
            estado = EstadoBatalha.DERROTA;
        }
        return ResultadoTurno.aceito(mensagens, estado);
    }

    public boolean terminou() {
        return estado != EstadoBatalha.EM_ANDAMENTO;
    }

    public Jogador getJogador() { return jogador; }
    public Monstro getInimigo() { return inimigo; }
    public EstadoBatalha getEstado() { return estado; }
}