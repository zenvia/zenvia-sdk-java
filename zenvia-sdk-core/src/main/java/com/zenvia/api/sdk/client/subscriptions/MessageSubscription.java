package com.zenvia.api.sdk.client.subscriptions;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class MessageSubscription extends Subscription {
	public static final String EVENT_TYPE = "MESSAGE";


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
	}


	public MessageSubscription(
		String id,
		Webhook webhook,
		MessageCriteria criteria,
		String status,
		ZonedDateTime createdAt,
		ZonedDateTime updatedAt
	) {
		super(
			id,
			EventType.MESSAGE,
			webhook,
			criteria,
			SubscriptionStatus.valueOf( status ),
			createdAt,
			updatedAt
		);
	}


	public MessageCriteria criteria() {
		return (MessageCriteria) criteria;
	}
}
