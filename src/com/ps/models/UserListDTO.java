package com.ps.models;

import java.util.ArrayList;
import java.util.List;

public class UserListDTO {
	private String listId;
	private String listName;
	private List<UserDTO> dudeList = new ArrayList<UserDTO>();

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public List<UserDTO> getDudeList() {
		return dudeList;
	}

	public void setDudeList(List<UserDTO> dudeList) {
		this.dudeList = dudeList;
	}

}
