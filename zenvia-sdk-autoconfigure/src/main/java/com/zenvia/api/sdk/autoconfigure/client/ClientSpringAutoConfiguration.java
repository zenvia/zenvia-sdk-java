package com.zenvia.api.sdk.autoconfigure.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zenvia.api.sdk.client.spring.Client;

@Configuration
@ConditionalOnClass(Client.class)
@EnableConfigurationProperties(ClientProperties.class)
public class ClientSpringAutoConfiguration {

	private final ClientProperties clientProperties;

	public ClientSpringAutoConfiguration(ClientProperties clientProperties) {
		this.clientProperties = clientProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty("zenvia.api.sdk.client.apiToken")
    public Client createClient() {
    	return new Client(
    		this.clientProperties.getApiToken(),
    		this.clientProperties.getApiUrl(),
    		this.clientProperties.getMaxConnections(),
    		this.clientProperties.getConnectionTimeout(),
    		this.clientProperties.getSocketTimeout(),
    		this.clientProperties.getMaxConnectionRetries(),
    		this.clientProperties.getConnectionPoolTimeout(),
    		this.clientProperties.getInactivityTimeBeforeStaleCheck()
    	);
    }

}
