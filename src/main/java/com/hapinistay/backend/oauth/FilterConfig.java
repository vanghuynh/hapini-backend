//package com.hapinistay.backend.oauth;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//
//import com.github.greengerong.PreRenderSEOFilter;
//
//@Configuration
//public class FilterConfig {
//	
//	@Autowired
//	private Environment environment;
//	
//	@Bean
//	public FilterRegistrationBean prerenderFilter(){
//	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//	         
//	    registrationBean.setFilter(new PreRenderSEOFilter());
//	    registrationBean.addUrlPatterns("/*");
//	    registrationBean.addInitParameter("prerenderToken", this.environment.getProperty("PRERENDER_TOKEN"));
//	    registrationBean.addInitParameter("crawlerUserAgents", "googlebot," + 
//	    		"bingbot," + 
//	    		"yandex," + 
//	    		"bufferbot," + 
//	    		"www.google.com/webmasters/tools/richsnippets," + 
//	    		"WhatsApp," + 
//	    		"flipboard," + 
//	    		"tumblr," + 
//	    		"bitlybot," + 
//	    		"SkypeUriPreview," + 
//	    		"nuzzel," + 
//	    		"Discordbot," + 
//	    		"Google Page Speed," + 
//	    		"Qwantify,"+ 
//	    		"twitterbot");
//	         
//	    return registrationBean;    
//	}
//
//}