package com.alpaca.infrastructure.core.utils;


import java.util.UUID;

public final class GuidHelper {

    /**
     * 获取guid
     *
     * @return
     */
    public static String getGuid() {
        UUID uuid = UUID.randomUUID();
        String guid = uuid.toString();
        return guid.replaceAll("-", "");
    }

}
