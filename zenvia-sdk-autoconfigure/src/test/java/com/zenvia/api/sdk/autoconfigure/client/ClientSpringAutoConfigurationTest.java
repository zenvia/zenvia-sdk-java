package com.zenvia.api.sdk.autoconfigure.client;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.zenvia.api.sdk.client.spring.Client;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ClientSpringAutoConfigurationTest {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(ClientSpringAutoConfiguration.class));

	@Test
	void testWhenThereIsNoApiTokenOnPropertiesTheClientShouldNotBeCreated() {
		this.contextRunner.run((context) -> {
			assertThat(catchThrowable(() -> { context.getBean(Client.class); }))
				.isInstanceOf(NoSuchBeanDefinitionException.class)
				.hasMessageContaining("No qualifying bean of type 'com.zenvia.api.sdk.client.spring.Client' available");
		});
	}

	@Test
	void testWhenThereIsOnlyApiTokenOnPropertiesTheClientShouldBeCreatedWithDefaultValues() {
		this.contextRunner.withPropertyValues("zenvia.api.sdk.client.apiToken:foobar").run((context) -> {
			Client client = context.getBean(Client.class);
			assertThat(client).isNotNull();
			assertThat(client.getApiUrl()).isEqualTo("https://api.zenvia.com");
			assertThat(client.getConnectionTimeout()).isEqualTo(25000);
			assertThat(client.getSocketTimeout()).isEqualTo(60000);
			assertThat(client.getMaxConnectionRetries()).isEqualTo(4);
			assertThat(client.getMaxConnections()).isEqualTo(100);
			assertThat(client.getConnectionPoolTimeout()).isEqualTo(0);
			assertThat(client.getInactivityTimeBeforeStaleCheck()).isEqualTo(5000);
		});
	}

	@Test
	void testWhenThereIsAllAvailableOnPropertiesTheClientShouldBeCreatedWithTheseValues() {
		this.contextRunner.withPropertyValues(
				"zenvia.api.sdk.client.apiToken:foobar",
				"zenvia.api.sdk.client.apiUrl:http://localhost",
				"zenvia.api.sdk.client.maxConnections:5",
				"zenvia.api.sdk.client.connectionTimeout:5",
				"zenvia.api.sdk.client.socketTimeout:5",
				"zenvia.api.sdk.client.maxConnectionRetries:5",
				"zenvia.api.sdk.client.connectionPoolTimeout:5",
				"zenvia.api.sdk.client.inactivityTimeBeforeStaleCheck:5"
			).run((context) -> {
			Client client = context.getBean(Client.class);
			assertThat(client).isNotNull();
			assertThat(client.getApiUrl()).isEqualTo("http://localhost");
			assertThat(client.getConnectionTimeout()).isEqualTo(5);
			assertThat(client.getSocketTimeout()).isEqualTo(5);
			assertThat(client.getMaxConnectionRetries()).isEqualTo(5);
			assertThat(client.getMaxConnections()).isEqualTo(5);
			assertThat(client.getConnectionPoolTimeout()).isEqualTo(5);
			assertThat(client.getInactivityTimeBeforeStaleCheck()).isEqualTo(5);
		});
	}

}
