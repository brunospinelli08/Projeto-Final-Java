package rpg.app;

import rpg.combate.Batalha;
import rpg.combate.EstadoBatalha;
import rpg.combate.ResultadoTurno;
import rpg.item.Arma;
import rpg.item.Item;
import rpg.item.Pocao;
import rpg.personagem.Chefe;
import rpg.personagem.Especializacao;
import rpg.personagem.Guerreiro;
import rpg.personagem.Jogador;
import rpg.personagem.Mago;
import rpg.personagem.Monstro;

public class TesteG1 {
    public static void main(String[] args) {
        System.out.println("--- Batalha: turno a turno, sem console ---");
        Jogador guerreiro = new Guerreiro("Aldric");
        Arma espada = new Arma("Espada curta", "teste", 4);
        Pocao pocao = new Pocao("Pocao de cura", "teste", 30);
        guerreiro.getInventario().adicionar(espada);
        guerreiro.getInventario().adicionar(pocao);
        guerreiro.equiparArma(espada);
        Monstro goblin = new Monstro("Goblin", 50, 12, 3);
        Batalha batalha = new Batalha(guerreiro, goblin);

        ResultadoTurno r = batalha.usarHabilidade();
        conferir("Habilidade aceita", r.foiAceito(), true);
        conferir("Golpe causou 41 (vida do Goblin)", goblin.getVidaAtual(), 9);
        conferir("Goblin revidou 4 (vida do Guerreiro)", guerreiro.getVidaAtual(), 136);
        conferir("Duas mensagens no turno", r.getMensagens().size(), 2);
        conferir("Batalha continua (1 = sim)", r.getEstado() == EstadoBatalha.EM_ANDAMENTO ? 1 : 0, 1);

        r = batalha.usarItem(espada);
        conferir("Arma como item recusada", r.foiAceito(), false);
        conferir("Goblin nao agiu", guerreiro.getVidaAtual(), 136);
        System.out.println("       motivo: " + r.getMensagens().get(0));

        r = batalha.usarItem(pocao);
        conferir("Pocao aceita", r.foiAceito(), true);
        conferir("Curou 4 e levou 4 (vida final)", guerreiro.getVidaAtual(), 136);

        r = batalha.atacar();
        conferir("Ataque final aceito", r.foiAceito(), true);
        conferir("Vitoria (1 = sim)", r.getEstado() == EstadoBatalha.VITORIA ? 1 : 0, 1);
        conferir("Sem revide: vida igual", guerreiro.getVidaAtual(), 136);

        r = batalha.atacar();
        conferir("Acao depois do fim e recusada", r.foiAceito(), false);

        System.out.println("--- Batalha: derrota ---");
        Jogador mago = new Mago("Merlin");
        Batalha contraChefe = new Batalha(mago, new Chefe("Guardiao", 130, 20, 7));
        int turnos = 0;
        while (!contraChefe.terminou()) {
            contraChefe.atacar();
            turnos++;
        }
        conferir("Derrota (1 = sim)", contraChefe.getEstado() == EstadoBatalha.DERROTA ? 1 : 0, 1);
        conferir("Mago cai em 5 turnos (20 por turno)", turnos, 5);

        System.out.println("--- Aventura ---");
        Aventura aventura = new Aventura("Teste", Especializacao.GUERREIRO);
        Jogador j = aventura.getJogador();
        conferir("Comeca na etapa 0", aventura.getEtapaAtual(), 0);
        conferir("Inventario inicial: arma + 2 pocoes", j.getInventario().tamanho(), 3);
        conferir("Arma inicial equipada (18 + 4)", j.getAtaqueTotal(), 22);

        Batalha b1 = aventura.iniciarBatalha();
        conferir("Registrar sem vencer nao faz nada", aventura.registrarVitoria(b1).size(), 0);
        vencer(b1);
        aventura.registrarVitoria(b1);
        conferir("Avancou para a etapa 1", aventura.getEtapaAtual(), 1);
        conferir("Ganhou 1 pocao (4 itens)", j.getInventario().tamanho(), 4);
        conferir("Vida restaurada", j.getVidaAtual(), 140);
        conferir("Energia restaurada", j.getEnergiaAtual(), 30);
        aventura.registrarVitoria(b1);
        conferir("Registrar de novo nao repete premio", j.getInventario().tamanho(), 4);
        conferir("Nem a etapa", aventura.getEtapaAtual(), 1);

        Batalha b2 = aventura.iniciarBatalha();
        vencer(b2);
        aventura.registrarVitoria(b2);
        conferir("Ganhou a arma reforcada (5 itens)", j.getInventario().tamanho(), 5);
        Item reforcada = j.getInventario().getItem(4);
        j.equiparArma((Arma) reforcada);
        conferir("Reforcada equipada (18 + 8)", j.getAtaqueTotal(), 26);

        Batalha b3 = aventura.iniciarBatalha();
        conferir("Terceira batalha e contra o chefe (1 = sim)", b3.getInimigo() instanceof Chefe ? 1 : 0, 1);
        vencer(b3);
        aventura.registrarVitoria(b3);
        conferir("Aventura concluida (1 = sim)", aventura.foiConcluida() ? 1 : 0, 1);
        conferir("Nao cria batalha depois do fim (1 = null)", aventura.iniciarBatalha() == null ? 1 : 0, 1);

        Aventura nova = new Aventura("Outro", Especializacao.MAGO);
        conferir("Nova aventura volta para etapa 0", nova.getEtapaAtual(), 0);
        conferir("Nova aventura com 3 itens", nova.getJogador().getInventario().tamanho(), 3);

        System.out.println("--- Especializacoes listadas pelo enum ---");
        for (Especializacao e : Especializacao.values()) {
            System.out.println(e.getNome() + ": " + e.getResumo());
        }
    }

    // Usa a habilidade enquanto der e depois ataque comum, ate a batalha acabar
    private static void vencer(Batalha batalha) {
        while (!batalha.terminou()) {
            ResultadoTurno r = batalha.usarHabilidade();
            if (!r.foiAceito()) {
                batalha.atacar();
            }
        }
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