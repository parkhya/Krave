package com.ps.models;

import com.ps.utill.AppConstants;

public class WhatAreYouDTO {
	private String feet = "";
	private String inches = "";
	private String meters = "";
	private String hight = "";
	private String weight = "";
	private String heightUnit = AppConstants.HEIGHT_IN_FEET;
	private String weightUnit = AppConstants.WEIGHT_IN_KILOGRAM;
	private String age = "";
	private String relationshipStatus = "1";
	private String whatAreYou = "";
	private String whatAreYouName = "";
	private String whatDoYouKrave = "";
	private String interest = "";

	public String getFeet() {
		return feet;
	}

	public void setFeet(String feet) {
		this.feet = feet;
	}

	public String getInches() {
		return inches;
	}

	public void setInches(String inches) {
		this.inches = inches;
	}

	public String getMeters() {
		return meters;
	}

	public void setMeters(String meters) {
		this.meters = meters;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getRelationshipStatus() {
		return relationshipStatus;
	}

	public void setRelationshipStatus(String relationshipStatus) {
		this.relationshipStatus = relationshipStatus;
	}

	public String getWhatAreYou() {
		return whatAreYou;
	}

	public void setWhatAreYou(String whatAreYou) {
		this.whatAreYou = whatAreYou;
	}

	public String getWhatDoYouKrave() {
		return whatDoYouKrave;
	}

	public void setWhatDoYouKrave(String whatDoYouKrave) {
		this.whatDoYouKrave = whatDoYouKrave;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getHight() {
		return hight;
	}

	public void setHight(String hight) {
		this.hight = hight;
	}

	public String getHeightUnit() {
		return heightUnit;
	}

	public void setHeightUnit(String heightUnit) {
		this.heightUnit = heightUnit;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public String getWhatAreYouName() {
		return whatAreYouName;
	}

	public void setWhatAreYouName(String whatAreYouName) {
		this.whatAreYouName = whatAreYouName;
	}
}
