package com.hytc.webmanage.web.biz.settings;

import javax.servlet.http.HttpServletRequest;

import com.hytc.webmanage.common.FwUserDetails;
import com.hytc.webmanage.common.entity.pwdchg.UserPwdChgOut;
import com.hytc.webmanage.common.exception.FwWebBusinessException;
import com.hytc.webmanage.common.io.FwBaseOut;
import com.hytc.webmanage.common.resolve.ResultMessages;
import com.hytc.webmanage.common.web.SessionManager;
import com.hytc.webmanage.web.biz.FwBaseController;
import com.hytc.webmanage.web.config.MappingMaster;
import com.hytc.webmanage.web.config.SessionKeyMaster;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor()
public class PwdChgController extends FwBaseController {

    final private PwdChgService pwdChgService;

    @GetMapping(MappingMaster.FORCE_CHANGE_PWD)
    public String forceChangePwd() {
        return "/settings/forceChangePwd.html";
    }

    @PostMapping(MappingMaster.DO_CHANGE_PWD)
    @ResponseBody
    public UserPwdChgOut doChangePwd(final @AuthenticationPrincipal FwUserDetails userDetails //
        , final @ModelAttribute("form") PwdChgForm form //
        , final HttpServletRequest request) {

        // 必須チェック処理追加
        if (StringUtils.isEmpty(form.getCurrentPassword())) {
            throw new FwWebBusinessException(FwBaseOut.ng(ResultMessages.fromCode("E_BL_9001", "currentPassword")));
        }
        if (StringUtils.isEmpty(form.getNewPassword())) {
            throw new FwWebBusinessException(FwBaseOut.ng(ResultMessages.fromCode("E_BL_9001", "newPassword")));
        } else if (8 > form.getNewPassword().length() || 16 < form.getNewPassword().length()) {
            throw new FwWebBusinessException(FwBaseOut.ng(ResultMessages.fromCode("E_BL_0004")));
        }

        if (StringUtils.isEmpty(form.getNewPasswordConfirm())) {
            throw new FwWebBusinessException(FwBaseOut.ng(ResultMessages.fromCode("E_BL_9001", "newPasswordConfirm")));
        } else if (8 > form.getNewPassword().length() || 16 < form.getNewPassword().length()) {
            throw new FwWebBusinessException(FwBaseOut.ng(ResultMessages.fromCode("E_BL_0004")));
        }

        // 入力パラメータチェック
        final UserPwdChgOut out = pwdChgService.doChangePassword(form, userDetails);
        SessionManager.me.remove(request, SessionKeyMaster.FORCE_CHANGE_PWD);
        return out;
    }
}
