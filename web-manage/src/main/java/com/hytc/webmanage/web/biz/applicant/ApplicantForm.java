package com.hytc.webmanage.web.biz.applicant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jp.co.gt.fw.web.biz.FwBaseForm;
import jp.co.jsto.code.FunctionId;
import jp.co.jsto.code.FunctionSubId;
import lombok.Data;

@Data
public class ApplicantForm  implements FwBaseForm {

	/*************************************** 検索用情報 start ********************************************************/
    /**
     * ステータス。
     *
     */
    private String statusCls;

    /**
     * 取引種別。
     *
     */
    private String transactionCls;

    /**
     * 売買区分。
     *
     */
    private String trandingCls;

    /**
     * 顧客コード。
     *
     */
    private String customerCd;
    /*************************************** 検索用情報 end ********************************************************/

    private List<ApplicantDispInfo> applicantDispInfo;

    private BigDecimal applicantId;

    private String applicantStatus;

    private String applicantStatusDt;

    private String inDenialReasonCls;

    private String applicantSelect;
    /** 画面所属機能ID */
    @Override
    public String funcId() {
        return FunctionId.DELETE_NOTICE.getValue();
    }

    /** 画面所属機能サブID */
    @Override
    public String funcSubId() {
        return FunctionSubId.TRADE_APP_MNG.getValue();
    }
}
