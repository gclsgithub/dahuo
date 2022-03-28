package com.hytc.webmanage.web.biz.dashboard;

import javax.servlet.http.HttpServletRequest;

import com.hytc.webmanage.common.FwUserDetails;
import com.hytc.webmanage.common.web.SessionManager;
import com.hytc.webmanage.web.config.MappingMaster;
import com.hytc.webmanage.web.config.SessionKeyMaster;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor()
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping(MappingMaster.DASHBOARD)
    @PreAuthorize("checkAuthRefByFuncSubId(#form.funcSubId())")
    public String index(final HttpServletRequest request //
        , final @ModelAttribute("form") DashboardForm form //
        , final @AuthenticationPrincipal FwUserDetails userDetails //
    ) {
        SessionManager.me.remove(request, SessionKeyMaster.LOGIN_STATUS);
        return "/dashboard/dashboard.html";
    }
}
