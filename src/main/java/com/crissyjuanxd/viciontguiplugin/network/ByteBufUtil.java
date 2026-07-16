package com.crissyjuanxd.viciontguiplugin.network;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

// Replica la codificación VarInt + UTF-8 que usa PacketCodecs.string() en el mod
public final class ByteBufUtil {
    private ByteBufUtil() {}

    public static void writeVarInt(ByteArrayOutputStream out, int value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                out.write(value);
                return;
            }
            out.write((value & 0x7F) | 0x80);
            value >>>= 7;
        }
    }

    public static void writeString(ByteArrayOutputStream out, String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeVarInt(out, bytes.length);
        out.write(bytes, 0, bytes.length);
    }

    public static int readVarInt(byte[] data, int[] cursor) {
        int value = 0, position = 0;
        byte currentByte;
        while (true) {
            currentByte = data[cursor[0]++];
            value |= (currentByte & 0x7F) << position;
            if ((currentByte & 0x80) == 0) break;
            position += 7;
            if (position >= 32) throw new RuntimeException("VarInt demasiado grande");
        }
        return value;
    }

    public static String readString(byte[] data, int[] cursor) {
        int length = readVarInt(data, cursor);
        String result = new String(data, cursor[0], length, StandardCharsets.UTF_8);
        cursor[0] += length;
        return result;
    }
}