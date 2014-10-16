package com.ps.models;

public class FacebookLikesDTO {
	private String likeId;
	private String imagePath;
	private String likeName;
	private String category;

	public String getLikeId() {
		return likeId;
	}

	public void setLikeId(String likeId) {
		this.likeId = likeId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getLikeName() {
		return likeName;
	}

	public void setLikeName(String likeName) {
		this.likeName = likeName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
