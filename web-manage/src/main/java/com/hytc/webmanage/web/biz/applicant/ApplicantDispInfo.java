package com.hytc.webmanage.web.biz.applicant;

import java.math.BigDecimal;

import jp.co.jsto.biz.trand.apply.ApplicantInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApplicantDispInfo extends ApplicantInfo {

    /**
     * シリアライズ可能なクラスに対して付与するバージョン番号。
     */
    private static final long serialVersionUID = -3731527398992917086L;
    /**
     * 顧客コード。
     */
    private String customerCode;

    /**
     * 顧客名。
     */
    private String customerName;

    /**
     * 申請日時。
     */
    private String applicantDtStr;

    /**
     * フレンド顧客コード。
     */
    private String friendCustomerCode;

    /**
     * 申請価格str。
     */
    private String applicantAmountStr;
}
