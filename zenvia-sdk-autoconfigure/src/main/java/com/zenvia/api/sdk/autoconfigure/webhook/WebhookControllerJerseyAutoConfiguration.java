package com.zenvia.api.sdk.autoconfigure.webhook;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.zenvia.api.sdk.autoconfigure.client.ClientApacheAutoConfiguration;
import com.zenvia.api.sdk.autoconfigure.client.ClientSpringAutoConfiguration;
import com.zenvia.api.sdk.autoconfigure.webhook.conditions.OnEventCallbacksCondition;
import com.zenvia.api.sdk.client.AbstractClient;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.webhook.MessageEventCallback;
import com.zenvia.api.sdk.webhook.MessageStatusEventCallback;
import com.zenvia.api.sdk.webhook.jersey.WebhookController;

@Configuration
@ConditionalOnClass({ WebhookController.class, ResourceConfig.class })
@Conditional(OnEventCallbacksCondition.class)
@AutoConfigureAfter({ ClientSpringAutoConfiguration.class, ClientApacheAutoConfiguration.class })
@AutoConfigureBefore({ JerseyAutoConfiguration.class })
@EnableConfigurationProperties(WebhookProperties.class)
public class WebhookControllerJerseyAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public WebhookController createWebhookController(
		WebhookProperties webhookProperties,
		ResourceConfig resourceConfig,
		ObjectProvider<MessageEventCallback> messageEventHandler,
		ObjectProvider<MessageStatusEventCallback> messageStatusEventHandler,
		ObjectProvider<AbstractClient> client
	) {
		ChannelType channel = null;
		if (webhookProperties.getChannel() != null) {
			channel = ChannelType.parse(webhookProperties.getChannel());
		}
		WebhookController controller = new WebhookController(
			resourceConfig,
			messageEventHandler.getIfAvailable(),
			messageStatusEventHandler.getIfAvailable(),
			webhookProperties.getPath(),
			client.getIfAvailable(),
			webhookProperties.getUrl(),
			channel
		);
		controller.init();
		return controller;
	}

	@Bean
	@ConditionalOnMissingBean
	public ResourceConfig createResourceConfig() {
		return new ResourceConfig();
	}

}
