package com.zenvia.api.sdk.client.subscriptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class PartialSubscription {
	public final Webhook webhook;

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
}
