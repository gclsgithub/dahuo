/*
 * (c) 2019 SmartFinancial Trade System.
 *
 *  システム名：SmartFinancial 取引システム
 *
 */

package com.hytc.webmanage.auth.io;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
public class CustomerLoginIn extends CustBaseIn {

    /** シリアライズ可能なクラスに対して付与するバージョン番号 */
    private static final long serialVersionUID = 7909485969692142561L;

    // ------ メンバ変数  ここから ------
    /**
     * パスワード。
     *
     * Nullチェック必須項目
     *
     * 最大桁数 : -
     * 整数桁数 : -
     * 少数桁数 : -
     *
     * <pre>
     * Hash後パスワード
     * </pre>
     */
    private String password;

    /**
     * UserAgent文字列
     */
    private String userAgent;

    /**
     * ソースIP
     */
    private String sourceIp;
    // ------ メンバ変数  ここまで ------
}
