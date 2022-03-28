package com.hytc.webmanage.common.io;

import java.io.Serializable;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Note:<br />
 * Type parameters are only used for type safety with {@link CallRestApi} {@link CallGraphqlApi}.
 * Extending classes without an {@link FwBaseIn} counterpart should use {@code Object} for {@code O}
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class FwBaseIn implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 企業コード */
    private String companyCd;

    /** 部店コード */
    private String branchCd;

    /** アクションID(CREATE_PRG_ID,UPDATE_PRG_ID 更新用) */
    private String actionId;

    private String token;
}
