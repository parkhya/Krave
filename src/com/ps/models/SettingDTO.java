package com.ps.models;

import java.util.List;

public class SettingDTO {
	private String userID;
	private String minAge="10";
	private String maxAge="75";
	private String searchRadius="500";
	private String isNotificationEnable="1";
	private String interest[];
	private List<InterestsDTO> selectedInterestList;
	private List<WhatAreYouDataDTO> whatAreYouDataDTOs ;
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getMinAge() {
		return minAge;
	}

	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	public String getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}

	public String getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(String searchRadius) {
		this.searchRadius = searchRadius;
	}

	public String getIsNotificationEnable() {
		return isNotificationEnable;
	}

	public void setIsNotificationEnable(String isNotificationEnable) {
		this.isNotificationEnable = isNotificationEnable;
	}

	public String[] getInterest() {
		return interest;
	}

	public void setInterest(String interest[]) {
		this.interest = interest;
	}

	public List<InterestsDTO> getSelectedInterestList() {
		return selectedInterestList;
	}

	public void setSelectedInterestList(List<InterestsDTO> selectedInterestList) {
		this.selectedInterestList = selectedInterestList;
	}

	public List<WhatAreYouDataDTO> getWhatAreYouDataDTOs() {
		return whatAreYouDataDTOs;
	}

	public void setWhatAreYouDataDTOs(List<WhatAreYouDataDTO> whatAreYouDataDTOs) {
		this.whatAreYouDataDTOs = whatAreYouDataDTOs;
	}

}
