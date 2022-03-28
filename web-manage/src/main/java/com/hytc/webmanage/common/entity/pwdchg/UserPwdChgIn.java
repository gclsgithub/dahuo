/*
 * (c) 2019 SmartFinancial Trade System.
 *
 *  システム名：SmartFinancial 取引システム
 *
 */

package com.hytc.webmanage.common.entity.pwdchg;

import com.hytc.webmanage.common.AdminBaseIn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
public class UserPwdChgIn extends AdminBaseIn {

    /** シリアライズ可能なクラスに対して付与するバージョン番号 */
    private static final long serialVersionUID = 1847160228401441585L;

    // ------ メンバ変数  ここから ------
    /**
     * 現在のパスワード<br>
     * hash化あり
     */
    private String currentPassword;

    /**
     * 新パスワード<br>
     * hash化なし（プレーンテキスト）。パスワード複雑さの要件を満たすかどうかをチェックするため
     */
    private String newPassword;

    /**
     * 新パスワード確認<br>
     */
    private String newPasswordConfirm;
    // ------ メンバ変数  ここまで ------
}
