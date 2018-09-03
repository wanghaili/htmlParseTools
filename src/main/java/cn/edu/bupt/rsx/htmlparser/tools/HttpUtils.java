package cn.edu.bupt.rsx.htmlparser.tools;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
    private final static OkHttpClient mOkHttpClient;

    static {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        mOkHttpClient = httpBuilder.readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES).writeTimeout(3, TimeUnit.MINUTES) //设置超时
                .build();
    }

    public static HttpEntity getData(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpEntity entity = null;
        HttpGet request = new HttpGet();
        try {
            request.setURI(new URI(url));
            HttpResponse response = httpclient.execute(request);
            entity = response.getEntity();
        } catch (Exception e) {
            LOGGER.error("访问出错：" + e);
        }
        return entity;
    }


    /**
     * 获取网页编码
     *
     * @param content
     * @return
     */
    public static String getCharSet(String content) {
        String charset = "UTF-8";//编码
        try {
            Pattern pattern = Pattern.compile(Constants.CHARSET_PATTERN);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                content = matcher.group();
            }
            content = content.replace("\"", "");
            content = content.replace("'", "");
            String[] strArr = content.split("charset=");
            if (strArr != null && strArr.length > 1) {
                charset = strArr[1];
            }
        } catch (Exception e) {
            LOGGER.error("发送http请求失败，失败信息：", e);
        }
        LOGGER.info("charset:{}", charset);
        return charset;
    }


    public static String okHttpGet(String url) {
        Request request = new Request.Builder().url(url).build();
        String result = "";
        try {
            Response response = execute(request);
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (IOException e) {
            LOGGER.error("okhttp request error:", e);
        }
        return result;
    }

    /**
     * 不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public static void enqueue(Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /**
     * post请求
     *
     * @param httpPost
     * @param formParams
     * @return
     */
    public static HttpEntity postData(HttpPost httpPost, List<NameValuePair> formParams) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpEntity entity = null;
        UrlEncodedFormEntity uefEntity = null;
        try {
            uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity(uefEntity);
            HttpResponse response = httpclient.execute(httpPost);
            entity = response.getEntity();
        } catch (Exception e) {
            LOGGER.error("访问出错：" + e);
        }
        return entity;
    }
}
