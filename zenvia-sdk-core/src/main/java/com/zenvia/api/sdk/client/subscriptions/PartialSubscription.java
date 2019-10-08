package com.zenvia.api.sdk.client.subscriptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.Json;


/** Holds the a {@link Subscription#webhook webhook} and a {@link Subscription#status status}
 *  to update a {@link Subscription subscription}.
 * 
 *  @since 0.9.0 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class PartialSubscription {
	/** {@link Subscription#webhook Webhook} to be applied.
	 * 
	 *  @since 0.9.0 */
	public final Webhook webhook;

	/** {@link Subscription#status Status}.
	 * 
	 *  @since 0.9.0 */
	public final SubscriptionStatus status;


	@JsonCreator
	public PartialSubscription(
		@JsonProperty( "webhook" ) Webhook webhook,
		@JsonProperty( "status" ) SubscriptionStatus status
	) {
		this.webhook = webhook;
		this.status = status;
	}


	public PartialSubscription( Subscription subscription ) {
		this( subscription.webhook, subscription.status );
	}


	/** String containg the object as an indented JSON.
	 * 
	 *  @since 0.9.0 */
	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
