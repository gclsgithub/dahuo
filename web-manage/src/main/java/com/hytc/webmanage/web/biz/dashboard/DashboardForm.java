package com.hytc.webmanage.web.biz.dashboard;


import com.hytc.webmanage.common.config.FunctionId;
import com.hytc.webmanage.common.config.FunctionSubId;
import com.hytc.webmanage.common.web.FwBaseForm;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class DashboardForm implements FwBaseForm {

    /** 画面所属機能ID */
    @Override
    public String funcId() {
        return FunctionId.ADMIN_LOGIN_INFO.getValue();
    }

    /** 画面所属機能サブID */
    @Override
    public String funcSubId() {
        return FunctionSubId.DASHBOARD.getValue();
    }
}
