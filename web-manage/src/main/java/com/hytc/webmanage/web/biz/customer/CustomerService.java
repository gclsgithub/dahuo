/**
 * (c) 2020 LBI Co., Ltd.
 *
 *  システム名：JSTO
 */
package com.hytc.webmanage.web.biz.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.gt.fw.common.exception.FwWebBusinessException;
import jp.co.gt.fw.common.graphql.CallGraphqlApi;
import jp.co.gt.fw.util.FwBeanUtils;
import jp.co.gt.fw.util.FwDateTimeFormat;
import jp.co.gt.fw.util.FwDateTimeUtils;
import jp.co.jsto.auth.bean.FwUserDetails;
import jp.co.jsto.biz.GraphqlReqName;
import jp.co.jsto.biz.customer.customer.CustomerInfoIn;
import jp.co.jsto.biz.customer.customer.CustomerInfoOut;
import jp.co.jsto.code.DisplayAccountManagementCls;
import jp.co.jsto.code.DisplayCustomerCls;
import jp.co.jsto.web.config.convert.WebLogic;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 顧客情報管理サービス
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class CustomerService {

    /* 口座管理区分 */
    private final String BANKNEXT_NORMAIL = "BANKNEXT一般";
    private final String JSTO_INTERNAL_CUSTOMER = "JSTO内部顧客";

    /* 顧客区分 */
    private final String CORPORATION = "法人";
    private final String INDIVIDUAL = "個人";

    /* GraphqlApi */
    private final CallGraphqlApi callGraphql;

    /* WebLogic */
    private final WebLogic webLogic;

    @Value("${jsto.file.server.path}")
    private String serverPath;

    /**
     * S0018.顧客情報取得処理 情報検索
     *
     * @param userDetails  ユーザー情報
     * @param customerForm 顧客情報管理フォーム
     * @return
     */
    public CustomerForm searchCustomerByCondition(FwUserDetails userDetails, CustomerForm customerForm) {

        // GrapHQL API 引数編集
        CustomerInfoIn in = FwBeanUtils.me.createCopy(customerForm, CustomerInfoIn.class);

        // 企業コード
        in.setCompanyCd(userDetails.getCompanyCd());
        // アクションID
        in.setUserSeqId(BigDecimal.valueOf(Integer.valueOf(userDetails.getUserId())));

        // GrapHQL API 呼出
        CustomerInfoOut CustomerInfoOut = callGraphql.doQuery(GraphqlReqName.FIND_CUSTOMER, in, CustomerInfoOut.class);

        // GrapHQL API異常あり
        if (CustomerInfoOut.isNotOK()) {
            throw new FwWebBusinessException(CustomerInfoOut);
        }

        // 顧客情報管理を設定
        List<CustomerDispInfo> customerInfos = new ArrayList<>();
        customerInfos.addAll(CustomerInfoOut.getCustomerInfoList().stream().map(item -> {CustomerDispInfo info = FwBeanUtils.me.createCopy(item, CustomerDispInfo.class);
            // 口座管理区分
            info.setAccountManagementCls(accountManagementClsCheck(webLogic.nullToSpace(item.getAccountManagementCls())));
            // 顧客コード
            info.setCustomerCd(webLogic.nullToSpace(item.getBranchCd()) + "-" + webLogic.nullToSpace(item.getCustomerId()));
            // 顧客区分
            info.setCustomerCls(customerClsCheck(webLogic.nullToSpace(item.getCustomerCls())));
            // 顧客氏名
            info.setCustomerName(webLogic.nullToSpace(item.getFamilyName()) + webLogic.nullToSpace(item.getGivenName()));
            // 電話番号（携帯電話）
            info.setMobilePhoneNumber(webLogic.nullToSpace(item.getMobilePhoneNumber()));
            // メールアドレス
            info.setMailAddress(webLogic.nullToSpace(item.getMailAddress()));
            // 本登録日
            info.setDefinitiveRegistrationDateStr(webLogic.nullToSpace(FwDateTimeUtils.me.localDateToString(item.getDefinitiveRegistrationDate().toLocalDate(), FwDateTimeFormat.yyyyMMdd_SLASH)));
            // 確認資料提出日
            info.setConfirmationDocumentSubmitDateStr(webLogic.nullToSpace(FwDateTimeUtils.me.localDateToString(item.getConfirmationDocumentSubmitDate().toLocalDate(), FwDateTimeFormat.yyyyMMdd_SLASH)));
            // 承認日
            info.setApprovalDateStr(webLogic.nullToSpace(FwDateTimeUtils.me.localDateToString(item.getApprovalDate().toLocalDate(), FwDateTimeFormat.yyyyMMdd_SLASH)));
            // 認証日
            info.setAuthenticationDateStr(webLogic.nullToSpace(FwDateTimeUtils.me.localDateToString(item.getAuthenticationDate().toLocalDate(), FwDateTimeFormat.yyyyMMdd_SLASH)));
            return info;
        }).collect(Collectors.toList()));
        customerForm.setCustomerInfos(customerInfos);
        return customerForm;
    }

    /**
     * 口座管理区分 設定
     *
     * @param accountManagementCls 口座管理区分コード
     * @return accountManagementCls 口座管理区分文字
     */
    public String accountManagementClsCheck(String accountManagementCls) {
        // ビジネスチェック
        if (DisplayAccountManagementCls.ALL.is(accountManagementCls)) {
            return "";
        } else if (DisplayAccountManagementCls.PRIMARY.is(accountManagementCls)) {
            return BANKNEXT_NORMAIL;
        } else if (DisplayAccountManagementCls.JSTO_INTERNAL_CUSTOMER.is(accountManagementCls)) {
            return JSTO_INTERNAL_CUSTOMER;
        } else {
            return accountManagementCls;
        }
    }

    /**
     * 顧客区分 設定
     *
     * @param customerCls 顧客区分コード
     * @return customerCls 顧客区分文字
     */
    public String customerClsCheck(String customerCls) {
        // ビジネスチェック
        if (DisplayCustomerCls.ALL.is(customerCls)) {
            return "";
        } else if (DisplayCustomerCls.CORPORATION.is(customerCls)) {
            return CORPORATION;
        } else if (DisplayCustomerCls.INDIVIDUAL.is(customerCls)) {
            return INDIVIDUAL;
        } else {
            return customerCls;
        }
    }

}
