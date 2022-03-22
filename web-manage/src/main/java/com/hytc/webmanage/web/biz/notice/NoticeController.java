package com.hytc.webmanage.web.biz.notice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.gt.fw.common.message.FwMessageResolve;
import jp.co.jsto.auth.bean.FwUserDetails;
import jp.co.jsto.code.OpenEntryCls;
import jp.co.jsto.code.OpenTargetCls;
import jp.co.jsto.web.biz.FwBaseController;
import jp.co.jsto.web.common.WebConstants;
import jp.co.jsto.web.common.WebErrorCode;
import jp.co.jsto.web.config.MappingMaster;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor()
public class NoticeController extends FwBaseController {

    private final NoticeService noticeService;

    private final FwMessageResolve messageResolve;

    @GetMapping(MappingMaster.NOTICE)
    @PreAuthorize("checkAuthRefByFuncSubId(#form.funcSubId())")
    public String noticeList(final HttpServletRequest request //
        , final @ModelAttribute("form") NoticeForm form //
    ) {
        return "/notice/noticeList.html";
    }

    @PostMapping(MappingMaster.SEARCH_NOTICE)
    @ResponseBody
    public List<NoticeDispInfo> searchNotice(@ModelAttribute("form") NoticeForm form //
        , final @AuthenticationPrincipal FwUserDetails userDetails //
    ) {
        noticeService.searchNoticeByCondition(userDetails, form);
        return form.getNoticeInfos();
    }

    /**
     * 入力データ登録・更新
     *
     * 順番： userDetails->form->bindingResult->response
     *
     * @param userDetails
     * @param form
     * @param bindingResult
     * @param response
     * @return
     */
    @PostMapping(MappingMaster.EDIT_NOTICE)
    public String insertUpdateNotice(final @AuthenticationPrincipal FwUserDetails userDetails
        , @Validated @ModelAttribute("form") NoticeForm form
        , BindingResult bindingResult
        , HttpServletResponse response ) {

        // 顧客別
        if (OpenTargetCls.CUSTOMER_SEP.is(form.getOpenTargetCls()) && ObjectUtils.isEmpty(form.getCustomCode())) {
            // 顧客コード必須チェック
            bindingResult.rejectValue(WebConstants.CUSTOM_CODE, WebErrorCode.E_PL_0002.name(), new Object[] {messageResolve.resolveMessage("noticeCustomerCode")}, null);
        } else if (OpenTargetCls.CUSTOMER_SEP.is(form.getOpenTargetCls()) && ObjectUtils.isNotEmpty(form.getCustomCode())) {
            // 顧客コード正確チェック
            String[] codeArr = form.getCustomCode().split(WebConstants.HYPHEN);
            if (codeArr.length != 2) {
                bindingResult.rejectValue(WebConstants.CUSTOM_CODE, WebErrorCode.E_PL_0000.name());
            }
        }

        // 予約済の場合、掲載日時間必須チェック
        if (OpenEntryCls.RESERVED.is(form.getOpenEntryCls()) && ObjectUtils.isEmpty(form.getOpenEntryDt())) {
            bindingResult.rejectValue(WebConstants.OPEN_ENTRY_DT, WebErrorCode.E_PL_0002.name(), new Object[] {messageResolve.resolveMessage("noticeOpenDt")}, null);
        }

        // 単項目チェックエラーあり
        if (bindingResult.hasErrors()) {
            return super.returnWhenBindError(response, "/notice/editNotice", "inserUpdateModalBody");
        }

        // 登録・更新
        noticeService.doUpdateInsert(userDetails, form);

        return  "/notice/noticeList :: #newNotice_modal";
    }

    @PostMapping(MappingMaster.DELETE_NOTICE)
    @ResponseBody
    public Map<String, Object> deleteNotice(@ModelAttribute("form") NoticeForm form //
        , final @AuthenticationPrincipal FwUserDetails userDetails //
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        noticeService.deleteNotice(userDetails, form);
        return resultMap;
    }
}
