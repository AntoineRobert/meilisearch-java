package com.meilisearch.sdk.annotations;

public class MeiliPropertyConfig {

	private boolean isKey;
	private String identifier;
	private String fieldName;
	private String methodName;

	public MeiliPropertyConfig(boolean isKey, String identifier) {
		super();
		this.isKey = isKey;
		this.identifier = identifier;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getMethodName() {
		return methodName;
	}

	public boolean isKey() {
		return isKey;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}
