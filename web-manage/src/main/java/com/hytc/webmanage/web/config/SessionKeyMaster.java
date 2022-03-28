package com.hytc.webmanage.web.config;


import com.hytc.webmanage.common.web.SessionKey;

public enum SessionKeyMaster implements SessionKey {

    /** ログインステータス */
    LOGIN_STATUS,
    /** パスワード強制変更 */
    FORCE_CHANGE_PWD,
    ;

}
