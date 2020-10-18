package com.meilisearch.sdk.utils;

import java.util.Map;

import com.meilisearch.sdk.annotations.MeiliIndex;
import com.meilisearch.sdk.annotations.MeiliProperty;

@MeiliIndex(name = "owner")
public class Owner extends NamebleEntity {

	private Map<String, Pet> pets;
	private String phoneNumber;
	private String password;

	public String getPassword() {
		return password;
	}

	@MeiliProperty(identifier = "pets")
	public Map<String, Pet> getPets() {
		return pets;
	}

	@MeiliProperty(identifier = "phoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPets(Map<String, Pet> pets) {
		this.pets = pets;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
