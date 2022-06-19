package com.hapinistay.backend;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.hapinistay.backend.model.AddressSetting;
import com.hapinistay.backend.oauth.OAuth2AuthorizationServerConfig;
import com.hapinistay.backend.service.AddressSettingService;


//@Import(JpaConfiguration.class)
//@Import(OAuth2AuthorizationServerConfig.class)
@SpringBootApplication(scanBasePackages={"com.hapinistay.backend"})
//@EnableDiscoveryClient
//@EnableFeignClients
public class HapiniStayApp {

	@Autowired
	private AddressSettingService addressSettingService;
	
	public static void main(String[] args) {
//	    System.setProperty("spring.config.name", "application.yml");
		SpringApplication.run(HapiniStayApp.class, args);
	}
	
//	@Bean
//	CommandLineRunner init(){
//		return (evt) -> {
//			
//			AddressSetting city = new AddressSetting("C1", "Ho Chi Minh", "CITY", "", "","");
//			AddressSetting district1 = new AddressSetting("D1", "District 1", "DISTRICT", "C1", "","");
//			AddressSetting district2 = new AddressSetting("D2", "District 2", "DISTRICT", "C1", "","");
//			AddressSetting ward1 = new AddressSetting("W1", "Ward 1", "WARD", "C1", "D1","");
//			AddressSetting ward2 = new AddressSetting("W2", "Ward 2", "WARD", "C1", "D1","");
//			AddressSetting ward3 = new AddressSetting("W3", "Ward 3", "WARD", "C1", "D2","");
//			AddressSetting ward4 = new AddressSetting("W4", "Ward 4", "WARD", "C1", "D2","");
//			List<AddressSetting> addressList = Arrays.asList(city, district1, district2, ward1, ward2, ward3, ward4);
//			this.addressSettingService.saveAddressSettings(addressList);
//		};
//		
//	}
	
}
