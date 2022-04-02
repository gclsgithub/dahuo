package com.hytc.webmanage.common.util;

import java.io.IOException;
import java.util.Map;

import com.hytc.webmanage.common.constant.FwCmnConst;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;



public class HttpRequestUtils {

    /**
     * Http Post 送信
     *
     * @param <O>
     * @param url
     * @param sendData
     * @param headers
     * @param outClass
     * @return
     * @throws ClientProtocolException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <O> O sendPost(String url, HttpEntity sendData, Map<String, String> headers, TypeReference<O> valueTypeRef) throws ClientProtocolException, JsonParseException, JsonMappingException, IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        O o = null;

        HttpPost httpPost = new HttpPost(url);
        // ヘッダを設定する
        if (headers != null) {
            headers.keySet().forEach(key -> httpPost.addHeader(key, headers.get(key)));
        }

        // 送信データを設定する
        httpPost.setEntity(sendData);

        // Http Request 実行
        HttpResponse response = httpClient.execute(httpPost);
        byte[] result = EntityUtils.toByteArray(response.getEntity());
        o = FwCmnConst.objectMapper.readValue(result, valueTypeRef);

        return o;
    }
}
