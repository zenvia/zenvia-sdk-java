package com.zenvia.api.sdk.client.subscriptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.messages.MessageDirection;


/** Indicates the criteria necessary to trigger a callback on a
 *  {@link MessageSubscription message subscription}.
 *
 *  @since 0.9.0 */
public class MessageCriteria extends Criteria {
	/** Indicates the origin of a message. IN means it originated from the contact, OUT means it was
	 *  sent to the contact.
	 *
	 *  @since 0.9.0 */
	public final MessageDirection direction;


	/** @param channel Channel which will trigger the callbacks.
	 *
	 *  @param direction Direction of the messages which will trigger the callbacks.
	 *  Null means both IN and OUT messages. 
	 *
	 *  @since 0.9.0 */
	@JsonCreator
	public MessageCriteria(
		@JsonProperty( "channel" ) ChannelType channel,
		@JsonProperty( "direction" ) MessageDirection direction
	) {
		super( channel );
		this.direction = direction;
	}


	/** @param channel Channel which will trigger the callbacks.
	 *
	 *  @param direction Direction of the messages which will trigger the callbacks.
	 *  Null means both {@link MessageDirection#IN IN} and {@link MessageDirection#OUT OUT} messages.
	 *
	 *  @since 0.9.0 */
	public MessageCriteria( String channel, String direction ) {
		super( channel );
		this.direction = direction == null ? null : MessageDirection.valueOf( direction );
	}
}
