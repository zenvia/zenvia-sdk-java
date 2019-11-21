package com.zenvia.api.sdk.autoconfigure.webhook;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@ConditionalOnClass(WebhookController.class)
@ConditionalOnBean(ResourceConfig.class)
@Conditional(OnEventCallbacksCondition.class)
@AutoConfigureAfter({ ClientSpringAutoConfiguration.class, ClientApacheAutoConfiguration.class })
@EnableConfigurationProperties(WebhookProperties.class)
public class WebhookControllerJerseyAutoConfiguration {

	@Autowired
	private WebhookProperties webhookProperties;

	@Autowired
	private ResourceConfig resourceConfig;

	@Autowired(required = false)
	private MessageEventCallback messageEventCallback;

	@Autowired(required = false)
	private MessageStatusEventCallback messageStatusEventCallback;

	@Autowired(required = false)
	private AbstractClient client;

	@Bean
	@ConditionalOnMissingBean
	public WebhookController createWithClient() {
		ChannelType channel = null;
		if (this.webhookProperties.getChannel() != null) {
			channel = ChannelType.parse(this.webhookProperties.getChannel());
		}
		return new WebhookController(
    		resourceConfig,
    		messageEventCallback,
    		messageStatusEventCallback,
    		this.webhookProperties.getPath(),
    		client,
    		this.webhookProperties.getUrl(),
    		channel
    	);
	}

}
