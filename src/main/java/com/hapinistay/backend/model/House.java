package com.hapinistay.backend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hapinistay.backend.util.Constants;

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name = "House.findBySearchTermNamedWithDescriptionOrName",
			query = "SELECT t FROM House t WHERE" + 
					"    LOWER(t.nameVi) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR" + 
					"    LOWER(t.nameEn) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR" + 
					"    LOWER(t.descriptionVi) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR" + 
					"    LOWER(t.descriptionEn) LIKE LOWER(CONCAT('%',:searchTerm, '%'))"
	),
	@NamedQuery(name = "House.findByDistrictCode",
	query = "SELECT r FROM House r JOIN r.address a WHERE" + 
			"    LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%'))"
	),
	@NamedQuery(name = "House.findNewHousesByDistrictCodeAndStatus",
	query = "SELECT r FROM House r JOIN r.address a WHERE" + 
			"    LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%')) AND" + 
			"    LOWER(r.status) LIKE LOWER(CONCAT('%',:status, '%'))"
	),
	@NamedQuery(name = "House.findByDistrictCodeWithAvailableRoom",
	query = "SELECT h FROM House h LEFT OUTER JOIN h.address a LEFT OUTER JOIN h.rooms r WHERE" + 
			"    LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%'))" +
			"    AND EXISTS (SELECT t from Room t where t = r AND t.available = :available)" +
			"	 GROUP BY h.id"
	),
	@NamedQuery(name = "House.findByDistrictCodeWithAvailableRoomSortByPrice",
	query = "SELECT h, MIN(r.price) as rPrice FROM House h JOIN h.address a JOIN h.rooms r WHERE" + 
			"    LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%'))" +
			"    AND EXISTS (SELECT t from Room t where t = r AND t.available = :available AND t.status = 'APPROVE')" + 
			"    AND h.status = 'APPROVE' " + 
			"	 GROUP BY h.id" + 
			"	 ORDER BY rPrice ASC"
	),
	@NamedQuery(name = "House.findByDistrictCodeWithAvailableRoomSortByLastDate",
	query = "SELECT h, MAX(r.lastUpdate) as rLastUpdate FROM House h JOIN h.address a JOIN h.rooms r WHERE" + 
			"    LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%'))" +
			"    AND EXISTS (SELECT t from Room t where t = r AND t.available = :available AND t.status = 'APPROVE')" + 
			"    AND h.status = 'APPROVE' " + 
			"	 GROUP BY h.id" +
			"	 ORDER BY rLastUpdate DESC"
	),
	@NamedQuery(name = "House.findHousesByAvailableAndStatus",
	query = "SELECT h, MAX(r.lastUpdate) as rLastUpdate FROM House h JOIN h.rooms r WHERE" + 
			"    LOWER(h.status) LIKE LOWER(CONCAT('%',:houseStatus, '%'))" +
			"    AND EXISTS (SELECT t from Room t where t = r AND t.available = :available AND t.level = 2 AND LOWER(t.status) LIKE LOWER(CONCAT('%',:roomStatus, '%')))" +
			"    AND h.status = 'APPROVE' " + 
			"	 GROUP BY h.id" +
			"	 ORDER BY rLastUpdate DESC"
	),
	@NamedQuery(name = "House.findAllWithAvailableRoomSortByLevelLastDate",
	query = "SELECT h, MAX(r.lastUpdate) as rLastUpdate, MAX(r.level) as rLevel FROM House h JOIN h.address a JOIN h.rooms r WHERE" + 
			"    LOWER(a.district) LIKE LOWER(CONCAT('%',:districtCode, '%'))" +
			"    AND EXISTS (SELECT t from Room t where t = r AND t.available = :available AND t.status = 'APPROVE' AND t.level > 0)" + 
			"    AND h.status = 'APPROVE' " + 
			"	 GROUP BY h.id" +
			"	 ORDER BY rLevel DESC, rLastUpdate DESC"
	),
})

@Entity
@Table(name="house")
public class House extends AbstractMultilangSupport<House>  implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	//@JsonIgnore
	@Column(name="name_vi")
	private String nameVi;
	
	//@JsonIgnore
	@Column(name="name_en")
	private String nameEn;
	
	@Transient
	private String name;
	
	//@JsonIgnore
	@Column(name="description_vi", length=2000)
	@Size(max=2000)
	private String descriptionVi;
	
	//@JsonIgnore
	@Column(name="description_en", length=2000)
	@Size(max=2000)
	private String descriptionEn;
	
	@Column(name="status")
	private String status;
	
	@Column(name="lastUpdate")
	private Date lastUpdate;
	
	@Transient
	private String description;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="address_id")
	private Address address;
	
	@OneToMany(mappedBy="house", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Image> images = new HashSet<Image>();
	
	@OneToMany(mappedBy="house", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Comment> comments = new HashSet<Comment>();;
	
	@Column(name="avatar", length=2000)
	private String avatar;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="facility_id")
	private Facility facility;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="policy_id")
	private Policy policy;
	
	@OneToMany(mappedBy="house", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Room> rooms = new ArrayList<Room>();
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="contact_id")
	private Contact contact;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name ="user_id")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Facility getFacility() {
		return facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getNameVi() {
		return nameVi;
	}

	public void setNameVi(String nameVi) {
		this.nameVi = nameVi;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public void getLanguageDto(String lang) {
		if(lang != null & Constants.LANG_EN.equals(lang)) {
			this.name = this.nameEn;
			this.description = this.descriptionEn;
		} else {
			this.name = this.nameVi;
			this.description = this.descriptionVi;
		}
		if(this.rooms != null && this.rooms.size() > 0) {
			this.rooms.forEach(e -> {
				e.getLanguageDto(lang);
			});
		}
		if(this.facility != null) {
			this.facility.getLanguageDto(lang);
		}
		if(this.address != null) {
			this.address.getLanguageDto(lang);
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
		if(this.rooms != null && this.rooms.size() > 0) {
			this.rooms.forEach(e -> {
				e.setLanguageDto(lang);
			});
		}
		if(this.facility != null) {
			this.facility.setLanguageDto(lang);
		}
		if(this.address != null) {
			this.address.setLanguageDto(lang);
		}
	}
	
	@Override
	public void setLanguageDto(String lang, House oldRoom) {
		if(lang != null & Constants.LANG_EN.equals(lang)) {
			this.nameEn = this.name;
			this.descriptionEn = this.description;
			
			this.nameVi = oldRoom.getNameVi();
			this.descriptionVi = oldRoom.getDescriptionVi();
		} else {
			this.nameVi = this.name;
			this.descriptionVi = this.description;
			
			this.nameEn = oldRoom.getNameEn();
			this.descriptionEn = oldRoom.getDescriptionEn();
		}
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}