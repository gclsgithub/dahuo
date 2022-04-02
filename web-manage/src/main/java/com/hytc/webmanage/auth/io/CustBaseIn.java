package com.hytc.webmanage.auth.io;

import com.hytc.webmanage.common.io.FwBaseIn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class CustBaseIn extends FwBaseIn {

    private static final long serialVersionUID = 5246358518245046987L;

    /** 顧客ID */
    private String customerId;
}
