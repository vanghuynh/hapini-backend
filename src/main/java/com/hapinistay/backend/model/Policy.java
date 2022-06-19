package com.hapinistay.backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name="policy")
public class Policy  implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="flexTime")
	private boolean flexTime;
	
	@Column(name="cookMeal")
	private boolean cookMeal;
	
	@Column(name="packing")
	private boolean packing;
	
	@Column(name="wifi")
	private boolean wifi;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isFlexTime() {
		return flexTime;
	}

	public void setFlexTime(boolean flexTime) {
		this.flexTime = flexTime;
	}

	public boolean isCookMeal() {
		return cookMeal;
	}

	public void setCookMeal(boolean cookMeal) {
		this.cookMeal = cookMeal;
	}

	public boolean isPacking() {
		return packing;
	}

	public void setPacking(boolean packing) {
		this.packing = packing;
	}

	public boolean isWifi() {
		return wifi;
	}

	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}
	
	
	
}