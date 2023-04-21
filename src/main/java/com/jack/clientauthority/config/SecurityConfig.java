package com.jack.clientauthority.config;

import com.jack.clientauthority.utils.ClientRegistrationConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public WebSecurityCustomizer clientAuthorityWebSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/webjars/**");
    }

    @Bean
    public SecurityFilterChain clientAuthoritySecurityFilterChain(HttpSecurity http, AccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                .authorizeHttpRequests(
                        authorize -> authorize.anyRequest().authenticated()
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/" + ClientRegistrationConstant.CLIENT_OIDC)
                )
                .oauth2Client(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler()));

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        http.oauth2ResourceServer()
                .jwt();
        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);

        // Set the location that the End-User`s User Agent will be redirected to
        // after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/index");

        return oidcLogoutSuccessHandler;
    }
}
