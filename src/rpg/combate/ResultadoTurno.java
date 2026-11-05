package rpg.combate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultadoTurno {
    private final boolean aceito;
    private final List<String> mensagens;
    private final EstadoBatalha estado;

    private ResultadoTurno(boolean aceito, List<String> mensagens, EstadoBatalha estado) {
        this.aceito = aceito;
        this.mensagens = Collections.unmodifiableList(new ArrayList<>(mensagens));
        this.estado = estado;
    }

    // A acao foi feita e o turno passou
    public static ResultadoTurno aceito(List<String> mensagens, EstadoBatalha estado) {
        return new ResultadoTurno(true, mensagens, estado);
    }

    // A acao foi recusada: nada mudou e o inimigo nao agiu
    public static ResultadoTurno recusado(String motivo, EstadoBatalha estado) {
        List<String> lista = new ArrayList<>();
        lista.add(motivo);
        return new ResultadoTurno(false, lista, estado);
    }

    public boolean foiAceito() { return aceito; }
    public List<String> getMensagens() { return mensagens; }
    public EstadoBatalha getEstado() { return estado; }
}