package com.hytc.webmanage.auth.io;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CustomerLoginOut extends FwBaseOut {

    /** シリアライズ可能なクラスに対して付与するバージョン番号。 */
    private static final long serialVersionUID = -2940623882283751111L;

    /** 会社コード */
    private String companyCd;
    /** 部店コード */
    private String branchCd = null;
    /** 顧客管理採番ID */
    private java.math.BigDecimal customerSeqId = null;
    /** 顧客ID */
    private String customerId = null;
    /** パスワード変更日時 */
    private LocalDateTime pwChangeDt;
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
        builder.append("customerSeqId ");
        builder.append("customerId ");
        builder.append("pwChangeDt ");
        return builder.toString();
    }
}
