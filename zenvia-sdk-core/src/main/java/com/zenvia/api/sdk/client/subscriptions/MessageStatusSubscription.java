package com.zenvia.api.sdk.client.subscriptions;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class MessageStatusSubscription extends Subscription {
	public static final String EVENT_TYPE = "MESSAGE_STATUS";


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


	public MessageStatusSubscription(
		String id,
		Webhook webhook,
		Criteria criteria,
		String status,
		ZonedDateTime createdAt,
		ZonedDateTime updatedAt
	) {
		super(
			id,
			EventType.MESSAGE_STATUS,
			webhook,
			criteria,
			SubscriptionStatus.valueOf( status ),
			createdAt,
			updatedAt
		);
	}
}
