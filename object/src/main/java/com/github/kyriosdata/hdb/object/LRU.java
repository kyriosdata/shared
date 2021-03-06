/*
 * Copyright (c) 2016
 *
 * Fábio Nogueira de Lucena
 * Fábrica de Software - Instituto de Informática (UFG)
 *
 * Creative Commons Attribution 4.0 International License.
 */

package com.github.kyriosdata.hdb.object;

import java.util.HashMap;

/**
 * Implementação de Least Recently Used (LRU) para permitir que
 * o bloco usado há mais tempo, dentre aqueles ditos "unlocked",
 * possa ceder o espaço do <i>buffer</i> correspondente para outro
 * bloco.
 *
 * <p>Essa classe não é <i>thread safe</i>. Ou seja, proteção
 * deverá ser oferecida, se for o caso, para que a funcionalidade
 * seja assegurada.
 *
 * <p>Quando uma instância é criada a lista é preenchida totalmente.
 * Ou seja, não são mais permitidas inserções, nem tampouco fazem
 * sentido as remoções. Restando uma única operação relevante para a
 * implementação do LRU: "trazer" o nó que irá referenciar o bloco
 * requisitado para a frente da lista ({@link #bringToFront(No)}).
 *
 * <p>A implementação aloca inicialmente o total de objetos a serem
 * empregados e os reutiliza, tornando objetos dessa classe
 * "amigáveis" ao GC.
 */
public class LRU {

    /**
     * Dicionário de blocos "livres" para reutilização.
     *
     *
     */
    private HashMap<Integer, No> unlocked;
    private No head;
    private No tail;

    public LRU(int capacity) {
        unlocked = new HashMap<>(capacity);

        head = new No(Integer.MIN_VALUE, 0);
        tail = new No(Integer.MIN_VALUE, 1);

        head.next = tail;
        tail.prev = head;

        for (int i = 2; i < capacity; i++) {
            No n = new No(Integer.MIN_VALUE, i);
            tail.next = n;
            n.prev = tail;
            tail = n;
        }
    }

    /**
     * Requisita <i>buffer</i> a ser utilizado para armazenar
     * um bloco. O identificador retornado unicamente aponta
     * para um <i>buffer</i> livre ou, caso não exista, para
     * o LRU, ou seja, aquele cujo uso é o mais remoto, ou há
     * mais tempo não é utilizado.
     *
     * <p>O método não realiza nenhuma operação para assegurar
     * que o <i>buffer</i> retornado não é mais utilizado. Nesse
     * sentido pode ser entendido que o retorno é o identificador
     * do <i>buffer</i> candidato a ser utilizado.
     *
     * <p>Esse método não é <i>idempotent</i>, ou seja, chamada
     * posterior pode retornar outro valor, distinto do anterior.
     *
     * @param blocoId O identificador do bloco para o qual a
     *                requisição de <i>buffer</i> é feita.
     *
     * @return O identificador do <i>buffer</i> a ser utilizado.
     *
     */
    public int use(int blocoId) {

        No reutilizado = unlocked.get(blocoId);

        if (reutilizado == null) {
            reutilizado = tail;
            unlocked.remove(reutilizado.key);
            unlocked.put(blocoId, reutilizado);
            reutilizado.key = blocoId;
        }

        bringToFront(reutilizado);

        return reutilizado.value;
    }

    private void bringToFront(No no) {
        if (head == no) {
            return;
        }

        no.prev.next = no.next;

        if (no != tail) {
            no.next.prev = no.prev;
        } else {
            tail = no.prev;
        }

        head.prev = no;
        no.next = head;
        no.prev = null;
        head = no;
    }

    /**
     * Adiciona o bloco como o mais recentemente utilizado.
     * Ou seja, será o último, dentre todos os disponíveis
     * após a inserção, a ser recuperado pelo método {@link #get()}.
     *
     * @param bloco O bloco a ser adicionado ao final da fila.
     */
    public void add(Bloco bloco) {
    }

    public Bloco get() {
        return new Bloco(new BufferByteBuffer());
    }

    private class No {
        int key;
        int value;
        No prev;
        No next;

        public No(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}