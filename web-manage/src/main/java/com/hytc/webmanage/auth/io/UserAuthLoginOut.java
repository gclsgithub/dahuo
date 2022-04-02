package com.hytc.webmanage.auth.io;

import java.time.LocalDateTime;
import java.util.List;

import com.hytc.webmanage.common.UserAuthInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UserAuthLoginOut extends FwBaseOut {

    /** シリアライズ可能なクラスに対して付与するバージョン番号。 */
    private static final long serialVersionUID = -2940623882283751111L;

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
    /** 権限リスト */
    private List<UserAuthInfo> authInfoList;
    // ------ メンバ変数  ここまで ------

    /**
     * Edit Graphql API Call query By Bean
     *
     * @return graphql query
     */
    @Override
    public String toGraphqlQueryByBean() {
        StringBuilder builder = new StringBuilder();
        builder.append("companyCd ");
        builder.append("branchCd ");
        builder.append("userSqqId ");
        builder.append("userId ");
        builder.append("pwChangeDt ");
        builder.append("authInfoList ");
        builder.append(UserAuthInfo.toGraphqlQueryByOut());
        return builder.toString();
    }
}
