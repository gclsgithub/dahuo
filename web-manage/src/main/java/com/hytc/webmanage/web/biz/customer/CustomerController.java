/**
 * (c) 2020 LBI Co., Ltd.
 *
 *  システム名：JSTO
 */
package com.hytc.webmanage.web.biz.customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.jsto.auth.bean.FwUserDetails;
import jp.co.jsto.web.biz.FwBaseController;
import jp.co.jsto.web.config.MappingMaster;
import lombok.RequiredArgsConstructor;

/**
 * 顧客情報管理コントローラ
 */
@Controller
@RequiredArgsConstructor()
public class CustomerController extends FwBaseController {

    /* 顧客情報管理サービス */
    private final CustomerService customerService;

    /* 顧客情報管理一覧パス */
    private final String customerList = "/customer/customerList.html";

    /**
     * S0018.顧客情報取得処理
     *
     * @param request リクエスト
     * @param form フォーム
     * @return
     */
    @GetMapping(MappingMaster.CUSTOMER)
    @PreAuthorize("checkAuthRefByFuncSubId(#form.funcSubId())")
    public String customerList(final HttpServletRequest request, final @ModelAttribute("form") CustomerForm form) {
        return customerList;
    }

    /**
     * S0018.顧客情報取得処理 検索
     *
     * @param form フォーム
     * @param userDetails ユーザー情報
     * @return
     */
    @PostMapping(MappingMaster.SEARCH_CUSTOMER)
    @ResponseBody
    public List<CustomerDispInfo> searchCustomer(@ModelAttribute("form") CustomerForm form, final @AuthenticationPrincipal FwUserDetails userDetails) {
        customerService.searchCustomerByCondition(userDetails, form);
        return form.getCustomerInfos();
    }
}
