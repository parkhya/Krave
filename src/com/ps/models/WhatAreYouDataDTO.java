package com.ps.models;

public class WhatAreYouDataDTO {
	private boolean Selected;
	private String Name="";
	private String id="";

	public boolean isSelected() {
		return Selected;
	}

	public void setSelected(boolean selected) {
		Selected = selected;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
