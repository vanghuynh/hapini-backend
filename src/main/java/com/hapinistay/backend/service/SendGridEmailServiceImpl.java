package com.hapinistay.backend.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;



@Service("sendGridEmailService")
public class SendGridEmailServiceImpl implements SendGridEmailService{

	@Autowired
	private Environment environment;
	
	@Override
	public void sendEmail(String fromEmail, String fromName, String toEmail, String toName, String url) throws IOException {
		Email from = new Email(fromEmail);
	    String subject = "Sending with SendGrid is Fun";
	    Email to = new Email(toEmail);
	    Content content = new Content("text/plain", "and easy to do anywhere, even with Java" + url);
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid(environment.getRequiredProperty("SENDGRID_API_KEY"));
	    Request request = new Request();
	    request.setMethod(Method.POST);
	    request.setEndpoint("mail/send");
	    request.setBody(mail.build());
	    Response response = sg.api(request);
	    System.out.println(response.getStatusCode());
	    System.out.println(response.getBody());
	    System.out.println(response.getHeaders());
	}

	@Override
	public void sendEmailResetPass(String fromEmail, String fromName, String toEmail, String toName, String url) throws IOException {
	    Mail mail = new Mail();
	    mail.setFrom(new Email(fromEmail, fromName));
	    mail.setTemplateId(environment.getRequiredProperty("SENDGRID_TEMPLATE_RESET_PASS"));

	    Personalization personalization = new Personalization();
	    personalization.addDynamicTemplateData("userName", toName);
	    personalization.addDynamicTemplateData("resetLink", url);
	    personalization.addTo(new Email(toEmail, toName));
	    mail.addPersonalization(personalization);
	            
	    SendGrid sg = new SendGrid(environment.getRequiredProperty("SENDGRID_API_KEY"));
	    Request request = new Request();
	    request.setMethod(Method.POST);
	    request.setEndpoint("mail/send");
	    request.setBody(mail.build());
	    Response response = sg.api(request);
	    System.out.println(response.getStatusCode());
	    System.out.println(response.getBody());
	    System.out.println(response.getHeaders());
	}

	@Override
	public void sendEmailRegisterAccount(String fromEmail, String fromName, String toEmail, String toName,
			String phone) throws IOException {
		Mail mail = new Mail();
	    mail.setFrom(new Email(fromEmail, fromName));
	    mail.setTemplateId(environment.getRequiredProperty("SENDGRID_TEMPLATE_REGISTER_ACCOUNT"));

	    Personalization personalization = new Personalization();
	    personalization.addDynamicTemplateData("userName", toName);
	    personalization.addDynamicTemplateData("loginLink", environment.getRequiredProperty("HAPINI_WEB_URL") + "/login");
	    personalization.addDynamicTemplateData("email", toEmail);
	    personalization.addDynamicTemplateData("phone", phone);
	    personalization.addTo(new Email(toEmail, toName));
	    mail.addPersonalization(personalization);
	            
	    SendGrid sg = new SendGrid(environment.getRequiredProperty("SENDGRID_API_KEY"));
	    Request request = new Request();
	    request.setMethod(Method.POST);
	    request.setEndpoint("mail/send");
	    request.setBody(mail.build());
	    Response response = sg.api(request);
	    System.out.println(response.getStatusCode());
	    System.out.println(response.getBody());
	    System.out.println(response.getHeaders());
	}

}
