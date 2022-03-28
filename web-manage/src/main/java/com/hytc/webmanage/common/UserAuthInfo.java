package com.hytc.webmanage.common;

import java.io.Serializable;

/**
 * ユーザ権限一覧
 *
 * @author グローテック
 * @version $Id:$
 */
@lombok.Getter
@lombok.Setter
@lombok.ToString
@lombok.experimental.Accessors(chain = true)
public class UserAuthInfo implements Serializable {

    /** シリアライズ可能なクラスに対して付与するバージョン番号 */
    private static final long serialVersionUID = -1271208796017758150L;

    /** 企業コード */
    private String companyCd = null;
    /** 機能ID */
    private java.math.BigDecimal functionId = null;
    /** 機能サブID */
    private java.math.BigDecimal functionSubId = null;
    /** 管理者ID */
    private String userId = null;
    /** 権限区分 */
    private String authorityCls = null;
    /** 機能名 */
    private String functionName = null;
    /** 機能内容 */
    private String functionContents = null;
    /** 実行パス */
    private String exePath = null;

    /**
     * Edit Graphql API Call query
     *
     * @return graphql query
     */
    public static String toGraphqlQueryByOut() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("companyCd ");
        builder.append("userId ");
        builder.append("functionId ");
        builder.append("functionSubId ");
        builder.append("authorityCls ");
        builder.append("functionName ");
        builder.append("functionContents ");
        builder.append("exePath ");
        builder.append("}");
        return builder.toString();
    }
}
