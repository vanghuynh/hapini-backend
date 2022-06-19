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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hapinistay.backend.util.Constants;

@SuppressWarnings("serial")
@Entity
@Table(name="facility")
public class Facility extends AbstractMultilangSupport<Facility> implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="electricity")
	private String electricity;
	
	@Column(name="water")
	private String water;
	
	@Column(name="note_en", length=2000)
	@Size(max=2000)
	private String noteEn;
	
	@Column(name="note_vi", length=2000)
	@Size(max=2000)
	private String noteVi;
	
	@Transient
	private String note;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getElectricity() {
		return electricity;
	}

	public void setElectricity(String electricity) {
		this.electricity = electricity;
	}

	public String getWater() {
		return water;
	}

	public void setWater(String water) {
		this.water = water;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNoteEn() {
		return noteEn;
	}

	public void setNoteEn(String noteEn) {
		this.noteEn = noteEn;
	}

	public String getNoteVi() {
		return noteVi;
	}

	public void setNoteVi(String noteVi) {
		this.noteVi = noteVi;
	}

	@Override
	public void getLanguageDto(String lang) {
		if(lang != null & Constants.LANG_EN.equals(lang)) {
			this.note = this.noteEn;
		} else {
			this.note = this.noteVi;
		}
		
	}

	@Override
	public void setLanguageDto(String lang) {
		if(lang != null & Constants.LANG_EN.equals(lang)) {
			this.setNoteEn(this.getNote());
		} else {
			this.setNoteVi(this.getNote());
		}
		
	}

	@Override
	public void setLanguageDto(String lang, Facility oldEntity) {
		// TODO Auto-generated method stub
		
	}
	
	
}