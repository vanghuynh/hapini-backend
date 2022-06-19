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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name = "Paper.findPapersByAvailable",
	query = "SELECT p, MAX(p.lastUpdate) as pLastUpdate FROM Paper p WHERE" + 
			"    p.available = :available" +
			"	 GROUP BY p.id" +
			"	 ORDER BY pLastUpdate DESC"
	)
})
@Entity
@Table(name="paper")
public class Paper  implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Column(name="title", columnDefinition="text")
	@Lob
	private String title;
	
	@Column(name="content", columnDefinition="text")
	@Lob
	private String content;
	
	@Column(name="description", columnDefinition="text")
	@Lob
	private String description;
	
	@OneToMany(mappedBy="paper", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Image> images;
	
	@Column(name="avatar", length=2000)
	private String avatar;
	
	@Column(name="lastUpdate")
	private Date lastUpdate;
	
	@Column(name="available")
	private Boolean available;
	
	@Column(name="tags", length=2000)
	private String tags;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
