package com.hapinistay.backend.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hapinistay.backend.util.Constants;

@SuppressWarnings("serial")
@Entity
@Table(name="building")
public class Building extends AbstractMultilangSupport<Building> implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name_vi")
	private String nameVi;
	
	@Column(name="name_en")
	private String nameEn;
	
	@Transient
	private String name;
	
	@Column(name="description_vi", length=2000)
	private String descriptionVi;
	
	@Column(name="description_en", length=2000)
	private String descriptionEn;
	
	@Transient
	private String description;
	
	@Column(name="note_vi", length=2000)
	private String noteVi;
	
	@Column(name="note_en", length=2000)
	private String noteEn;
	
	@Transient
	private String note;
	
	@Column(name="status")
	private String status;
	
	@Column(name="lastUpdate")
	private Date lastUpdate;
	
	@OneToMany(mappedBy="building", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Image> images;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="address_id")
	private Address address;
	
	@JsonIgnore
	@OneToMany(mappedBy="building", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Apartment> apartments;


	@Override
	public void getLanguageDto(String lang) {
		if(lang != null & Constants.LANG_EN.equals(lang)) {
			this.name = this.nameEn;
			this.description = this.descriptionEn;
			this.note =  this.noteEn;
		} else {
			this.name = this.nameVi;
			this.description = this.descriptionVi;
			this.note =  this.noteVi;
		}
	}

	@Override
	public void setLanguageDto(String lang) {
		if(lang != null & Constants.LANG_EN.equals(lang)) {
			this.setNameEn(this.getName());
			this.setDescriptionEn(this.getDescription());
			this.setNoteEn(this.getNote());
		} else {
			this.setNameVi(this.getName());
			this.setDescriptionVi(this.getDescription());
			this.setNoteVi(this.getNote());
		}
	}

	@Override
	public void setLanguageDto(String lang, Building oldEntity) {
		// TODO Auto-generated method stub
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameVi() {
		return nameVi;
	}

	public void setNameVi(String nameVi) {
		this.nameVi = nameVi;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptionVi() {
		return descriptionVi;
	}

	public void setDescriptionVi(String descriptionVi) {
		this.descriptionVi = descriptionVi;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNoteVi() {
		return noteVi;
	}

	public void setNoteVi(String noteVi) {
		this.noteVi = noteVi;
	}

	public String getNoteEn() {
		return noteEn;
	}

	public void setNoteEn(String noteEn) {
		this.noteEn = noteEn;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(List<Apartment> apartments) {
		this.apartments = apartments;
	}

	
}
