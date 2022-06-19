package com.hapinistay.backend.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name="image")
public class Image  implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Column(name="url")
	private String url;
	
	@Column(name="secure_url")
	private String secure_url;
	
	@Column(name="public_id")
	private String public_id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name ="house_id")
	private House house;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name ="room_id")
	private Room room;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name ="apartment_id")
	private Apartment apartment;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name ="building_id")
	private Building building;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name ="paper_id")
	private Paper paper;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSecure_url() {
		return secure_url;
	}

	public void setSecure_url(String secure_url) {
		this.secure_url = secure_url;
	}

	public String getPublic_id() {
		return public_id;
	}

	public void setPublic_id(String public_id) {
		this.public_id = public_id;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Paper getPaper() {
		return paper;
	}

	public void setPaper(Paper paper) {
		this.paper = paper;
	}
	
}
