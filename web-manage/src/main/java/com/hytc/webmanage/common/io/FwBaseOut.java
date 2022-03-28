package com.hytc.webmanage.common.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


import com.hytc.webmanage.common.resolve.FwMessageResolve;
import com.hytc.webmanage.common.resolve.ResultMessages;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Note: <br />
 * Type parameters are only used for type safety with {@link CallRestApi}. Extending classes without an {@link FwBaseIn} counterpart should use
 * {@code Object} for {@code I}
 */
@Accessors(chain = true)
public class FwBaseOut implements Serializable {

    private static final long serialVersionUID = 1L;

    /** resultCd ⇒ "0": success "1": failure */
    @Getter
    private int resultCd = ResultCd.OK;

    /**
     * backのエラーリスト
     */
    @Getter
    protected final List<ResultMessages> errors = new ArrayList<>();

    /**
     * backのエラーメッセージをfrontのメッセージに変換。
     * @param messageResolve
     */
    final public void backErrorConvert(final FwMessageResolve messageResolve) {
        this.doBackErrorConvert(messageResolve, this.errors);
    }

    /**
     * 子クラスを継承してください
     * @param messageResolve
     * @param backErrors
     */
    protected void doBackErrorConvert(final FwMessageResolve messageResolve, final List<ResultMessages> backErrors) {
        List<ResultMessages> tmpErrors = new ArrayList<>();
        for (ResultMessages msg : backErrors) {
            tmpErrors.add(new ResultMessages(null, msg.getArgs(), messageResolve.resolveMessage(msg)));
        }
        this.errors.clear();
        this.errors.addAll(tmpErrors);
    }

    protected FwBaseOut() {
    }

    private FwBaseOut(int resultCd) {
        this.resultCd = resultCd;
    }

    @JsonIgnore
    final public boolean isNotOK() {
        return resultCd != ResultCd.OK;
    }

    final public static boolean isNotOK(FwBaseOut out) {
        if (out == null) {
            return true;
        }
        return out.getResultCd() != ResultCd.OK;
    }

    final public static FwBaseOut ng() {
        FwBaseOut out = new FwBaseOut(ResultCd.NG);
        return out;
    }

    final public static FwBaseOut ok() {
        FwBaseOut out = new FwBaseOut(ResultCd.OK);
        return out;
    }

    final public static FwBaseOut ng(ResultMessages resultMessages) {
        FwBaseOut out = new FwBaseOut(ResultCd.NG);
        out.errors.add(resultMessages);
        return out;
    }


    /**
     * Creates an instance of {@link FwBaseOut} subclass {@code T} with resultCd set to {@link ResultCd#NG}
     *
     * @param type
     * @return a new instance of {@code T}
     */
    final public static <T extends FwBaseOut> T ng(Class<T> type) {
        try {
            T inst = type.newInstance();
            ((FwBaseOut) inst).resultCd = ResultCd.NG;
            return inst;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    final public static <T extends FwBaseOut> T ng(Class<T> type, ResultMessages resultMessages) {
        T out = ng(type);
        out.errors.add(resultMessages);
        return out;
    }

    /**
     * Edit Call Graphql Query
     *
     * @return Graphql Query
     */
    public String toGraphqlQuery() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("resultCd ");
        builder.append(toGraphqlQueryByBean());
        builder.append("}");
        return builder.toString();
    }

    /**
     * Edit Call Graphql Query
     *
     * @return Graphql Query
     */
    public String toGraphqlQueryByBean() {
        return "";
    }
}
