package com.hytc.webmanage.web.biz.applicant;

import jp.co.gt.fw.common.message.FwMessageResolve;
import jp.co.jsto.auth.bean.FwUserDetails;
import jp.co.jsto.web.biz.FwBaseController;
import jp.co.jsto.web.common.WebConstants;
import jp.co.jsto.web.common.WebErrorCode;
import jp.co.jsto.web.config.MappingMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor()
public class ApplicantController extends FwBaseController {

    private final ApplicantService applicantService;

    private final FwMessageResolve messageResolve;

    @GetMapping(MappingMaster.APPLICANT)
    @PreAuthorize("checkAuthRefByFuncSubId(#form.funcSubId())")
    public String applicantList(final HttpServletRequest request
        , final @ModelAttribute("form") ApplicantForm form
    ) {

        return "applicant/applicantList.html";
    }

    @PostMapping(MappingMaster.SEARCH_APPLICANT)
    @ResponseBody
    public List<ApplicantDispInfo> searchNotice(@ModelAttribute("form") ApplicantForm form //
        , final @AuthenticationPrincipal FwUserDetails userDetails //
    ) {
        applicantService.searchApplicantByCondition(userDetails, form);
        return form.getApplicantDispInfo();
    }

    @PostMapping(MappingMaster.DENY_APPLICANT)
    public String denyApplicant(@ModelAttribute("form") ApplicantForm form //
        , final @AuthenticationPrincipal FwUserDetails userDetails
        , BindingResult bindingResult
        , HttpServletResponse response//
    ) {
        if(form.getInDenialReasonCls().equals("0100")) {
        	bindingResult.rejectValue(WebConstants.IN_DENIAL_REASON_CLS, WebErrorCode.E_PL_0002.name(), new Object[] {messageResolve.resolveMessage("inDenialReasonCls")}, null);
        }
        // 単項目チェックエラーあり
        if (bindingResult.hasErrors()) {
            return super.returnWhenBindError(response, "/applicant/denyApplicant", "denyBody");
        }
        applicantService.denyApplicant(userDetails, form);
        return "/applicant/applicantList :: #confirm_modal_deny";
    }
}
