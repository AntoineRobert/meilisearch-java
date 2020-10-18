package com.meilisearch.sdk.utils;

import java.util.Date;

import com.meilisearch.sdk.annotations.MeiliProperty;

public class IdeableEntity {

	private Long id = 1L;
	private Date dateCreated = new Date(System.currentTimeMillis() - 1000);
	private Date Updated = new Date(System.currentTimeMillis());

	@MeiliProperty(identifier = "Date created")
	public Date getDateCreated() {
		return dateCreated;
	}

	@MeiliProperty(identifier = "Id", isKey = true)
	public Long getId() {
		return id;
	}

	public Date getUpdated() {
		return Updated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUpdated(Date updated) {
		Updated = updated;
	}

}
