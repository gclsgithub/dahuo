package com.hytc.webmanage.common;

import com.hytc.webmanage.common.io.FwBaseIn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class AdminBaseIn extends FwBaseIn {

    private static final long serialVersionUID = 5246358518245046987L;

    /** ログインSEQ_ID */
    private BigDecimal userSeqId;

    /** IDMS·USERID */
    private String userId;

    /** ログインID */
    private BigDecimal customerId;



}
