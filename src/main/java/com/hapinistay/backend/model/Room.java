package com.hapinistay.backend.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hapinistay.backend.util.Constants;

@SuppressWarnings("serial")
@Entity
@Table(name="room")
public class Room extends AbstractMultilangSupport<Room> implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name_vi")
	private String nameVi;
	
	@Column(name="name_en")
	private String nameEn;
	
	@Transient
	private String name;

	@Column(name="area")
	private Long area;
	
	@Column(name="maxNumber")
	private Long maxNumber;
	
	@Column(name="toilet")
	private String toilet;
	
	@Column(name="sex")
	private String sex;
	
	@Column(name="price")
	private Long price;
	
	@Column(name="description_vi", length=2000)
	@Size(max=2000)
	private String descriptionVi;
	
	@Column(name="description_en", length=2000)
	@Size(max=2000)
	private String descriptionEn;
	
	@Transient
	private String description;
	
	@Column(name="note_vi", length=2000)
	@Size(max=2000)
	private String noteVi;
	
	@Column(name="note_en", length=2000)
	@Size(max=2000)
	private String noteEn;
	
	@Transient
	private String note;
	
	@Column(name="avatar", length=2000)
	private String avatar;
	
	@Column(name="available")
	private Boolean available;
	
	@Column(name="level")
	private Long level;
	
	@Column(name="status")
	private String status;
	
	@Column(name="lastUpdate")
	private Date lastUpdate;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name ="house_id")
	private House house;
	
	@OneToMany(mappedBy="room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Image> images;
	
	@Transient
	private Long houseId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getArea() {
		return area;
	}

	public void setArea(Long area) {
		this.area = area;
	}

	public Long getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Long maxNumber) {
		this.maxNumber = maxNumber;
	}

	public String getToilet() {
		return toilet;
	}

	public void setToilet(String toilet) {
		this.toilet = toilet;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
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

	public String getNoteEn() {
		return noteEn;
	}

	public void setNoteEn(String noteEn) {
		this.noteEn = noteEn;
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

	public String getNoteVi() {
		return noteVi;
	}

	public void setNoteVi(String noteVi) {
		this.noteVi = noteVi;
	}

	public String getStatus() {
		return status;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getHouseId() {
		return houseId;
	}

	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}

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
	public void setLanguageDto(String lang, Room oldEntity) {
		// TODO Auto-generated method stub
		
	}
}
