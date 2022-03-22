package com.hytc.webmanage.web.biz.applicant;

import jp.co.gt.fw.common.exception.FwWebBusinessException;
import jp.co.gt.fw.common.graphql.CallGraphqlApi;
import jp.co.gt.fw.util.FwBeanUtils;
import jp.co.gt.fw.util.FwDateTimeFormat;
import jp.co.gt.fw.util.FwDateTimeUtils;
import jp.co.jsto.auth.bean.FwUserDetails;
import jp.co.jsto.biz.GraphqlReqName;
import jp.co.jsto.biz.trand.apply.ApplicantInfoIn;
import jp.co.jsto.biz.trand.apply.ApplicantInfoOut;
import jp.co.jsto.biz.trand.apply.ApplicantInfoUpdateIn;
import jp.co.jsto.biz.trand.apply.ApplicantInfoUpdateOut;
import jp.co.jsto.code.DisplayApplicantStatus;
import jp.co.jsto.code.DisplayTransactionCls;
import jp.co.jsto.web.common.WebCommonUtil;
import jp.co.jsto.web.config.convert.WebLogic;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApplicantService {

    final private CallGraphqlApi callGraphql;

    final private WebLogic webLogic;

    /**
     * 売買申請管理情報検索
     *
     * @param userDetails
     * @param applicantForm
     * @return
     */
    public ApplicantForm searchApplicantByCondition(FwUserDetails userDetails, ApplicantForm applicantForm) {
        // GrapHQL API 引数編集
        ApplicantInfoIn in = FwBeanUtils.me.createCopy(applicantForm, ApplicantInfoIn.class);
        in.setApplicantStatus(applicantForm.getStatusCls());
        in.setCompanyCd(userDetails.getCompanyCd());
        in.setUserSeqId(BigDecimal.valueOf(Integer.valueOf(userDetails.getUserId())));
        // GrapHQL API 呼出
        ApplicantInfoOut applicantInfoOut = callGraphql.doQuery(GraphqlReqName.APPLICANT_SEARCH, in, ApplicantInfoOut.class);
        // GrapHQL API異常あり
        if (applicantInfoOut.isNotOK()) {
            throw new FwWebBusinessException(applicantInfoOut);
        }

        List<ApplicantDispInfo> applicantDispInfos = new ArrayList<>();
        applicantDispInfos.addAll(applicantInfoOut.getApplicantInfoList().stream().map(item -> {
            ApplicantDispInfo info = FwBeanUtils.me.createCopy(item, ApplicantDispInfo.class);
            info.setCustomerCode(webLogic.nullToSpace(item.getApplicantBranchCd()) + "-" + webLogic.nullToSpace(item.getApplicantCustomerId()));
            info.setCustomerName(webLogic.nullToSpace(item.getApplicantFamilyName()) + webLogic.nullToSpace(item.getApplicantGivenName()));
            info.setApplicantDtStr(webLogic.nullToSpace(FwDateTimeUtils.me.localDateTimeToString(item.getApplicantStatusDt(), FwDateTimeFormat.yyyyMMddHHmm_SLASH)));
            info.setFriendCustomerCode(webLogic.nullToSpace(item.getAuthorizerBranchCd() + "-" + webLogic.nullToSpace(item.getAuthorizerCustomerId())));
            info.setApplicantAmountStr(WebCommonUtil.formatBigDecimal2String(item.getApplicantAmount(), "#,###"));
            return info;
        }).collect(Collectors.toList()));

        for(ApplicantDispInfo item : applicantDispInfos) {
        	if(item.getContractCls().equals(DisplayTransactionCls.BILATERAL.getValue())) {
        		item.setFriendCustomerCode("-");
        	}
        }

        applicantForm.setApplicantDispInfo(applicantDispInfos);
        return applicantForm;
    }

    /**
     * 売買申請管理否認
     *
     * @param userDetails
     * @param form
     */
    public void denyApplicant(FwUserDetails userDetails, ApplicantForm form) {
        ApplicantInfoUpdateIn in = FwBeanUtils.me.createCopy(form, ApplicantInfoUpdateIn.class);
        in.setCompanyCd(userDetails.getCompanyCd());
        in.setUserSeqId(BigDecimal.valueOf(Integer.valueOf(userDetails.getUserId())));
        in.setApplicantStatusDt(FwDateTimeUtils.me.toLocalDateTime(form.getApplicantStatusDt(),FwDateTimeFormat.yyyyMMddHHmmss));
        in.setApplicantStatus(DisplayApplicantStatus.DENIAL.getValue());
        in.setDenialReasonCls(form.getInDenialReasonCls());
        ApplicantInfoUpdateOut out = callGraphql.doMutation(GraphqlReqName.APPLICANT_DENY, in, ApplicantInfoUpdateOut.class);
        if (out.isNotOK()) {
            throw new FwWebBusinessException(out);
        }
    }
}
