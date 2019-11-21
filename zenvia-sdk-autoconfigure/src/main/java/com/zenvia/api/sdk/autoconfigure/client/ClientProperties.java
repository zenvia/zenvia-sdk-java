package com.zenvia.api.sdk.autoconfigure.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zenvia.api.sdk.client")
public class ClientProperties {

	private String apiToken;
	private String apiUrl;
	private Integer maxConnections;
	private Integer connectionTimeout;
	private Integer socketTimeout;
	private Integer maxConnectionRetries;
	private Integer connectionPoolTimeout;
	private Integer inactivityTimeBeforeStaleCheck;

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public Integer getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Integer getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(Integer socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public Integer getMaxConnectionRetries() {
		return maxConnectionRetries;
	}

	public void setMaxConnectionRetries(Integer maxConnectionRetries) {
		this.maxConnectionRetries = maxConnectionRetries;
	}

	public Integer getConnectionPoolTimeout() {
		return connectionPoolTimeout;
	}

	public void setConnectionPoolTimeout(Integer connectionPoolTimeout) {
		this.connectionPoolTimeout = connectionPoolTimeout;
	}

	public Integer getInactivityTimeBeforeStaleCheck() {
		return inactivityTimeBeforeStaleCheck;
	}

	public void setInactivityTimeBeforeStaleCheck(Integer inactivityTimeBeforeStaleCheck) {
		this.inactivityTimeBeforeStaleCheck = inactivityTimeBeforeStaleCheck;
	}

}
