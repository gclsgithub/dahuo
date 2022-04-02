package com.hytc.webmanage.common.util;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

public enum CommonUtils {
    me;

    public final String EMPTY = "";

    /**
     * lengthで指定した桁数までゼロ埋めした文字列を返却する
     *
     * @param in 入力文字列
     * @param len ゼロ埋めする桁数
     * @return length分まで0を付加したin
     */
    public String zeroPadding(String in, int len) {
        return String.format("%" + len + "s", in).replace(" ", "0");
    }

    /**
     * 名前編集
     *
     * @param lastName 姓
     * @param middleName ミドル名
     * @param firstName 名
     * @return 名前
     */
    public String editName(String lastName, String middleName, String firstName) {
        if (EMPTY.equals(trimToEmpty(middleName))) {
            return String.format("%s %s", trimToEmpty(lastName), trimToEmpty(firstName));
        }
        return String.format("%s %s %s", trimToEmpty(lastName), trimToEmpty(middleName), trimToEmpty(firstName));
    }

    /**
     * trimToEmpty
     *
     * @param str the String to be trimmed, may be null
     * @return the trimmed String, or an empty String if {@code null} input
     */
    public String trimToEmpty(final String str) {
        return str == null ? EMPTY : str.trim();
    }


    /**
     * trimToEmpty
     *
     * @param str the String to be trimmed, may be null
     * @return the trimmed String, or an empty String if {@code null} input
     */
    public String trimSpaceToEmpty(final String str) {
        if (str == null || EMPTY.equals(str)) {
            return null;
        }
        return str.replaceAll(" ", EMPTY).replaceAll("　", EMPTY);
    }

    /**
     * Checks whether the passed list is null or empty
     * @param pstrTarget The list to check
     * @return true if null/empty, false otherwise
     */
    public boolean isNullOrEmptyList(List<?> list) {
        return null == list || list.isEmpty();
    }

    /**
     * ランダム文字列を作成する
     *
     * @param cnt 桁数
     * @return ランダム文字列
     */
    public String createRandomString(int cnt) {
        if (cnt < 1) {
            return "";
        }
        String str = RandomStringUtils.randomAscii(cnt * 100);
        str = RandomStringUtils.random(cnt * 10, str);
        byte[] bytes = str.getBytes();
        str = bytesToHex(bytes);
        str = RandomStringUtils.random(cnt, str);

        return str;
    }

    /**
     * byte配列をHex文字列に変換する
     *
     * @param bytes 変換するbyte配列
     * @return 変換後Hex文字列
     */
    public String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
