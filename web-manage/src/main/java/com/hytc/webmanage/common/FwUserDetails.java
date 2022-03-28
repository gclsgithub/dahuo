package com.hytc.webmanage.common;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * SpringSecurity ログインユーザー情報
 *
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
final public class FwUserDetails extends FwUserInfo {

    private static final long serialVersionUID = 8654203563338040342L;

    /** 会社コード */
    private String companyCd;
    /** 部店コード */
    private String branchCd = null;
    /** 管理者採番ID */
    private java.math.BigDecimal userSqqId = null;
    /** 管理者ID */
    private String userId = null;
    /** パスワード変更日時 */
    private LocalDateTime pwChangeDt;
    /** ログイン状態を記憶 */
    private String remLoginSts;
    /** ホームURL */
    private String homeUrl;
    /** 権限リスト */
    private List<UserAuthInfo> authInfoList;

    public FwUserDetails() {
        super(null);
    }

    public FwUserDetails(final String userId) {
        super(userId);
    }
}
