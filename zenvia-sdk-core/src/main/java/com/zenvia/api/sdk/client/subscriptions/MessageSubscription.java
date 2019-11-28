package com.zenvia.api.sdk.client.subscriptions;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/** Describes a <a href="https://en.wikipedia.org/wiki/Webhook" target="_blank">webkook</a> setup on
 *  <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia CPaaS API</a> for receiving messages
 *  trafficking on a specific channel.
 *
 *  @since 0.9.0 */
public class MessageSubscription extends Subscription {
	/** @since 0.9.0 */
	public static final String EVENT_TYPE = "MESSAGE";

	public final MessageCriteria criteria;


	/** Creates a message subscription without id and timestamps, and with status ACTIVE.
	 *  <br><br>
	 *  Useful for subscription creation.
	 *
	 *  @param webhook Setup how to execute de callbacks on webhooks.
	 *
	 *  @param criteria Indicates the criteria necessary to trigger a callback.
	 *
	 *  @since 1.0.0 */
	public MessageSubscription(
		Webhook webhook,
		MessageCriteria criteria
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
	public MessageSubscription(
		Webhook webhook,
		MessageCriteria criteria,
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
	public MessageSubscription(
		Webhook webhook,
		MessageCriteria criteria,
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
	public MessageSubscription(
		@JsonProperty( "id" ) String id,
		@JsonProperty( "webhook" ) Webhook webhook,
		@JsonProperty( "criteria" ) MessageCriteria criteria,
		@JsonProperty( "status" ) SubscriptionStatus status,
		@JsonProperty( "createdAt" ) ZonedDateTime createdAt,
		@JsonProperty( "updatedAt" ) ZonedDateTime updatedAt
	) {
		super( id, EventType.MESSAGE, webhook, criteria, status, createdAt, updatedAt );
		this.criteria = criteria;
	}


	@Override
	@SuppressWarnings( "unchecked" )
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION apply( Webhook webhook, SubscriptionStatus status ) {
		return (SUBSCRIPTION) new MessageSubscription( id, webhook, criteria, status, createdAt, updatedAt );
	}
}
