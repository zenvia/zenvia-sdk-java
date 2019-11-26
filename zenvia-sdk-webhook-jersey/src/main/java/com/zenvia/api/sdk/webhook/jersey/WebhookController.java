package com.zenvia.api.sdk.webhook.jersey;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zenvia.api.sdk.client.AbstractClient;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.subscriptions.EventType;
import com.zenvia.api.sdk.webhook.AbstractWebhookController;
import com.zenvia.api.sdk.webhook.Event;
import com.zenvia.api.sdk.webhook.MessageEventCallback;
import com.zenvia.api.sdk.webhook.MessageStatusEventCallback;

/** WebhookController that is backed by <a href="https://eclipse-ee4j.github.io/jersey/" target="_blank">Jersey</a>.
 *
 *  @since 1.1.0 */
public class WebhookController extends AbstractWebhookController {
	
	private static final Logger LOG = LoggerFactory.getLogger( WebhookController.class );

	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE}
	 *  events using the given configurations and default root
	 *  path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *  
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventHandler
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @since 1.1.0 */
	public WebhookController(ResourceConfig resourceConfig, MessageEventCallback messageEventHandler) {
		this(resourceConfig, messageEventHandler, null, null);
	}

	/** Creates the {@link WebhookController} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations
	 *  and default root path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventHandler
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @param messageStatusEventHandler
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler
	) {
		this(resourceConfig, messageEventHandler, messageStatusEventHandler, null, null, null, null);
	}

	/** Creates the {@link WebhookController} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations.
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventHandler
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @param messageStatusEventHandler
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *
	 *  @param path
	 *  Identifies the URI path to serve requests for webhook call.
	 *  When null, the default {@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH} will be used instead.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler,
		String path
	) {
		this(resourceConfig, messageEventHandler, messageStatusEventHandler, path, null, null, null);
	}

	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE} events
	 *  using the given configurations.
	 *  
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventHandler
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *  
	 *  @param path
	 *  Identifies the URI path to serve requests for webhook call.
	 *  When null, the default {@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH} will be used instead.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageEventCallback messageEventHandler,
		String path
	) {
		this(resourceConfig, messageEventHandler, null, path, null, null, null);
	}
	
	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE_STATUS} events
	 *  using the given configurations and default root
	 *  path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *  
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageStatusEventHandler
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *  
	 *  @since 1.1.0 */
	public WebhookController(ResourceConfig resourceConfig,
		MessageStatusEventCallback messageStatusEventHandler
	) {
		this(resourceConfig, null, messageStatusEventHandler, null);
	}

	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE_STATUS} events
	 *  using the given configurations.
	 *  
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageStatusEventHandler
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *  
	 *  @param path
	 *  Identifies the URI path to serve requests for webhook call.
	 *  When null, the default {@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH} will be used instead.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageStatusEventCallback messageStatusEventHandler,
		String path
	) {
		this(resourceConfig, null, messageStatusEventHandler, path);
	}

	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE} events
	 *  using the given configurations and default root
	 *  path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *  
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventHandler
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @param client
	 *  Zenvia API Client to automatically creates a subscription.
	 *
	 *  @param url
	 *  URL to be used in subscription creation.
	 *  
	 *  @param channel
	 *  {@link ChannelType} to trigger the callbacks for subscription to be created.
	 *
	 *  @since 1.1.0 */	
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageEventCallback messageEventHandler,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(resourceConfig, messageEventHandler, null, null, client, url, channel);
	}

	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE_STATUS} events
	 *  using the given configurations and default root 
	 *  path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *  
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageStatusEventHandler
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *
	 *  @param client
	 *  Zenvia API Client to automatically creates a subscription.
	 *
	 *  @param url
	 *  URL to be used in subscription creation.
	 *  
	 *  @param channel
	 *  {@link ChannelType} to trigger the callbacks for subscription to be created.
	 *
	 *  @since 1.1.0 */	
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageStatusEventCallback messageStatusEventHandler,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(resourceConfig, null, messageStatusEventHandler, null, client, url, channel);
	}

	/** Creates the {@link WebhookController} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations and
	 *  default root path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *  
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventHandler
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @param messageStatusEventHandler
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *
	 *  @param client
	 *  Zenvia API Client to automatically creates a subscription.
	 *
	 *  @param url
	 *  URL to be used in subscription creation.
	 *  
	 *  @param channel
	 *  {@link ChannelType} to trigger the callbacks for subscription to be created.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(resourceConfig, messageEventHandler, messageStatusEventHandler, null, client, url, channel);
	}

	/** Creates the {@link WebhookController} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations.
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventHandler
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @param messageStatusEventHandler
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *
	 *  @param path
	 *  Identifies the URI path to serve requests for webhook call.
	 *  When null, the default {@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH} will be used instead.
	 *
	 *  @param client
	 *  Zenvia API Client to automatically creates a subscription.
	 *
	 *  @param url
	 *  URL to be used in subscription creation.
	 *  
	 *  @param channel
	 *  {@link ChannelType} to trigger the callbacks for subscription to be created.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler,
		String path,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		super(messageEventHandler, messageStatusEventHandler, path, client, url, channel);
		this.create(resourceConfig, this.path);
	}

	private void create(ResourceConfig resourceConfig, String path) {
		final Resource.Builder resourceBuilder = Resource.builder(path);

		resourceBuilder.addMethod("POST").handledBy(new Inflector<ContainerRequestContext, Response>() {
			@Override
			public Response apply(ContainerRequestContext data) {
				Event event = ((ContainerRequest) data).readEntity(Event.class);
				switch (event.type) {
				case MESSAGE:
					if (messageEventHandler != null) {
						try {
							messageEventHandler.onMessageEvent(event.ofMessage());
						} catch (Exception e) {
							LOG.warn("Error on handling MESSAGE event", e);
						}
					}
					break;

				case MESSAGE_STATUS:
					if (messageStatusEventHandler != null) {
						try {
							messageStatusEventHandler.onMessageStatusEvent(event.ofMessageStatus());
						} catch( Exception e) {
							LOG.warn("Error on handling MESSAGE_STATUS event", e);
						}
					}
					break;
				
				default:
					break;
				}
				return Response.ok().build();
			}
		});

		resourceConfig.registerResources(resourceBuilder.build());
	}
	
}
