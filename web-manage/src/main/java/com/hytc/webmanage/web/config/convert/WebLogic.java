package com.hytc.webmanage.web.config.convert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.servlet.http.HttpSession;

import com.hytc.webmanage.common.CodeEnum;
import com.hytc.webmanage.common.FwUserDetails;
import com.hytc.webmanage.common.io.BranchCd;
import com.hytc.webmanage.common.resolve.FwMessageResolve;
import com.hytc.webmanage.web.common.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class WebLogic {

    @Autowired
    private HttpSession session;

    @Autowired
    private FwMessageResolve messageResolve;

    /**
     * メニュー判定
     *
     * @param funcId
     * @param key 判定対象キー
     * @return
     */
    public boolean isMenuOrSession(String funcId, String key) {
        // 判定キー未設定
        if (funcId == null || key == null) {
            return false;
        }
        // 指定メニューあるいは、メニューセッションあり
        if (funcId.equals(key) || session.getAttribute(WebConstants.OPEN_MENU + key) != null) {
            return true;
        }
        return false;
    }

    /**
     * メニュー判定
     *
     * @param funcId
     * @param key 判定対象キー
     * @return
     */
    public boolean isMenu(String funcId, String key) {
        // 判定キー未設定
        if (funcId == null || key == null) {
            return false;
        }
        // 指定メニュー
        if (funcId.equals(key)) {
            return true;
        }
        return false;
    }

    /**
     * サブメニュー判定
     *
     * @param funcSubId
     * @param key 判定対象キー
     * @return
     */
    public boolean isSubMenu(String funcSubId, String key) {
        // 判定キー未設定
        if (funcSubId == null || key == null) {
            return false;
        }
        // 指定サブメニュー
        if (funcSubId.equals(key)) {
            return true;
        }
        return false;
    }

    /**
     * 営業者判定
     *
     * @return
     */
    public boolean isBusiness() {
        // 認証情報取得
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        FwUserDetails userDetails = (FwUserDetails) a.getPrincipal();
        // 営業者判定
        if (BranchCd.BUSINESS.is(userDetails.getBranchCd())) {
            return true;
        }
        return false;
    }

    /**
     * 媒介者判定
     *
     * @return
     */
    public boolean isMediator() {
        // 認証情報取得
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        FwUserDetails userDetails = (FwUserDetails) a.getPrincipal();
        // 媒介者判定
        if (BranchCd.MEDIATOR.is(userDetails.getBranchCd())) {
            return true;
        }
        return false;
    }

    /**
     * Takes a string formatted like: 'my_string_variable' and returns it as: 'myStringVariable'
     *
     * @param strs
     * @return
     */
    public String underScoreToCamelCase(String str) {
        if (str == null) {
            return str;
        }
        if (!str.contains("_")) {
            return str.toLowerCase();
        }
        StringBuilder sb = new StringBuilder("");
        String[] elems = str.split("_");
        for (int i = 0; i < elems.length; i++) {
            String elem = elems[i].toLowerCase();
            if (i != 0) {
                char first = elem.toCharArray()[0];
                sb.append((char) (first - 32)).append(elem.substring(1));
            } else {
                sb.append(elem);
            }
        }
        return sb.toString();
    }

    /**
     * Enum値をもとに、国際化Keyに変換
     *
     * @param enumArray
     * @param value
     * @return 国際化Key
     */
    public String convertEnumToKey(CodeEnum<?>[] enumArray, String value) {
        for (CodeEnum<?> codeEnum : enumArray) {
            if (codeEnum.is(value)) {
                return this.underScoreToCamelCase(codeEnum.toEnum().name());
            }
        }
        return "hyphen";
    }

    /**
     * Enum値をもとに、国際化メッセージに変換
     *
     * @param enumArray
     * @param value
     * @return 国際化メッセージ
     */
    public String resolveEnumMessage(CodeEnum<?>[] enumArray, String value) {
        if (enumArray == null || value == null) {
            return "";
        }
        return messageResolve.resolveMessage(convertEnumToKey(enumArray, value));
    }

    /**
     * NULLからスペースに変換
     *
     * @param value
     * @return 変換後Str
     */
    public String nullToSpace(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    /**
     * ファイルパスからbase64の文字列に変更
     *
     * @param path
     * @return base64に変更
     */
    public String pathToBase64Byte(String path) {
        if (path == null) {
            return null;
        }
        try {
            byte[] bFile = Files.readAllBytes(Paths.get(path));
            return Base64.getEncoder().encodeToString(bFile);
        } catch (IOException e) {
            log.warn("the path {} is Incorrect.", path);
        }
        return null;
    }
}
