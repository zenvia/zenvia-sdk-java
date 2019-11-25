package com.zenvia.api.sdk.autoconfigure.webhook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.lang.reflect.Field;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zenvia.api.sdk.autoconfigure.client.ClientApacheAutoConfiguration;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.webhook.AbstractWebhookController;
import com.zenvia.api.sdk.webhook.MessageEvent;
import com.zenvia.api.sdk.webhook.MessageEventCallback;
import com.zenvia.api.sdk.webhook.MessageStatusEvent;
import com.zenvia.api.sdk.webhook.MessageStatusEventCallback;
import com.zenvia.api.sdk.webhook.jersey.WebhookController;

public class WebhookControllerJerseyAutoConfigurationTest {
	
	private final Field messageEventHandlerField;
	private final Field messageStatusEventHandlerField;
	private final Field pathField;
	private final Field clientField;
	private final Field urlField;
	private final Field channelField;

	private static MessageEventCallback messageEventHandler = new MessageEventCallback() {
		public void onMessageEvent(MessageEvent message) {}
	};
	
	private static MessageStatusEventCallback messageStatusEventHandler = new MessageStatusEventCallback() {
		public void onMessageStatusEvent(MessageStatusEvent status) {}
	};

	public WebhookControllerJerseyAutoConfigurationTest() throws Exception {
		messageEventHandlerField = AbstractWebhookController.class.getDeclaredField( "messageEventHandler" );
		messageEventHandlerField.setAccessible( true );
		
		messageStatusEventHandlerField = AbstractWebhookController.class.getDeclaredField( "messageStatusEventHandler" );
		messageStatusEventHandlerField.setAccessible( true );
		
		pathField = AbstractWebhookController.class.getDeclaredField( "path" );
		pathField.setAccessible( true );
		
		clientField = AbstractWebhookController.class.getDeclaredField( "client" );
		clientField.setAccessible( true );
		
		urlField = AbstractWebhookController.class.getDeclaredField( "url" );
		urlField.setAccessible( true );
		
		channelField = AbstractWebhookController.class.getDeclaredField( "channel" );
		channelField.setAccessible( true );
	}

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(ClientApacheAutoConfiguration.class, WebhookControllerJerseyAutoConfiguration.class));

	@Test
	void testWhenThereIsNoEventCallbacksOnClasspathTheWebhookControllerShouldNotBeCreated() {
		this.contextRunner.withUserConfiguration(ResourceConfig.class).run((context) -> {
			assertThat(catchThrowable(() -> { context.getBean(WebhookController.class); }))
				.isInstanceOf(NoSuchBeanDefinitionException.class)
				.hasMessageContaining("No qualifying bean of type 'com.zenvia.api.sdk.webhook.jersey.WebhookController' available");
		});
	}

	@Test
	void testWhenThereIsNoResourceConfigOnClasspathTheResourceConfigAndWebhookControllerShouldBeCreated() {
		this.contextRunner.withUserConfiguration(MessageEventCallbackConfiguration.class).run((context) -> {
			WebhookController controller = context.getBean(WebhookController.class);
			assertThat(controller).isNotNull();
			assertThat(messageEventHandlerField.get(controller)).isEqualTo(messageEventHandler);
			assertThat(pathField.get(controller)).isEqualTo(AbstractWebhookController.DEFAULT_PATH);
			assertThat(messageStatusEventHandlerField.get(controller)).isNull();
			assertThat(clientField.get(controller)).isNull();
			assertThat(urlField.get(controller)).isNull();
			assertThat(channelField.get(controller)).isNull();
		});
	}

	@Test
	void testWhenHasMessageEventCallbackOnClasspathTheWebhookControllerShouldBeCreated() {
		this.contextRunner.withUserConfiguration(ResourceConfig.class, MessageEventCallbackConfiguration.class).run((context) -> {
			WebhookController controller = context.getBean(WebhookController.class);
			assertThat(controller).isNotNull();
			assertThat(messageEventHandlerField.get(controller)).isEqualTo(messageEventHandler);
			assertThat(pathField.get(controller)).isEqualTo(AbstractWebhookController.DEFAULT_PATH);
			assertThat(messageStatusEventHandlerField.get(controller)).isNull();
			assertThat(clientField.get(controller)).isNull();
			assertThat(urlField.get(controller)).isNull();
			assertThat(channelField.get(controller)).isNull();
		});
	}

	@Test
	void testWhenHasMessageStatusEventCallbackOnClasspathTheWebhookControllerShouldBeCreated() {
		this.contextRunner.withUserConfiguration(ResourceConfig.class, MessageStatusEventCallbackConfiguration.class).run((context) -> {
			WebhookController controller = context.getBean(WebhookController.class);
			assertThat(controller).isNotNull();
			assertThat(messageStatusEventHandlerField.get(controller)).isEqualTo(messageStatusEventHandler);
			assertThat(pathField.get(controller)).isEqualTo(AbstractWebhookController.DEFAULT_PATH);
			assertThat(messageEventHandlerField.get(controller)).isNull();
			assertThat(clientField.get(controller)).isNull();
			assertThat(urlField.get(controller)).isNull();
			assertThat(channelField.get(controller)).isNull();
		});
	}

	@Test
	void testWhenHasCallbacksOnClasspathTheWebhookControllerShouldBeCreated() {
		this.contextRunner.withUserConfiguration(ResourceConfig.class, MessageEventCallbackConfiguration.class, MessageStatusEventCallbackConfiguration.class).run((context) -> {
			WebhookController controller = context.getBean(WebhookController.class);
			assertThat(controller).isNotNull();
			assertThat(messageEventHandlerField.get(controller)).isEqualTo(messageEventHandler);
			assertThat(messageStatusEventHandlerField.get(controller)).isEqualTo(messageStatusEventHandler);
			assertThat(pathField.get(controller)).isEqualTo(AbstractWebhookController.DEFAULT_PATH);
			assertThat(clientField.get(controller)).isNull();
			assertThat(urlField.get(controller)).isNull();
			assertThat(channelField.get(controller)).isNull();
		});
	}

	@Test
	void testWhenHasClientOnClasspathTheWebhookControllerShouldBeCreated() {
		this.contextRunner
			.withUserConfiguration(
				ResourceConfig.class,
				MessageEventCallbackConfiguration.class
			)
			.withPropertyValues("zenvia.api.sdk.client.apiToken:foobar")
			.run((context) -> {
				WebhookController controller = context.getBean(WebhookController.class);
				assertThat(controller).isNotNull();
				assertThat(messageEventHandlerField.get(controller)).isEqualTo(messageEventHandler);
				assertThat(pathField.get(controller)).isEqualTo(AbstractWebhookController.DEFAULT_PATH);
				assertThat(clientField.get(controller)).isNotNull();
				assertThat(messageStatusEventHandlerField.get(controller)).isNull();
				assertThat(urlField.get(controller)).isNull();
				assertThat(channelField.get(controller)).isNull();
			});
	}

	@Test
	void testWhenHasClientOnClasspathAndUrlAndChannelOnPropertiesTheWebhookControllerShouldBeCreated() {
		this.contextRunner
			.withUserConfiguration(
				ResourceConfig.class,
				MessageEventCallbackConfiguration.class
			)
			.withPropertyValues(
				"zenvia.api.sdk.client.apiToken:foobar",
				"zenvia.api.sdk.webhook.url:http://some-webhook.com",
				"zenvia.api.sdk.webhook.channel:sms"
			)
			.run((context) -> {
				WebhookController controller = context.getBean(WebhookController.class);
				assertThat(controller).isNotNull();
				assertThat(messageEventHandlerField.get(controller)).isEqualTo(messageEventHandler);
				assertThat(pathField.get(controller)).isEqualTo(AbstractWebhookController.DEFAULT_PATH);
				assertThat(clientField.get(controller)).isNotNull();
				assertThat(urlField.get(controller)).isEqualTo("http://some-webhook.com");
				assertThat(channelField.get(controller)).isEqualTo(ChannelType.sms);
				assertThat(messageStatusEventHandlerField.get(controller)).isNull();
			});
	}

	@Test
	void testWhenHasPathOnPropertiesTheWebhookControllerShouldBeCreated() {
		this.contextRunner
			.withUserConfiguration(
				ResourceConfig.class,
				MessageEventCallbackConfiguration.class
			)
			.withPropertyValues("zenvia.api.sdk.webhook.path:/some-path")
			.run((context) -> {
				WebhookController controller = context.getBean(WebhookController.class);
				assertThat(controller).isNotNull();
				assertThat(messageEventHandlerField.get(controller)).isEqualTo(messageEventHandler);
				assertThat(pathField.get(controller)).isEqualTo("/some-path");
				assertThat(messageStatusEventHandlerField.get(controller)).isNull();
				assertThat(clientField.get(controller)).isNull();
				assertThat(urlField.get(controller)).isNull();
				assertThat(channelField.get(controller)).isNull();
			});
	}

	@Configuration()
	static class MessageEventCallbackConfiguration {
		@Bean
		MessageEventCallback createMessageEvent() {
			return messageEventHandler;
		}
	}

	@Configuration()
	static class MessageStatusEventCallbackConfiguration {
		@Bean
		MessageStatusEventCallback createMessageStatusEvent() {
			return messageStatusEventHandler;
		}
	}

}
