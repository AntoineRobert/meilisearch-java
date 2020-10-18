package com.meilisearch.sdk.utils;

import java.math.BigDecimal;

import com.meilisearch.sdk.annotations.MeiliIndex;
import com.meilisearch.sdk.annotations.MeiliProperty;

@MeiliIndex(name = "toys")
public class Toys extends NamebleEntity {

	private BigDecimal price;

	@MeiliProperty(identifier = "price")
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
