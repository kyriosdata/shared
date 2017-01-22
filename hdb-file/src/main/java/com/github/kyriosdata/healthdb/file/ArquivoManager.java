package com.github.kyriosdata.healthdb.file;

/**
 * Encapsula serviços básicos de gerência do
 * ciclo de vida de um arquivo ({@link Arquivo}).
 *
 * @see Arquivo
 */
public interface ArquivoManager {

    /**
     * Abre o arquivo para leitura e escrita.
     *
     * @param filename O nome do arquivo.
     *
     * @return Objeto por meio do qual operações de
     * leitura e escrita podem ser realizadas sobre o
     * arquivo.
     *
     * @see #existe(String)
     * @see #remove(String)
     */
    Arquivo abre(String filename);

    /**
     * Verifica se o arquivo existe.
     *
     * @param filename O nome do arquivo.
     *
     * @return {@code true} se o arquivo existe e
     * {@code false}, caso contrário.
     *
     * @see #abre(String)
     * @see #remove(String)
     */
    boolean existe(String filename);

    /**
     * Remove o arquivo, caso exista.
     *
     * @param filename O nome do arquivo.
     *
     * @return {@code true} se o arquivo foi
     * removido ou não existe. Retorna {@code false}
     * apenas se o arquivo existe, mesmo após a
     * tentativa de removê-lo.
     */
    boolean remove(String filename);
}