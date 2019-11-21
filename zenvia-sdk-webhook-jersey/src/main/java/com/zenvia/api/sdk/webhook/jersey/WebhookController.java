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
import com.zenvia.api.sdk.webhook.AbstractWebhookController;
import com.zenvia.api.sdk.webhook.Event;
import com.zenvia.api.sdk.webhook.MessageEventCallback;
import com.zenvia.api.sdk.webhook.MessageStatusEventCallback;

public class WebhookController extends AbstractWebhookController {
	
	private static final Logger LOG = LoggerFactory.getLogger( WebhookController.class );

	/** Creates the {@link WebhookControler} to receive only {@link EventType#MESSAGE}
	 *  events using the given configurations and below default configurations.
	 *  <br>
	 *  <ul>
	 *    <li><b>Path:</b> {@value #DEFAULT_PATH}</li>
	 *  </ul>
	 *  
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventCallback
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @since 1.1.0 */
	public WebhookController(ResourceConfig resourceConfig, MessageEventCallback messageEventCallback) {
		this(resourceConfig, messageEventCallback, null, null);
	}

	/** Creates the {@link WebhookControler} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations
	 *  and below default configurations.
	 *  <br>
	 *  <ul>
	 *    <li><b>Path:</b> {@value #DEFAULT_PATH}</li>
	 *  </ul>
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventCallback
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @param messageStatusEventCallback
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageEventCallback messageEventCallback,
		MessageStatusEventCallback messageStatusEventCallback
	) {
		this(resourceConfig, messageEventCallback, messageStatusEventCallback, null, null, null, null);
	}

	/** Creates the {@link WebhookControler} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations.
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventCallback
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @param messageStatusEventCallback
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *
	 *  @param path
	 *  Identifies the URI path to serve requests for webhook call.
	 *  When null, the default {@value #DEFAULT_PATH} will be used instead.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageEventCallback messageEventCallback,
		MessageStatusEventCallback messageStatusEventCallback,
		String path
	) {
		this(resourceConfig, messageEventCallback, messageStatusEventCallback, path, null, null, null);
	}

	/** Creates the {@link WebhookControler} to receive only {@link EventType#MESSAGE} events
	 *  using the given configurations.
	 *  
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventCallback
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *  
	 *  @param path
	 *  Identifies the URI path to serve requests for webhook call.
	 *  When null, the default {@value #DEFAULT_PATH} will be used instead.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageEventCallback messageEventCallback,
		String path
	) {
		this(resourceConfig, messageEventCallback, null, path, null, null, null);
	}
	
	/** Creates the {@link WebhookControler} to receive only {@link EventType#MESSAGE_STATUS} events
	 *  using the given configurations and below default configurations.
	 *  <br>
	 *  <ul>
	 *    <li><b>Path:</b> {@value #DEFAULT_PATH}</li>
	 *  </ul>
	 *  
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageStatusEventCallback
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *  
	 *  @since 1.1.0 */
	public WebhookController(ResourceConfig resourceConfig,
		MessageStatusEventCallback messageStatusEventCallback
	) {
		this(resourceConfig, null, messageStatusEventCallback, null);
	}

	/** Creates the {@link WebhookControler} to receive only {@link EventType#MESSAGE_STATUS} events
	 *  using the given configurations.
	 *  
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageStatusEventCallback
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *  
	 *  @param path
	 *  Identifies the URI path to serve requests for webhook call.
	 *  When null, the default {@value #DEFAULT_PATH} will be used instead.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		ResourceConfig resourceConfig,
		MessageStatusEventCallback messageStatusEventCallback,
		String path
	) {
		this(resourceConfig, null, messageStatusEventCallback, path);
	}

	/** Creates the {@link WebhookControler} to receive only {@link EventType#MESSAGE} events
	 *  using the given configurations and below default configurations.
	 *  <br>
	 *  <ul>
	 *    <li><b>Path:</b> {@value #DEFAULT_PATH}</li>
	 *  </ul>
	 *  
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventCallback
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
		MessageEventCallback messageEventCallback,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(resourceConfig, messageEventCallback, null, null, client, url, channel);
	}

	/** Creates the {@link WebhookControler} to receive only {@link EventType#MESSAGE_STATUS} events
	 *  using the given configurations and below default configurations.
	 *  <br>
	 *  <ul>
	 *    <li><b>Path:</b> {@value #DEFAULT_PATH}</li>
	 *  </ul>
	 *  
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageStatusEventCallback
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
		MessageStatusEventCallback messageStatusEventCallback,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(resourceConfig, null, messageStatusEventCallback, null, client, url, channel);
	}

	/** Creates the {@link WebhookControler} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations and 
	 *  below default configurations.
	 *  <br>
	 *  <ul>
	 *    <li><b>Path:</b> {@value #DEFAULT_PATH}</li>
	 *  </ul>
	 *  
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventCallback
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @param messageStatusEventCallback
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
		MessageEventCallback messageEventCallback,
		MessageStatusEventCallback messageStatusEventCallback,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(resourceConfig, messageEventCallback, messageStatusEventCallback, null, client, url, channel);
	}

	/** Creates the {@link WebhookControler} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations.
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param resourceConfig
	 *  The resource configuration to configure the webhook controller.
	 *
	 *  @param messageEventCallback
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @param messageStatusEventCallback
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *
	 *  @param path
	 *  Identifies the URI path to serve requests for webhook call.
	 *  When null, the default {@value #DEFAULT_PATH} will be used instead.
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
		MessageEventCallback messageEventCallback,
		MessageStatusEventCallback messageStatusEventCallback,
		String path,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		super(messageEventCallback, messageStatusEventCallback, path, client, url, channel);
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
					if (messageEventCallback != null) {
						try {
							messageEventCallback.onMessageEvent(event.ofMessage());
						} catch (Exception e) {
							LOG.warn("Error on handling MESSAGE event", e);
						}
					}
					break;

				case MESSAGE_STATUS:
					if (messageStatusEventCallback != null) {
						try {
							messageStatusEventCallback.onMessageStatusEvent(event.ofMessageStatus());
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
