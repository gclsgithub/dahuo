package com.hytc.webmanage.common.entity.pwdchg;

import com.hytc.webmanage.common.io.FwBaseOut;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UserPwdChgOut extends FwBaseOut {

    /** シリアライズ可能なクラスに対して付与するバージョン番号。 */
    private static final long serialVersionUID = 4247482865899447805L;
    // ------ メンバ変数  ここまで ------
}
