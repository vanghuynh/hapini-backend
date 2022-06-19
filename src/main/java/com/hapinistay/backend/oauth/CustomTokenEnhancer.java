package com.hapinistay.backend.oauth;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.hapinistay.backend.model.User;
import com.hapinistay.backend.service.UserService;

public class CustomTokenEnhancer implements TokenEnhancer {
	@Autowired
	private UserService userService;
	
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        User user = this.userService.findByUsername(authentication.getName());
        if(user != null) {
        	additionalInfo.put("organization", authentication.getName() + randomAlphabetic(4));
        	additionalInfo.put("userId", user.getId());
        	additionalInfo.put("name", user.getName());
        	additionalInfo.put("phone", user.getPhone());
        	additionalInfo.put("username", user.getUsername());
        	additionalInfo.put("authority", user.getAuthority());
        }
        if(user != null && user.getHouses() != null) {
        	additionalInfo.put("rooms", user.getHouses());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}
