package com.zenvia.api.sdk.client.subscriptions;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/** Describes a webkook {@link "https://en.wikipedia.org/wiki/Webhook"}) setup on
 *  Zenvia CPaaS API ({@link "https://app.zenvia.com/home/api"}) for receiving status updates 
 *  of messages sent to other contacts on a specific channel.
 *
 *  @since 0.9.0 */
public class MessageStatusSubscription extends Subscription {
	/** @since 0.9.0 */
	public static final String EVENT_TYPE = "MESSAGE_STATUS";


	/** Creates a message subscription without id and timestamps.
	 *  <br><br>
	 *  Useful for subscription creation.
	 *
	 *  @param webhook Setup how to execute de callbacks on webhooks.
	 *
	 *  @param criteria Indicates the criteria necessary to trigger a callback.
	 *
	 *  @since 1.0.0 */
	public MessageStatusSubscription(
		Webhook webhook,
		Criteria criteria
	) {
		this( null, webhook, criteria, SubscriptionStatus.ACTIVE, null, null );
	}


	/** Creates a message subscription without id and timestamps.
	 *  <br><br>
	 *  Useful for subscription creation.
	 *
	 *  @param webhook Setup how to execute de callbacks on webhooks.
	 *
	 *  @param criteria Indicates the criteria necessary to trigger a callback.
	 *
	 *  @param status Indicate if subscription is enabled.
	 *
	 *  @since 1.0.0 */
	public MessageStatusSubscription(
		Webhook webhook,
		Criteria criteria,
		SubscriptionStatus status
	) {
		this( null, webhook, criteria, status, null, null );
	}


	/** Creates a message subscription without id and timestamps.
	 *  <br><br>
	 *  Useful for subscription creation.
	 *
	 *  @param webhook Setup how to execute de callbacks on webhooks.
	 *
	 *  @param criteria Indicates the criteria necessary to trigger a callback.
	 *
	 *  @param status Indicate if subscription is enabled.
	 *
	 *  @since 1.0.0 */
	public MessageStatusSubscription(
		Webhook webhook,
		Criteria criteria,
		String status
	) {
		this( null, webhook, criteria, SubscriptionStatus.valueOf( status ), null, null );
	}


	/** @param id Ignored on subscription creation and update.
	 *
	 *  @param webhook Setup how to execute de callbacks on webhooks.
	 *
	 *  @param criteria Indicates the criteria necessary to trigger a callback.
	 *
	 *  @param status Indicate if subscription is enabled.
	 *
	 *  @param createdAt Ignored on subscription creation and update.
	 *
	 *  @param updatedAt Ignored on subscription creation and update.
	 *
	 *  @since 0.9.0 */
	@JsonCreator
	public MessageStatusSubscription(
		@JsonProperty( "id" ) String id,
		@JsonProperty( "webhook" ) Webhook webhook,
		@JsonProperty( "criteria" ) Criteria criteria,
		@JsonProperty( "status" ) SubscriptionStatus status,
		@JsonProperty( "createdAt" ) ZonedDateTime createdAt,
		@JsonProperty( "updatedAt" ) ZonedDateTime updatedAt
	) {
		super( id, EventType.MESSAGE_STATUS, webhook, criteria, status, createdAt, updatedAt );
	}


	@Override
	@SuppressWarnings( "unchecked" )
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION apply( Webhook webhook, SubscriptionStatus status ) {
		return (SUBSCRIPTION) new MessageStatusSubscription( id, webhook, criteria, status, createdAt, updatedAt );
	}
}
