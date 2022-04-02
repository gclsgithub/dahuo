package com.hytc.webmanage.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.util.Assert;

import lombok.extern.log4j.Log4j2;

@Log4j2
public enum FwAuth {
    me;

    public String shaString(String value) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return this.bytesToHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            log.catching(e);
        }
        return "";
    }

    private String bytesToHex(byte[] hash) {
        return this.bytesToHex(hash, true);
    }

    private String bytesToHex(byte[] hash, boolean toUpperCase) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        if (toUpperCase) {
            return hexString.toString().toUpperCase();
        }
        return hexString.toString();
    }

    final private char[] group1 = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' }; // 半角英字（小文字）
    final private char[] group2 = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' }; // 半角英字（大文字）
    final private char[] group3 = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' }; // 半角数字
    final private char[] group4 = new char[] { '_', '-', '!', '#', ':', '+', '@', '*', '%', '$' }; // 使用可能記号

    /**
     * プレーンテキストのパスワードを複雑さの要件を満たすかどうかをチェック
     * @param password
     * @return
     */
    final public boolean checkPasswordRequirements(String password) {
        Assert.notNull(password, "password must not be null");
        boolean hasGrp1 = false;
        boolean hasGrp2 = false;
        boolean hasGrp3 = false;
        boolean hasGrp4 = false;
        int cnt = 0;
        for (int i = 0; i < password.length(); i++) {
            final char charAt = password.charAt(i);
            if (!hasGrp1 && this.anyMatch(charAt, group1)) {
                hasGrp1 = true;
                cnt++;
            }
            if (!hasGrp2 && this.anyMatch(charAt, group2)) {
                hasGrp2 = true;
                cnt++;
            }
            if (!hasGrp3 && this.anyMatch(charAt, group3)) {
                hasGrp3 = true;
                cnt++;
            }
            if (!hasGrp4 && this.anyMatch(charAt, group4)) {
                hasGrp4 = true;
                cnt++;
            }
            if (cnt >= 3) {
                return true;
            }
        }
        return false;
    }

    final public boolean anyMatch(char charA, char[] chars) {
        for (char charB : chars) {
            if (charA == charB) {
                return true;
            }
        }
        return false;
    }

    /**
     * ログインIDを生成。<br>
     * ランダム英大文字1桁+部店コード3桁+顧客コード6桁
     * @param branchCd
     * @param customerCd
     * @return
     */
    final public String genLoginId(String branchCd, String customerCd) {
        Assert.notNull(branchCd, () -> "branchCd can not be null");
        Assert.notNull(customerCd, () -> "customerCd can not be null");
        Assert.isTrue(branchCd.length() == 3, () -> "branchCd.length() == 3");
        Assert.isTrue(customerCd.length() == 6, () -> "customerCd.length() == 6");
        char[] group = new char[] { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' }; // 半角英字（大文字）
        Random random = ThreadLocalRandom.current();
        return String.format("%s%s%s", group[random.nextInt(group.length)], branchCd, customerCd);
    }

    /**
     * ランダムのパスワードを生成<br>
     * １．半角英字（小文字）、半角英字（大文字）、半角数字を含む。<br>
     * ２．同じ文字は2回以上使用しない。<br>
     * アルゴリズム：<br>
     * １．配列の前n-1からランダムで選ぶ、選んだものとn個目と交換。<br>
     * ２．配列の前n-2からランダムで選ぶ、選んだものとn-1個目と交換。<br>
     * ３．上記の繰り返す。<br>
     * 例：（イメージ）<br>
     * 初期：<br>
     * [a,b,c,d,e,f,g]<br>
     * 一回目、2番目のbが選ばれた場合、bとgを交換<br>
     * [a,g,c,d,e,f,b]になる。<br>
     * 二回目、4番目のdが選ばれた場合、dとfを交換<br>
     * [a,g,c,f,e,d,b]になる。<br>
     *
     * @param length
     * @return
     */
    final public String genRandomPassword(int length) {
        Assert.isTrue(length >= 3 && length <= 12, () -> "3 <= length <= 12 is request");

        char[] group1 = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}; // 半角英字（小文字）
        char[] group2 = new char[] {'A', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}; // 半角英字（大文字）
        char[] group3 = new char[] {'2', '3', '4', '5', '6', '7', '9'}; // 半角数字

        // オリジナルをコピー
        final CharHolder gp1 = new CharHolder(group1);
        final CharHolder gp2 = new CharHolder(group2);
        final CharHolder gp3 = new CharHolder(group3);

        Random random = ThreadLocalRandom.current();
        char[] password = new char[length];
        // 全種類を使用したいので、まずは全種類を１つつ使用。
        password[0] = gp1.get(random);
        password[1] = gp2.get(random);
        password[2] = gp3.get(random);
        // 残りは、全種類の中でランダムで選ぶ
        for (int ii = 3; ii < length; ii++) {
            // random 大文字 40% 小文字 40% 数字 20% の確率で。
            password[ii] = this.getGp(random, gp1, gp1, gp1, gp1, gp2, gp2, gp2, gp2, gp3, gp3).get(random);
        }
        // シャッフルする
        this.shuffleArray(random, password);
        return new String(password);
    }

    private CharHolder getGp(Random random, CharHolder... ggg) {
        return ggg[random.nextInt(ggg.length)];
    }

    private void shuffleArray(Random random, char[] array) {
        for (int ii = array.length - 1; ii > 0; ii--) {
            int index = random.nextInt(ii + 1);
            // Simple swap
            char cc = array[index];
            array[index] = array[ii];
            array[ii] = cc;
        }
    }

    class CharHolder {
        private int pos = 1;
        final public char[] array;

        CharHolder(char[] array) {
            this.array = array;
        }

        char get(Random random) {
            int index = random.nextInt(this.array.length - 1 - this.pos);
            char result = this.array[index];
            this.swap(this.array, index, this.array.length - this.pos);
            this.pos++;
            return result;
        }

        private void swap(char[] array, int from, int to) {
            if (from == to) {
                return;
            }
            char tmp = array[from];
            array[from] = array[to];
            array[to] = tmp;
        }
    }

}
