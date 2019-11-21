package com.zenvia.api.sdk.webhook;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zenvia.api.sdk.client.AbstractClient;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.subscriptions.Criteria;
import com.zenvia.api.sdk.client.subscriptions.EventType;
import com.zenvia.api.sdk.client.subscriptions.MessageCriteria;
import com.zenvia.api.sdk.client.subscriptions.MessageStatusSubscription;
import com.zenvia.api.sdk.client.subscriptions.MessageSubscription;
import com.zenvia.api.sdk.client.subscriptions.Subscription;
import com.zenvia.api.sdk.client.subscriptions.SubscriptionStatus;
import com.zenvia.api.sdk.client.subscriptions.Webhook;
import com.zenvia.api.sdk.messages.MessageDirection;

public abstract class AbstractWebhookController {
	
	private static final Logger LOG = LoggerFactory.getLogger( AbstractWebhookController.class );

	/** {@value} */
	public static final String DEFAULT_PATH = "/";

	protected final MessageEventCallback messageEventCallback;
	
	protected final MessageStatusEventCallback messageStatusEventCallback;

	protected final String path;

	protected final AbstractClient client;
	
	protected final String url;

	protected final ChannelType channel;
	
	public AbstractWebhookController(MessageEventCallback messageEventCallback) {
		this(messageEventCallback, null, null);
	}

	public AbstractWebhookController(
		MessageEventCallback messageEventCallback,
		MessageStatusEventCallback messageStatusEventCallback
	) {
		this(messageEventCallback, messageStatusEventCallback, null, null, null, null);
	}

	public AbstractWebhookController(
		MessageEventCallback messageEventCallback,
		MessageStatusEventCallback messageStatusEventCallback,
		String path
	) {
		this(messageEventCallback, messageStatusEventCallback, path, null, null, null);
	}

	public AbstractWebhookController(MessageEventCallback messageEventCallback, String path) {
		this(messageEventCallback, null, path, null, null, null);
	}

	public AbstractWebhookController(MessageStatusEventCallback messageStatusEventCallback) {
		this(null, messageStatusEventCallback, null);
	}
	
	public AbstractWebhookController(MessageStatusEventCallback messageStatusEventCallback, String path) {
		this(null, messageStatusEventCallback, path);
	}

	public AbstractWebhookController(
		MessageEventCallback messageEventCallback,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(messageEventCallback, null, null, client, url, channel);
	}

	public AbstractWebhookController(
		MessageStatusEventCallback messageStatusEventCallback,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this(null, messageStatusEventCallback, null, client, url, channel);
	}
	
	public AbstractWebhookController(
		MessageEventCallback messageEventCallback,
		MessageStatusEventCallback messageStatusEventCallback,
		AbstractClient client,
		String url,
		ChannelType
		channel
	) {
		this(messageEventCallback, messageStatusEventCallback, null, client, url, channel);
	}
	
	public AbstractWebhookController(
		MessageEventCallback messageEventCallback,
		MessageStatusEventCallback messageStatusEventCallback,
		String path,
		AbstractClient client,
		String url,
		ChannelType channel
	) {
		this.messageEventCallback = messageEventCallback;
		this.messageStatusEventCallback = messageStatusEventCallback;
		this.path = valueOrDefault( path, DEFAULT_PATH );
		this.client = client;
		this.url = url;
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
		if (client == null || url == null || channel == null || (messageEventCallback == null && messageStatusEventCallback == null)) {
			return;
		}

		LOG.debug("Verifying subscriptions before create them if not exist");
		List<Subscription> subscriptions = client.listSubscriptions();
		boolean shouldCreateMessageSubscription = true;
		boolean shouldCreateMessageStatusSubscription = true;
		
		for (Subscription subscription : subscriptions) {
			if (
				SubscriptionStatus.ACTIVE.equals(subscription.status) && 
				url.equalsIgnoreCase(subscription.webhook.url) && 
				channel.equals(subscription.criteria.channel)
			) {
				if (EventType.MESSAGE.equals(subscription.eventType)) {
					LOG.debug("It wont be necessary to create subscription for MESSAGE event");
					shouldCreateMessageSubscription = false;
				} else if(EventType.MESSAGE_STATUS.equals(subscription.eventType)) {
					shouldCreateMessageStatusSubscription = false;
					LOG.debug("It wont be necessary to create subscription for MESSAGE_STAUS event");
				}
			}
		}
		
		if (shouldCreateMessageSubscription || shouldCreateMessageStatusSubscription) {
			Webhook webhook = new Webhook(url);
			
			if (messageEventCallback != null && shouldCreateMessageSubscription) {
				MessageCriteria criteria = new MessageCriteria(channel, MessageDirection.IN);
				LOG.debug("Trying to create subscription for MESSAGE event of channel {}", channel);
				client.createSubscription(new MessageSubscription(webhook, criteria));
			}
			
			if (messageStatusEventCallback != null && shouldCreateMessageStatusSubscription) {
				Criteria criteria = new Criteria(channel);
				LOG.debug("Trying to create subscription for MESSAGE_STATUS event of channel {}", channel);
				client.createSubscription(new MessageStatusSubscription(webhook, criteria));
			}
		}
	}
	
	private static final <TYPE> TYPE valueOrDefault( TYPE value, TYPE defaultValue ) {
		return value == null ? defaultValue : value;
	}

	/** Returns the configuration of this client, except for the token.
	 *
	 *  @since 0.9.0 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{"
			+ "\n  messageEventCallback = [" + messageEventCallback + "]"
			+ "\n  messageStatusEventCallback = [" + messageStatusEventCallback + "]"
			+ "\n  path = [" + path + "]"
			+ "\n  url = [" + url + "]"
			+ "\n  channel = [" + channel + "]"
			+ "\n  client = [" + client + "]"
			+ "\n}";
	}

}
