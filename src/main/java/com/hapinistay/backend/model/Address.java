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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hapinistay.backend.util.Constants;

@SuppressWarnings("serial")
@Entity
@Table(name="address")
public class Address extends AbstractMultilangSupport<Address> implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="streetNumber")
	private String streetNumber;
	
	@Column(name="streetName_vi")
	private String streetNameVi;
	
	@Column(name="streetName_en")
	private String streetNameEn;
	
	@Transient
	private String streetName;
	
	@Column(name="ward")
	private String ward;
	
	@Column(name="district")
	private String district;
	
	@Column(name="city")
	private String city;
	
	@Column(name="province")
	private String province;
	
	@Column(name="latitude")
	private Double latitude;
	
	@Column(name="longitude")
	private Double longitude;
	
	@Transient
	private String wardName;
	
	@Transient
	private String districtName;
	
	@Transient
	private String cityName;
	
	//@OneToOne()
	//@MapsId //=> if using this way, can not use @GeneratedValue in ID
    //@JoinColumn(name="room_id")
	@JsonIgnore
	private House house;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public House getHouse() {
		return house;
	}

	public void setRHouse(House house) {
		this.house = house;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStreetNameVi() {
		return streetNameVi;
	}

	public void setStreetNameVi(String streetNameVi) {
		this.streetNameVi = streetNameVi;
	}

	public String getStreetNameEn() {
		return streetNameEn;
	}

	public void setStreetNameEn(String streetNameEn) {
		this.streetNameEn = streetNameEn;
	}

	@Override
	public void getLanguageDto(String lang) {
		if(lang != null & Constants.LANG_EN.equals(lang)) {
			this.streetName = this.streetNameEn;
		} else {
			this.streetName = this.streetNameVi;
		}
		
	}

	@Override
	public void setLanguageDto(String lang) {
		if(lang != null & Constants.LANG_EN.equals(lang)) {
			this.streetNameEn = this.streetName;
		} else {
			this.streetNameVi = this.streetName;
		}
		
	}

	@Override
	public void setLanguageDto(String lang, Address oldEntity) {
		// TODO Auto-generated method stub
		
	}
	
}