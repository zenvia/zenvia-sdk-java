package com.zenvia.api.sdk.webhook.webmvc;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.zenvia.api.sdk.client.AbstractClient;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.subscriptions.EventType;
import com.zenvia.api.sdk.webhook.AbstractWebhookController;
import com.zenvia.api.sdk.webhook.Event;
import com.zenvia.api.sdk.webhook.MessageEventCallback;
import com.zenvia.api.sdk.webhook.MessageStatusEventCallback;

/** WebhookController that is backed by <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc" target="_blank">Spring Web MVC</a>.
 *
 *  @since 1.1.0 */
public class WebhookController extends AbstractWebhookController {

	private static final Logger LOG = LoggerFactory.getLogger( WebhookController.class );
	
	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE}
	 *  events using the given configurations and default root
	 *  path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *  
	 *  @param handlerMapping
	 *  Request mapping to configure the webhook controller.
	 *
	 *  @param messageEventHandler
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @since 1.1.0 */
	public WebhookController(RequestMappingHandlerMapping handlerMapping, MessageEventCallback messageEventHandler) {
		this(handlerMapping, messageEventHandler, null, null);
	}

	/** Creates the {@link WebhookController} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations
	 *  and default root path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *
	 *  @param handlerMapping
	 *  Request mapping to configure the webhook controller.
	 *
	 *  @param messageEventHandler
	 *  An implementation of {@link MessageEventCallback} to receive a message event.
	 *
	 *  @param messageStatusEventHandler
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *
	 *  @since 1.1.0 */
	public WebhookController(
		RequestMappingHandlerMapping handlerMapping,
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler
	) {
		this(handlerMapping, messageEventHandler, messageStatusEventHandler, null, null, null, null);
	}

	/** Creates the {@link WebhookController} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations.
	 *
	 *  @param handlerMapping
	 *  Request mapping to configure the webhook controller.
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
		RequestMappingHandlerMapping handlerMapping,
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler,
		String path
	) {
		this(handlerMapping, messageEventHandler, messageStatusEventHandler, path, null, null, null);
	}

	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE} events
	 *  using the given configurations.
	 *  
	 *  @param handlerMapping
	 *  Request mapping to configure the webhook controller.
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
		RequestMappingHandlerMapping handlerMapping,
		MessageEventCallback messageEventHandler,
		String path
	) {
		this(handlerMapping, messageEventHandler, null, path, null, null, null);
	}
	
	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE_STATUS} events
	 *  using the given configurations and default root
	 *  path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *  
	 *  @param handlerMapping
	 *  Request mapping to configure the webhook controller.
	 *
	 *  @param messageStatusEventHandler
	 *  An implementation of {@link MessageStatusEventCallback} to receive a message status event.
	 *  
	 *  @since 1.1.0 */
	public WebhookController(RequestMappingHandlerMapping handlerMapping,
		MessageStatusEventCallback messageStatusEventHandler
	) {
		this(handlerMapping, null, messageStatusEventHandler, null);
	}

	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE_STATUS} events
	 *  using the given configurations.
	 *  
	 *  @param handlerMapping
	 *  Request mapping to configure the webhook controller.
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
		RequestMappingHandlerMapping handlerMapping,
		MessageStatusEventCallback messageStatusEventHandler,
		String path
	) {
		this(handlerMapping, null, messageStatusEventHandler, path);
	}

	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE} events
	 *  using the given configurations and default root
	 *  path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *  
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param handlerMapping
	 *  Request mapping to configure the webhook controller.
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
		RequestMappingHandlerMapping handlerMapping,
		MessageEventCallback messageEventHandler,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(handlerMapping, messageEventHandler, null, null, client, url, channel);
	}

	/** Creates the {@link WebhookController} to receive only {@link EventType#MESSAGE_STATUS} events
	 *  using the given configurations and default root
	 *  path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *  
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param handlerMapping
	 *  Request mapping to configure the webhook controller.
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
		RequestMappingHandlerMapping handlerMapping,
		MessageStatusEventCallback messageStatusEventHandler,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(handlerMapping, null, messageStatusEventHandler, null, client, url, channel);
	}

	/** Creates the {@link WebhookController} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations
	 *  and default root path ("{@value com.zenvia.api.sdk.webhook.AbstractWebhookController#DEFAULT_PATH}").
	 *  
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param handlerMapping
	 *  Request mapping to configure the webhook controller.
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
		RequestMappingHandlerMapping handlerMapping,
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(handlerMapping, messageEventHandler, messageStatusEventHandler, null, client, url, channel);
	}

	/** Creates the {@link WebhookController} to receive both {@link EventType#MESSAGE} and
	 *  {@link EventType#MESSAGE_STATUS} events using the given configurations.
	 *  The subscription will be created if it does not exist for these configurations.
	 *
	 *  @param handlerMapping
	 *  Request mapping to configure the webhook controller.
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
		RequestMappingHandlerMapping handlerMapping,
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler,
		String path,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		super(messageEventHandler, messageStatusEventHandler, path, client, url, channel);
		this.create(handlerMapping, this.path);
	}
	private void create(RequestMappingHandlerMapping handlerMapping, String path) {
		Method method;
		try {
			method = WebhookController.class.getDeclaredMethod("apply", Event.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		
		handlerMapping.registerMapping(
			RequestMappingInfo.paths(path).methods(RequestMethod.POST).build(),
			this,
			method
		);
	}

	protected ResponseEntity<Void> apply(@RequestBody Event event) {
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
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
