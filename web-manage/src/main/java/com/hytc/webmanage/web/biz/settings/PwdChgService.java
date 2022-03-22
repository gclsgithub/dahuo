package com.hytc.webmanage.web.biz.settings;

import org.springframework.stereotype.Service;

import jp.co.gt.fw.common.exception.FwWebBusinessException;
import jp.co.gt.fw.common.graphql.CallGraphqlApi;
import jp.co.jsto.auth.bean.FwUserDetails;
import jp.co.jsto.biz.GraphqlReqName;
import jp.co.jsto.biz.pwdchg.UserPwdChgIn;
import jp.co.jsto.biz.pwdchg.UserPwdChgOut;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor()
public class PwdChgService {

    final private CallGraphqlApi callGraphql;

    public UserPwdChgOut doChangePassword(final PwdChgForm form, FwUserDetails userDetails) {
        UserPwdChgIn in = new UserPwdChgIn();

        // ログインセッション情報
        in.setCompanyCd(userDetails.getCompanyCd());
        in.setUserSeqId(BigDecimal.valueOf(Integer.valueOf(userDetails.getUserId())));

        // 画面入力値
        in
        .setCurrentPassword(form.getCurrentPassword())
        .setCurrentPassword(form.getCurrentPassword())
        .setNewPassword(form.getNewPassword())
        .setNewPasswordConfirm(form.getNewPasswordConfirm())
        ;

        // パスワード変更ロジック呼び出し
        UserPwdChgOut out = callGraphql.doMutation(GraphqlReqName.USER_PWD_CHANGE, in, UserPwdChgOut.class);
        if (UserPwdChgOut.isNotOK(out)) {
            throw new FwWebBusinessException(out);
        }
        return out;
    }
}
