package com.hapinistay.backend.dto;

public class UserRatingDto {

	private Double averageRating;
	private Integer numOfRating;
	
	
	public Double getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}
	public Integer getNumOfRating() {
		return numOfRating;
	}
	public void setNumOfRating(Integer numOfRating) {
		this.numOfRating = numOfRating;
	}
}
