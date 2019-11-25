package com.zenvia.api.sdk.webhook;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zenvia.api.sdk.Json;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.subscriptions.EventType;


/** Type of event used for status of messages sent.
 *
 *  @since 1.1.0 */
public class MessageStatusEvent extends Event {
	/** @since 1.1.0 */
	public static final String TYPE = "MESSAGE_STATUS";

	/** Message identifier.
	 *
	 *  @since 1.1.0 */
	public final String messageId;
	
	/** The index of sent array contents that is receiving the status.
	 *
	 *  @since 1.1.0 */
	public final Integer contentIndex;
	
	/** The status of a send message request
	 *
	 *  @since 1.1.0 */
	public final MessageStatus messageStatus;


	/** @param id Event identifier.
	 *
	 *  @param timestamp Timestamp of event occurrence.
	 *
	 *  @param subscriptionId Subscription identifier.
	 *
	 *  @param channel Channel of sent message.
	 *
	 *  @param messageId Message identifier.
	 *
	 *  @param contentIndex The index of sent array contents that is receiving the status.
	 *  
	 *  @param messageStatus The status of sent message.
	 *  
	 *  @since 1.1.0 */
	@JsonCreator
	public MessageStatusEvent(
		@JsonProperty( "id" ) String id,
		@JsonProperty( "timestamp" ) ZonedDateTime timestamp,
		@JsonProperty( "subscriptionId" ) String subscriptionId,
		@JsonProperty( "channel" ) ChannelType channel,
		@JsonProperty( "messageId" ) String messageId,
		@JsonProperty( "contentIndex" ) Integer contentIndex,
		@JsonProperty( "messageStatus" ) MessageStatus messageStatus
	) {
		super( EventType.MESSAGE_STATUS, id, timestamp, subscriptionId, channel );
		this.messageId = messageId;
		this.contentIndex = contentIndex;
		this.messageStatus = messageStatus;
		
	}

	/** String containg the object as an indented JSON.
	 *
	 *  @since 1.1.0 */
	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
