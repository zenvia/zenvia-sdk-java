package com.zenvia.api.sdk.client.subscriptions;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;


@JsonIgnoreProperties( ignoreUnknown = true )
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, property = "eventType", include = As.EXISTING_PROPERTY )
@JsonSubTypes( {
	@JsonSubTypes.Type( value = MessageSubscription.class, name = MessageSubscription.EVENT_TYPE ),
	@JsonSubTypes.Type( value = MessageStatusSubscription.class, name = MessageStatusSubscription.EVENT_TYPE )
} )
@JsonPropertyOrder( { "eventType" } )
public abstract class Subscription {
	public final String id;

	public final EventType eventType;

	public final Webhook webhook;

	public final Criteria criteria;

	public final SubscriptionStatus status;

	public final ZonedDateTime createdAt;

	public final ZonedDateTime updatedAt;


	public Subscription(
		String id,
		EventType eventType,
		Webhook webhook,
		Criteria criteria,
		SubscriptionStatus status,
		ZonedDateTime createdAt,
		ZonedDateTime updatedAt
	) {
		this.id = id;
		this.eventType = eventType;
		this.webhook = webhook;
		this.criteria = criteria;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}


	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION apply( PartialSubscription partialSubscription ) {
		return apply( partialSubscription.webhook, partialSubscription.status );
	}


	public abstract <SUBSCRIPTION extends Subscription> SUBSCRIPTION apply( Webhook webhook, SubscriptionStatus status );
}
