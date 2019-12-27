package com.example.lib_network.okhttp.request;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 对外提供get/post/文件上传请求
 */
public class CommonRequest {

    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }

    /**
     * 对外创建post请求对象
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params, RequestParams headers) {
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //参数遍历
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }

        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                //请求头遍历
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .headers(mHeaderBuild.build())
                .post(mFormBodyBuild.build())
                .build();
        return request;

    }

    public static  Request createGetRequest(String url,RequestParams params){
        return createGetRequest(url,params,null);
    }

    /**
     * 可以带请求头的Get请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params, RequestParams headers) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //参数遍历
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }

        return new Request.Builder().
                url(url)
                .headers(mHeaderBuild.build())
                .get()
                .build();

    }

    /**
     * 文件上传请求
     */
    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    public static Request createMultiPostRequest(String url,RequestParams params){
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (params != null){
            for (Map.Entry<String,Object> entry : params.fileParams.entrySet()){
                if (entry.getValue() instanceof File){
                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE,(File) entry.getValue()));
                }else if (entry.getValue() instanceof String){
                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null,(String)entry.getValue()));
                }
            }
        }
        return new Request.Builder().url(url).post(requestBody.build()).build();
    }

}
