package com.github.kyriosdata.healthdb.file;

import java.nio.ByteBuffer;

/**
 * Abstração para uma sequência de bytes geralmente
 * armazenada em meio secundário, juntamente com operações
 * pertinentes.
 *
 * <p>As operações são baseadas na perspectiva do Cliente,
 * código que faz uso dessa interface, sem referência explícita
 * ou dependência do real meio empregado para armazenar ou
 * consultar as informações requisitadas.
 */
public interface Arquivo {

    /**
     * Acrescenta o conteúdo do buffer ao final do arquivo.
     *
     * @param buffer Buffer cujo conteúdo será acrescido ao arquivo.
     */
    void acrescenta(ByteBuffer buffer);

    /**
     * Acrescenta o total de bytes do buffer, a partir da posição
     * indicada, ao final do arquivo.
     *
     * @param buffer Buffer contendo a origem dos bytes a serem inseridos.
     *
     * @param i Posição inicial do buffer a partir do qual bytes serão copiados.
     *
     * @param total Total de bytes a serem inseridos no arquivo, a partir da
     *              posição indicada, ao final do arquivo.
     */
    void acrescenta(byte[] buffer, int i, int total);

    /**
     * Obtém do arquivo, a partir de determinada posição, um total de bytes
     * a ser depositado no buffer.
     *
     * @param buffer Buffer onde bytes lidos serão depositados.
     *
     * @param posicao Posição inicial do arquivo a partir da qual bytes
     *                serão lidos.
     *
     * @param total Total de bytes a serem lidos e depositados no buffer.
     */
    void carrega(ByteBuffer buffer, int posicao, int total);

    /**
     * Obtém do arquivo o total de bytes, a partir de determinada posição, e
     * os deposita em um buffer.
     *
     * @param buffer Vetor de bytes no qual o conteúdo lido do arquivo será
     *               depositado.
     *
     * @param posicao Posição inicial no arquivo a partir da qual bytes serão
     *                lidos.
     *
     * @param total Total de bytes a serem lidos.
     */
    void carrega(byte[] buffer, int posicao, int total);

    /**
     * Deposita no arquivo, a partir da posição indicada, o total de
     * bytes, obtidos a partir da primeira posição do buffer.
     *
     * @param buffer Buffer do qual bytes serão consultados para a
     *               inserção no arquivo.
     *
     * @param posicao Posição inicial no arquivo a partir da qual
     *                bytes serão escritos.
     *
     * @param total Total de bytes escritos.
     *
     * @see #escreve(byte[], int, int)
     */
    void escreve(ByteBuffer buffer, int posicao, int total);

    /**
     * Deposita no arquivo, a partir da posição indicada, o total de
     * bytes, obtidos a partir da primeira posição do buffer.
     *
     * @param buffer Buffer do qual bytes serão consultados para a
     *               inserção no arquivo.
     *
     * @param posicao Posição inicial no arquivo a partir da qual
     *                bytes serão escritos.
     *
     * @param total Total de bytes escritos.
     *
     * @see #escreve(ByteBuffer, int, int)
     */
    void escreve(byte[] buffer, int posicao, int total);
}