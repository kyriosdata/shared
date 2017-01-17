package com.github.kyriosdata.shared;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ByteBufferTest {

    @Test
    public void quandoTudoCabeNoBuffer() {

        byte[] bytes = "teste".getBytes();

        ByteBuffer bb = ByteBuffer.allocate(bytes.length);

        int resto = Log.copyToBuffer(bb, bytes, 0);

        assertEquals(0, resto);
        assertEquals(bytes.length, bb.position());
        assertTrue(bb.remaining() >= 0);
    }

    @Test
    public void quandoNemTudoCabeNoBuffer() {

        byte[] bytes = "teste".getBytes();

        // Buffer com 1 byte a menos
        ByteBuffer bb = ByteBuffer.allocate(bytes.length - 1);

        int resto = Log.copyToBuffer(bb, bytes, 0);

        assertEquals(1, resto);
        assertEquals(bytes.length - 1, bb.position());
        assertTrue(bb.remaining() == 0);
    }

    @Test
    public void seNadaCabeNadaCopiado() {

        byte[] bytes = "casa".getBytes();

        // Buffer com 1 byte a menos
        ByteBuffer bb = ByteBuffer.allocate(0);

        int resto = Log.copyToBuffer(bb, bytes, 0);

        assertEquals(bytes.length, resto);
        assertEquals(0, bb.position());
        assertTrue(bb.remaining() == 0);
    }

    @Test
    public void reutilizandoByteBuffer() {
        byte[] saude = "saúde".getBytes(StandardCharsets.UTF_8);
        byte[] vida = "vida".getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(10);

        Log.transferToBuffer(buffer, saude, false);
        Log.transferToBuffer(buffer, vida, true);
    }


}
