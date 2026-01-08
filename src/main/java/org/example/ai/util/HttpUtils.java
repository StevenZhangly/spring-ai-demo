package org.example.ai.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 01423171
 * @ClassName HttpUtil
 * @description: TODO
 * @datetime 2022/9/13 20:45
 * @version: 1.0
 */
@Slf4j
public class HttpUtils {

    private static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

    private static CloseableHttpClient httpClient = null;

    private static boolean isInitConnection = false;

    public static final String CODING_UTF = "utf-8";

    public static void initConnectionManager() {
        try {
            if (isInitConnection) {
                return;
            }
            // 设置整个连接池最大连接数 根据自己的场景决定
            connManager.setMaxTotal(500);
            // 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)，路由是对maxTotal的细分。
            connManager.setDefaultMaxPerRoute(500);
            SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setSoTimeout(600000).setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(600000).setConnectTimeout(600000).setSocketTimeout(600000).build();
            httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(connManager).build();
            isInitConnection = true;
        } catch (Exception e) {
            log.error("initConnectionManager error. ", e);
        }
    }

    public static String sendGet(String url) {
        log.debug("start send get, url - {}.", url);
        String jsonStr = "";
        HttpGet get = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            initConnectionManager();
            get = new HttpGet(url);
            response = httpClient.execute(get);
            entity = response.getEntity();
            return EntityUtils.toString(entity, CODING_UTF);
        } catch (Exception e) {
            log.error("execute access http get url err. " + url, e);
            return null;
        } finally {
            try {
                if (jsonStr != null) {
                    if (get != null) {
                        EntityUtils.consume(entity);
                        get.releaseConnection();
                    }
                    if (response != null) {
                        response.close();
                    }
                }
            } catch (Exception e) {
                log.error("execute close http get url err. " + url, e);
            }
        }
    }

    public static String sendGetByParam(String url, Map<String, String> params) {
        log.debug("start send get, url - {}, params - {}", url, params);
        String jsonStr = "";
        HttpGet get = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            initConnectionManager();
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            get = new HttpGet(uriBuilder.build());
            response = httpClient.execute(get);
            entity = response.getEntity();
            return EntityUtils.toString(entity, CODING_UTF);
        } catch (Exception e) {
            log.error("execute access http get url err. " + url, e);
            return null;
        } finally {
            try {
                if (jsonStr != null) {
                    if (get != null) {
                        EntityUtils.consume(entity);
                        get.releaseConnection();
                    }
                    if (response != null) {
                        response.close();
                    }
                }
            } catch (Exception e) {
                log.error("execute close http get url err. " + url, e);
            }
        }
    }

    public static String sendGetWithHeader(String url, Map<String, String> headers, Map<String, String> params) {
        log.debug("start send get, url - {}, headers - {}, params - {}", url, headers, params);
        String jsonStr = "";
        HttpGet get = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            initConnectionManager();
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            get = new HttpGet(uriBuilder.build());
            if(headers != null){
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    get.addHeader(entry.getKey(), entry.getValue());
                }
            }
            response = httpClient.execute(get);
            log.info("response status code: {}", response.getStatusLine().getStatusCode());
            entity = response.getEntity();
            return EntityUtils.toString(entity, CODING_UTF);
        } catch (Exception e) {
            log.error("execute access http get url err. " + url, e);
            return null;
        } finally {
            try {
                if (jsonStr != null) {
                    if (get != null) {
                        EntityUtils.consume(entity);
                        get.releaseConnection();
                    }
                    if (response != null) {
                        response.close();
                    }
                }
            } catch (Exception e) {
                log.error("execute close http get url err. " + url, e);
            }
        }
    }

    public static String sendPostByParam(String url, Map<String, String> headerMap, Map<String, String> bodyMap) {
        log.debug("start send post, url - {}.", url);
        HttpPost post = null;
        CloseableHttpResponse response = null;
        try {
            initConnectionManager();
            post = new HttpPost(url);
            if(headerMap != null){
                for(Map.Entry<String, String> entry : headerMap.entrySet()){
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            if(bodyMap != null){
                List<NameValuePair> parameters = new ArrayList<>();
                for(Map.Entry<String, String> entry : bodyMap.entrySet()){
                    parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                // 构造一个form表单式的实体
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, Consts.UTF_8);
                post.setEntity(formEntity);
            }
            response = httpClient.execute(post);
            return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        } catch (Exception e) {
            log.error("execute access http post send form url err. " + url, e);
            return null;
        } finally {
            closeHttpPostConnection(post, response);
        }
    }

    public static String sendPostByJson(String url, Map<String, String> headerMap, String json) {
        log.info("start send post, url - {}, json - {}", url, json);
        HttpPost post = null;
        CloseableHttpResponse response = null;
        try {
            initConnectionManager();
            post = new HttpPost(url);
            if(headerMap != null){
                for(Map.Entry<String, String> entry : headerMap.entrySet()){
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            if(json != null && json != ""){
                StringEntity stringEntity = new StringEntity(json, Consts.UTF_8);
                stringEntity.setContentType("text/json");
                stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(stringEntity);
            }
            response = httpClient.execute(post);
            return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        } catch (Exception e) {
            log.error("execute access http post send json url err, url - {}", url, e);
            return null;
        } finally {
            closeHttpPostConnection(post, response);
        }
    }

    public static List<Cookie> getCookieList(String url){
        String jsonStr = "";
        HttpGet get = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            BasicCookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            get = new HttpGet(url);
            response = httpclient.execute(get);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){
                entity = response.getEntity();
                jsonStr = EntityUtils.toString(entity, CODING_UTF);
                return cookieStore.getCookies();
            }
            return null;
        } catch (Exception e) {
            log.error("execute access http get url err. " + url, e);
            return null;
        } finally {
            try {
                if (jsonStr != null) {
                    if (get != null) {
                        EntityUtils.consume(entity);
                        get.releaseConnection();
                    }
                    if (response != null) {
                        response.close();
                    }
                }
            } catch (Exception e) {
                log.error("execute close http get url err. " + url, e);
            }
        }
    }

    public static void closeHttpPostConnection(HttpPost post, CloseableHttpResponse response){
        try {
            if (post != null) {
                post.releaseConnection();
            }
            if (response != null) {
                response.close();
            }
        } catch (Exception e) {
            log.error("execute close http post err" , e);
        }
    }
}
