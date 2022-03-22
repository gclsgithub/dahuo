/**
 * (c) 2020 LBI Co., Ltd.
 *
 *  システム名：JSTO
 */

package com.hytc.webmanage.web.biz.customer;

import jp.co.jsto.biz.customer.customer.CustomerInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 顧客情報取得処理を表す。
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomerDispInfo extends CustomerInfo {

    /**
     * シリアライズ可能なクラスに対して付与するバージョン番号。
     */
    public static final long serialVersionUID = 7055475115776060000L;

    /**
     * 顧客コード
     *
     * 顧客管理．部店コード＋顧客管理．顧客ID
     */
    private String customerCd;

    /**
     * 顧客氏名
     *
     * 顧客管理．顧客姓＋顧客管理．顧客名
     */
    private String customerName;

    /**
     * 本登録日
     */
    private String definitiveRegistrationDateStr;

    /**
     * 確認書類提出日
     *
     * 画面項目：確認資料提出日
     */
    private String confirmationDocumentSubmitDateStr;

    /**
     * 承認日時
     *
     * 画面項目：承認日
     */
    private String approvalDateStr;

    /**
     * 認証日時
     *
     * 画面項目：認証日
     */
    private String authenticationDateStr;

}
