package com.hytc.webmanage.web.biz.notice;

import jp.co.jsto.biz.customer.notice.NoticeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NoticeDispInfo extends NoticeInfo {

    /**
     * シリアライズ可能なクラスに対して付与するバージョン番号。
     */
    private static final long serialVersionUID = -3731527398992917086L;

    /**
     * 顧客コード。
     */
    private String customerCode;

    /**
     * 顧客区分Str。
     */
    private String applicantIdStr;

    /**
     * 顧客名。
     */
    private String customerName;

    /**
     * 掲載予約日付Str。
     */
    private String openEntryDtStr;

    /**
     * 作成日時Str。
     */
    private String createDtStr;

    /**
     * お知らせステータスStr。
     */
    private String noticeStatusClsStr;

    /**
     * お知らせ画像Byte。
     */
    private String noticeImageByte;
}
