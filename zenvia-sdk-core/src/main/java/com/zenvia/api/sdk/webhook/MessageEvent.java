package com.zenvia.api.sdk.webhook;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zenvia.api.sdk.Json;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.subscriptions.EventType;
import com.zenvia.api.sdk.messages.Message;
import com.zenvia.api.sdk.messages.MessageDirection;


/** Type of event used for message notification.
*
*  @since 1.1.0 */
public class MessageEvent extends Event {
	/** @since 1.1.0 */
	public static final String TYPE = "MESSAGE";

	/** Indicates the origin of a message. IN means it originated from the contact, OUT means it was
	 *  sent to the contact.
	 *
	 *  @since 1.1.0 */
	public final MessageDirection direction;

	/** Either the result from a message request, or a message sent by a contact. 
	 *
	 *  @since 1.1.0 */
	public final Message message;

	/** @param id
	 *  Event identifier.
	 *
	 *  @param timestamp
	 *  Timestamp of event occurrence.
	 *
	 *  @param subscriptionId
	 *  Subscription identifier.
	 *  
	 *  @param channel
	 *  Channel where the message will go through while sending a message, the source channel when
	 *  receiving a message.
	 *  
	 *  @param direction
	 *  Indicates the origin of a message. IN means it originated from the contact, OUT means it was
	 *  sent to the contact.
	 *  
	 *  @param message
	 *  Either the result from a message request, or a message sent by a contact.  
	 *
	 *  @since 1.1.0 */
	@JsonCreator
	public MessageEvent(
		@JsonProperty( "id" ) String id,
		@JsonProperty( "timestamp" ) ZonedDateTime timestamp,
		@JsonProperty( "subscriptionId" ) String subscriptionId,
		@JsonProperty( "channel" ) ChannelType channel,
		@JsonProperty( "direction" ) MessageDirection direction,
		@JsonProperty( "message" ) Message message
	) {
		super( EventType.MESSAGE, id, timestamp, subscriptionId, channel );
		this.direction = direction;
		this.message = message;
	}
	
	/** String containg the object as an indented JSON.
	 *
	 *  @since 1.1.0 */
	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
