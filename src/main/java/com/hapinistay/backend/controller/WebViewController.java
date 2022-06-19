//package com.hapinistay.backend.controller;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.web.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.client.HttpStatusCodeException;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//
//
//
//
///**
// * This class is used for routing the admin web module to avoid whitelabel error, so that query string code is obtained
// * 
// *
// */
//@Controller
//@RequestMapping("/")
//public class WebViewController implements ErrorController {
//
//    private static final String PATH = "/error";
//    private final Logger logger = LoggerFactory.getLogger(WebViewController.class);
//
//    @Override
//    public String getErrorPath() {
//        return PATH;
//    }
//
////    @RequestMapping("/")
////    public String home() {
////        return "index.html";
////    }
//    
//    @RequestMapping("")
//    public String defaultPage() {
//        return "forward:/index.html";
//    }
//
//    @RequestMapping(value = PATH)
//    public String error() {
//        return "index.html";
//    }
//    public void addViewControllers(final ViewControllerRegistry registry) {
//    	registry.addViewController("/**").setViewName("forward:/index.html");
//    }
//    @GetMapping("/{path:[^\\.]*}")
//    String redirect() {
//        return "forward:/index.html";
//    }
//  
//}
