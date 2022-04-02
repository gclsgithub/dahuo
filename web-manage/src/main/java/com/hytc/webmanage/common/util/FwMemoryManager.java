package com.hytc.webmanage.common.util;

import java.util.concurrent.ConcurrentHashMap;

public class FwMemoryManager {

    /** メモリ */
    private static final ConcurrentHashMap<String, Object> memory = new ConcurrentHashMap<>();

    /**
     * メモリに保存する情報クリアする
     */
    public static void clearAllMemory() {
        memory.clear();
    }

    /**
     * キーでメモリの情報を削除する
     */
    public static void remove(String key) {
        if (key == null) {
            return;
        }
        memory.remove(key);
    }

    /**
     * 情報をメモリに保存する
     */
    public static void put(String key, Object obj) {
        if (key == null || obj == null) {
            return;
        }
        memory.put(key, obj);
    }

    /**
     * キーでメモリの情報を取得する
     */
    public static Object get(String key) {
        if (key == null) {
            return null;
        }
        return memory.get(key);
    }
}
