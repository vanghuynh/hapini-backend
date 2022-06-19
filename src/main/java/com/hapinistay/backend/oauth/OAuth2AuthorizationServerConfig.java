package com.hapinistay.backend.oauth;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.hapinistay.backend.model.AddressSetting;
import com.hapinistay.backend.model.OauthClientDetails;
import com.hapinistay.backend.model.User;
import com.hapinistay.backend.repositories.AddressSettingRepository;
import com.hapinistay.backend.repositories.OauthClientDetailsRepository;
import com.hapinistay.backend.repositories.UserRepository;


@Configuration
//@PropertySource({ "classpath:application.properties" })
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserRepository userRepository;
	
    //@Autowired
    //private Environment env;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

//    @Value("classpath:schema.sql")
//    private Resource schemaScript;
//
//    @Value("classpath:data.sql")
//    private Resource dataScript;

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {// @formatter:off
		clients
		.jdbc(this.dataSource);
	}

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));
		endpoints.tokenStore(tokenStore())
		//TODO:
		//.accessTokenConverter(accessTokenConverter())
		.tokenEnhancer(tokenEnhancerChain)
		.authenticationManager(authenticationManager);
		
		endpoints.userDetailsService(userDetailsService);
    }

    //TODO: JwtAccessTokenConverter
    
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    // JDBC token store configuration
//    @Bean
//    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
//        final DataSourceInitializer initializer = new DataSourceInitializer();
//        initializer.setDataSource(dataSource);
//        initializer.setDatabasePopulator(databasePopulator());
//        return initializer;
//    }
//
//    private DatabasePopulator databasePopulator() {
//        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//        populator.addScript(schemaScript);
//        populator.addScript(dataScript);
//        return populator;
//    }

//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
    
//    @Bean
//    public DataSource dataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
//        dataSource.setUrl(env.getProperty("spring.datasource.url"));
//        dataSource.setUsername(env.getProperty("spring.datasource.user"));
//        dataSource.setPassword(env.getProperty("spring.datasource.pass"));
//        return dataSource;
//    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(this.dataSource);
    }
    
//    @Bean
//	CommandLineRunner init(OauthClientDetailsRepository oauthClientDetailsRepository,
//			AddressSettingRepository addressSettingRepository){
//		return (evt) -> {
//			if(oauthClientDetailsRepository.findAll() == null || oauthClientDetailsRepository.findAll().size() == 0 ) {
//				OauthClientDetails oauthClientDetails1 = new  OauthClientDetails();
//				oauthClientDetails1.setClient_id("fooClientIdPassword");
//				oauthClientDetails1.setClient_secret("secret");
//				oauthClientDetails1.setScope("foo,read,write");
//				oauthClientDetails1.setAuthorized_grant_types("password,authorization_code,refresh_token");
//				oauthClientDetails1.setAccess_token_validity(120);
//				oauthClientDetails1.setRefresh_token_validity(3600);
//				oauthClientDetails1.setAutoapprove("true");
//				oauthClientDetailsRepository.save(oauthClientDetails1);
//			}
//			
//			if(addressSettingRepository.findAll() == null || addressSettingRepository.findAll().size() == 0) {
//				AddressSetting city = new AddressSetting("C1", "Ho Chi Minh", "TP Hồ Chí Minh, Việt Nam", "CITY", "", "","");
//				AddressSetting district1 = new AddressSetting("D1", "District 1", "Quận 1, TP Hồ Chí Minh", "DISTRICT", "C1", "","");
//				AddressSetting district2 = new AddressSetting("D2", "District 2", "Quận 2, TP Hồ Chí Minh", "DISTRICT", "C1", "","");
//				AddressSetting ward1 = new AddressSetting("W1", "Ward 1", "Phường 1, Quận 1", "WARD", "C1", "D1","");
//				AddressSetting ward2 = new AddressSetting("W2", "Ward 2", "Phường 2, Quận 1", "WARD", "C1", "D1","");
//				AddressSetting ward3 = new AddressSetting("W3", "Ward 3", "Phường 3, Quận 1", "WARD", "C1", "D2","");
//				AddressSetting ward4 = new AddressSetting("W4", "Ward 4", "Phường 4, Quận 1", "WARD", "C1", "D2","");
//				List<AddressSetting> addressList = Arrays.asList(city, district1, district2, ward1, ward2, ward3, ward4);
//				addressSettingRepository.save(addressList);
//			}
//		};
//		
//	}

}
