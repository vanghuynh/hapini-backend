package com.hapinistay.backend.controller;

import java.io.IOException;
import java.util.*;

import com.hapinistay.backend.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hapinistay.backend.model.Balance;
import com.hapinistay.backend.model.User;
import com.hapinistay.backend.service.SendGridEmailService;
import com.hapinistay.backend.service.UserService;
import com.hapinistay.backend.util.CommonUtil;
import com.hapinistay.backend.util.Constants;
import com.hapinistay.backend.util.DateUtil;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private SendGridEmailService sendGridEmailService;
	
	@Autowired
	private Environment environment;

	@RequestMapping(value = "/sign-up", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> signUp(@RequestBody UserDto userDto) {

		User user = userService.loadUserByUsername(userDto.getUsername());
		
		if (user != null) {
			System.out.println("found user");
			return new ResponseEntity<>(new ResponseDto("User already exists.", userDto.getUsername()), HttpStatus.CONFLICT);
		} else {
			user = new User();
			System.out.println("user not found");
		}

		user.setUsername(userDto.getUsername().toLowerCase());
		user.setPassword(userDto.getPassword());
		user.setName(userDto.getName());
		user.setPhone(userDto.getPhone());
		user.setEnabled(true);
		user.setAuthority(Constants.USER_AUTHORITY);
		user.setLastUpdate(DateUtil.getCurrentDate());
		user.setActiveCode(CommonUtil.getRandomCode());
		Balance balance = new Balance();
		balance.setAmount(0.0);
		balance.setLastUpdate(DateUtil.getCurrentDate());
		user.setBalance(balance);
		userService.saveUser(user);
		try {
			this.sendGridEmailService.sendEmailRegisterAccount("no-reply" + environment.getRequiredProperty("EMAIL_DOMAIN"), environment.getRequiredProperty("EMAIL_NAME"), user.getUsername(), user.getName(), user.getPhone());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error send email grid " + e);
		}
		return new ResponseEntity<ResponseDto>(new ResponseDto("User registered", user), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> changePassword(@RequestBody UserPasswordDto userPasswordDto) {

		User user = userService.loadUserByUsername(userPasswordDto.getUsername());

		if (user == null) {
			System.out.println("not found user");
			return new ResponseEntity<>(new ResponseDto("User with username "
					+ " not found", userPasswordDto.getUsername()), HttpStatus.NOT_FOUND);
		}
		if (!user.getPassword().equals(userPasswordDto.getOldPassword())) {
			System.out.println("Incorrect old password");
			return new ResponseEntity<>(new ResponseDto("User password not match", userPasswordDto.getUsername()), HttpStatus.NOT_FOUND);
		}
		user.setPassword(userPasswordDto.getNewPassword());
		userService.updateUser(user);
		return new ResponseEntity<ResponseDto>(new ResponseDto("User password updated", user.getUsername()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> resetPassword(@RequestBody UserPasswordDto userPasswordDto) {

		User user = userService.loadUserByUsername(userPasswordDto.getUsername());

		if (user == null) {
			System.out.println("not found user");
			return new ResponseEntity<>(new ResponseDto("User not found", userPasswordDto.getUsername()), HttpStatus.NOT_FOUND);
		}
//		if (!user.getPhone().equals(userPasswordDto.getPhone())) {
//			System.out.println("Incorrect phone number");
//			return new ResponseEntity<>(new ResponseDto("User phone not match", userPasswordDto.getPhone()), HttpStatus.NOT_FOUND);
//		}
		user.setPassword(userPasswordDto.getNewPassword());
		user.setResetCode(null);
		userService.updateUser(user);
		return new ResponseEntity<ResponseDto>(new ResponseDto("User password updated", user.getUsername()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get-user", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> getUserByIdAndCode(@RequestParam(required=true) String userId, @RequestParam(required=true) String code) {

		User user = userService.findById(Long.valueOf(userId));

		if (user == null) {
			System.out.println("not found user");
			return new ResponseEntity<>(new ResponseDto("User not found", userId), HttpStatus.NOT_FOUND);
		}
		if(code == null || code.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("The code must not null", code), HttpStatus.NOT_FOUND);
		}
		if(!code.equals(user.getResetCode())) {
			return new ResponseEntity<>(new ResponseDto("The code not match with server", code), HttpStatus.NOT_FOUND);
		}
		UserPasswordDto userPasswordDto = new UserPasswordDto();
		userPasswordDto.setUsername(user.getUsername());
		return new ResponseEntity<ResponseDto>(new ResponseDto("User found", userPasswordDto), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/forget-password", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> forgetPassword(@RequestBody UserPasswordDto userPasswordDto) {

		User user = userService.loadUserByUsername(userPasswordDto.getUsername());

		if (user == null) {
			System.out.println("not found user");
			return new ResponseEntity<>(new ResponseDto("User not found", userPasswordDto.getUsername()), HttpStatus.NOT_FOUND);
		}
		if (!user.getPhone().equals(userPasswordDto.getPhone())) {
			System.out.println("Incorrect phone number");
			return new ResponseEntity<>(new ResponseDto("User phone not match", userPasswordDto.getPhone()), HttpStatus.NOT_FOUND);
		}
		String resetCode = CommonUtil.getRandomCode();
		user.setResetCode(resetCode);
		userService.updateUser(user);
		String url = environment.getRequiredProperty("HAPINI_WEB_URL") +  "/resetpass/" + user.getId() + "/" + resetCode;
		System.out.println(url);
		
		try {
			this.sendGridEmailService.sendEmailResetPass("no-reply" + environment.getRequiredProperty("EMAIL_DOMAIN"), environment.getRequiredProperty("EMAIL_NAME"), user.getUsername(), user.getName(), url);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error send email grid " + e);
		}
		
		return new ResponseEntity<ResponseDto>(new ResponseDto("Code generated, email sent", user.getUsername() + " : " + url), HttpStatus.OK);
	}
	

	@RequestMapping(value = "/facebook-register", method = RequestMethod.POST)
    public ResponseEntity<?> authorizeOrCreateSocialUser(@RequestBody UserDto userDto) {

		User user = userService.loadUserByUsername(userDto.getUsername());
		
		if (user != null) {
			System.out.println("found user");
			return new ResponseEntity<>(new ResponseDto("User already exists.", user), HttpStatus.OK);
		} else {
			user = new User();
			System.out.println("user not found");
		}

		user.setUsername(userDto.getUsername().toLowerCase());
		user.setPassword(userDto.getPassword());
		user.setName(userDto.getName());
		user.setPhone(userDto.getPhone());
		user.setEmail(userDto.getEmail());
		user.setEnabled(true);
		user.setAuthority(Constants.USER_AUTHORITY);
		user.setLastUpdate(DateUtil.getCurrentDate());
		user.setActiveCode(CommonUtil.getRandomCode());
		Balance balance = new Balance();
		balance.setAmount(0.0);
		balance.setLastUpdate(DateUtil.getCurrentDate());
		user.setBalance(balance);
		//set note for user
		user.setType(userDto.getType());
		user.setNote(userDto.getNote());
		userService.saveUser(user);
		
//	    try {
//	    	CloseableHttpClient client = HttpClients.createDefault();
//		    HttpPost httpPost = new HttpPost("http://localhost:8080/oauth/token");
//		    List<NameValuePair> params = new ArrayList<NameValuePair>();
//		    params.add(new BasicNameValuePair("grant_type", "password"));
//		    params.add(new BasicNameValuePair("username", user.getUsername()));
//		    params.add(new BasicNameValuePair("password", user.getPassword()));
//		    httpPost.setEntity(new UrlEncodedFormEntity(params));
//		    
//		    UsernamePasswordCredentials creds
//		      = new UsernamePasswordCredentials("fooClientIdPassword", "secret");
//			httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
//			//response
//			CloseableHttpResponse response;
//			response = client.execute(httpPost);
//		    System.out.println("Res: " + response);
//		    client.close();
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return new ResponseEntity<ResponseDto>(new ResponseDto("User registered", user), HttpStatus.OK);
    }

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> loginByUsernamePassword(@RequestBody LoginDto loginDto) throws IOException {

		LoginResponseDto body = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			Map<String, String> headerMap = new HashMap<>();
			headerMap.put("Authorization", "Basic Zm9vQ2xpZW50SWRQYXNzd29yZDpzZWNyZXQ=");
			headers.setAll(headerMap);

			HttpEntity<Object> request = new HttpEntity<>(headers);
			ResponseEntity<LoginResponseDto> response = restTemplate
					.exchange("http://localhost:8080/oauth/token?grant_type=password&username="+ loginDto.getUsername() +"&password=" + loginDto.getPassword() + "", HttpMethod.POST, request, LoginResponseDto.class);
			body = response.getBody();
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error getting token " + e);
		}

		return new ResponseEntity<ResponseDto>(new ResponseDto("OK", body), HttpStatus.OK);
	}

}