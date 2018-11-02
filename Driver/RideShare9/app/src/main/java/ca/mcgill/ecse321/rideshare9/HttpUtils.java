package ca.mcgill.ecse321.rideshare9;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class HttpUtils {
    public static final String DEFAULT_BASE_URL = "https://rideshare9.herokuapp.com/";

    private static String baseUrl;
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        baseUrl = DEFAULT_BASE_URL;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        HttpUtils.baseUrl = baseUrl;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void get(Context context, String url, HttpEntity entity, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(context,getAbsoluteUrl(url),headers,params,responseHandler);
    }

    public static void get(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(context, getAbsoluteUrl(url),headers,params,responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    public static void post(Context context, String url, HttpEntity entity,String contenttype, AsyncHttpResponseHandler responseHandler ){
        client.post(context, getAbsoluteUrl(url),entity, contenttype, responseHandler);
    }

    public static void post(Context context, String url, Header[] headers, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler){
        client.post(context,getAbsoluteUrl(url),headers,entity,contentType,responseHandler);
    }

    public static void put(Context context, String url, Header[] headers, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler){
        client.put(context,getAbsoluteUrl(url),headers,entity,contentType,responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);

    }
    public static void delete(Context context,String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.delete(context,getAbsoluteUrl(url),headers, params,responseHandler);
    }

    public static void addHeader(String header, String value) { client.addHeader(header, value);}
    public static void put(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.put(context,getAbsoluteUrl(url),params,responseHandler);
    }
    public static void removeHeader(String header) { client.removeHeader(header);}
    private static String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }
}
