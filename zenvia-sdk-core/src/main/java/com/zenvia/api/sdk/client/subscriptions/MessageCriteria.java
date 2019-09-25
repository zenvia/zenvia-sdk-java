package com.zenvia.api.sdk.client.subscriptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.messages.MessageDirection;


public class MessageCriteria extends Criteria {
	public final MessageDirection direction;


	@JsonCreator
	public MessageCriteria(
		@JsonProperty( "channel" ) ChannelType channel,
		@JsonProperty( "direction" ) MessageDirection direction
	) {
		super( channel );
		this.direction = direction;
	}


	public MessageCriteria( String channel, String direction ) {
		super( channel );
		this.direction = direction == null ? null : MessageDirection.valueOf( direction );
	}
}
