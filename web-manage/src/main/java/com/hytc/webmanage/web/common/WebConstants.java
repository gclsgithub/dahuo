package com.hytc.webmanage.web.common;

import java.util.HashMap;
import java.util.Map;

import jp.co.jsto.web.biz.dashboard.DashboardForm;
import jp.co.jsto.web.config.MappingMaster;

public class WebConstants {

    public static final String JA = "ja";

    public static final String EN = "en";

    public static final String ZH = "zh";

    public static final String EMPTY_STRING = "";

    public static final String UNDERSCORE = "_";

    public static final String HYPHEN = "-";

    /** redirect: */
    public static final String REDIRECT = "redirect:";

    /** 送信タイトル内容: フォーマット */
    public static final String MAIL_FOMART_SUBJECT = "key1=%s";

    /** 送信メッセージ内容: フォーマット */
    public static final String MAIL_FOMART_MSG = "key1=%s";

    /** 送信メッセージ内容: フォーマット(二つキー) */
    public static final String MAIL_FOMART_MSG_TWO = "key1=%s##key2=%s";

    public static final String USER_AGENT = "User-Agent";

    /** 開始画面 */
    public static final String START_SCREEN = "start_screen";

    /** ログイン状態を記憶 */
    public static final String REM_LOGIN_STS = "rem_login_sts";

    /** エラー */
    public static final String ERROR = "error";

    /** エラーあり */
    public static final String ERROR_YES = "1";

    /** エラータイプ */
    public static final String ERROR_TYPE = "type";

    /** Bindエラー */
    public static final String ERROR_BIND = "bind";

    /** jsonエラー */
    public static final String ERROR_JSON = "json";

    /** OPEN-MENU */
    public static final String OPEN_MENU = "openMenu";

    /** メニュー情報引き渡す */
    public static final String MENU_FOMART_PARAM = "?funcId=%s&funcSubId=%s";

    /** MappingとForm.class */
    public static final Map<String, Class<?>> MAPPING_FORM;
    static {
        MAPPING_FORM = new HashMap<>();
        MAPPING_FORM.put(MappingMaster.DASHBOARD, DashboardForm.class);
    }


    /** 単項目チェックキー */
    /** 顧客コード */
    public static final String CUSTOM_CODE = "customCode";
    /** 掲載予約日付 */
    public static final String OPEN_ENTRY_DT = "openEntryDt";
    /** 申請否認理由 */
    public static final String IN_DENIAL_REASON_CLS = "inDenialReasonCls";
}
