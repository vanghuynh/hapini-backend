package com.hapinistay.backend.dto;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
	private String username;
	private String password;
	
	private String name;
	private String phone;
	private Long type;
	private String note;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonCreator
	public UserDto(@JsonProperty("username") String username, @JsonProperty("password") String password,
			@JsonProperty("name") String name,
			@JsonProperty("phone") String phone, @JsonProperty("type") Long type, @JsonProperty("note") String note, @JsonProperty("email") String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.type = type;
		this.note = note;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "UserDto [username=" + username + ", password=" + password + "]";
	}

	public String getName() {
		return name;
	}


	public String getPhone() {
		return phone;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
