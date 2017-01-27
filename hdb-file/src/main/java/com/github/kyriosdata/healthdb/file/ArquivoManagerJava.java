/*
 * Copyright (c) 2016
 *
 * Fábio Nogueira de Lucena
 * Fábrica de Software - Instituto de Informática (UFG)
 *
 * Creative Commons Attribution 4.0 International License.
 */

package com.github.kyriosdata.healthdb.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementação de {@link ArquivoService} baseada no sistema de
 * nomeToHandle encapsulado por Java. Ou seja, essa implementação deve
 * funcionar nos ambientes Windows, Linux e MacOS.
 */
public class ArquivoManagerJava implements ArquivoManager {

    private final int INITIAL_CAPACITY = 128;
    private AtomicInteger handleGenerator;

    /**
     * Mantém relação entre nomes (identificadores) de nomeToHandle
     * e o handle único correspondente.
     */
    private Map<String, Integer> nomeToHandle;

    /**
     * Mantém a relação entre handles e os nomes correspondentes.
     * Fornece informação inversa ao da propriedade {@link #nomeToHandle}.
     */
    private Map<Integer, String> handleToName;

    /**
     * Mantém a relação entre um handle e o arquivo
     * correspondente.
     */
    private Map<Integer, Arquivo> handleToArquivo;

    public ArquivoManagerJava() {
        nomeToHandle = new HashMap<>(INITIAL_CAPACITY);
        handleToName = new HashMap<>(INITIAL_CAPACITY);
        handleToArquivo = new HashMap<>(INITIAL_CAPACITY);
        handleGenerator = new AtomicInteger(-1);
    }

    @Override
    public boolean abre(int handle) {
        Arquivo arquivo = handleToArquivo.get(handle);
        if (arquivo == null) {
            return false;
        }

        return false;
    }

    @Override
    public boolean fecha(int handle) {
        return false;
    }

    @Override
    public Arquivo get(int handle) {
        return null;
    }

    @Override
    public boolean existe(int handle) {
        return Files.exists(Paths.get(filename(handle)));
    }

    private String filename(int handle) {
        Arquivo arquivo = handleToArquivo.get(handle);

        return arquivo == null ? null : arquivo.filename();
    }

    @Override
    public boolean cria(int handle) {
        try {
            Files.createFile(Paths.get(filename(handle)));
            return true;
        } catch (IOException exp) {
            return false;
        }
    }

    @Override
    public boolean remove(int handle) {
        try {
            Files.deleteIfExists(Paths.get(filename(handle)));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void start(Object[] params) {
        nomeToHandle = new HashMap<>(INITIAL_CAPACITY);
        handleGenerator = new AtomicInteger(-1);
    }

    @Override
    public int register(String filename) {
        Integer handle = nomeToHandle.get(filename);
        if (handle == null) {
            int newHandle = handleGenerator.decrementAndGet();
            nomeToHandle.put(filename, newHandle);
            handleToName.put(newHandle, filename);
            handle = newHandle;
        }

        return handle;
    }

    @Override
    public void unregister(int handle) {

        String name = handleToName.get(handle);
        if (name != null) {
            Arquivo arquivo = handleToArquivo.get(handle);
            if (arquivo != null) {
                fecha(handle);
            }

            handleToArquivo.remove(handle);
            nomeToHandle.remove(name);
            handleToName.remove(handle);
        }
    }
}