package dk.mikkelrj.confluence.rest.core.impl;

import java.util.Collections;
import java.util.Map;

public class AuthProperties {

	private Map<String, String> properties;

	public AuthProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String get(String key) {
		return properties.get(key);
	}

	public String set(String key, String value) {
		return properties.put(key, value);
	}

	public Map<String, String> toMap() {
		return Collections.unmodifiableMap(properties);
	}

}
