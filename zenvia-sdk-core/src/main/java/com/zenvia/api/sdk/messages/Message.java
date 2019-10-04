package com.zenvia.api.sdk.client.messages;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.contents.Content;


/** Either the result from a message request, or a message sent by a contact.
 *
 * @since 0.9.0 */
public class Message extends MessageRequest {
	/** While sending a message, this id is the one that will be used on message status callbacks.
	 * 
	 *  @since 0.9.0 */
	public final String id;

	/** Indicates the origin of a message. IN means it originated from the contact, OUT means it was
	 *  sent to the contact.
	 *
	 *  @since 0.9.0 */
	public final MessageDirection direction;

	/** Channel where the message will go through while sending a message, the source channel when
	 *  receiving a message.
	 *
	 *  @since 0.9.0 */
	public final ChannelType channel;


	/** @param id Message id.
	 *
	 *  @param from Id of the sender of the message.
	 *
	 *  @param to Id of the receiver of the message.
	 *
	 *  @param direction Indicates the origin of a message.
	 *
	 *  @param channel Channel the message went or will go through. 
	 *
	 *  @param contents List of contents.
	 *
	 *  @since 0.9.0 */
	@JsonCreator
	public Message(
		@JsonProperty( "id" ) String id,
		@JsonProperty( "from" ) String from,
		@JsonProperty( "to" ) String to,
		@JsonProperty( "direction" ) MessageDirection direction,
		@JsonProperty( "channel" ) ChannelType channel,
		@JsonProperty( "contents" ) List<Content> contents
	) {
		super( from, to, contents );
		this.id = id;
		this.direction = direction;
		this.channel = channel;
	}
}
