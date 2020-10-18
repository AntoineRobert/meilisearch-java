package com.meilisearch.sdk.annotations;

import java.util.HashMap;
import java.util.Map;

public class MeiliEntityConfig {

	private String uid;

	private Map<String, MeiliPropertyConfig> properties;

	public MeiliEntityConfig(String uid) {
		super();
		this.uid = uid;
	}

	public String getPrimaryKey() {
		for (MeiliPropertyConfig conf : getProperties().values()) {
			if (conf.isKey()) {
				return conf.getIdentifier();
			}
		}

		return null;
	}

	public Map<String, MeiliPropertyConfig> getProperties() {

		if (properties == null) {
			properties = new HashMap<String, MeiliPropertyConfig>();
		}

		return properties;
	}

	public String getUid() {
		return uid;
	}

	public void setProperties(Map<String, MeiliPropertyConfig> properties) {
		this.properties = properties;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
