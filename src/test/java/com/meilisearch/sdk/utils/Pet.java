package com.meilisearch.sdk.utils;

import java.util.List;

import com.meilisearch.sdk.annotations.MeiliIndex;
import com.meilisearch.sdk.annotations.MeiliProperty;

@MeiliIndex(name = "pet")
public class Pet extends NamebleEntity {

	private String type;
	private List<Toys> toys;

	@MeiliProperty(identifier = "Toys")
	public List<Toys> getToys() {
		return toys;
	}

	public String getType() {
		return type;
	}

	public void setToys(List<Toys> toys) {
		this.toys = toys;
	}

	public void setType(String type) {
		this.type = type;
	}

}
