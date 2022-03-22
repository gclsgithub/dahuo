/**
 * (c) 2020 LBI Co., Ltd.
 *
 *  システム名：JSTO
 */
package com.hytc.webmanage.web.biz.customer;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import jp.co.gt.fw.web.biz.FwBaseForm;
import jp.co.jsto.code.FunctionId;
import jp.co.jsto.code.FunctionSubId;
import lombok.Data;

/**
 * 顧客情報管理フォーム
 */
@Data
public class CustomerForm implements FwBaseForm {

    /****************************************************************************************************
     * 検索用情報 start
     ****************************************************************************************************/

    /**
     * 部店コード
     */
    private String branchCd;

    /**
     * 顧客ID
     */
    private String customerId;

    /**
     * 顧客コード
     *
     * 部店コード＋顧客ID
     */
    private String customerCd;

    /**
     * 顧客姓
     */
    private String familyName;

    /**
     * 顧客名
     */
    private String givenName;

    /**
     * 顧客氏名
     *
     * 顧客管理．顧客姓＋顧客管理．顧客名
     */
    private String customerName;

    /**
     * 顧客区分
     */
    private String displayCustomerCls;

    /**
     * 口座管理区分
     */
    private String displayAccountManagementCls;

    /****************************************************************************************************
     * 検索用情報 end
     ****************************************************************************************************/

    /****************************************************************************************************
     * 検索結果 start
     ****************************************************************************************************/
    private List<CustomerDispInfo> customerInfos;
    /****************************************************************************************************
     * 検索結果 end
     ****************************************************************************************************/

    /****************************************************************************************************
     * 更新登録 start
     ****************************************************************************************************/

    /**
     * ステータス
     */
    @NotEmpty
    @NotNull
    private String customerStatus;

    /**
     * 掲載予約区分。
     */
    @NotEmpty
    private String openEntryCls;

    /**
     * 掲載日時間
     */
    private String openEntryDt;

    /**
     * お知らせタイトル。
     */
    @NotNull
    @NotEmpty
    private String customerTitle;

    /**
     * 掲載顧客区分。
     */
    @NotEmpty
    private String openTargetCls;

    /**
     * 顧客コード
     */
    private String customCode;

    /**
     * 重要ブラグ
     */
    private String importantFlg;

    /**
     * お知らせ内容
     */
    private String customerContent;

    /**
     * おしらせ画象
     */
    private MultipartFile imageFile;

    /**
     * 操作区分
     */
    private String operationCls;

    /****************************************************************************************************
     * 更新登録 end
     ****************************************************************************************************/

    /** 画面所属機能ID */
    @Override
    public String funcId() {
        return FunctionId.ADMIN_LOGIN_INFO.getValue();
    }

    /** 画面所属機能サブID */
    @Override
    public String funcSubId() {
        return FunctionSubId.REF_USER_INFO.getValue();
    }

}
