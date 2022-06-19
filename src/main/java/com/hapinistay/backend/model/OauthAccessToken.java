package com.hapinistay.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@SuppressWarnings("serial")
@Entity
@Table(name="oauth_access_token")
public class OauthAccessToken {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="token_id")
	private String token_id;
	
	@Column(name="token")
	//@Lob
	//@Type(type = "org.hibernate.type.TextType")
	private byte[] token;//BYTEA
	
	@Column(name="authentication_id")
	private String authentication_id;
	
	@Column(name="user_name")
	private String user_name;
	
	@Column(name="client_id")
	private String client_id;
	
	@Column(name="authentication")
	//@Lob
	//@Type(type = "org.hibernate.type.TextType")
	private byte[]  authentication;//BYTEA
	
	@Column(name="refresh_token")
	private String refresh_token;

	public String getToken_id() {
		return token_id;
	}

	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}

	public byte[]  getToken() {
		return token;
	}

	public void setToken(byte[]  token) {
		this.token = token;
	}

	public String getAuthentication_id() {
		return authentication_id;
	}

	public void setAuthentication_id(String authentication_id) {
		this.authentication_id = authentication_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public byte[]  getAuthentication() {
		return authentication;
	}

	public void setAuthentication(byte[]  authentication) {
		this.authentication = authentication;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
}