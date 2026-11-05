package rpg.combate;

import rpg.item.Inventario;
import rpg.item.Item;
import rpg.personagem.Jogador;
import rpg.personagem.Monstro;
import rpg.util.Entrada;

// Versao de CONSOLE do combate: so le o teclado, chama a Batalha e imprime o resultado
public class SistemaCombate {
    private final Batalha batalha;
    private final Entrada entrada;

    public SistemaCombate(Batalha batalha, Entrada entrada) {
        this.batalha = batalha;
        this.entrada = entrada;
    }

    // Executa a batalha inteira. Retorna true se o jogador venceu
    public boolean executar() {
        System.out.println();
        System.out.println("=== Batalha: " + batalha.getJogador().getNome() + " x " + batalha.getInimigo().getNome() + " ===");

        while (!batalha.terminou()) {
            exibirStatus();
            ResultadoTurno resultado = lerEExecutarAcao();
            if (resultado == null) {
                continue; // voltou da lista de itens: nada aconteceu
            }
            for (String mensagem : resultado.getMensagens()) {
                System.out.println(mensagem);
            }
        }
        return batalha.getEstado() == EstadoBatalha.VITORIA;
    }

    private void exibirStatus() {
        Jogador jogador = batalha.getJogador();
        Monstro inimigo = batalha.getInimigo();
        System.out.println();
        System.out.println(jogador.getNome() + " - Vida " + jogador.getVidaAtual() + "/" + jogador.getVidaMaxima()
                + " | Energia " + jogador.getEnergiaAtual() + "/" + jogador.getEnergiaMaxima());
        System.out.println(inimigo.getNome() + " - Vida " + inimigo.getVidaAtual() + "/" + inimigo.getVidaMaxima());
        System.out.println("1 - Ataque comum");
        System.out.println("2 - " + jogador.getNomeHabilidade() + " (custo " + jogador.getCustoHabilidade() + " de energia)");
        System.out.println("3 - Usar item");
    }

    private ResultadoTurno lerEExecutarAcao() {
        int opcao = entrada.lerOpcao("Sua acao: ", 1, 3);
        switch (opcao) {
            case 1:
                return batalha.atacar();
            case 2:
                return batalha.usarHabilidade();
            default:
                Item item = escolherItem();
                return (item == null) ? null : batalha.usarItem(item);
        }
    }

    // Devolve o item escolhido, ou null se o jogador voltou
    private Item escolherItem() {
        Jogador jogador = batalha.getJogador();
        Inventario inventario = jogador.getInventario();
        if (inventario.estaVazio()) {
            System.out.println("Inventario vazio.");
            return null;
        }

        System.out.println("Itens:");
        for (int i = 0; i < inventario.tamanho(); i++) {
            Item item = inventario.getItem(i);
            String marca = (item == jogador.getArmaEquipada()) ? " [equipada]" : "";
            System.out.println((i + 1) + " - " + item + marca);
        }
        System.out.println("0 - Voltar");

        int opcao = entrada.lerOpcao("Item: ", 0, inventario.tamanho());
        return (opcao == 0) ? null : inventario.getItem(opcao - 1);
    }
}