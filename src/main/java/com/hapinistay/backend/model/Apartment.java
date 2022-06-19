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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hapinistay.backend.util.Constants;

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name = "Apartment.findByDistrictCode",
	query = "SELECT p FROM Apartment p JOIN p.address a WHERE" + 
			"    LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%'))"
	),
	@NamedQuery(name = "Apartment.findByDistrictCodeWithAvailableSortByPrice",
	query = "SELECT p, MIN(p.price) as pPrice FROM Apartment p JOIN p.address a WHERE" + 
			"    LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%'))" +
			"    AND (p.available = :available AND p.status = 'APPROVE')" + 
			"	 GROUP BY p.id" + 
			"	 ORDER BY pPrice ASC"
	),
	@NamedQuery(name = "Apartment.findByDistrictCodeWithAvailableSortByLastDate",
	query = "SELECT p, MAX(p.lastUpdate) as pLastUpdate FROM Apartment p JOIN p.address a WHERE" + 
			"    LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%'))" +
			"    AND ( p.available = :available AND p.status = 'APPROVE')" +  
			"	 GROUP BY p.id" +
			"	 ORDER BY pLastUpdate DESC"
	),
	@NamedQuery(name = "Apartment.findApartmentByAvailableAndStatus",
	query = "SELECT p, MAX(p.lastUpdate) as pLastUpdate FROM Apartment p JOIN p.address a WHERE" + 
			"    LOWER(p.status) LIKE LOWER(CONCAT('%',:status, '%'))" +
			"    AND LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%'))" +
			"    AND (p.available = :available AND LOWER(p.status) LIKE LOWER(CONCAT('%',:status, '%')))" +
			"	 GROUP BY p.id" +
			"	 ORDER BY pLastUpdate DESC"
	),
	@NamedQuery(name = "Apartment.findAllWithAvailableSortByLevelLastDate",
	query = "SELECT p, MAX(p.lastUpdate) as pLastUpdate, MAX(p.level) as pLevel FROM Apartment p JOIN p.address a WHERE" + 
			"    LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%'))" +
			"    AND (p.available = :available AND p.status = 'APPROVE' AND p.level > 0)" + 
			"	 GROUP BY p.id" +
			"	 ORDER BY pLevel DESC, pLastUpdate DESC"
	),
})
@Entity
@Table(name="apartment")
public class Apartment extends AbstractMultilangSupport<Apartment> implements Serializable {

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
	
	@Column(name="area")
	private Long area;
	
	@Column(name="price")
	private Long price;

	
	@Column(name="available")
	private Boolean available;
	
	@Column(name="level")
	private Long level;
	
	@Column(name="status")
	private String status;
	
	@Column(name="lastUpdate")
	private Date lastUpdate;
	
	@Column(name="bath")
	private Long bath;
	
	@Column(name="bed")
	private Long bed;
	
	@Column(name="maxNumber")
	private Long maxNumber;
	
	@Column(name="furniture")
	private Boolean furniture;
	
	@Column(name="tags", length=2000)
	private String tags;
	
	@Column(name="floor")
	private Long floor;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name ="user_id")
	private User user;
	
	@OneToMany(mappedBy="apartment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Image> images;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="address_id")
	private Address address;

	//@JsonIgnore
	@ManyToOne
	@JoinColumn(name ="building_id")
	private Building building;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="contact_id")
	private Contact contact;

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
		if(this.address != null) {
			this.address.getLanguageDto(lang);
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
		if(this.address != null) {
			this.address.setLanguageDto(lang);
		}
	}

	@Override
	public void setLanguageDto(String lang, Apartment oldEntity) {
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

	public Long getArea() {
		return area;
	}

	public void setArea(Long area) {
		this.area = area;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Long getBath() {
		return bath;
	}

	public void setBath(Long bath) {
		this.bath = bath;
	}

	public Long getBed() {
		return bed;
	}

	public void setBed(Long bed) {
		this.bed = bed;
	}

	public Long getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Long maxNumber) {
		this.maxNumber = maxNumber;
	}

	public Boolean getFurniture() {
		return furniture;
	}

	public void setFurniture(Boolean furniture) {
		this.furniture = furniture;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Long getFloor() {
		return floor;
	}

	public void setFloor(Long floor) {
		this.floor = floor;
	}
}
