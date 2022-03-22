package com.hytc.webmanage.web.config.aop;

import java.lang.reflect.Method;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public abstract class FwBaseAspect {

    /**
     * アノテーションキーを取得、SPELサポート
     *
     * @return key
     */
    final protected String parseKey(String key, Method method, Object[] args) {
        if (key == null || key.trim().length() == 0) {
            return null;
        }
        // メソッドパラメータ取得Object(Spring Object)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);

        // SPELを用いて、keyの解析
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        // パラメータをSPELのcontextに入れる
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }
}
