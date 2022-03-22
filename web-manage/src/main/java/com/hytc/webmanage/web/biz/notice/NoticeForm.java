package com.hytc.webmanage.web.biz.notice;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import jp.co.gt.fw.web.biz.FwBaseForm;
import jp.co.jsto.code.FunctionId;
import jp.co.jsto.code.FunctionSubId;
import lombok.Data;

@Data
public class NoticeForm implements FwBaseForm {

    /*************************************** 検索用情報 start ********************************************************/
    /**
     * 顧客区分。
     *
     */
    private String customerCls;

    /**
     * 顧客検索キー。
     *
     */
    private String customerSearchKey;

    /**
     * お知らせステータス。
     *
     */
    private String noticeStatusCls;
    /*************************************** 検索用情報 end ********************************************************/

    /*************************************** 検索結果 start ********************************************************/
    private List<NoticeDispInfo> noticeInfos;
    /*************************************** 検索結果 end ********************************************************/

    /*************************************** 更新登録 start ********************************************************/
    /**
     * お知らせID。
     */
    private BigDecimal noticeId;

    /**
     * ステータス
     */
    @NotBlank
    private String noticeStatus;

    /**
     * 掲載予約区分。
     */
    @NotBlank
    private String openEntryCls;

    /**
     * 掲載日時間
     */
    private String openEntryDt;

    /**
     * お知らせタイトル。
     */
    @NotBlank
    private String noticeTitle;

    /**
     * 掲載顧客区分。
     */
    @NotBlank
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
    private String noticeContent;

    /**
     * おしらせ画象
     */
    private MultipartFile imageFile;

    /**
     * 操作区分
     */
    private String operationCls;

    /*************************************** 更新登録 end ********************************************************/

    /** 画面所属機能ID */
    @Override
    public String funcId() {
        return FunctionId.ADMIN_LOGIN_INFO.getValue();
    }

    /** 画面所属機能サブID */
    @Override
    public String funcSubId() {
        return FunctionSubId.NOTICE_MNG.getValue();
    }
}
