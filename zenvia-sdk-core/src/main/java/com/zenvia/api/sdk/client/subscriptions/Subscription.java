package com.zenvia.api.sdk.client.subscriptions;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import com.zenvia.api.sdk.Json;
import com.zenvia.api.sdk.ZonedDateTimeDeserializer;


/** Describes a webkook {@link "https://en.wikipedia.org/wiki/Webhook"}) setup on
 *  Zenvia CPaaS API ({@link "https://app.zenvia.com/home/api"}).
 *
 *  @since 0.9.0 */
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, property = "eventType", include = As.EXISTING_PROPERTY )
@JsonSubTypes( {
	@JsonSubTypes.Type( value = MessageSubscription.class, name = MessageSubscription.EVENT_TYPE ),
	@JsonSubTypes.Type( value = MessageStatusSubscription.class, name = MessageStatusSubscription.EVENT_TYPE )
} )
@JsonPropertyOrder( { "eventType" } )
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Subscription {
	/** @since 0.9.0 */
	public final String id;

	/** Event type can be either MESSAGE or MESSAGE_STATUS.
	 *
	 *  @since 0.9.0 */
	public final EventType eventType;

	/** Setup how to execute de callbacks on webhooks.
	 *
	 *  @since 0.9.0 */
	public final Webhook webhook;

	/** Indicates the criteria necessary to trigger a callback.
	 *
	 *  @since 0.9.0 */
	public final Criteria criteria;

	/** Indicate if subscription is enabled.
	 *
	 *  @since 0.9.0 */
	public final SubscriptionStatus status;

	/** Subscription creation timestamp.
	 *
	 *  @since 0.9.0 */
	@JsonDeserialize( using = ZonedDateTimeDeserializer.class )
	@JsonSerialize( using = ZonedDateTimeSerializer.class )
	@JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" )
	public final ZonedDateTime createdAt;

	/** Subscription update timestamp.
	 *
	 *  @since 0.9.0 */
	@JsonDeserialize( using = ZonedDateTimeDeserializer.class )
	@JsonSerialize( using = ZonedDateTimeSerializer.class )
	@JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" )
	public final ZonedDateTime updatedAt;


	protected Subscription(
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


	/** Clones this Subscription, replacing webhook and status by the ones in the given
	 * PartialSubscription.
	 *
	 *  @since 0.9.0 */
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION apply( PartialSubscription partialSubscription ) {
		return apply( partialSubscription.webhook, partialSubscription.status );
	}


	/** Clones this Subscription, replacing webhook and status by the ones given.
	 *
	 *  @since 0.9.0 */
	public abstract <SUBSCRIPTION extends Subscription> SUBSCRIPTION apply( Webhook webhook, SubscriptionStatus status );


	/** Casts the Subscription to MessageSubscription. Usuful when listing subscriptions.
	 *
	 *  @since 0.9.0 */
	public MessageSubscription ofMessage() {
		return (MessageSubscription) this;
	}


	/** Casts the Subscription to MessageStatusSubscription. Usuful when listing subscriptions.
	 *
	 *  @since 0.9.0 */
	public MessageStatusSubscription ofMessageStatus() {
		return (MessageStatusSubscription) this;
	}


	/** String containg the object as an indented JSON.
	 *
	 *  @since 0.9.0 */
	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
