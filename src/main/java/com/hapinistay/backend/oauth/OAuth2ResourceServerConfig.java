package com.hapinistay.backend.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableAutoConfiguration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
    private Environment environment;
	
    @Override
    public void configure(final HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).
		and().authorizeRequests()
		.anyRequest().permitAll();
    }

    // Remote token service
    /*@Primary
    @Bean
    public RemoteTokenServices tokenService() {
        final RemoteTokenServices tokenService = new RemoteTokenServices();
        String endpointTokenUrl = environment.getProperty("app.vtsolutions.checkTokenEndPointUrl") + "/oauth/check_token";
        System.out.println("endpointTokenUrl: " + endpointTokenUrl);
        tokenService.setCheckTokenEndpointUrl(endpointTokenUrl);
        tokenService.setClientId(environment.getProperty("app.vtsolutions.clientId"));
        tokenService.setClientSecret(environment.getProperty("app.vtsolutions.clientSecret"));
        return tokenService;
    }*/

}