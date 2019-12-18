package org.breeze.core.utils.net;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther: 黑面阿呆
 * @Date: 2019/2/19 21:06
 * @Version: 1.0.0
 */
public class UtilHttpClient {

    private static Log log = LogFactory.getLog(UtilHttpClient.class);

    /**
     * post提交
     *
     * @param url
     * @param postStr post提交的字符串
     * @return
     * @throws Exception
     */
    public static String post(String url, String postStr) throws Exception {
        HttpClient hc = new HttpClient();
        try {
            return hc.sendPost(url, postStr, null, null);
        } catch (Exception e) {
            throw e;
        } finally {
            hc.close();
        }
    }

    /**
     * post提交
     *
     * @param url
     * @param postStr
     * @param requestCfg
     * @return
     * @throws Exception
     */
    public static String post(String url, String postStr, RequestConfig requestCfg) throws Exception {
        HttpClient hc = new HttpClient();
        try {
            return hc.sendPost(url, postStr, null, null, requestCfg);
        } catch (Exception e) {
            throw e;
        } finally {
            hc.close();
        }
    }

    /**
     * Get提交
     *
     * @param url
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = null;
        httpget = new HttpGet(url);
        try {
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                String reValue = EntityUtils.toString(entity);
                return reValue;
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                httpget.abort();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * HttpClient的post方法
     *
     * @param url
     * @param params 参数map集合
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params) throws IOException {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        if (params != null) {
            Object[] keys = params.keySet().toArray();
            int len = keys.length;
            for (int i = 0; i < len; i++) {
                String key = (String) keys[i];
                formParams.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String reValue = EntityUtils.toString(entity);
            return reValue;
        } catch (ClientProtocolException e) {
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                httppost.abort();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
