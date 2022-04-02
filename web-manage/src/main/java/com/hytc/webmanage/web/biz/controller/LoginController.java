package com.hytc.webmanage.web.biz.controller;

import com.hytc.webmanage.web.biz.FwBaseController;
import com.hytc.webmanage.web.biz.UserForm;
import com.hytc.webmanage.web.biz.service.LoginService;
import com.hytc.webmanage.web.config.MappingMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController extends FwBaseController {

    private final LoginService loginService;

    @PostMapping(MappingMaster.AUTH.AUTH_LOGIN)
    public String login(@ModelAttribute("form") UserForm form){

        String  userId = form.getLoginId();
        String password = form.getPassword();

        if (loginService.login(userId,password)){

            return "/dashboard/dashboard.html";
        }
        return "/auth/login.html";
    }
}
