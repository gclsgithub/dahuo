package com.hytc.webmanage.web.biz.settings;

@lombok.Getter
@lombok.Setter
public class PwdChgForm {

    /** 当前パスワード */
    private String currentPassword;

    /** 新パスワード */
    private String newPassword;

    /** 確認パスワード */
    private String newPasswordConfirm;
}
