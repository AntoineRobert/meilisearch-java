package com.meilisearch.sdk.utils;

import com.meilisearch.sdk.annotations.MeiliProperty;

public class NamebleEntity extends IdeableEntity {

	private String name;

	@MeiliProperty(identifier = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
