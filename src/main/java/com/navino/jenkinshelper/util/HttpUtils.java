package com.navino.jenkinshelper.util;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zhangZhuoWen
 * @ClassName HttpUtils
 * @date 2021/1/18 15:24
 * @Description TODO
 */
public class HttpUtils {

    /**
     * 向目的URL发送post请求
     *
     * @param url    目的url
     * @param params 发送的参数
     * @return ResponseEntity
     */
    public static ResponseEntity sendPostRequest(String url, MultiValueMap<String, String> params) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity response = client.exchange(url, method, requestEntity, ResponseEntity.class);

        return response;
    }

    /**
     * sendGetRequest
     *
     * @param url
     * @return
     */
    public static ResponseEntity<String> sendGetRequest(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        return forEntity;
    }

    /**
     * sendGetRequest
     *
     * @param url
     * @param charset
     * @param cookie
     * @return
     */
    public static String sendGetRequest(String url, String charset,String cookie) {
        try {
            URL httpURL = new URL(url);
            HttpURLConnection http = (HttpURLConnection) httpURL
                    .openConnection();
            http.setRequestProperty("Cookie", cookie);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    http.getInputStream(), charset));
            StringBuilder sb = new StringBuilder();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
                sb.append("\n");
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
