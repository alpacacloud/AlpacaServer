package com.alpaca.infrastructure.core.utils;


import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

/**
 * @Author ：lichenw
 * @Date ：Created in 13:28 2019/3/31
 * @Description：
 * @Modified By：
 */

public class HttpHelper {

    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    /**
     * 修改返回值为String json
     *
     * @param url
     * @param params
     * @return
     */
    public static HttpEntity HttpPost(String url, List<NameValuePair> params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        try {
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            request.setEntity(entity);
            HttpResponse response = client.execute(request);

            // 如果状态码为200,就是正常返回
            if (response.getStatusLine().getStatusCode() == 200) {
//				 String result = EntityUtils.toString(response.getEntity());
                return response.getEntity();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
        }
        return null;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            // 打开和URL之间的连接
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送 POST 请求出现异常！", e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                logger.error("", ex);
            }
        }
        return result;
    }

    public static HttpEntity HttpGet(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                return response.getEntity();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    public static <T> T HttpGet(String url, Class<T> cls) {
        HttpEntity httpEntity = HttpGet(url);
        if (httpEntity == null) {
            return null;
        }
        try {
            T result = JSONObject.parseObject(EntityUtils.toString(httpEntity), cls);
            return result;
        } catch (Exception e) {
            logger.error("HttpGet EntityUtils.toString", e);
        }
        return null;
    }


    /**
     * @param @param  request
     * @param @param  response
     * @param @return 参数
     * @return String    返回类型
     * @throws
     * @Title: getIpAddress
     * @Description: 获取客户端真实IP地址
     * @author yihj
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 避免反向代理不能获取真实地址, 取X-Forwarded-For中第一个非unknown的有效IP字符串
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getIpAddress() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return getIpAddress(request);
    }


    //连接超时时间，默认10秒
    private static final int socketTimeout = 10000;

    //传输超时时间，默认30秒
    private static final int connectTimeout = 30000;

    /**
     * post请求
     *
     * @throws IOException
     * @throws ClientProtocolException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws KeyManagementException
     * @throws UnrecoverableKeyException
     */
    public static String sendPostXml(String url, Object xmlObj) throws ClientProtocolException, IOException, UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException {


        HttpPost httpPost = new HttpPost(url);
        //解决XStream对出现双下划线的bug
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStreamForRequestPostData.alias("xml", xmlObj.getClass());
        //将要提交给API的数据对象转换成XML格式数据Post给API
        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);

        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        //设置请求器的配置
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        httpPost.setConfig(requestConfig);

        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        return result;
    }

    /**
     * 自定义证书管理器，信任所有证书
     *
     * @author pc
     */
    public static class MyX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }


}
