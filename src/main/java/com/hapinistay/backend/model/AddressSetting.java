package com.hapinistay.backend.model;

import java.io.Serializable;
import java.util.Set;

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
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hapinistay.backend.util.Constants;

@SuppressWarnings("serial")
@Entity
@Table(name="address_setting")
public class AddressSetting extends AbstractMultilangSupport<AddressSetting>  implements Serializable {

	
	
	public AddressSetting() {
	}

	public AddressSetting(String code, String name,String description,  String type, String cityCode, String districtCode,
			String wardCode) {
		this.code = code;
		this.name = name;
		this.type = type;
		this.cityCode = cityCode;
		this.districtCode = districtCode;
		this.wardCode = wardCode;
		this.description = description;
	}

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="code")
	private String code;
	
	@Column(name="name_vi")
	private String nameVi;
	
	@Column(name="name_en")
	private String nameEn;
	
	@Transient
	private String name;
	
	@Column(name="description_vi")
	private String descriptionVi;
	
	@Column(name="description_en")
	private String descriptionEn;
	
	@Transient
	private String description;
	
	@Column(name="type")
	private String type;
	
	@Column(name="cityCode")
	private String cityCode;
	
	@Column(name="districtCode")
	private String districtCode;
	
	@Column(name="wardCode")
	private String wardCode;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	@Override
	public void getLanguageDto(String lang) {
		if(lang != null & Constants.LANG_EN.equals(lang)) {
			this.name = this.nameEn;
			this.description = this.descriptionEn;
		} else {
			this.name = this.nameVi;
			this.description = this.descriptionEn;
		}
		
	}

	@Override
	public void setLanguageDto(String lang) {
		if(lang != null & Constants.LANG_EN.equals(lang)) {
			this.nameEn = this.name;
			this.descriptionEn = this.description;
		} else {
			this.nameVi = this.name;
			this.descriptionVi = this.description;
		}
		
	}

	@Override
	public void setLanguageDto(String lang, AddressSetting oldEntity) {
		// TODO Auto-generated method stub
		
	}

	public String getNameVi() {
		return nameVi;
	}

	public void setNameVi(String nameVi) {
		this.nameVi = nameVi;
	}

	public String getDescriptionVi() {
		return descriptionVi;
	}

	public void setDescriptionVi(String descriptionVi) {
		this.descriptionVi = descriptionVi;
	}
	
}
