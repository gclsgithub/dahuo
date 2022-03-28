package com.hytc.webmanage.web.biz.settings;

import com.hytc.webmanage.common.FwUserDetails;
import com.hytc.webmanage.common.entity.pwdchg.UserPwdChgIn;
import com.hytc.webmanage.common.entity.pwdchg.UserPwdChgOut;
import com.hytc.webmanage.common.exception.FwWebBusinessException;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor()
public class PwdChgService {

//    final private CallGraphqlApi callGraphql;

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
//        UserPwdChgOut out = callGraphql.doMutation(GraphqlReqName.USER_PWD_CHANGE, in, UserPwdChgOut.class);
        UserPwdChgOut out = new UserPwdChgOut();
        if (UserPwdChgOut.isNotOK(out)) {
            throw new FwWebBusinessException(out);
        }
        return out;
    }
}
