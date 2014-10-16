package com.ps.models;

public class InterestsDTO {
	private String interestId = "";
	private String interestIcon = "";
	private String interestName = "";
	private Boolean isSelected = false;

	public String getInterestId() {
		return interestId;
	}

	public void setInterestId(String interestId) {
		this.interestId = interestId;
	}

	public String getInterestIcon() {
		return interestIcon;
	}

	public void setInterestIcon(String interestIcon) {
		this.interestIcon = interestIcon;
	}

	public String getInterestName() {
		return interestName;
	}

	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
}
