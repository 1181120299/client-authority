package com.jack.clientauthority.config;

import com.jack.clientauthority.annotation.ClientAccessUsernameStrategy;
import com.jack.clientauthority.annotation.HomePageProvider;
import com.jack.clientauthority.annotation.OAuth2Properties;
import com.jack.clientauthority.annotation.UserCache;
import com.jack.clientauthority.annotation.defaultImpl.ClientAccessUsernameStrategyImpl;
import com.jack.clientauthority.annotation.defaultImpl.DefaultHomePageController;
import com.jack.clientauthority.processor.CustomAccessDeniedHandler;
import com.jack.clientauthority.service.UserDetailService;
import com.jack.clientauthority.service.impl.RemoteUserDetailServiceImpl;
import com.jack.clientauthority.utils.ApplicationContextHelper;
import com.jack.clientauthority.utils.WebClientHelper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)
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
    public DefaultHomePageController clientAuthorityDefaultHomePageController() {
        return new DefaultHomePageController();
    }

    @Bean
    public UsernameProcessingFilter usernameProcessingFilter() {
        return new UsernameProcessingFilter();
    }

    @Bean
    public UserCache clientAuthorityUserCache() {
        return new UserCache();
    }

    @Bean
    public ApplicationContextHelper clientAuthorityApplicationContextHelper() {
        return new ApplicationContextHelper();
    }
}
