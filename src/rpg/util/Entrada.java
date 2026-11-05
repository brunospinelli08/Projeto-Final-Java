package rpg.util;

import java.util.Scanner;

public class Entrada {
    private final Scanner scanner;

    public Entrada(Scanner scanner) {
        this.scanner = scanner;
    }

    // Repete ate o usuario digitar um numero inteiro entre min e max
    public int lerOpcao(String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(linha);
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // entrada nao numerica: cai na mensagem abaixo e pede de novo
            }
            System.out.println("Opcao invalida. Digite um numero entre " + min + " e " + max + ".");
        }
    }

    // Repete ate o usuario digitar algum texto
    public String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            if (!linha.isEmpty()) {
                return linha;
            }
            System.out.println("O texto nao pode ficar vazio.");
        }
    }
}