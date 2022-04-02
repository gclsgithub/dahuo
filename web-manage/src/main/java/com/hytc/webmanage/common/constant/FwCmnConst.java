package com.hytc.webmanage.common.constant;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 定数を定義するクラス
 */
public class FwCmnConst {

    /** ObjectMapper **/
    public static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static final String GRAPHQL_AUTHORIZATION_KEY = "Authorization";

    public static final String GRAPHQL_AUTHORIZATION_PREFIX = "bearer ";

    public static final String FIELD_NAME_KID = "kid";

    public static final String FIELD_NAME_CLIENTID = "client_id";

    public static final String FIELD_NAME_GRANTTYPE = "grant_type";

    public static final String FIELD_NAME_SCOPE = "scope";

    public static final String FIELD_NAME_CLIENTASSERTIONTYPE = "client_assertion_type";

    public static final String FIELD_NAME_CLIENTASSERTION = "client_assertion";

    public static final String FIELD_NAME_ACCESSTOKEN = "access_token";

    public static final String KEY_RSA = "RSA";
}
