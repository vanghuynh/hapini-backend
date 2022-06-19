package com.hapinistay.backend.service;

import java.io.IOException;

public interface SendGridEmailService {
	void sendEmail(String fromEmail, String fromName, String toEmail, String toName, String url) throws IOException;
	void sendEmailResetPass(String fromEmail, String fromName, String toEmail, String toName, String url) throws IOException;
	void sendEmailRegisterAccount(String fromEmail, String fromName, String toEmail, String toName, String phone) throws IOException;
}