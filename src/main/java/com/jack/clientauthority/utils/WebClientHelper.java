package com.jack.clientauthority.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

/**
 * WebClient的帮助类。用于在client之间互相请求彼此的接口。
 * <p></p>
 *
 * 在发送请求时，会自动添加当前client信息、用户信息，完成身份认证。
 * <p></p>
 *
 * 如果需要访问不是"自己"的应用（没有在授权服务器上注册的应用），例如访问企业微信的接口，
 * 请直接使用{@link WebClient}，这时候不适合使用此帮助类，以免暴露用户信息。
 */
public class WebClientHelper {

    public static final String JACK_OAUTH2_USERNAME_HEADER = "jack.oauth2.username";

    private static WebClient webClient;

    public WebClientHelper(WebClient wc) {
        webClient = wc;
    }

    /**
     * 发送一个GET请求
     * @return 请求结果，封装成指定类型的数据
     */
    public static <T> T get(String uri, Class<T> tClass) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        String resultStr = webClient.get()
                .uri(uri)
                .attributes(clientRegistrationId(ClientRegistrationConstant.CLIENT_CLIENT_CREDENTIALS))
                .header(JACK_OAUTH2_USERNAME_HEADER, username)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return JSON.parseObject(resultStr, tClass);
    }
}












