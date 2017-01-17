package com.github.kyriosdata.shared;

/**
 * Definição de serviços de <i>logging</i>.
 *
 * <p>Orientações sobre separação da interface e da implementação correspondente.
 *     http://softwareengineering.stackexchange.com/questions/246620/in-java-what-are-some-good-ways-to-separate-apis-from-implementation-of-entire
 */
interface LogService extends Runnable {
    /**
     * Registra mensagem de log (informativa).
     *
     * @param msg Mensagem a ser registrada.
     */
    void info(String msg);

    /**
     * Registra mensagem de log (aviso). Um aviso
     * é entendido como uma situação excepcional,
     * embora não configure necessariamente algo
     * indesejável.
     *
     * @param msg Mensagem a ser registrada.
     */
    void warn(String msg);

    /**
     * Registra mensagem de pertinente à falha. Representa
     * situação indesejável.
     *
     * @param msg Mensagem a ser registrada.
     */
    void fail(String msg);
}

