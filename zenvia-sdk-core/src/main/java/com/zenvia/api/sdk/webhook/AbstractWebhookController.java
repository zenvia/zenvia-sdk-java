package com.zenvia.api.sdk.webhook;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zenvia.api.sdk.client.AbstractClient;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.subscriptions.Criteria;
import com.zenvia.api.sdk.client.subscriptions.MessageCriteria;
import com.zenvia.api.sdk.client.subscriptions.MessageStatusSubscription;
import com.zenvia.api.sdk.client.subscriptions.MessageSubscription;
import com.zenvia.api.sdk.client.subscriptions.Webhook;
import com.zenvia.api.sdk.messages.MessageDirection;

public abstract class AbstractWebhookController {
	
	private static final Logger LOG = LoggerFactory.getLogger( AbstractWebhookController.class );

	/** {@value} */
	public static final String DEFAULT_PATH = "/";

	protected final MessageEventCallback messageEventHandler;
	
	protected final MessageStatusEventCallback messageStatusEventHandler;

	protected String path;

	protected final AbstractClient client;
	
	protected final String url;

	protected final ChannelType channel;
	
	public AbstractWebhookController(MessageEventCallback messageEventHandler) {
		this(messageEventHandler, null, null);
	}

	public AbstractWebhookController(
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler
	) {
		this(messageEventHandler, messageStatusEventHandler, null, null, null, null);
	}

	public AbstractWebhookController(
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler,
		String path
	) {
		this(messageEventHandler, messageStatusEventHandler, path, null, null, null);
	}

	public AbstractWebhookController(MessageEventCallback messageEventHandler, String path) {
		this(messageEventHandler, null, path, null, null, null);
	}

	public AbstractWebhookController(MessageStatusEventCallback messageStatusEventHandler) {
		this(null, messageStatusEventHandler, null);
	}
	
	public AbstractWebhookController(MessageStatusEventCallback messageStatusEventHandler, String path) {
		this(null, messageStatusEventHandler, path);
	}

	public AbstractWebhookController(
		MessageEventCallback messageEventHandler,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(messageEventHandler, null, null, client, url, channel);
	}

	public AbstractWebhookController(
		MessageStatusEventCallback messageStatusEventHandler,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(null, messageStatusEventHandler, null, client, url, channel);
	}
	
	public AbstractWebhookController(
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler,
		AbstractClient client,
		String url,
		ChannelType
		channel
	) {
		this(messageEventHandler, messageStatusEventHandler, null, client, url, channel);
	}
	
	public AbstractWebhookController(
		MessageEventCallback messageEventHandler,
		MessageStatusEventCallback messageStatusEventHandler,
		String path,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this.messageEventHandler = messageEventHandler;
		this.messageStatusEventHandler = messageStatusEventHandler;
		this.client = client;
		this.path = valueOrDefault( path, DEFAULT_PATH );
		if (!this.path.startsWith("/")) {
			this.path = "/".concat(this.path);
		}
		this.url = url.concat(this.path);
		this.channel = channel;
	}

	/** Initializes the webhook controller.
	 * 
	 *  @since 1.1.0 */
	public void init() {
		try {
			this.createSubscriptions();
		} catch (Exception e) {
			LOG.error("Error on try to create subscription", e);
			throw e;
		}
	}
	
	private void createSubscriptions() {
		if (client == null || url == null || channel == null || (messageEventHandler == null && messageStatusEventHandler == null)) {
			return;
		}

		Webhook webhook = new Webhook(url);
		
		if (messageEventHandler != null) {
			MessageCriteria criteria = new MessageCriteria(channel, MessageDirection.IN);
			LOG.debug("Trying to create subscription for MESSAGE event of channel {}", channel);
			try {
				client.createSubscription(new MessageSubscription(webhook, criteria));
			} catch (UnsuccessfulRequestException error) {
				if (error.httpStatusCode == HttpStatus.SC_CONFLICT) {
					LOG.debug("Message subscription already exists.");
				} else {
					throw error;
				}
			}
		}
		
		if (messageStatusEventHandler != null) {
			Criteria criteria = new Criteria(channel);
			LOG.debug("Trying to create subscription for MESSAGE_STATUS event of channel {}", channel);
			try {
				client.createSubscription(new MessageStatusSubscription(webhook, criteria));
			} catch (UnsuccessfulRequestException error) {
				if (error.httpStatusCode == HttpStatus.SC_CONFLICT) {
					LOG.debug("Message status subscription already exists.");
				} else {
					throw error;
				}
			}
		}
	}
	
	private static final <TYPE> TYPE valueOrDefault( TYPE value, TYPE defaultValue ) {
		return value == null ? defaultValue : value;
	}

	/** Returns the configuration of this Webhook.
	 *
	 *  @since 0.9.0 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{"
			+ "\n  messageEventHandler = [" + messageEventHandler + "]"
			+ "\n  messageStatusEventHandler = [" + messageStatusEventHandler + "]"
			+ "\n  path = [" + path + "]"
			+ "\n  url = [" + url + "]"
			+ "\n  channel = [" + channel + "]"
			+ "\n  client = [" + client + "]"
			+ "\n}";
	}

}
