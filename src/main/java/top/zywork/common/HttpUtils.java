package top.zywork.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import top.zywork.enums.CharsetEnum;
import top.zywork.enums.ContentTypeEnum;
import top.zywork.enums.MIMETypeEnum;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;

/**
 * Http操作类，主要用于发送get或post请求，对HttpClient的封装<br/>
 * 创建于2017-09-01<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Slf4j
public class HttpUtils {

    private static final HttpGet NONE_GET = null;
    private static final HttpPost NONE_POST = null;

    /**
     * 用于接收服务端返回的cookies数据
     */
    private static List<Cookie> cookies;

    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(15000)
            .setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000)
            .build();

    /**
     * 重设timeout
     * @param millis 毫秒数
     */
    public static void timeout(int millis) {
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(millis)
                .setConnectTimeout(millis)
                .setConnectionRequestTimeout(millis)
                .build();
    }

    /**
     * HTTP GET请求访问指定的url
     *
     * @param url url,包括查询字符串
     * @return 指定url所返回的字符串数据
     */
    public static String get(String url) {
        return send(new HttpGet(url), NONE_POST, stringResponseHandler);
    }

    /**
     * HTTP POST请求访问指定的url
     *
     * @param url url,可包含查询字符串
     * @return 指定url所返回的字符串数据
     */
    public static String post(String url) {
        return send(NONE_GET, new HttpPost(url), stringResponseHandler);
    }

    /**
     * HTTP POST请求访问指定的url
     *
     * @param url    url,可包含查询字符串
     * @param params post请求发送的参数，参数为param1=value1&param2=value2形式
     * @return 请求返回相应的字符串数据
     */
    public static String post(String url, String params) {
        return send(NONE_GET, buildHttpPost(url, params), stringResponseHandler);
    }

    /**
     * HTTP POST请求访问指定的url
     *
     * @param url    url,可包含查询字符串
     * @param params key-value结构的参数
     * @return 指定url响应的字符串数据
     */
    public static String post(String url, Map<String, String> params) {
        return send(NONE_GET, buildHttpPost(url, params), stringResponseHandler);
    }

    /**
     * HTTP POST请求访问指定的url
     *
     * @param url        url,可包含查询字符串
     * @param params     key-value结构的参数
     * @param cookieList 需要提交到服务端的cookie数据，如果为null，则不提交cookie
     * @param getCookie  判断是否需要从服务端获取cookie数据
     * @return 指定url响应的字符串数据
     */
    public static String post(String url, Map<String, String> params, List<Cookie> cookieList, boolean getCookie) {
        return send(NONE_GET, buildHttpPost(url, params), cookieList, getCookie, stringResponseHandler);
    }

    /**
     * 通过post请求上传单个文件
     *
     * @param url       请求地址，可包含查询字符串
     * @param params    请求参数
     * @param inputName 上传文件的input name
     * @param filePath  上传文件的完整路径
     * @return 请求响应返回的字符串数据
     */
    public static String post(String url, Map<String, String> params, String inputName, String filePath) {
        return post(url, params, inputName, new File(filePath));
    }

    /**
     * 通过post请求上传单个文件
     *
     * @param url       请求地址，可包含查询字符串
     * @param params    请求参数
     * @param inputName 上传文件的input name
     * @param file      上传的文件，为File类型
     * @return 请求响应返回的字符串数据
     */
    public static String post(String url, Map<String, String> params, String inputName, File file) {
        Map<String, File> files = new HashMap<>(1);
        files.put(inputName, file);
        return post(url, params, files);
    }

    /**
     * 通过post请求上传多个文件
     *
     * @param url    请求地址，可包含查询字符串
     * @param params 请求参数
     * @param files  上传的文件，为Map结构，键为上传文件时对应的input name，值为File类型的文件
     * @return 请求响应返回的字符串数据
     */
    public static String post(String url, Map<String, String> params, Map<String, File> files) {
        return send(NONE_GET, buildHttpPost(url, params, files), stringResponseHandler);
    }

    /**
     * 提交json格式数据到指定url
     *
     * @param url  url,可包含查询字符中
     * @param json json格式的字符串数据
     * @return 指定url响应的字符串数据
     */
    public static String postJSON(String url, String json) {
        return post(url, json, MIMETypeEnum.JSON);
    }

    /**
     * 提交xml格式数据到指定url
     *
     * @param url url，可以包含查询字符串
     * @param xml xml格式的字符串数据
     * @return 指定url响应的字符串数据
     */
    public static String postXML(String url, String xml) {
        return post(url, xml, MIMETypeEnum.XML);
    }

    /**
     * 通过指定的数据类型把数据post提交到指定的url
     *
     * @param url          url，可以包含查询字符串
     * @param data         需要提交的数据
     * @param dataTypeEnum 提交数据的MIME type
     * @return 指定url响应的字符串数据
     */
    public static String post(String url, String data, MIMETypeEnum dataTypeEnum) {
        return send(NONE_GET, buildHttpPost(url, data, dataTypeEnum), stringResponseHandler);
    }

    /**
     * 通过指定的数据类型把数据post提交到指定的url
     *
     * @param url          url，可以包含查询字符串
     * @param data         需要提交的数据
     * @param dataTypeEnum 提交数据的MIME type
     * @param cookieList   提交到服务端的cookie数据
     * @param getCookie    是否需要从服务端获取cookie数据
     * @return 指定url响应的字符串数据，并获取由服务端返回的cookie数据
     */
    public static String post(String url, String data, MIMETypeEnum dataTypeEnum, List<Cookie> cookieList, boolean getCookie) {
        return send(NONE_GET, buildHttpPost(url, data, dataTypeEnum), cookieList, getCookie, stringResponseHandler);
    }

    /**
     * 通过指定的数据类型把数据post提交到指定的url，并可指定数字证书
     *
     * @param url          url，可以包含查询字符串
     * @param data         需要提交的数据
     * @param dataTypeEnum 提交数据的MIME type
     * @param certPath     证书路径
     * @param certPassword 证书密码
     * @param certType     证书类型
     * @param sslProtocols ssl协议
     * @return 指定url响应的字符串数据
     */
    public static String post(String url, String data, MIMETypeEnum dataTypeEnum, String certPath, String certPassword, String certType, String[] sslProtocols) {
        return send(NONE_GET, buildHttpPost(url, data, dataTypeEnum), certPath, certPassword, certType, sslProtocols, stringResponseHandler);
    }

    /**
     * 构建http post请求
     * @param url 请求url
     * @param params post请求发送的参数，参数为param1=value1&param2=value2形式
     * @return
     */
    public static HttpPost buildHttpPost(String url, String params) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(params, CharsetEnum.UTF8.getValue());
        stringEntity.setContentType(ContentTypeEnum.FORM_URLENCODED.getValue());
        httpPost.setEntity(stringEntity);
        return httpPost;
    }

    /**
     * 构建http post请求
     * @param url 请求url
     * @param params 请求参数map
     * @return
     */
    public static HttpPost buildHttpPost(String url, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, CharsetEnum.UTF8.getValue()));
        } catch (UnsupportedEncodingException e) {
            log.error("http post not supported encoding: {}", e.getMessage());
        }
        return httpPost;
    }

    /**
     * 构建http post请求
     * @param url 请求url
     * @param data 请求的字符串数据，如json或xml
     * @param dataTypeEnum 指定请求的数据类型
     * @return
     */
    public static HttpPost buildHttpPost(String url, String data, MIMETypeEnum dataTypeEnum) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-type", dataTypeEnum.getMime() + "; charset=" + CharsetEnum.UTF8.getValue());
        httpPost.addHeader("Accept", dataTypeEnum.getMime());
        httpPost.setEntity(new StringEntity(data, CharsetEnum.UTF8.getValue()));
        return httpPost;
    }

    /**
     * 构建http post请求
     * @param url 请求url
     * @param params
     * @param files
     * @return
     */
    public static HttpPost buildHttpPost(String url, Map<String, String> params, Map<String, File> files) {
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        Set<Map.Entry<String, String>> paramEntrySet = params.entrySet();
        for (Map.Entry<String, String> entry : paramEntrySet) {
            multipartEntityBuilder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.TEXT_PLAIN));
        }
        Set<Map.Entry<String, File>> fileEntrySet = files.entrySet();
        for (Map.Entry<String, File> entry : fileEntrySet) {
            multipartEntityBuilder.addPart(entry.getKey(), new FileBody(entry.getValue()));
        }
        HttpEntity entity = multipartEntityBuilder.build();
        httpPost.setEntity(entity);
        return httpPost;
    }

    /**
     * 发送HTTP请求，请求方式可以是GET，也可以是POST，并且需要指定相应的ResponseHandler
     *
     * @param httpGet         如果是GET请求，则传入HttpGet对象，如果为null，表示不使用GET请求
     * @param httpPost        如果是POST请求，则传入HttpPost对象，如果为null，表示不使用POST请求
     * @param responseHandler ResponseHandler响应处理对象
     * @param <T>             响应处理对象中可通过泛型来指定返回的具体数据类型
     * @return 通过在ResponseHandler中指定的具体类型来返回响应数据
     */
    public static <T> T send(HttpGet httpGet, HttpPost httpPost, ResponseHandler<T> responseHandler) {
        T t = null;
        try (
                CloseableHttpClient httpClient = HttpClients.createDefault()
        ) {
           t = executeResponse(httpClient, httpGet, httpPost, responseHandler);
        } catch (IOException e) {
            log.error("http client execute error: {}", e.getMessage());
        }
        return t;
    }

    /**
     * 获取服务端返回的cookie数据
     *
     * @return 服务端返回的cookie数据
     */
    public static List<Cookie> getCookies() {
        return cookies;
    }

    /**
     * 发送HTTP请求，请求方式可以是GET，也可以是POST，并且需要指定相应的ResponseHandler
     *
     * @param httpGet         如果是GET请求，则传入HttpGet对象，如果为null，表示不使用GET请求
     * @param httpPost        如果是POST请求，则传入HttpPost对象，如果为null，表示不使用POST请求
     * @param cookieList      需要提交到服务端的cookie数据
     * @param getCookie       是否需要从服务端获取cookie数据
     * @param responseHandler ResponseHandler响应处理对象
     * @param <T>             响应处理对象中可通过泛型来指定返回的具体数据类型
     * @return 通过在ResponseHandler中指定的具体类型来返回响应数据，并获取由服务端返回的cookie数据到cookies成员变量中
     */
    public static <T> T send(HttpGet httpGet, HttpPost httpPost, List<Cookie> cookieList, boolean getCookie, ResponseHandler<T> responseHandler) {
        T t = null;
        CookieStore cookieStore = new BasicCookieStore();
        try (
                CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build())
        {
            if (cookieList != null) {
                for (Cookie cookie : cookieList) {
                    cookieStore.addCookie(cookie);
                }
            }
            t = executeResponse(httpClient, httpGet, httpPost, responseHandler);
            if (getCookie) {
                cookies = cookieStore.getCookies();
            }
        } catch (IOException e) {
            log.error("http client execute error: {}", e.getMessage());
        }
        return t;
    }

    /**
     * 发送HTTP请求，请求方式可以是GET，也可以是POST，并且需要指定相应的ResponseHandler，同时还可以指定数字证书与协议
     *
     * @param httpGet         如果是GET请求，则传入HttpGet对象，如果为null，表示不使用GET请求
     * @param httpPost        如果是POST请求，则传入HttpPost对象，如果为null，表示不使用POST请求
     * @param certPath        证书路径
     * @param certPassword    证书密码
     * @param certType        证书类型
     * @param sslProtocols    ssl协议数组
     * @param responseHandler ResponseHandler响应处理对象
     * @param <T>             响应处理对象中可通过泛型来指定返回的具体数据类型
     * @return 通过在ResponseHandler中指定的具体类型来返回响应数据
     */
    public static <T> T send(HttpGet httpGet, HttpPost httpPost, String certPath, String certPassword, String certType, String[] sslProtocols, ResponseHandler<T> responseHandler) {
        T t = null;
        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(certType);
            try (FileInputStream inputStream = new FileInputStream(new File(certPath))) {
                keyStore.load(inputStream, certPassword.toCharArray());
            }
            // Trust own CA and all self-signed certs
            SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, certPassword.toCharArray()).build();
            // Allow TLSv1 protocol only
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, sslProtocols,
                    null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        } catch(KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyManagementException e) {
            log.error("read certification or create ssl connection socket factory error: {}", e.getMessage());
        }
        if (sslConnectionSocketFactory != null) {
            try (CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build()) {
                t = executeResponse(httpClient, httpGet, httpPost, responseHandler);
            } catch (IOException e) {
                log.error("http client execute error: {}", e.getMessage());
            }
        }
        return t;
    }

    /**
     * 执行http请求
     * @param httpClient
     * @param httpGet
     * @param httpPost
     * @param responseHandler
     * @param <T>
     * @return
     */
    private static <T> T executeResponse(HttpClient httpClient, HttpGet httpGet, HttpPost httpPost, ResponseHandler<T> responseHandler) {
        T t = null;
        try {
            if (httpGet != null) {
                httpGet.setConfig(requestConfig);
                t = httpClient.execute(httpGet, responseHandler);
            } else {
                httpPost.setConfig(requestConfig);
                t = httpClient.execute(httpPost, responseHandler);
            }
        } catch (IOException e) {
            log.error("http client execute error: {}", e.getMessage());
        }
        return t;
    }

    /**
     * 获取String字符串响应的ResponseHandler<br/>
     * 如果状态为200，则直接获取response结果，如果状态为302，则获取重定向的url，其他状态暂时返回非正常响应状态
     */
    public static ResponseHandler<String> stringResponseHandler = response -> {
        int status = response.getStatusLine().getStatusCode();
        if (status == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity, CharsetEnum.UTF8.getValue()) : null;
        } else if (status == HttpStatus.SC_MOVED_TEMPORARILY) {
            return response.getFirstHeader("Location").getValue();
        } else {
            throw new ClientProtocolException("no normal status: " + status);
        }
    };

}
