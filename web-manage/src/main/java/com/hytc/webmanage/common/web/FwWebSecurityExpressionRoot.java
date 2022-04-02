package com.hytc.webmanage.common.web;

import java.math.BigDecimal;

import com.hytc.webmanage.common.FwUserDetails;
import com.hytc.webmanage.common.UserAuthInfo;
import com.hytc.webmanage.common.codeEnum.AuthorityCls;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;


import lombok.extern.log4j.Log4j2;

@Log4j2
public class FwWebSecurityExpressionRoot extends WebSecurityExpressionRoot {

    public FwWebSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
        super(authentication, fi);
    }

    /**
     * 機能IDメニュー権限チェック
     *
     * @param functionId
     * @return 権限あり: true 権限なし: false
     */
    public boolean checkGlobalMenuAuth(BigDecimal functionId) {
        log.debug("functionId={}", functionId);
        final Object principal = super.getAuthentication().getPrincipal();
        if (!(principal instanceof FwUserDetails)) {
            return false;
        }
        final FwUserDetails userDetails = (FwUserDetails) principal;
        if (userDetails.getAuthInfoList() == null) {
            return false;
        }
        return userDetails.getAuthInfoList().stream().filter(info -> info.getFunctionId().equals(functionId)).anyMatch(subMenu -> (AuthorityCls.NO_AUTHORITY.is(subMenu.getAuthorityCls()) == false));
    }

    /**
     * 機能サブIDメニュー権限チェック(参照)
     *
     * @param functionSubId
     * @return 権限あり: true 権限なし: false
     */
    public boolean checkAuthRefByFuncSubId(BigDecimal functionSubId) {
        return checkAuth(functionSubId, AuthorityCls.REFERENCE.getValue());
    }

    /**
     * 機能サブIDメニュー権限チェック(更新)
     *
     * @param functionSubId
     * @return 権限あり: true 権限なし: false
     */
    public boolean checkAuthUpdByFuncSubId(BigDecimal functionSubId) {
        return checkAuth(functionSubId, AuthorityCls.UPDATE.getValue());
    }

    /**
     * 機能サブIDメニュー権限チェック(承認)
     *
     * @param functionSubId
     * @return 権限あり: true 権限なし: false
     */
    public boolean checkAuthApprByFuncSubId(BigDecimal functionSubId) {
        return checkAuth(functionSubId, AuthorityCls.APPROVAL.getValue());
    }

    /**
     * 機能サブIDメニュー権限チェック
     *
     * @param functionSubId
     * @param authotityCd
     * @return 権限あり: true 権限なし: false
     */
    public boolean checkAuth(BigDecimal functionSubId, String authCls) {
        log.debug("functionSubId={}, authotityCls={}", functionSubId, authCls);
        if (functionSubId == null || authCls == null) {
            return false;
        }
        // 認証情報取得
        final Object principal = super.getAuthentication().getPrincipal();
        if (!(principal instanceof FwUserDetails)) {
            return false;
        }

        // 権限チェック処理
        final FwUserDetails userDetails = (FwUserDetails) principal;
        for (UserAuthInfo auth : userDetails.getAuthInfoList()) {
            // 機能サブIDチェック
            if (functionSubId.compareTo(auth.getFunctionSubId()) == 0) {
                // 権限なし
                if (auth.getAuthorityCls() == null || AuthorityCls.NO_AUTHORITY.is(auth.getAuthorityCls())) {
                    return false;
                }
                // 権限区分よりチェック
                if (authCls.compareTo(auth.getAuthorityCls()) <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
