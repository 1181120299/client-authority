package com.jack.clientauthority.config;

import com.jack.clientauthority.annotation.ClientAccessUsernameStrategy;
import com.jack.clientauthority.annotation.HomePageProvider;
import com.jack.clientauthority.annotation.defaultImpl.ClientAccessUsernameStrategyImpl;
import com.jack.clientauthority.annotation.defaultImpl.DefaultHomePageController;
import com.jack.clientauthority.processor.CustomAccessDeniedHandler;
import com.jack.clientauthority.service.UserDetailService;
import com.jack.clientauthority.service.impl.RemoteUserDetailServiceImpl;
import com.jack.clientauthority.utils.WebClientHelper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.jack.clientauthority.mapper");
        return configurer;
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailService.class)
    public RemoteUserDetailServiceImpl remoteUserDetailService() {
        return new RemoteUserDetailServiceImpl();
    }

    @Bean
    public WebClientHelper  webClientHelper(WebClient webClient) {
        return new WebClientHelper(webClient);
    }

    @Bean
    @ConditionalOnMissingBean(ClientAccessUsernameStrategy.class)
    public ClientAccessUsernameStrategyImpl defaultClientAccessUsernameStrategyImpl() {
        return new ClientAccessUsernameStrategyImpl();
    }

    @Bean
    @ConditionalOnMissingBean(HomePageProvider.class)
    public DefaultHomePageController defaultHomePageController() {
        return new DefaultHomePageController();
    }
}
