package com.zenvia.api.sdk.client.subscriptions;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import com.zenvia.api.sdk.ZonedDateTimeDeserializer;


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

	@JsonDeserialize( using = ZonedDateTimeDeserializer.class )
	@JsonSerialize( using = ZonedDateTimeSerializer.class )
	@JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" )
	public final ZonedDateTime createdAt;

	@JsonDeserialize( using = ZonedDateTimeDeserializer.class )
	@JsonSerialize( using = ZonedDateTimeSerializer.class )
	@JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" )
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


	public MessageSubscription ofMessage() {
		return (MessageSubscription) this;
	}


	public MessageStatusSubscription ofMessageStatus() {
		return (MessageStatusSubscription) this;
	}
}
