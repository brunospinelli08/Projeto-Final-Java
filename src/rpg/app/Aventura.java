package rpg.app;

import java.util.ArrayList;
import java.util.List;

import rpg.combate.Batalha;
import rpg.combate.EstadoBatalha;
import rpg.item.Arma;
import rpg.item.Pocao;
import rpg.personagem.Chefe;
import rpg.personagem.Especializacao;
import rpg.personagem.Jogador;
import rpg.personagem.Monstro;

// Estado e regras de UMA partida: jogador, inimigos, etapa e recompensas. Sem console
public class Aventura {
    private static final String[] LOCAIS = {
            "Entre os portoes quebrados da entrada, um Goblin bloqueia a passagem.",
            "No corredor escuro, ossos se arrastam pelo chao. Um Esqueleto ergue a espada enferrujada.",
            "Na camara final, o Guardiao da Fortaleza se levanta do trono de pedra."
    };

    private final Jogador jogador;
    private final Arma armaReforcada;            // recompensa da segunda batalha
    private final ArrayList<Monstro> inimigos;   // chefe na ultima posicao
    private int etapaAtual;                      // indice do proximo inimigo

    // Cada nova Aventura cria tudo do zero: nada de uma partida anterior e reaproveitado
    public Aventura(String nomeJogador, Especializacao especializacao) {
        this.jogador = especializacao.criarJogador(nomeJogador);

        Arma armaInicial = new Arma(especializacao.getNomeArmaInicial(), "Arma inicial", 4);
        jogador.getInventario().adicionar(armaInicial);
        jogador.equiparArma(armaInicial);
        jogador.getInventario().adicionar(criarPocao());
        jogador.getInventario().adicionar(criarPocao());

        this.armaReforcada = new Arma(especializacao.getNomeArmaReforcada(), "Recompensa do corredor", 8);

        this.inimigos = new ArrayList<>();
        inimigos.add(new Monstro("Goblin da entrada", 50, 12, 3));
        inimigos.add(new Monstro("Esqueleto do corredor", 75, 16, 5));
        inimigos.add(new Chefe("Guardiao da Fortaleza", 130, 20, 7));

        this.etapaAtual = 0;
    }

    private Pocao criarPocao() {
        return new Pocao("Pocao de cura", "Frasco de liquido vermelho", 30);
    }

    // Cria a batalha contra o proximo inimigo. Devolve null se a aventura ja acabou
    public Batalha iniciarBatalha() {
        if (foiConcluida()) {
            return null;
        }
        return new Batalha(jogador, getProximoInimigo());
    }

    // Chamado depois de uma batalha vencida. Avanca a etapa e da a recompensa no mesmo lugar,
    // entao e impossivel receber o mesmo premio duas vezes ou repetir a etapa
    public List<String> registrarVitoria(Batalha batalha) {
        List<String> mensagens = new ArrayList<>();
        if (batalha == null || batalha.getEstado() != EstadoBatalha.VITORIA
                || foiConcluida() || batalha.getInimigo() != getProximoInimigo()) {
            return mensagens; // vitoria invalida ou ja registrada: nada acontece
        }

        int etapaVencida = etapaAtual;
        etapaAtual++;

        if (etapaVencida == 0) {
            jogador.getInventario().adicionar(criarPocao());
            mensagens.add("Recompensa: uma Pocao de cura foi adicionada ao inventario.");
        } else if (etapaVencida == 1) {
            jogador.getInventario().adicionar(armaReforcada);
            mensagens.add("Recompensa: " + armaReforcada.getNome() + " (+" + armaReforcada.getBonusAtaque()
                    + " de ataque). Equipe pelo inventario antes da proxima batalha.");
        }

        jogador.restaurar();
        if (!foiConcluida()) {
            mensagens.add("Vida e energia restauradas.");
        }
        return mensagens;
    }

    public boolean foiConcluida() {
        return etapaAtual >= inimigos.size();
    }

    public Monstro getProximoInimigo() {
        return foiConcluida() ? null : inimigos.get(etapaAtual);
    }

    public String getDescricaoLocal() {
        return foiConcluida() ? "" : LOCAIS[etapaAtual];
    }

    public Jogador getJogador() { return jogador; }
    public int getEtapaAtual() { return etapaAtual; }
    public int getTotalEtapas() { return inimigos.size(); }
}