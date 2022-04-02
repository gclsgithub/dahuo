package com.hytc.webmanage.auth;

import com.hytc.webmanage.auth.io.UserAuthLoginIn;
import com.hytc.webmanage.auth.io.UserAuthLoginOut;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * ユーザーパスワード認証を行う
 */
@Service
@RequiredArgsConstructor()
final public class FwAuthService {

//    final private CallGraphqlApi callGraphql;

    /**
     * ユーザログイン
     *
     * @param authIn
     * @return UserAuthLoginOut
     */
    public UserAuthLoginOut authLogin(UserAuthLoginIn authIn) {
//        return callGraphql.doMutation(GraphqlReqName.USER_AUTH_LOGIN, authIn, UserAuthLoginOut.class);
        return new UserAuthLoginOut();
    }
}
