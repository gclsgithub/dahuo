package com.hytc.webmanage.web.config;

import jp.co.gt.fw.web.SessionKey;

public enum SessionKeyMaster implements SessionKey {

    /** ログインステータス */
    LOGIN_STATUS,
    /** パスワード強制変更 */
    FORCE_CHANGE_PWD,
    ;

}
