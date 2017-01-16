/*
 * Copyright (c) 2016 Fábio Nogueira de Lucena
 * Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */

package com.github.kyriosdata.shared;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe que encapsula operações de controle de concorrência
 * em cenário de vários produtores e um único consumidor.
 *
 * <p>Orientações para o uso correto. O método
 * {@link #aloca()} indica o desejo de produzir (algo), o retorno
 * é um inteiro no intervalo fechado [0, 31], que unicamente
 * identifica o que se deseja produzir. Em geral, o valor fornecido
 * é o índice de um <i>array</i> ou permite identificar, de fato,
 * o dado compartilhado, ou seja, esse valor funciona como um
 * <i>handle</i> para a real informação.
 *
 * <p>A alocação não é suficiente para o
 * consumo. Antes de ser consumido o valor alocado precisa ser
 * "produzido" pelo método {@link #produz(int)}. O valor fornecido
 * como argumento deve ser aquele obtido do método {@link #aloca()}.
 * Noutras palavras, uma chamada desse método com o argumento
 * 7, por exemplo, indica que a informação associada ao valor
 * 7 está disponível para consumo.
 *
 * <p>O consumo propriamente dito ocorre pelo método {@link #consome(int, boolean)}.
 * O usuário dessa classe deve sobrescrever esse método. Quando chamado
 * sabe-se que a faixa de valores indicada foi produzida e deve ser
 * consumida.
 */
public class Shared {

    // Tamanho da "lista circular" (ring buffer).
    // Necessariamente uma potência de 2.
    private final int SIZE = 128;

    // Máscara para "rotacionar" índices
    // Permite substituir "% SIZE" por "& MASCARA" (mais eficiente).
    private final int MASCARA = SIZE - 1;

    // Empregada para evitar reentrância do consumidor.
    private AtomicInteger working = new AtomicInteger(0);

    // Indica primeira entrada livre (first free).
    private AtomicInteger ff = new AtomicInteger(0);

    // Indica última entrada livre (las free)
    private int lf = SIZE - 1;

    // Cada byte indica, para o índice (valor) em questão,
    // se há produto disponível (1) ou não (0).
    private byte[] producao = new byte[SIZE];

    /**
     * Consome a informação produzida e associada
     * ao valor indicado.
     *
     * <p>Após a execução desse método o valor
     * correspondente estará disponível para
     * reutilização pelo método {@link #aloca()}.
     *
     * @param v Valor a ser consumido.
     * @param ultimo {@code true} se e somente se
     *                           é o "último" evento
     *                           registrado até o momento
     *                           para consumo.
     */
    public void consome(int v, boolean ultimo) {}

    /**
     * Indica a produção de informação associada
     * ao valor fornecido.
     *
     * @param v O valor que identifica a produção.
     *           Esse valor necessariamente deve ser
     *           obtido do método {@link #aloca()}.
     *
     * @see #aloca()
     * @see #flush()
     */
    public void produz(int v) {
        producao[v] = 1;
    }

    /**
     * Quantidade de entradas ocupadas (já alocadas).
     *
     * <p>Nenhuma das entradas, necessariamente, foi
     * "produzida", mas seguramente já foi alocada.
     *
     * @return Número de entradas ocupadas.
     */
    public int totalAlocados() {
        return SIZE - totalLiberados();
    }

    /**
     * Quantidade de entradas disponíveis para alocação.
     *
     * @return Número de entradas (valores) disponíveis
     * para alocação imediata.
     */
    public int totalLiberados() {
        return lf - ff.get() + 1;
    }

    /**
     * Verifica se, no instante em questão, o valor está produzido.
     * @param v O valor sobre o qual a verificação é feita.
     * @return {@code true} se e somente se, no instante em que a
     * chamada é realizada, o valor fornecido está produzido.
     */
    public boolean produzido(int v) {
        return producao[v] == 1;
    }

    /**
     * Realiza alocação de um valor.
     *
     * @return O identificador único, valor de 0 a 31, inclusive,
     * que deve ser "produzido" e posteriormente consumido.
     */
    public int aloca() {
        while (true) {
            int candidato = ff.get();
            if (candidato <= lf) {
                if (ff.compareAndSet(candidato, candidato + 1)) {
                    producao[candidato & MASCARA] = 0;
                    return candidato & MASCARA;
                }
            } else {
                flush();
            }
        }
    }

    /**
     * Processa valores já disponíveis. Ou seja,
     * todos os que já foram alocados e também
     * usados.
     */
    public void flush() {

        // Evita reentrância
        if (!working.compareAndSet(0, 1)) {
            return;
        }

        int fa = lf + 1;
        int la = ff.get() - 1 + SIZE;

        // Obém o total de alocados já produzidos
        int totalProducao = totalDaProducao(fa, la);

        if (totalProducao > 0) {
            // Alocados e usados é |[fa, lu]| = producao
            int lu = lf + totalProducao;

            // Consome entradas (exceto o último)
            for (int i = fa; i < lu; i++) {
                int valor = i & MASCARA;
                try {
                    consome(valor, false);
                } catch (Exception exp) {
                    System.out.println(exp.toString());
                }

                producao[valor] = 0;
            }

            // Indica que se trata do ÚLTIMO
            consome(lu & MASCARA, true);
            producao[lu & MASCARA] = 0;

            // Disponibiliza valores para reutilização
            lf = lf + totalProducao;
        }

        working.set(0);
    }

    private int totalDaProducao(int first, int last) {
        int producao = 0;
        int i = first;
        while (i <= last && produzido(i & MASCARA)) {
            i++;
            producao++;
        }

        return producao;
    }
}
